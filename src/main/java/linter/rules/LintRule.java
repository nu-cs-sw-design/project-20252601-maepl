package linter.rules;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;

public interface LintRule {

    List<LintWarning> check(ClassNode classNode);
    

}
