package backend;

import backend.instr.MipsInstr;
import backend.operand.MipsImmOp;
import backend.operand.MipsLabel;
import backend.operand.MipsReg;
import backend.operand.MipsReg.Type;
import backend.util.IrInstrToMipsInstr;
import ir.IRFunction;
import ir.IRInstruction;
import ir.datatype.IRArrayType;
import ir.operand.IRLabelOperand;
import ir.operand.IRVariableOperand;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MipsFunction {

    public static Map<String, MipsFunction> intrinsics = Map.of(
        "geti",
        new MipsFunction("geti", List.of()),
        "getc",
        new MipsFunction("getc", List.of()),
        "puti",
        new MipsFunction("geti", List.of(new MipsReg("puti_in"))),
        "putc",
        new MipsFunction("getc", List.of(new MipsReg("putc_in")))
    );

    IRFunction sourceIr;

    // upon construction
    public String name;
    public List<MipsReg> parameters; // listed in reverse order on stack
    public List<MipsReg> variables; // listed in order on stack
    Map<String, MipsReg> irToMipsReg;

    // post-function instantiation
    boolean usesCall;
    public List<MipsInstr> instrs;

    // post-register allocation
    List<MipsReg> usedSavedRegs;
    List<MipsReg> usedTempRegs;
    Map<MipsReg, MipsReg> symbolicToArchReg;
    public Map<IRLabelOperand, MipsLabel> irToMipsLabel;
    public Map<MipsReg, MipsImmOp> symbolToStackOff;

    boolean intrinsic;

    // for intrinsics
    private MipsFunction(String name, List<MipsReg> parameters) {
        this.name = name;
        this.parameters = parameters;
        this.intrinsic = true;
    }

    public MipsFunction(IRFunction irf) {
        // things that can be determined now; these may be updated after instruction selection however.
        this.sourceIr = irf;
        this.name = sourceIr.name;
        this.parameters = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.irToMipsReg = new HashMap<>();
        this.symbolToStackOff = new HashMap<>(); // must be populated now so that instructions have a reference to an immediate that can be changed later
        this.intrinsic = false;

        this.instrs = null; // can only be populated after all MipsFunctions are set
        this.irToMipsLabel = null; // can only be populated after all MipsFunctions are set
        this.usesCall = false; // can only be populated after all MipsFunctions are set

        this.usedSavedRegs = null; // can only be populated after register allocation
        this.usedTempRegs = null; // can only be populated after register allocation
        this.symbolicToArchReg = null; // can only be populated after register allocation

        for (IRVariableOperand irfp : irf.parameters) {
            MipsReg newReg = irfp.type instanceof IRArrayType
                ? new MipsReg(irfp.getName(), false, 4) // this 4 is ignored lol
                : new MipsReg(irfp.getName());
            irToMipsReg.put(irfp.getName(), newReg);
            parameters.add(newReg);
        }

        for (IRVariableOperand irfv : irf.variables) {
            MipsReg newReg = irfv.type instanceof IRArrayType
                ? new MipsReg(
                    irfv.getName(),
                    true,
                    ((IRArrayType) irfv.type).getSize()
                )
                : new MipsReg(irfv.getName());
            irToMipsReg.put(irfv.getName(), newReg);
            variables.add(newReg);
        }
    }

    public void populateInstrs(List<MipsFunction> allFuncs) {
        instrs = new ArrayList<>();
        irToMipsLabel = new HashMap<>();
        // current method of stack allocation is a hack
        this.symbolToStackOff = this.allocateStackPositions();
        for (IRInstruction iri : sourceIr.instructions) {
            IrInstrToMipsInstr.appendTranslation(iri, this, allFuncs);
        }
    }

    public int getStackSize() {
        int localVarSection = 0;
        for (MipsReg varReg : this.variables) {
            localVarSection += varReg.getSize();
        }

        // round up size to a multiple of 8
        int frameSize = SAVEDREGSECTIONSIZE + localVarSection;
        if (frameSize % 8 != 0) frameSize = 8 * (frameSize / 8) + 8;

        return frameSize;
    }

    // we have guaranteed less than 4 arguments, and we have to reserve stack space for them anyway.
    // 8 saved regs, then the ra and fp
    final int SAVEDREGSECTIONSIZE = 16 + 4 * 8 + 4 + 4;

    Map<MipsReg, MipsImmOp> allocateStackPositions() {
        final int sfSize = getStackSize();
        Map<MipsReg, MipsImmOp> result = new HashMap<>();
        int i = 0;
        for (MipsReg param : parameters) {
            result.put(param, MipsImmOp.of(sfSize + i));
            i += param.getSize();
            // worth checking
            if (param.getSize() != 4) {
                throw new RuntimeException(
                    String.format(
                        "parameter %s had unexpected size %d",
                        param.getName(),
                        param.getSize()
                    )
                );
            }
        }

        i = 0;
        for (MipsReg variable : variables) {
            i += variable.getSize();
            if (i > sfSize - SAVEDREGSECTIONSIZE) {
                throw new RuntimeException(
                    String.format(
                        "total size of variables %d was larger than size allocated for variables %d",
                        i,
                        sfSize - SAVEDREGSECTIONSIZE
                    )
                );
            }
            result.put(variable, MipsImmOp.of(sfSize - i));
        }

        // saved regs like $ra and $s0 have implicit positions on the stack
        return result;
    }

    public void printPrologue() {
        System.out.println(name + ":");
        // TODO
    }

    public void printBody() {
        for (MipsInstr instr : instrs) {
            instr.printMipsRepresentation();
        }
    }

    public void printEpilogue() {
        // TODO
    }

    public void print() {
        printPrologue();
        printBody();
        printEpilogue();
    }

    public MipsReg irVarToMipsReg(IRVariableOperand irvar) {
        return irToMipsReg.get(irvar.getName());
    }
}
