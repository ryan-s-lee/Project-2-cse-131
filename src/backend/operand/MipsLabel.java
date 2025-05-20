package backend.operand;

import backend.instr.MipsInstr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MipsLabel extends MipsInstr {

    String name;

    public MipsLabel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void printMipsRepresentation() {
        System.out.println(name + ":");
    }

    @Override
    public boolean isBranch() {
        return false;
    }

    @Override
    public MipsReg[] getInRegOps() {
        return new MipsReg[0];
    }

    @Override
    public MipsReg getOutRegOp() {
        return null;
    }

    static Map<String, MipsLabel> cache = new HashMap<>();
    public static MipsLabel of(String string) {
        return cache.computeIfAbsent(string, e -> new MipsLabel(string));
    }

    @Override
    public MipsInstr regAllocTrans() {
        return this;
    }

    @Override
    public MipsInstr regAllocTrans(MipsReg... newRegs) {
        return this;
    }
}
