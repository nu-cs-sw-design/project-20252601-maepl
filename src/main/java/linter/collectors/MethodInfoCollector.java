package linter.collectors;

import org.objectweb.asm.tree.MethodNode;

import linter.info.MethodInfo;
import linter.info.InstructionsInfo;

public class MethodInfoCollector {

	private InstructionsCollector instructionsCollector = new InstructionsCollector();

	public MethodInfo collectMethodInfo(MethodNode node) {
		InstructionsInfo insnInfo = instructionsCollector.collectInstructions(node);
		return MethodInfo.fromMethodNode(node, insnInfo);
	}

}
