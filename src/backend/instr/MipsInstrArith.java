package backend.instr;

import backend.operand.MipsImmOp;
import backend.operand.MipsReg;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    public Set<MipsReg> getInRegOps() {
        if (itype) {
            return new HashSet<>(Arrays.asList(rs));
        } else {
            return new HashSet<>(Arrays.asList(rs, rt));
        }
    }

    @Override
    public Set<MipsReg> getOutRegOps() {
        return new HashSet<>(Arrays.asList(dest));
    }
}
