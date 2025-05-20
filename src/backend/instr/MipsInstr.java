package backend.instr;

import backend.operand.MipsReg;
import java.util.Set;

public abstract class MipsInstr implements MipsLine {

    public abstract boolean isBranch();

    public Set<MipsReg> getRegOps() {
        Set<MipsReg> regs = getInRegOps();
        regs.addAll(getOutRegOps());
        return regs;
    }

    public abstract Set<MipsReg> getInRegOps();

    public abstract Set<MipsReg> getOutRegOps();
}
