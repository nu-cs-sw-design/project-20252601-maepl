import external.ExternalService;
import external.AnotherExternalClass;

public class TestTightCouple {

    // TEST CASE 1: High coupling through many field references (SHOULD WARN)
    // 10+ references to ExternalService
    public class HighCouplingFields {
        private ExternalService service1;
        private ExternalService service2;
        private ExternalService service3;
        private ExternalService service4;
        private ExternalService service5;
        private ExternalService service6;
        private ExternalService service7;
        private ExternalService service8;
        private ExternalService service9;
        private ExternalService service10;
        private ExternalService service11; // 11 field references
    }

    // TEST CASE 2: High coupling through method calls (SHOULD WARN)
    // Multiple calls to same external class
    public class HighCouplingMethodCalls {
        private ExternalService service = new ExternalService();

        public void method1() {
            service.doSomething();
            service.doSomething();
            service.doSomething();
            service.getData();
            service.getData();
            service.processData("test");
            service.processData("test");
            service.processData("test");
            service.processData("test");
            service.processData("test");
            service.processData("test"); // 11+ method calls
        }
    }

    // TEST CASE 3: Low coupling - under threshold (SHOULD NOT WARN)
    // Only 5 references to ExternalService
    public class LowCoupling {
        private ExternalService service1;
        private ExternalService service2;
        private ExternalService service3;
        private ExternalService service4;
        private ExternalService service5; // Only 5 references
    }

    // TEST CASE 4: High coupling through method parameters and return types (SHOULD WARN)
    public class HighCouplingMethodSignatures {
        public ExternalService method1(ExternalService s) { return s; }
        public ExternalService method2(ExternalService s) { return s; }
        public ExternalService method3(ExternalService s) { return s; }
        public ExternalService method4(ExternalService s) { return s; }
        public ExternalService method5(ExternalService s) { return s; }
        public ExternalService method6(ExternalService s) { return s; }
        // 12 references total (6 params + 6 return types)
    }

    // TEST CASE 5: High coupling through local variables (SHOULD WARN)
    public class HighCouplingLocalVariables {
        public void method() {
            ExternalService var1 = new ExternalService();
            ExternalService var2 = new ExternalService();
            ExternalService var3 = new ExternalService();
            ExternalService var4 = new ExternalService();
            ExternalService var5 = new ExternalService();
            ExternalService var6 = new ExternalService();
            ExternalService var7 = new ExternalService();
            ExternalService var8 = new ExternalService();
            ExternalService var9 = new ExternalService();
            ExternalService var10 = new ExternalService();
            ExternalService var11 = new ExternalService(); // 11+ local vars
        }
    }

    // TEST CASE 6: Mixed coupling across multiple external classes (MAY WARN)
    // High coupling to one class, low to another
    public class MixedCoupling {
        private ExternalService service1;
        private ExternalService service2;
        private ExternalService service3;
        private ExternalService service4;
        private ExternalService service5;
        private ExternalService service6;
        private ExternalService service7;
        private ExternalService service8;
        private ExternalService service9;
        private ExternalService service10;
        private ExternalService service11; // High coupling to ExternalService

        private AnotherExternalClass other1;
        private AnotherExternalClass other2; // Low coupling to AnotherExternalClass
    }

    // TEST CASE 7: No external coupling - only JDK classes (SHOULD NOT WARN)
    public class OnlyJdkClasses {
        private java.util.List<String> list1;
        private java.util.Map<String, String> map1;
        private java.util.Set<String> set1;
        private java.io.File file1;
        private java.io.InputStream stream1;
        private java.lang.String str1;
        private java.lang.Integer int1;
        private java.util.ArrayList<String> list2;
        private java.util.HashMap<String, String> map2;
        private java.util.HashSet<String> set2;
        private java.util.Date date1;
        private java.util.Calendar calendar1;
        // Many references but all to JDK classes - should be ignored
    }

    // TEST CASE 8: Array types with tight coupling (SHOULD WARN)
    public class ArrayTypeCoupling {
        private ExternalService[] array1;
        private ExternalService[] array2;
        private ExternalService[] array3;
        private ExternalService[] array4;
        private ExternalService[] array5;
        private ExternalService[] array6;
        private ExternalService[] array7;
        private ExternalService[] array8;
        private ExternalService[] array9;
        private ExternalService[] array10;
        private ExternalService[] array11; // 11 array field references
    }

    // TEST CASE 9: Self-references and inner classes (SHOULD NOT WARN)
    public class SelfReferences {
        private SelfReferences self1;
        private SelfReferences self2;
        private SelfReferences self3;
        private SelfReferences self4;
        private SelfReferences self5;
        private SelfReferences self6;
        private SelfReferences self7;
        private SelfReferences self8;
        private SelfReferences self9;
        private SelfReferences self10;
        private SelfReferences self11;
        // Many self-references should be ignored

        class Inner {}
        private Inner inner1;
        private Inner inner2;
        private Inner inner3;
        // Inner class references should also be ignored
    }

    // TEST CASE 10: Exactly at threshold (SHOULD WARN)
    public class ExactlyAtThreshold {
        private ExternalService s1;
        private ExternalService s2;
        private ExternalService s3;
        private ExternalService s4;
        private ExternalService s5;
        private ExternalService s6;
        private ExternalService s7;
        private ExternalService s8;
        private ExternalService s9;
        private ExternalService s10; // Exactly 10 references - should warn
    }

    // TEST CASE 11: Just below threshold (SHOULD NOT WARN)
    public class JustBelowThreshold {
        private ExternalService s1;
        private ExternalService s2;
        private ExternalService s3;
        private ExternalService s4;
        private ExternalService s5;
        private ExternalService s6;
        private ExternalService s7;
        private ExternalService s8;
        private ExternalService s9; // Only 9 references - should not warn
    }
}
