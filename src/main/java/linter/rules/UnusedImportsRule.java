package linter.rules;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;

public class UnusedImportsRule implements LintRule {

    @Override
    public List<LintWarning> check(ClassNode classNode) {
        java.util.List<LintWarning> warnings = new java.util.ArrayList<>();

        // Best-effort: Class files don't retain source-level import lists, but
        // a ClassNode usually contains the source file name (SourceFile attribute).
        // We'll try to locate the original .java file in the workspace (src/main/java, src/test/java)
        // and parse import statements. For each non-wildcard import we will check whether
        // the imported simple type name appears elsewhere in the source. If it doesn't, warn.

        // THIS ONE MIGHT INDICATE THAT THE TYPE OF INFO CLASSES WE'VE BUILT NEEDS TO BE EXPANDED

        String sourceFile = classNode.sourceFile;
        if (sourceFile == null || sourceFile.isEmpty()) {
            return warnings;
        }

        java.nio.file.Path projectRoot = java.nio.file.Paths.get(System.getProperty("user.dir"));

        java.util.List<java.nio.file.Path> searchRoots = java.util.List.of(
                projectRoot.resolve("src/main/java"),
                projectRoot.resolve("src/test/java")
        );

        java.nio.file.Path found = null;
        for (java.nio.file.Path root : searchRoots) {
            if (!java.nio.file.Files.exists(root)) continue;
            try (java.util.stream.Stream<java.nio.file.Path> stream = java.nio.file.Files.walk(root)) {
                java.util.Optional<java.nio.file.Path> opt = stream
                        .filter(p -> p.getFileName().toString().equals(sourceFile))
                        .findFirst();
                if (opt.isPresent()) {
                    found = opt.get();
                    break;
                }
            } catch (java.io.IOException e) {
                // ignore and continue
                e.printStackTrace();
            }
        }

        if (found == null) {
            // no accessible source file, = cannot analyze imports
            return warnings;
        }

        java.util.List<String> lines;
        try {
            lines = java.nio.file.Files.readAllLines(found);
        } catch (java.io.IOException e) {
            return warnings;
        }

        // collect explicit import lines
        java.util.List<String> imports = new java.util.ArrayList<>();
        for (String l : lines) {
            String t = l.trim();
            if (t.startsWith("import ")) {
                imports.add(t);
            }
        }

        if (imports.isEmpty()) return warnings;

        // Build a single string of source excluding import lines so we can search for simple uses
        StringBuilder sourceNoImports = new StringBuilder();
        for (String l : lines) {
            String t = l.trim();
            if (!t.startsWith("import ")) {
                sourceNoImports.append(l).append('\n');
            }
        }

        String content = sourceNoImports.toString();

        for (String imp : imports) {

            // remove trailing semicolon
            String cleaned = imp.replaceFirst(";\\s*$", "").trim();
            // ignore static imports and wildcard imports for now
            if (cleaned.contains("*")) continue;

            // import examples: import java.util.List; or import static org.foo.Bar.BAZ;
            String importedType = cleaned.substring("import".length()).trim();
            // for static imports there may be a single member after last dot
            if (importedType.startsWith("static")) {
                importedType = importedType.substring("static".length()).trim();
            }

            // get the simple name
            String simpleName = importedType;
            int lastDot = simpleName.lastIndexOf('.');
            if (lastDot >= 0) simpleName = simpleName.substring(lastDot + 1);

            // If the simple name does not appear anywhere in the remainder of the source, it's unused
            // Use a word-boundary search to reduce false positives.
            java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\b" + java.util.regex.Pattern.quote(simpleName) + "\\b");
            java.util.regex.Matcher m = p.matcher(content);
            if (!m.find()) {
                warnings.add(new LintWarning(classNode.name, "Unused import: " + cleaned));
            }
        }

        return warnings;
    }
    
}
