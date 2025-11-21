package linter.collectors;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import linter.info.ClassInfo;
import linter.info.FieldInfo;
import linter.info.MethodInfo;

public class ClassInfoCollector {

    private FieldInfoCollector fieldCollector = new FieldInfoCollector();
    private MethodInfoCollector methodCollector = new MethodInfoCollector();

    public ClassInfo collectClassInfo(ClassNode classNode) {

        List<FieldNode> fieldNodes = (List<FieldNode>) classNode.fields;
        List<FieldInfo> fieldInfos = new java.util.ArrayList<>();
        for (FieldNode f : fieldNodes) {
            fieldInfos.add(fieldCollector.collectFieldInfo(f));
        }

        List<MethodNode> methodNodes = (List<MethodNode>) classNode.methods;
        List<MethodInfo> methodInfos = new java.util.ArrayList<>();
        for (MethodNode m : methodNodes) {
            methodInfos.add(methodCollector.collectMethodInfo(m));
        }

        ClassInfo info = new ClassInfo(classNode.name, classNode.superName, classNode.interfaces, (classNode.access & Opcodes.ACC_PUBLIC) != 0, fieldInfos, methodInfos);

        return info;

    }
    
}
