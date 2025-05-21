package backend.util;

import backend.MipsFunction;
import backend.instr.MipsInstr;
import backend.instr.MipsInstrArith;
import backend.instr.MipsInstrArith.Op;
import backend.instr.MipsInstrBranch;
import backend.instr.MipsInstrCall;
import backend.instr.MipsInstrBranch.Cmp;
import backend.instr.MipsInstrJumpLabel;
import backend.instr.MipsInstrMem;
import backend.instr.MipsInstrMove;
import backend.operand.MipsImmOp;
import backend.operand.MipsLabel;
import backend.operand.MipsReg;
import backend.operand.MipsReg.Type;
import ir.IRInstruction;
import ir.IRPrinter;
import ir.IRInstruction.OpCode;
import ir.datatype.IRIntType;
import ir.operand.IRConstantOperand;
import ir.operand.IRFunctionOperand;
import ir.operand.IRLabelOperand;
import ir.operand.IROperand;
import ir.operand.IRVariableOperand;
import java.util.ArrayList;
import java.util.List;

public class IrInstrToMipsInstr {

    private static int nextTempIdx = 0;

    public static void appendTranslation(
        IRInstruction iri,
        MipsFunction owner,
        List<MipsFunction> funcs
    ) {
        switch (iri.opCode) {
            case ASSIGN:
                if (iri.operands.length > 2) {
                    tAssignArr(iri, owner);
                } else {
                    tAssign(iri, owner);
                }
                break;
            case ADD:
                tArith(iri, owner, Op.ADD);
                break;
            case SUB:
                tArith(iri, owner, Op.SUB);
                break;
            case MULT:
                tArith(iri, owner, Op.MUL);
                break;
            case DIV:
                tArith(iri, owner, Op.DIV);
                break;
            case AND:
                tArith(iri, owner, Op.AND);
                break;
            case OR:
                tArith(iri, owner, Op.OR);
                break;
            case BREQ:
                tBranch(iri, owner, Cmp.BEQ);
                break;
            case BRNEQ:
                tBranch(iri, owner, Cmp.BNE);
                break;
            case BRLT:
                tBranch(iri, owner, Cmp.BLT);
                break;
            case BRGT:
                tBranch(iri, owner, Cmp.BGT);
                break;
            case BRLEQ:
                tBranch(iri, owner, Cmp.BLE);
                break;
            case BRGEQ:
                tBranch(iri, owner, Cmp.BGE);
                break;
            case RETURN:
                tReturn(iri, owner);
                break;
            case CALL:
            case CALLR:
                tCall(iri, owner, funcs);
                break;
            case ARRAY_STORE:
            case ARRAY_LOAD:
                tArrMem(iri, owner);
                break;
            case LABEL:
                tLabel(iri, owner);
                break;
            case GOTO:
                tGoto(iri, owner);
                break;
            default:
                throw new RuntimeException(
                    "Could not map IR instruction to MIPS instruction:" + iri.opCode
                );
        }
    }

    private static void tGoto(IRInstruction iri, MipsFunction owner) {
        MipsLabel target = MipsLabel.of(owner.name + "_" + ((IRLabelOperand)iri.operands[0]).getName());
        owner.instrs.add(new MipsInstrJumpLabel(target));
	}

	private static void tLabel(IRInstruction iri, MipsFunction owner) {
		owner.instrs.add(MipsLabel.of(owner.name + "_" + ((IRLabelOperand)iri.operands[0]).getName()));
	}

	private static void tAssign(IRInstruction iri, MipsFunction owner) {
        // TODO
        // search for immediate. If there is one, use li
        // otherwise, use move
        MipsReg[] regs = getRegs(iri, owner);
        Integer[] imms = getImms(iri, owner);
        List<MipsInstr> translation = new ArrayList<>();
        if (imms.length == 1 && regs.length == 1) {
            translation.add(new MipsInstrMove(regs[0], MipsImmOp.of(imms[0])));
        } else if (imms.length == 0 && regs.length == 2) {
            translation.add(new MipsInstrMove(regs[0], regs[1]));
        } else {
            throw new RuntimeException("Unexpected operands");
        }

        owner.instrs.addAll(translation);
    }

