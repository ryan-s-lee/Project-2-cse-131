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
    public Set<MipsReg> getInRegOps() {
        return new HashSet<>();
    }

    @Override
    public Set<MipsReg> getOutRegOps() {
        return new HashSet<>();
    }

    static Map<String, MipsLabel> cache = new HashMap<>();
    public static MipsLabel of(String string) {
        return cache.computeIfAbsent(string, e -> new MipsLabel(string));
    }
}
