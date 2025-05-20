package backend.instr;

import java.util.HashSet;
import java.util.Set;

import backend.operand.MipsReg;

public class MipsInstrJumpReg extends MipsInstr{

    MipsReg rs;
    public MipsInstrJumpReg(MipsReg rs) {
        this.rs = rs;
    }

	@Override
	public void printMipsRepresentation() {
		System.out.printf("jr %s\n", rs);
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
