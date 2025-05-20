package backend.func;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import backend.MipsFunction;
import backend.instr.MipsInstr;
import backend.operand.MipsReg;
import ir.IRFunction;
import ir.IRInstruction;
import ir.datatype.IRArrayType;
import ir.operand.IRVariableOperand;

public class IrfToMipsf implements Function<IRFunction, MipsFunction>{

	@Override
	public MipsFunction apply(IRFunction irf) {

        // translate IRInstructions to MipsInstr using a predefined map
        // Use the special "call" MipsInstr to translate calls; these calls can
        // only be fully transformed to MIPS instructions after register alloc
        for (IRInstruction iri : irf.instructions) {
            // TODO
        }
	}
}
