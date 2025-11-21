package linter.rules;

public class LintWarning {

    public final String className;
    public final String message;

    public LintWarning(String className, String message) {
        this.className = className;
        this.message = message;
    }
    
}
