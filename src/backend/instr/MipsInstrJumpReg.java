package backend.instr;

import backend.operand.MipsReg;

public class MipsInstrJumpReg extends MipsInstr {

    MipsReg rs;

    public MipsInstrJumpReg(MipsReg rs) {
        this.rs = rs;
    }

    @Override
    public void printMipsRepresentation() {
        System.out.printf("jr %s\n", rs);
    }

    @Override
    public boolean isBranch() {
        return true;
    }

    @Override
    public MipsReg[] getInRegOps() {
        return new MipsReg[] { rs };
    }

    @Override
    public MipsReg getOutRegOp() {
        return null;
    }

    @Override
    public MipsInstr regAllocTrans() {
        return regAllocTrans(MipsReg.RA);
    }

    @Override
    public MipsInstr regAllocTrans(MipsReg... newRegs) {
        return new MipsInstrJumpReg(newRegs[0]);
    }
}