    private static void tAssignArr(IRInstruction iri, MipsFunction owner) {
        // TODO
        //
        // generate an inline function! that loops over each element of the array.
        //
        // create a new symbolic register to be an iterator over this array
        MipsReg tempReg = newTempReg(owner);

        List<MipsInstr> translation = new ArrayList<>();
        // # load the position of the array into the iterating register
        // # If array is a pointer:
        // move $temp, $array
        // # else if array is an actual array:
        // lui $temp, $temp, $arrayUpper
        // or $temp, $temp, $arrayLower
        throw new UnsupportedOperationException("Unimplemented method 'tAssignArr'");
    }

	private static void tArith(
        IRInstruction iri,
        MipsFunction owner,
        Op operation
    ) {
        MipsReg[] regs = getRegs(iri, owner);
        Integer[] imms = getImms(iri, owner);

        MipsReg dest = regs[0]; // always expect there to be one destination register at the front
        int numImmediate = imms.length;
        List<MipsInstr> translation = new ArrayList<>();

        int evaluatedImm = 0; // bruh
        switch (operation) {
            case ADD:
                if (numImmediate == 2) evaluatedImm = imms[0] + imms[1];
                break;
            case SUB:
                if (numImmediate == 2) evaluatedImm = imms[0] - imms[1];
                break;
            case MUL:
                if (numImmediate == 2) evaluatedImm = imms[0] * imms[1];
                break;
            case DIV:
                if (numImmediate == 2) evaluatedImm = imms[0] / imms[1];
                break;
            case AND:
                if (numImmediate == 2) evaluatedImm = imms[0] & imms[1];
                break;
            case OR:
                if (numImmediate == 2) evaluatedImm = imms[0] | imms[1];
                break;
            default:
                throw new RuntimeException(
                    "Unexpected opcode when translating arithmetic ir instruction: " +
                    iri.opCode
                );
        }
        if (numImmediate == 2 && regs.length == 1) {
            if (evaluatedImm <= Short.MAX_VALUE) {
                translation.add(
                    new MipsInstrMove(dest, MipsImmOp.of(evaluatedImm))
                );
            } else {
                translation.add(
                    new MipsInstrArith(
                        operation,
                        dest,
                        MipsReg.ZERO,
                        MipsImmOp.of(evaluatedImm)
                    )
                );
            }
        } else if (numImmediate == 1 && regs.length == 2) {
            int imm = imms[0];
            MipsReg rs = regs[1];
            // right now it is easier to always use two instructions.
            // However, we can save an instruction in the case that an immediate version
            // of the operation exists.
            // Also gotta watch out for when dest == rs!
            // if (imm > Short.MAX_VALUE) {
            MipsReg tempReg = newTempReg(owner);
                translation.add(new MipsInstrMove(tempReg, MipsImmOp.of(imm)));
                translation.add(
                    new MipsInstrArith(
                        operation,
                        dest,
                        rs,
                        tempReg
                    )
                );
            // }
            // else {
            // translation.add(
            //     new MipsInstrArith(operation, dest, rs, MipsImmOp.of(imm))
            // );
            // }
        } else if (numImmediate == 0 && regs.length == 3) {
            translation.add(
                new MipsInstrArith(operation, dest, regs[1], regs[2])
            );
        } else {
            throw new RuntimeException("Unexpected operand types");
        }
        owner.instrs.addAll(translation);
    }

    private static void tBranch(
        IRInstruction iri,
        MipsFunction owner,
        MipsInstrBranch.Cmp cmp
    ) {
        List<MipsInstr> translation = new ArrayList<>();
        MipsReg[] regs = getRegs(iri, owner);
        Integer[] imms = getImms(iri, owner);
        IRLabelOperand irlabel = (IRLabelOperand) iri.operands[0];
        MipsLabel label = owner.irToMipsLabel.computeIfAbsent(
            irlabel,
            (IRLabelOperand e) -> MipsLabel.of(owner.name + "_" + e.getName())
        );
        // if there are two immediates this instruction can be replaced
        // with a goto lol
        if (imms.length == 2 && regs.length == 0) {
            boolean evaluation;
            switch (cmp) {
                case BEQ:
                    evaluation = imms[0] == imms[1];
                    break;
                case BNE:
                    evaluation = imms[0] != imms[1];
                    break;
                case BLT:
                    evaluation = imms[0] < imms[1];
                    break;
                case BGT:
                    evaluation = imms[0] > imms[1];
                    break;
                case BLE:
                    evaluation = imms[0] <= imms[1];
                    break;
                case BGE:
                    evaluation = imms[0] >= imms[1];
                    break;
                default:
                    throw new RuntimeException("unexpected comparator: " + cmp);
            }
            if (evaluation) {
                translation.add(new MipsInstrJumpLabel(label));
            }
        } else if (imms.length == 1 && regs.length == 1) {
            MipsReg newReg = newTempReg(owner);
            translation.add(new MipsInstrMove(newReg, MipsImmOp.of(imms[0])));
            // we need to retrieve the ordering from the instruction...
            if (iri.operands[1] instanceof IRConstantOperand) translation.add(
                new MipsInstrBranch(cmp, newReg, regs[0], label)
            );
            else translation.add(
                new MipsInstrBranch(cmp, regs[0], newReg, label)
            );
        } else if (imms.length == 0 && regs.length == 2) {
            translation.add(new MipsInstrBranch(cmp, regs[0], regs[1], label));
        } else {
            throw new RuntimeException("Unexpected operands");
        }
        owner.instrs.addAll(translation);
    }

