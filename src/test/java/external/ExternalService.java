package external;

// This external class is in a different package to trigger coupling detection
public class ExternalService {
    public void doSomething() {}
    public String getData() { return ""; }
    public void processData(String data) {}
}
