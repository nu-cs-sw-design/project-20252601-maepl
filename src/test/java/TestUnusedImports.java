// Used imports:
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.Date;
import java.io.IOException;
import java.lang.InterruptedException;
import static java.lang.Math.sqrt;

// Unused imports that should be flagged:
import java.util.HashMap;
import java.util.LinkedList;
import static java.lang.Math.PI;
import java.util.Queue;
import java.util.Deque;
import java.util.TreeMap;

// Wildcard import - ignored by the rule:
import java.io.*;

public class TestUnusedImports {

    private List<String> items;
    private ArrayList<Integer> numbers = new ArrayList<>();
    private List<Date> dates;

    public Map<String, String> getConfig() {
        return null;
    }

    public void process(Set<String> values) {
        double result = sqrt(16.0);

        try {
            riskyOperation();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void waitOperation() throws InterruptedException {
        Thread.sleep(1000);
    }

    public void otherMethod() {
        java.util.TreeSet<String> set = new java.util.TreeSet<>();
    }

    private void riskyOperation() throws IOException {
        throw new IOException("test");
    }
}