    private static void tReturn(IRInstruction iri, MipsFunction owner) {
        // return will always jump to a special label that precedes the epilogue.
        MipsReg[] regs = getRegs(iri, owner);
        Integer[] imms = getImms(iri, owner);
        if (regs.length == 1) {
            owner.instrs.add(new MipsInstrMove(MipsReg.V0, regs[0]));
        } else if (imms.length == 1) {
            owner.instrs.add(new MipsInstrMove(MipsReg.V0, MipsImmOp.of(imms[0])));
        } else {
            IRPrinter printer = new IRPrinter(System.err);
            printer.printInstruction(iri);
            System.err.println("# of regs found in instruction: " + regs.length);
            System.err.printf("operand 0: %s\n", iri.operands[0]);
            System.err.printf("operand 1: %s\n", iri.operands[1]);
            throw new RuntimeException("Unexpected operands");
        }
        owner.instrs.add(
            new MipsInstrJumpLabel(MipsLabel.of(owner.name + "_epilogue"))
        );
    }

    private static void tCall(
        IRInstruction iri,
        MipsFunction owner,
        List<MipsFunction> funcs
    ) {
        // Determine the minimum number of arguments used between the owner function and callee
        // This number, minArgs, is the number of arguments that must be saved to the stack.
        boolean returns = iri.opCode == OpCode.CALLR;
        int targetOpIdx = returns ? 1 : 0;
        List<MipsInstr> translation = new ArrayList<>();

        MipsFunction calledFunc = null;
        String target = (((IRFunctionOperand)iri.operands[targetOpIdx]).getName());
        if (MipsFunction.intrinsics.containsKey(target)) {
            calledFunc = MipsFunction.intrinsics.get(target);
        }
        else {
            for (MipsFunction func : funcs) {
                if (func.name.equals(target)) {
                    calledFunc = func;
                    break;
                }
            }
        }

        if (calledFunc == null) {
            throw new RuntimeException(String.format("Failed to find matching function. target: %s", target));
        }

        int minArgs = Math.min(owner.parameters.size(), calledFunc.parameters.size());
        for (int i = 0; i < minArgs; i++) {
            MipsReg ownerArg = owner.parameters.get(i);
            translation.add(new MipsInstrMem(MipsReg.A[i], MipsReg.SP, owner.symbolToStackOff.get(ownerArg), MipsInstrMem.Op.SW));
        }

        // Move the passed arguments to $a0-$a3
        // If the passed-in argument is a symbolic array, move the stack position in instead.
        // NOTE: This stack position will not be known until after register allocation.
        // Hence, we will need to create a mapping of MipsRegs to immediates.
        // These immediates will be initialized to 0, but should eventually updated with their appropriate stack positions.
        int callArgStart = targetOpIdx + 1;
        for (int i = callArgStart; i < iri.operands.length; i++) {
            int aRegIdx = i - callArgStart;
            IROperand irop = iri.operands[i];
            if (irop instanceof IRConstantOperand) {
                IRConstantOperand ircop = (IRConstantOperand) irop;
                translation.add(new MipsInstrMove(MipsReg.A[aRegIdx], MipsImmOp.of(Integer.valueOf(ircop.getValueString()))));
            } else if (irop instanceof IRVariableOperand) {
                IRVariableOperand irvop = (IRVariableOperand) irop;
                MipsReg argumentReg = owner.irVarToMipsReg(irvop);

                if (argumentReg.getType() == Type.SYMBOLICARR) {
                    translation.add(new MipsInstrMove(MipsReg.A[aRegIdx], MipsReg.SP));
                    translation.add(new MipsInstrArith(Op.ADD, MipsReg.A[aRegIdx], MipsReg.A[aRegIdx], owner.symbolToStackOff.get(argumentReg)));
                } else {
                    translation.add(new MipsInstrMove(MipsReg.A[aRegIdx], argumentReg));
                }
            }
        }
        if (calledFunc.parameters.size() > 4) {
            throw new RuntimeException("More than four parameters in function call; compiler cannot currently handle this case");
        }

        // TODO: move all this commenting below to the call print function.
        // The number of temporary variables that we need to save is also not known until register allocation.
        // Additionally, we do not know which symbolic register will be map to those temp variables until register allocation
        // furthermore, even after reg alloc, reversing the mapping at this point in time is an utter pain.
        // We will thus have a "placeholder" call instruction
        // that will eventually compute the appropriate stores/loads to print after register allocation.
        // Additionally, we will simply allocate more space on the stack for these temp variables,
        // rather than trying to figure out where on the stack their mapped symbolic registers went.
        // The call will also handle
        // If call to one of the intrinsics, syscall
        // otherwise, jal to appropriate label.
        // if instruction is callr, move $v0 to the symbolic register
        translation.add(new MipsInstrCall(owner, calledFunc, returns));
        if (returns) {
            MipsReg dest = owner.irVarToMipsReg((IRVariableOperand)iri.operands[0]);
            translation.add(new MipsInstrMove(dest, MipsReg.V0));
        }

        // restore arguments
        for (int i = 0; i < minArgs; i++) {
            MipsReg ownerArg = owner.parameters.get(i);
            translation.add(new MipsInstrMem(MipsReg.A[i], MipsReg.SP, owner.symbolToStackOff.get(ownerArg), MipsInstrMem.Op.LW));
        }

        owner.instrs.addAll(translation);
    }

