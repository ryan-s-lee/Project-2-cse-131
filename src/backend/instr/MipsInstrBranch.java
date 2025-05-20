package backend.instr;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import backend.operand.MipsLabel;
import backend.operand.MipsReg;

public class MipsInstrBranch extends MipsInstr{

    public enum Cmp {
        BEQ, BNE, BGT, BGE, BLT, BLE;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    Cmp comparator;
    MipsReg rs;
    MipsReg rt;
    MipsLabel target;
    public MipsInstrBranch(Cmp comparator, MipsReg rs, MipsReg rt, MipsLabel target) {
        this.comparator = comparator;
        this.rs = rs;
        this.rt = rt;
        this.target = target;
    }
	@Override
	public void printMipsRepresentation() {
		System.out.printf("\t%s %s, %s, %s\n", comparator, rs, rt, target.getName());
	}
	@Override
	public boolean isBranch() {
		return true;
	}
	@Override
	public Set<MipsReg> getInRegOps() {
		return new HashSet<>(Arrays.asList(rs, rt));
	}
	@Override
	public Set<MipsReg> getOutRegOps() {
		return new HashSet<>();
	}
}
