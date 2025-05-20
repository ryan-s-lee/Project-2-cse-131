package backend.instr;

import backend.operand.MipsLabel;
import backend.operand.MipsReg;

public class MipsInstrJumpLabel extends MipsInstr {

    MipsLabel target;

    public MipsInstrJumpLabel(MipsLabel target) {
        this.target = target;
    }

    @Override
    public void printMipsRepresentation() {
        System.out.printf("\tj %s\n", target.getName());
    }

    @Override
    public boolean isBranch() {
        return true;
    }

    @Override
    public MipsReg[] getInRegOps() {
        return new MipsReg[0];
    }

    @Override
    public MipsReg getOutRegOp() {
        return null;
    }

    @Override
    public MipsInstr regAllocTrans() {
        return new MipsInstrJumpLabel(target);
    }

    @Override
    public MipsInstr regAllocTrans(MipsReg... newRegs) {
        return new MipsInstrJumpLabel(target);
    }
}
