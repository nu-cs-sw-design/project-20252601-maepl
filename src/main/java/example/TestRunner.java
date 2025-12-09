package example;

import linter.ClassLoaderService;
import linter.LinterEngine;
import linter.rules.EmptyCatchRule;
import linter.rules.UnusedImportsRule;
import linter.rules.TightCouplingRule;
import linter.rules.LintWarning;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;

public class TestRunner {
    public static void main(String[] args) throws Exception {
        String classToTest = args.length > 0 ? args[0] : "TestEmptyCatch";

        ClassLoaderService loader = new ClassLoaderService();
        LinterEngine engine = new LinterEngine();
        engine.registerRule(new EmptyCatchRule());
        engine.registerRule(new UnusedImportsRule());
        engine.registerRule(new TightCouplingRule());

        // Try to load the main class to check for inner classes
        ClassNode mainClassNode = loader.load(classToTest);

        // Check if this class has inner classes by looking at the compiled class files
        List<String> classesToTest = new java.util.ArrayList<>();
        classesToTest.add(classToTest);

        // If the class has inner classes, add them to the test list
        if (mainClassNode.innerClasses != null && !mainClassNode.innerClasses.isEmpty()) {
            for (Object innerObj : mainClassNode.innerClasses) {
                org.objectweb.asm.tree.InnerClassNode inner = (org.objectweb.asm.tree.InnerClassNode) innerObj;
                // Only test inner classes that belong to this outer class
                if (inner.name != null && inner.name.startsWith(classToTest + "$")) {
                    // Skip inner classes of inner classes (e.g., TestTightCouple$SelfReferences$Inner)
                    String innerName = inner.name.substring(classToTest.length() + 1);
                    if (!innerName.contains("$")) {
                        classesToTest.add(inner.name.replace('/', '.'));
                    }
                }
            }
        }

        System.out.println("Testing " + classesToTest.size() + " class(es)");
        System.out.println("========================================\n");

        int totalWarnings = 0;
        for (String className : classesToTest) {
            System.out.println("Testing: " + className);
            System.out.println("----------------------------------------");

            ClassNode classNode = loader.load(className);
            List<LintWarning> warnings = engine.run(classNode);

            if (warnings.isEmpty()) {
                System.out.println("✓ No warnings found!");
            } else {
                System.out.println("Found " + warnings.size() + " warning(s):\n");
                totalWarnings += warnings.size();

                int count = 1;
                for (LintWarning warning : warnings) {
                    System.out.println(count + ". " + warning.className);
                    System.out.println("   " + warning.message);
                    System.out.println();
                    count++;
                }
            }
            System.out.println();
        }

        System.out.println("========================================");
        System.out.println("Test completed. Total warnings: " + totalWarnings);
    }

}
