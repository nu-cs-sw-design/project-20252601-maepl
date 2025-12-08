package linter.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class TightCouplingRule implements LintRule {

    /**
     * If a single external class is referenced at least this many (arbitrarily 10, but feels good for a large codebase?) times,
     * we consider the current class "tightly coupled" to it.
     */
    private static final int COUPLING_THRESHOLD = 10;

    @Override
    public List<LintWarning> check(ClassNode classNode) {
        List<LintWarning> warnings = new ArrayList<>();

        if (classNode == null) {
            return warnings;
        }

        String thisInternalName = classNode.name;            // e.g. com/example/Foo
        String thisPackage = getPackageInternalName(thisInternalName);

        Map<String, Integer> couplingCounts = new HashMap<>();

        // fields 
        if (classNode.fields != null) {
            for (Object fObj : classNode.fields) {
                FieldNode field = (FieldNode) fObj;

                // field type from descriptor
                collectType(Type.getType(field.desc),
                        thisInternalName, thisPackage, couplingCounts);

                // generics signature if present 
                if (field.signature != null) {
                    collectFromSignature(field.signature,
                            thisInternalName, thisPackage, couplingCounts);
                }
            }
        }

        if (classNode.methods != null) {
            for (Object mObj : classNode.methods) {
                MethodNode method = (MethodNode) mObj;

                // Method descriptor: return + arguments
                Type methodType = Type.getMethodType(method.desc);
                collectType(methodType.getReturnType(),
                        thisInternalName, thisPackage, couplingCounts);
                for (Type argType : methodType.getArgumentTypes()) {
                    collectType(argType,
                            thisInternalName, thisPackage, couplingCounts);
                }

                // Local variables (if debug info is present)
                if (method.localVariables != null) {
                    for (Object lvObj : method.localVariables) {
                        LocalVariableNode lv = (LocalVariableNode) lvObj;
                        collectType(Type.getType(lv.desc),
                                thisInternalName, thisPackage, couplingCounts);
                    }
                }

                // Bytecode instructions
                AbstractInsnNode insn =
                        (method.instructions != null) ? method.instructions.getFirst() : null;
                while (insn != null) {
                    if (insn instanceof MethodInsnNode) {
                        MethodInsnNode mi = (MethodInsnNode) insn;
                        // owner is an internal type name, e.g. com/example/Bar
                        collectInternalName(mi.owner,
                                thisInternalName, thisPackage, couplingCounts);
                    } else if (insn instanceof FieldInsnNode) {
                        FieldInsnNode fi = (FieldInsnNode) insn;
                        collectInternalName(fi.owner,
                                thisInternalName, thisPackage, couplingCounts);
                        // also track the field's type
                        collectType(Type.getType(fi.desc),
                                thisInternalName, thisPackage, couplingCounts);
                    } else if (insn instanceof TypeInsnNode) {
                        // Instructions like NEW, ANEWARRAY, CHECKCAST, INSTANCEOF
                        TypeInsnNode ti = (TypeInsnNode) insn;
                        collectInternalName(ti.desc,
                                thisInternalName, thisPackage, couplingCounts);
                    }

                    insn = insn.getNext();
                }
            }
        }

        // here we decide if coupling is "too tight"
        for (Map.Entry<String, Integer> entry : couplingCounts.entrySet()) {
            String otherInternalName = entry.getKey();
            int count = entry.getValue();

            if (count >= COUPLING_THRESHOLD) {
                String otherClassName = otherInternalName.replace('/', '.');
                String message = "Class is tightly coupled to " + otherClassName
                        + " (" + count + " references)";
                warnings.add(new LintWarning(classNode.name, message));
            }
        }

        return warnings;
    }

    /**
     * Collects coupling information from a Type (field type, argument type, etc.).
     */
    private static void collectType(
            Type type,
            String thisInternalName,
            String thisPackage,
            Map<String, Integer> counts) {

        if (type == null) {
            return;
        }

        switch (type.getSort()) {
            case Type.ARRAY:
                // Only element type matters for coupling purposes
                collectType(type.getElementType(), thisInternalName, thisPackage, counts);
                break;
            case Type.OBJECT:
                collectInternalName(type.getInternalName(),
                        thisInternalName, thisPackage, counts);
                break;
            default:
                // primitives / void – no coupling
                break;
        }
    }

    // best-effor parser for generic signatures
    private static void collectFromSignature(
            String signature,
            String thisInternalName,
            String thisPackage,
            Map<String, Integer> counts) {

        if (signature == null) {
            return;
        }

        int idx = 0;
        int len = signature.length();

        while (idx < len) {
            int start = signature.indexOf('L', idx);
            if (start == -1) {
                break;
            }
            int end = signature.indexOf(';', start);
            if (end == -1) {
                break;
            }

            String internalName = signature.substring(start + 1, end);
            collectInternalName(internalName, thisInternalName, thisPackage, counts);

            idx = end + 1;
        }
    }

    // Records a reference to a given internal class name, after filtering out JDK classes, the current class, its inner classes, and same-package types.
    private static void collectInternalName(
            String internalName,
            String thisInternalName,
            String thisPackage,
            Map<String, Integer> counts) {

        if (internalName == null || internalName.isEmpty()) {
            return;
        }

        // Ignore common JDK / platform packages
        if (internalName.startsWith("java/")
                || internalName.startsWith("javax/")
                || internalName.startsWith("jakarta/")
                || internalName.startsWith("sun/")
                || internalName.startsWith("org/w3c/")
                || internalName.startsWith("org/xml/")) {
            return;
        }

        // Ignore self and its inner classes
        if (internalName.equals(thisInternalName)
                || internalName.startsWith(thisInternalName + "$")) {
            return;
        }

        String otherPackage = getPackageInternalName(internalName);

        // don't relly worry about same-package refercen
        if (otherPackage.equals(thisPackage)) {
            return;
        }

        counts.put(internalName, counts.getOrDefault(internalName, 0) + 1);
    }

    /**
     * Returns the "internal" package name, e.g. "com/example" for "com/example/Foo".
     */
    private static String getPackageInternalName(String internalName) {
        int idx = internalName.lastIndexOf('/');
        if (idx < 0) {
            return "";
        }
        return internalName.substring(0, idx);
    }
}
