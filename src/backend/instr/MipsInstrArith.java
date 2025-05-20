package backend.instr;

import backend.operand.MipsImmOp;
import backend.operand.MipsReg;

public class MipsInstrArith extends MipsInstr {

    public enum Op {
        ADD,
        SUB,
        MUL,
        DIV,
        AND,
        OR,
        SLL;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    Op op;
    MipsReg dest;
    MipsReg rs;
    MipsReg rt;
    MipsImmOp immediate;

    boolean itype;

    public MipsInstrArith(
        Op op,
        MipsReg dest,
        MipsReg rs,
        MipsImmOp immediate
    ) {
        switch (op) {
            case MUL:
            case DIV:
                throw new RuntimeException(
                    op + " cannot be passed an immediate as an input operand"
                );
            default:
                break;
        }
        this.op = op;
        this.dest = dest;
        this.rs = rs;
        this.immediate = immediate;
        this.rt = null;
        this.itype = true;
    }

    public MipsInstrArith(Op op, MipsReg dest, MipsReg rs, MipsReg rt) {
        switch (op) {
            case SLL:
                throw new RuntimeException(
                    op + " cannot be passed two registers as input operands"
                );
            default:
                break;
        }
        this.op = op;
        this.dest = dest;
        this.rs = rs;
        this.immediate = null;
        this.rt = rt;
        this.itype = false;
    }

    @Override
    public void printMipsRepresentation() {
        switch (op) {
            case ADD:
            case AND:
            case OR:
            case DIV:
            case MUL:
                System.out.printf(
                    "\t%s %s, %s, %s\n",
                    op.toString() + (itype ? "i" : ""),
                    dest,
                    rs,
                    itype ? immediate : rt
                );
                break;
            case SUB:
                System.out.printf(
                    "\t%s %s, %s, %s\n",
                    (itype ? "addi" : "sub"),
                    dest,
                    rs,
                    itype ? -immediate.getVal() : rt
                );
                break;
            case SLL:
                System.out.printf(
                    "\t%s %s, %s, %s\n",
                    op.toString(),
                    dest,
                    rs,
                    immediate
                );
                break;
        }
    }

    @Override
    public boolean isBranch() {
        return false;
    }

    @Override
    public MipsReg[] getInRegOps() {
        if (itype) {
            return new MipsReg[] { rs };
        } else {
            return new MipsReg[] { rs, rt };
        }
    }

    @Override
    public MipsReg getOutRegOp() {
        return dest;
    }

    @Override
    public MipsInstr regAllocTrans() {
        return regAllocTrans(MipsReg.T0, MipsReg.T1, MipsReg.T2);
    }

    @Override
    public MipsInstr regAllocTrans(MipsReg... newRegs) {
        if (itype) {
            return new MipsInstrArith(op, dest.isSymbolic() ? newRegs[0] : dest, rs.isSymbolic() ? newRegs[1] : rs, immediate);
        } else {
            return new MipsInstrArith(op, dest.isSymbolic() ? newRegs[0] : dest, rs.isSymbolic() ? newRegs[1] : rs, rt.isSymbolic() ? newRegs[2] : rt);
        }
    }
}
