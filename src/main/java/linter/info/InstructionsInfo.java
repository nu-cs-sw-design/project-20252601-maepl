package linter.info;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class InstructionsInfo {

	public static class MethodCall {
		private final String owner;
		private final String name;

		public MethodCall(String owner, String name) {
			this.owner = Objects.requireNonNull(owner);
			this.name = Objects.requireNonNull(name);
		}

		public String getOwner() {
			return owner;
		}

		public String getName() {
			return name;
		}
	}

	public static class VarInsn {
		private final int opcode;

		public VarInsn(int opcode) {
			this.opcode = opcode;
		}

		public int getOpcode() {
			return opcode;
		}
	}

	private final List<MethodCall> methodCalls;
	private final List<VarInsn> varInsns;

	public InstructionsInfo(List<MethodCall> methodCalls, List<VarInsn> varInsns) {
		this.methodCalls = Collections.unmodifiableList(Objects.requireNonNull(methodCalls));
		this.varInsns = Collections.unmodifiableList(Objects.requireNonNull(varInsns));
	}

	public List<MethodCall> getMethodCalls() {
		return methodCalls;
	}

	public List<VarInsn> getVarInsns() {
		return varInsns;
	}

}
