package linter.collectors;

import org.objectweb.asm.tree.FieldNode;

import linter.info.FieldInfo;

public class FieldInfoCollector {

	public FieldInfo collectFieldInfo(FieldNode node) {
		return FieldInfo.fromFieldNode(node);
	}

}
