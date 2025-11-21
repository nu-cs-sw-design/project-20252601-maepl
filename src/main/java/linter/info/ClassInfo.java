package linter.info;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ClassInfo {

    private final String internalName;
    private final String superName;
    private final List<String> interfaceNames;
    private final boolean isPublic;
    private final List<FieldInfo> fields;
    private final List<MethodInfo> methods;

    public ClassInfo(String internalName, String superName, List<String> interfaceNames, boolean isPublic, List<FieldInfo> fields, List<MethodInfo> methods) {
        this.internalName = Objects.requireNonNull(internalName);
        this.superName = superName; // superName may be null for java.lang.Object
        this.interfaceNames = Collections.unmodifiableList(interfaceNames == null ? Collections.emptyList() : new java.util.ArrayList<>(interfaceNames));
        this.isPublic = isPublic;
        this.fields = Collections.unmodifiableList(fields == null ? Collections.emptyList() : new java.util.ArrayList<>(fields));
        this.methods = Collections.unmodifiableList(methods == null ? Collections.emptyList() : new java.util.ArrayList<>(methods));
    }

    public String getInternalName() {
        return internalName;
    }

    public String getSuperName() {
        return superName;
    }

    public List<String> getInterfaceNames() {
        return interfaceNames;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public List<FieldInfo> getFields() {
        return fields;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

}
