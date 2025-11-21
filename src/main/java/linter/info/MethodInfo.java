package linter.info;

import java.lang.reflect.Type;
import java.util.List;

import org.objectweb.asm.tree.MethodNode;

public class MethodInfo {
    private MethodNode methodNode;
    private String name;
    private String methodSignature;
    private String returnType; // should we keep this as a string (describing) or make it a Type (more usable?)
    private List<Type> parameterTypes;
    private boolean isPublic;
    private boolean isStatic;
    
    
}
