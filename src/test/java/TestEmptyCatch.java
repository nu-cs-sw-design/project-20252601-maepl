public class TestEmptyCatch {

    // TEST CASE 1: Completely empty catch block (SHOULD WARN)
    public void emptyCatchExample() {
        try {
            riskyOperation();
        } catch (Exception e) {
            // empty should trigger warning
        }
    }

    // TEST CASE 2: Non-empty catch - prints stack trace (SHOULD NOT WARN)
    public void nonEmptyCatchExample() {
        try {
            riskyOperation();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TEST CASE 3: Multiple catch blocks - first empty, second not (SHOULD WARN for first)
    public void multipleCatchBlocks() {
        try {
            riskyOperation();
        } catch (NullPointerException e) {
            // Empty catch for NPE
        } catch (Exception e) {
            System.out.println("Caught: " + e);
        }
    }

    // TEST CASE 4: Catch that re-throws (SHOULD NOT WARN)
    public void catchWithRethrow() throws Exception {
        try {
            riskyOperation();
        } catch (Exception e) {
            throw e;
        }
    }

    // TEST CASE 5: Catch with return statement only (SHOULD WARN)
    public String catchWithOnlyReturn() {
        try {
            riskyOperation();
            return "success";
        } catch (Exception e) {
            return null;
        }
    }

    // TEST CASE 6: Catch with actual error handling (SHOULD NOT WARN)
    public void catchWithHandling() {
        try {
            riskyOperation();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // TEST CASE 7: Empty catch for IOException (SHOULD WARN)
    public void emptyCatchIOException() {
        try {
            riskyIOOperation();
        } catch (java.io.IOException e) {
            // Empty
        }
    }

    // Helper methods
    private void riskyOperation() throws Exception {
        throw new Exception("test");
    }

    private void riskyIOOperation() throws java.io.IOException {
        throw new java.io.IOException("IO error");
    }
}
