package linter.collectors;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import linter.info.InstructionsInfo;

public class InstructionsCollector {

	public InstructionsInfo collectInstructions(MethodNode methodNode) {
		if (methodNode == null || methodNode.instructions == null) {
			return new InstructionsInfo(java.util.Collections.emptyList(), java.util.Collections.emptyList());
		}

		java.util.List<InstructionsInfo.MethodCall> methodCalls = new java.util.ArrayList<>();
		java.util.List<InstructionsInfo.VarInsn> varInsns = new java.util.ArrayList<>();

		for (int i = 0; i < methodNode.instructions.size(); i++) {
			AbstractInsnNode insn = methodNode.instructions.get(i);
			if (insn instanceof MethodInsnNode) {
				MethodInsnNode m = (MethodInsnNode) insn;
				methodCalls.add(new InstructionsInfo.MethodCall(m.owner, m.name));
			} else if (insn instanceof VarInsnNode) {
				VarInsnNode v = (VarInsnNode) insn;
				varInsns.add(new InstructionsInfo.VarInsn(v.getOpcode()));
			}
		}

		return new InstructionsInfo(methodCalls, varInsns);
	}

}
