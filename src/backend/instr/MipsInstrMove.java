package backend.instr;

import backend.operand.MipsImmOp;
import backend.operand.MipsReg;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    public Set<MipsReg> getInRegOps() {
        if (imm == null) {
            return new HashSet<>(Arrays.asList(rs));
        } else {
            return new HashSet<>();
        }
    }

    @Override
    public Set<MipsReg> getOutRegOps() {
        return new HashSet<>(Arrays.asList(dest));
    }
}
