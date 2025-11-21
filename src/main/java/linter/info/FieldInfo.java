package linter.info;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.Type;
import java.util.Objects;

public class FieldInfo {

	private final String name;
	private final String descriptor;
	private final String friendlyType;
	private final boolean isPublic;

	public FieldInfo(String name, String descriptor, boolean isPublic) {
		this.name = Objects.requireNonNull(name);
		this.descriptor = Objects.requireNonNull(descriptor);
		this.isPublic = isPublic;
		String friendly;
		try {
			friendly = Type.getType(descriptor).getClassName();
		} catch (Exception e) {
			friendly = descriptor;
		}
		this.friendlyType = friendly;
	}

	public static FieldInfo fromFieldNode(FieldNode node) {
		return new FieldInfo(node.name, node.desc, (node.access & org.objectweb.asm.Opcodes.ACC_PUBLIC) != 0);
	}

	public String getName() {
		return name;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public String getFriendlyType() {
		return friendlyType;
	}

	public boolean isPublic() {
		return isPublic;
	}

}