    private static void tArrMem(IRInstruction iri, MipsFunction owner) {
        // TODO: consider the case where immediate is larger than a short (unlikely)
        // if array is a parameter and offset is an immediate, compute byte offset using ir immediate
        // then use lw $dest, soff($A)
        // this works for both loads and stores since it doesn't mutate the destination.
        //
        // if array is a parameter and offset is variable, and loading:
        // sll $dest, $offset, 2
        // add $dest, $dest, $array
        // lw dest 0($dest)
        //
        // if array is a parameter and offset is variable, and storing:
        // make new temp symbolic register
        // sll $temp, $offset, 2
        // add $temp, $temp, $array
        // sw dest 0($temp)
        //
        //
        // if array is a variable (and is thus some ethereal stack position) and offset is immediate, and loading:
        // move $dest, $sp
        // addi $dest, arrImm
        // lw $dest, soff($dest)
        //
        // if array is a variable (and is thus some ethereal stack position) and offset is immediate, and storing:
        // make new temp symbolic register
        // move $temp, $sp
        // addi $temp, arrImm  // high chance that array offset in stack frame is smaller than a short
        // sw $dest, soff($temp)
        //
        // if array is a variable and offset is also a variable, we need an extra register!:
        // move $dest, $sp
        // addi $dest, arrImm  // high chance that array offset in stack frame is smaller than a short
        // sll $temp, $offset, 2
        // add $dest, $dest, $temp
        // lw $dest, 0($dest)
        //
        IRVariableOperand data = (IRVariableOperand)iri.operands[0];  // first operand must be a variable
        IRVariableOperand array = (IRVariableOperand)iri.operands[1];  // second op must be array
        IROperand offOp = iri.operands[2];
        MipsReg regData = owner.irVarToMipsReg(data);

        MipsReg arrayReg = owner.irVarToMipsReg(array);
        List<MipsInstr> translation = new ArrayList<>();
        backend.instr.MipsInstrMem.Op op = (iri.opCode == OpCode.ARRAY_LOAD ? backend.instr.MipsInstrMem.Op.LW : MipsInstrMem.Op.SW);

        boolean regIsPtr =  arrayReg.getType() == Type.SYMBOLICPTR || arrayReg.getType() == Type.ARG;

        if (regIsPtr && offOp instanceof IRConstantOperand) {
            int imm = Integer.valueOf(((IRConstantOperand)offOp).getValueString()) << 2;
            translation.add(new MipsInstrMem(regData, arrayReg, MipsImmOp.of(imm), op));
        } else if (regIsPtr && offOp instanceof IRVariableOperand) {
            MipsReg offReg = owner.irVarToMipsReg((IRVariableOperand)offOp);
            MipsReg byteOffReg;
            if (op == MipsInstrMem.Op.SW) byteOffReg = newTempReg(owner); else byteOffReg = regData;
            translation.add(new MipsInstrArith(Op.SLL, byteOffReg, offReg, MipsImmOp.of(2)));
            translation.add(new MipsInstrArith(Op.ADD, byteOffReg, byteOffReg, arrayReg));
            translation.add(new MipsInstrMem(regData, byteOffReg, MipsImmOp.of(0), op));
        } else if (arrayReg.getType() == Type.SYMBOLICARR && offOp instanceof IRConstantOperand) {
            MipsReg arrPosReg;
            if (op == MipsInstrMem.Op.SW) arrPosReg = newTempReg(owner); else arrPosReg = regData;
            int imm = Integer.valueOf(((IRConstantOperand)offOp).getValueString()) << 2;
            MipsImmOp arrOffsetImm = owner.symbolToStackOff.get(arrayReg);

            translation.add(new MipsInstrMove(arrPosReg, MipsReg.SP));
            translation.add(new MipsInstrArith(Op.ADD, arrPosReg, arrPosReg, arrOffsetImm));
            translation.add(new MipsInstrMem(regData, arrPosReg, MipsImmOp.of(imm), op));
        } else if (arrayReg.getType() == Type.SYMBOLICARR && offOp instanceof IRVariableOperand) {
            // if array is and offset are variables, and storing, we need two extra symbols!!
            // move $temp0, $sp
            // addi $temp0, $temp0, arrayImm
            // sll $temp1, $offset, 2
            // add $temp0, $temp0, $temp1
            // sw $data, 0($temp0)
            MipsReg arrPosReg;
            if (op == MipsInstrMem.Op.SW) arrPosReg = newTempReg(owner); else arrPosReg = regData;
            MipsReg byteOffsetReg = newTempReg(owner);
            MipsImmOp arrOffsetImm = owner.symbolToStackOff.get(arrayReg);
            MipsReg offsetReg = owner.irVarToMipsReg((IRVariableOperand)offOp);

            translation.add(new MipsInstrMove(arrPosReg, MipsReg.SP));
            translation.add(new MipsInstrArith(Op.ADD, arrPosReg, arrPosReg, arrOffsetImm));
            translation.add(new MipsInstrArith(Op.SLL, byteOffsetReg, offsetReg, MipsImmOp.of(2)));
            translation.add(new MipsInstrArith(Op.ADD, arrPosReg, arrPosReg, byteOffsetReg));
            translation.add(new MipsInstrMem(regData, arrPosReg, MipsImmOp.of(0), op));
        } else {
            throw new RuntimeException("Unexpectedly, array register was not a pointer or array");
        }
        owner.instrs.addAll(translation);
    }

    private static MipsReg newTempReg(MipsFunction owner) {
        final String tempRegPrefix = "X";
        MipsReg newReg = new MipsReg(tempRegPrefix + nextTempIdx++);
        owner.variables.add(newReg);
        owner.symbolToStackOff.put(newReg, new MipsImmOp(0));
        return newReg;
    }

    private static MipsReg[] getRegs(IRInstruction iri, MipsFunction func) {
        List<MipsReg> result = new ArrayList<>();

        for (int i = 0; i < iri.operands.length; i++) {
            if (iri.operands[i] instanceof IRVariableOperand) {
                result.add(
                    func.irVarToMipsReg((IRVariableOperand) iri.operands[i])
                );
            }
        }
        return result.toArray(new MipsReg[0]);
    }

    private static Integer[] getImms(IRInstruction iri, MipsFunction func) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < iri.operands.length; i++) {
            if (
                iri.operands[i] instanceof IRConstantOperand &&
                ((IRConstantOperand) iri.operands[i]).type == IRIntType.get()
            ) {
                result.add(
                    Integer.valueOf(
                        ((IRConstantOperand) iri.operands[i]).getValueString()
                    )
                );
            }
        }
        return result.toArray(new Integer[0]);
    }
}
