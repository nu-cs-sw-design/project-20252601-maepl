package example;

import linter.ClassLoaderService;
import linter.LinterEngine;
import linter.rules.EmptyCatchRule;
import linter.rules.UnusedImportsRule;
import linter.rules.LintWarning;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;

public class TestRunner {
    public static void main(String[] args) throws Exception {
        String classToTest = args.length > 0 ? args[0] : "TestEmptyCatch";

        System.out.println("Testing class: " + classToTest);
        System.out.println("----------------------------------------\n");

        ClassLoaderService loader = new ClassLoaderService();
        ClassNode classNode = loader.load(classToTest);

        LinterEngine engine = new LinterEngine();
        engine.registerRule(new EmptyCatchRule());
        engine.registerRule(new UnusedImportsRule());

        List<LintWarning> warnings = engine.run(classNode);

        if (warnings.isEmpty()) {
            System.out.println("✓ No warnings found!");
        } else {
            System.out.println("Found " + warnings.size() + " warning(s):\n");

            int count = 1;
            for (LintWarning warning : warnings) {
                System.out.println(count + ". " + warning.className);
                System.out.println("   " + warning.message);
                System.out.println();
                count++;
            }
        }

        System.out.println("----------------------------------------");
        System.out.println("Test completed.");
    }

}
