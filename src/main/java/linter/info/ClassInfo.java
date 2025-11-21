package linter.info;

import java.util.List;

public class ClassInfo {

    private String internalName;
    private String superName;
    private List<String> interfaceNames;
    private boolean isPublic;

    public ClassInfo(String internalName, String superName, List<String> interfaceNames, boolean isPublic) {
        this.internalName = internalName;
        this.superName = superName;
        this.interfaceNames = interfaceNames;
        this.isPublic = isPublic;
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
    
}
