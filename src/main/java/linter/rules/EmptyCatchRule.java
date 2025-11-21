package linter.rules;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;

public class EmptyCatchRule implements LintRule {

    @Override
    public List<LintWarning> check(ClassNode classNode) {
        // @ SILVIA -- implement!!
        return new java.util.ArrayList<>();
    }
    
}
