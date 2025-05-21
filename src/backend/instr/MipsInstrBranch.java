package backend.instr;

import backend.operand.MipsLabel;
import backend.operand.MipsReg;

public class MipsInstrBranch extends MipsInstr {

    public enum Cmp {
        BEQ,
        BNE,
        BGT,
        BGE,
        BLT,
        BLE;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    Cmp comparator;
    MipsReg rs;
    MipsReg rt;
    MipsLabel target;

    public MipsInstrBranch(
        Cmp comparator,
        MipsReg rs,
        MipsReg rt,
        MipsLabel target
    ) {
        this.comparator = comparator;
        this.rs = rs;
        this.rt = rt;
        this.target = target;
    }

    @Override
    public void printMipsRepresentation() {
        System.out.printf(
            "\t%s %s, %s, %s\n",
            comparator,
            rs,
            rt,
            target.getName()
        );
    }

    @Override
    public boolean isBranch() {
        return true;
    }

    @Override
    public MipsReg[] getInRegOps() {
        return new MipsReg[] { rs, rt };
    }

    @Override
    public MipsReg getOutRegOp() {
        return null;
    }

    @Override
    public MipsInstr regAllocTrans() {
        return regAllocTrans(MipsReg.T1, MipsReg.T2);
    }

    @Override
    public MipsInstr regAllocTrans(MipsReg... newRegs) {
        return new MipsInstrBranch(comparator, rs.isSymbolic() ? newRegs[0] : rs, rt.isSymbolic() ? newRegs[1] : rt, target);
    }
}
