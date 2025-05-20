package backend.instr;

import backend.MipsFunction;
import backend.operand.MipsReg;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MipsInstrCall extends MipsInstr {

    MipsFunction caller;
    MipsFunction callee;

    boolean returns;

    public MipsInstrCall(MipsFunction caller, MipsFunction callee, boolean returns) {
        this.caller = caller;
        this.callee = callee;
        this.returns = returns;
    }

    static Map<String, Integer> intrinsicToDescriptorMap = Map.of(
        "geti", 5,
        "puti", 1,
        "getc", 12,
        "putc", 11
    );

    @Override
    public void printMipsRepresentation() {
        // TODO:
        // arguments are stored to $a0-$a3 externally, to help with register allocation.
        // caller's argument registers are also saved externally.
        // Thus, output should only store and retrieve temporary variables.
        // If call is to an intrinsic, replace jal with $v0 move and syscall.
        if (caller.usedTempRegs == null || callee.usedTempRegs == null) {
            // functions not yet concrete, use placeholder
            System.out.printf("\tjal %s\n", callee.name);
            return;
        }
        Set<MipsReg> startSet = new HashSet<>(caller.usedTempRegs);
        Set<MipsReg> calleeTempSet = new HashSet<>(callee.usedTempRegs);
        startSet.retainAll(calleeTempSet);
        for (int i = 0; i < MipsReg.T.length; i++) {
            int spOff = 4*(4 + 8 + i);  // skip the 4 arg positions and 8 saved reg positions
            if (calleeTempSet.contains(MipsReg.T[i])) {
                System.out.printf("\tsw %s, %d($%s)\n", MipsReg.T[i], spOff, MipsReg.SP);
            }
        }

        // only syscalls have numbers as names
        System.err.printf("Called function: %s; intrinsic: %s\n", this.callee.name, intrinsicToDescriptorMap.get(this.callee.name));
        if (intrinsicToDescriptorMap.containsKey(this.callee.name)) {  // pray that no function uses a number as their name
            System.out.printf("\taddi $v0, $zero, %d\n", intrinsicToDescriptorMap.get(this.callee.name));  // TODO
            System.out.printf("\tsyscall\n");
        } else {
            System.out.printf("\tjal %s\n", this.callee.name);
        }

        for (int i = MipsReg.T.length - 1; i <= 0; i--) {
            int spOff = 4*(4 + 8 + i);  // skip the 4 arg positions and 8 saved reg positions
            if (calleeTempSet.contains(MipsReg.T[i])) {
                System.out.printf("\tlw %s, %d($%s)\n", MipsReg.T[i], spOff, MipsReg.SP);
            }
        }

        return;
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

    @Override
    public MipsInstr regAllocTrans() {
        return new MipsInstrCall(caller, callee, returns);
    }

    @Override
    public MipsInstr regAllocTrans(MipsReg... newRegs) {
        return new MipsInstrCall(caller, callee, returns);
    }
}
