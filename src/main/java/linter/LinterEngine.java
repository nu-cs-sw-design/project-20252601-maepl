package linter;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;

import linter.rules.LintRule;
import linter.rules.LintWarning;

public class LinterEngine {

    private final List<LintRule> rules;

    public LinterEngine(List<LintRule> rules) {
        this.rules = rules;
    }

    // OVERLOAAAAAD
    public LinterEngine() {
        this.rules = new java.util.ArrayList<>();
    }

    public void registerRule(LintRule rule) {
        this.rules.add(rule);
    }

    public List<LintWarning> run(ClassNode node) {
        List<LintWarning> warnings = new java.util.ArrayList<>();
        for (LintRule rule : rules) {
            warnings.addAll(rule.check(node));
        }
        return warnings;
    }


    
}
