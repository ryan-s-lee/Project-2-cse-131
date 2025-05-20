package backend.instr;

import backend.operand.MipsImmOp;
import backend.operand.MipsReg;

public class MipsInstrMove extends MipsInstr {

    MipsReg dest;
    MipsReg rs;
    MipsImmOp imm;

    public MipsInstrMove(MipsReg dest, MipsReg rs) {
        this.dest = dest;
        this.rs = rs;
        this.imm = null;
    }

    public MipsInstrMove(MipsReg dest, MipsImmOp imm) {
        this.dest = dest;
        this.rs = null;
        this.imm = imm;
    }

    @Override
    public void printMipsRepresentation() {
        if (imm == null) {
            System.out.printf("\tmove %s, %s\n", dest, rs);
        } else {
            if (imm.getVal() > Short.MAX_VALUE)
                System.out.printf("\tli %s, %s\n", dest, imm);
            else
                System.out.printf("\taddi %s, $zero, %s\n", dest, imm);
        }
    }

    @Override
    public boolean isBranch() {
        return false;
    }

    @Override
    public MipsReg[] getInRegOps() {
        if (imm == null) {
            return new MipsReg[]{rs};
        } else {
            return new MipsReg[0];
        }
    }

    @Override
    public MipsReg getOutRegOp() {
        return dest;
    }

    @Override
    public MipsInstr regAllocTrans() {
        return regAllocTrans(MipsReg.T0, MipsReg.T1);
    }

    @Override
    public MipsInstr regAllocTrans(MipsReg... newRegs) {
        if (imm == null) {
            return new MipsInstrMove(dest.isSymbolic() ? newRegs[0] : dest, rs.isSymbolic() ? newRegs[1] : rs);
        } else {
            return new MipsInstrMove(dest.isSymbolic() ? newRegs[0] : dest, imm);
        }
    }
}
