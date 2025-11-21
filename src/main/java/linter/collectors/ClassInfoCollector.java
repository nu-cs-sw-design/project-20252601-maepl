package linter.collectors;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import linter.info.ClassInfo;

public class ClassInfoCollector {

    public ClassInfo collectClassInfo(ClassNode classNode) {

        // might be nicer style to make each of these params their own local var beforehand to be clearer about what we're passing, but its pretty obvious.
        ClassInfo info = new ClassInfo(classNode.name, classNode.superName, classNode.interfaces, (classNode.access & Opcodes.ACC_PUBLIC) != 0);

        return info;

    }
    
}
