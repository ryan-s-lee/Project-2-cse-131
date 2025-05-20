package backend.instr;

import backend.operand.MipsReg;

import java.util.HashSet;
import java.util.Set;

public abstract class MipsInstr implements MipsLine {

    public abstract boolean isBranch();

    public Set<MipsReg> getRegOps() {
        Set<MipsReg> regs = new HashSet<MipsReg>();
        for (MipsReg reg : getInRegOps()) regs.add(reg);
        regs.add(getOutRegOp());
        return regs;
    }

    public abstract MipsReg[] getInRegOps();

    public abstract MipsReg getOutRegOp();

    public abstract MipsInstr regAllocTrans();  // naive

    public abstract MipsInstr regAllocTrans(MipsReg... newRegs);  // from map
}
