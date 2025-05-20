package backend.instr;

import backend.operand.MipsImmOp;
import backend.operand.MipsReg;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    public Set<MipsReg> getInRegOps() {
        switch (op) {
            case LW:
                return new HashSet<>(Arrays.asList(regWhence));
            case SW:
                return new HashSet<>(Arrays.asList(regWhence, regData));
            default:  // should never happen
                return new HashSet<>(Arrays.asList(regWhence, regData));
        }
    }

    @Override
    public Set<MipsReg> getOutRegOps() {
        switch (op) {
            case LW:
                return new HashSet<>(Arrays.asList(regData));
            case SW:
                return new HashSet<>();
            default:  // should never happen
                return new HashSet<>();
        }
    }
}
