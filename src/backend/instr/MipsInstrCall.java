package backend.instr;

import backend.MipsFunction;
import backend.operand.MipsReg;
import java.util.HashSet;
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

    @Override
    public void printMipsRepresentation() {
        // TODO:
        // output should look like:
        // # store live temp variables used by the function to the stack
        // # store argument variables to the stack
        // # jump and link to function
        // # restore argument variables from stack
        // # restore temp variables from the stack
        StringBuilder sb = new StringBuilder();
        System.out.printf("\tTODO: call and callr\n");
        return;
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
}
