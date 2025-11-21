package linter.info;

import java.util.List;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodNode;

public class MethodInfo {
    private final String name;
    private final String methodSignature;
    private final String returnType;
    private final List<String> parameterTypeNames;
    private final boolean isPublic;
    private final boolean isStatic;
    private final InstructionsInfo instructions;

    public MethodInfo(String name, String methodSignature, String returnType, List<String> parameterTypeNames, boolean isPublic, boolean isStatic, InstructionsInfo instructions) {
        this.name = java.util.Objects.requireNonNull(name);
        this.methodSignature = java.util.Objects.requireNonNull(methodSignature);
        this.returnType = java.util.Objects.requireNonNull(returnType);
        this.parameterTypeNames = java.util.Collections.unmodifiableList(new java.util.ArrayList<>(parameterTypeNames == null ? java.util.Collections.emptyList() : parameterTypeNames));
        this.isPublic = isPublic;
        this.isStatic = isStatic;
        this.instructions = java.util.Objects.requireNonNull(instructions);
    }

    public static MethodInfo fromMethodNode(MethodNode node, InstructionsInfo instructions) {
        String name = node.name;
        String sig = node.desc;
        String ret;
        try {
            ret = Type.getReturnType(node.desc).getClassName();
        } catch (Exception e) {
            ret = "unknown";
        }
        java.util.List<String> params = new java.util.ArrayList<>();
        try {
            for (Type t : Type.getArgumentTypes(node.desc)) {
                params.add(t.getClassName());
            }
        } catch (Exception e) {
            // ignore
        }
        boolean pub = (node.access & org.objectweb.asm.Opcodes.ACC_PUBLIC) != 0;
        boolean stat = (node.access & org.objectweb.asm.Opcodes.ACC_STATIC) != 0;
        return new MethodInfo(name, sig, ret, params, pub, stat, instructions == null ? new InstructionsInfo(java.util.Collections.emptyList(), java.util.Collections.emptyList()) : instructions);
    }

    public String getName() {
        return name;
    }

    public String getMethodSignature() {
        return methodSignature;
    }

    public String getReturnType() {
        return returnType;
    }

    public List<String> getParameterTypeNames() {
        return parameterTypeNames;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public InstructionsInfo getInstructions() {
        return instructions;
    }

}
