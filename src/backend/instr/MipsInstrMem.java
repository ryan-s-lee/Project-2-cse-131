package backend.instr;

import backend.operand.MipsImmOp;
import backend.operand.MipsReg;

public class MipsInstrMem extends MipsInstr {

    public enum Op {
        LW,
        SW;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    MipsReg regData;
    MipsReg regWhence;
    MipsImmOp offset;
    Op op;

    public MipsInstrMem(
        MipsReg regData,
        MipsReg regWhence,
        MipsImmOp offset,
        Op op
    ) {
        this.regData = regData;
        this.regWhence = regWhence;
        this.offset = offset;
        this.op = op;
    }

    @Override
    public void printMipsRepresentation() {
        System.out.printf(
            "\t%s %s, %d(%s)\n",
            op.toString(),
            regData.toString(),
            offset.getVal(),
            regWhence.toString()
        );
    }

    @Override
    public boolean isBranch() {
        return false;
    }

    @Override
    public MipsReg[] getInRegOps() {
        switch (op) {
            case LW:
                return new MipsReg[]{regData};
            case SW:
                return new MipsReg[]{regData, regWhence};
            default:  // should never happen
                return new MipsReg[]{regData, regWhence};
        }
    }

    @Override
    public MipsReg getOutRegOp() {
        switch (op) {
            case LW:
                return  regData;
            case SW:
                return null;
            default:  // should never happen
                return null;
        }
    }

    @Override
    public MipsInstr regAllocTrans() {
        return regAllocTrans(MipsReg.T0, MipsReg.T1);
    }

    @Override
    public MipsInstr regAllocTrans(MipsReg... newRegs) {
        return new MipsInstrMem(regData.isSymbolic() ? newRegs[0] : regData, regWhence.isSymbolic() ? newRegs[1] : regWhence, offset, op);
    }
}
