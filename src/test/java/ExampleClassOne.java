public class ExampleClassOne {

    public int pubInt;
    private String privString;

    public ExampleClassOne() {
        this.pubInt = 0;
        this.privString = "default";
    }

    public void publicMethod(int param) {
        System.out.println("This is a public method with param: " + param);
        if (param > 10) {
            privateMethod();
        }
    }

    private String privateMethod() {
        return "This is a private method. " + privString;
    }
    
}
