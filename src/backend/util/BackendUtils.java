package backend.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import backend.MipsFunction;
import backend.instr.MipsInstr;
import backend.instr.MipsInstrMem;
import backend.instr.MipsInstrMem.Op;
import backend.operand.MipsImmOp;
import backend.operand.MipsReg;

public class BackendUtils {
    public static List<MipsInstr> NaiveRegAlloc(MipsFunction func) {
        List<MipsInstr> result = new ArrayList<>();
        Map<MipsReg, MipsReg> symbToArch = new HashMap<>();
        // generate mapping from symbols to arch regs
        for (int i = 0; i < func.parameters.size(); i++) {
            symbToArch.put(func.parameters.get(i), MipsReg.A[i]);
            // if i is too large, we get an intentional array out of bounds error.
        }

        // TODO: for each instruction, convert it to architectural form
        // that is, generate load instructions for each of the inputs
        // then, generate store instructions for each of the outputs
        // then, each symbolic variable is translated to a concrete variable.
        for (MipsInstr i : func.instrs) {
            MipsInstr mainTranslation = i.regAllocTrans();
            int j = 1;
            for (MipsReg inreg : i.getInRegOps()) {
                if (inreg.isSymbolic()) {
                    MipsImmOp offset = func.symbolToStackOff.get(inreg);
                    if (offset == null) {
                        System.out.println("Error on the following instruction:");
                        i.printMipsRepresentation();
                        throw new RuntimeException(String.format("Failed to find offset for outreg %s", inreg));
                    }
                    result.add(new MipsInstrMem(MipsReg.T[j], MipsReg.SP, func.symbolToStackOff.get(inreg), Op.LW));
                }
                j += 1;
            }
            result.add(mainTranslation);
            MipsReg outreg = i.getOutRegOp();
            if (outreg != null && outreg.isSymbolic()) {
                MipsImmOp offset = func.symbolToStackOff.get(outreg);
                if (offset == null) {
                    System.out.println("Error on the following instruction:");
                    i.printMipsRepresentation();
                    throw new RuntimeException(String.format("Failed to find offset for outreg %s", outreg));
                }
                result.add(new MipsInstrMem(MipsReg.T0, MipsReg.SP, offset, Op.SW));
            }
        }
        return result;
    }
}
