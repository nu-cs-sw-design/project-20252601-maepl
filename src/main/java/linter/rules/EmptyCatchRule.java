package linter.rules;

import java.util.List;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.Opcodes;

public class EmptyCatchRule implements LintRule {

    @Override
    public List<LintWarning> check(ClassNode classNode) {
        List<LintWarning> warnings = new java.util.ArrayList<>();

        // Go through all methods in the class
        for (MethodNode method : classNode.methods) {
            // Check if the method has try catch blocks
            if (method.tryCatchBlocks == null || method.tryCatchBlocks.isEmpty()) {
                continue;
            }

            // Check each try catch block
            for (TryCatchBlockNode tryCatchBlock : method.tryCatchBlocks) {
                if (isCatchBlockEmpty(method, tryCatchBlock)) {
                    String exceptionType = tryCatchBlock.type != null ? tryCatchBlock.type : "any exception";
                    warnings.add(new LintWarning(
                        classNode.name,
                        "Empty catch block in method '" + method.name + "' for " + exceptionType
                    ));
                }
            }
        }

        return warnings;
    }

    /**
     * Helper to check if a catch block is empty (contains no meaningful instructions).
     * A catch block is considered empty if it only contains:
     * - Label nodes
     * - Line number nodes
     * - Frame nodes
     * - A single RETURN, ARETURN, IRETURN, etc. instruction
     */
    private boolean isCatchBlockEmpty(MethodNode method, TryCatchBlockNode tryCatchBlock) {
        LabelNode handlerLabel = tryCatchBlock.handler;

        int handlerIndex = method.instructions.indexOf(handlerLabel);
        if (handlerIndex == -1) {
            return false;
        }

        int meaningfulInstructions = 0;
        AbstractInsnNode current = method.instructions.get(handlerIndex).getNext();

        while (current != null) {
            int opcode = current.getOpcode();

            if (current instanceof LabelNode && current != handlerLabel) {
                boolean isAnotherHandler = false;
                for (TryCatchBlockNode otherBlock : method.tryCatchBlocks) {
                    if (otherBlock.handler == current && otherBlock != tryCatchBlock) {
                        isAnotherHandler = true;
                        break;
                    }
                }
                if (isAnotherHandler) {
                    break;
                }
            }

            if (opcode == -1) {
                current = current.getNext();
                continue;
            }

            if (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) {
                // A catch block with only a return is considered empty
                current = current.getNext();
                continue;
            }

            if (opcode == Opcodes.POP || opcode == Opcodes.POP2) {
                current = current.getNext();
                continue;
            }

            // Check for ASTORE (storing exception to local variable without using it)
            if (opcode >= Opcodes.ISTORE && opcode <= Opcodes.ASTORE) {
                current = current.getNext();
                continue;
            }

            if (opcode == Opcodes.ATHROW) {
                return false;
            }

            if (opcode == Opcodes.GOTO) {
                current = current.getNext();
                continue;
            }

            meaningfulInstructions++;

            if (meaningfulInstructions > 0) {
                return false;
            }

            current = current.getNext();
        }

        return true;
    }

}
