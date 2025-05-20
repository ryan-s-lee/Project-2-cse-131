package backend.instr;

import java.util.HashSet;
import java.util.Set;

import backend.operand.MipsLabel;
import backend.operand.MipsReg;

public class MipsInstrJumpLabel extends MipsInstr{

    MipsLabel target;
    public MipsInstrJumpLabel(MipsLabel target) {
        this.target = target;
    }

	@Override
	public void printMipsRepresentation() {
		System.out.printf("\tj %s\n", target.getName());
	}

	@Override
	public boolean isBranch() {
		return true;
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
