package backend;

import backend.instr.MipsInstr;
import backend.operand.MipsImmOp;
import backend.operand.MipsLabel;
import backend.operand.MipsReg;
import backend.util.BackendUtils;
import backend.util.IrInstrToMipsInstr;
import ir.IRFunction;
import ir.IRInstruction;
import ir.IRInstruction.OpCode;
import ir.datatype.IRArrayType;
import ir.operand.IRLabelOperand;
import ir.operand.IRVariableOperand;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class MipsFunction {

    // TODO: consider making these intrinsics utilized all saved and all temp registers. For safety.
    public static Map<String, MipsFunction> intrinsics = Map.of(
        "puti",
        new MipsFunction(
            "puti",
            List.of(new MipsReg("puti_in")),
            new ArrayList<>(),
            new ArrayList<>()
        ),
        "geti",
        new MipsFunction(
            "geti",
            List.of(),
            new ArrayList<>(),
            new ArrayList<>()
        ),
        "putc",
        new MipsFunction(
            "putc",
            List.of(new MipsReg("putc_in")),
            new ArrayList<>(),
            new ArrayList<>()
        ),
        "getc",
        new MipsFunction(
            "getc",
            List.of(),
            new ArrayList<>(),
            new ArrayList<>()
        )
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
    public List<MipsReg> usedSavedRegs;
    public List<MipsReg> usedTempRegs;
    Map<MipsReg, MipsReg> symbolicToArchReg;
    public Map<IRLabelOperand, MipsLabel> irToMipsLabel;
    public Map<MipsReg, MipsImmOp> symbolToStackOff;

    boolean intrinsic;

    // for intrinsics
    private MipsFunction(
        String name,
        List<MipsReg> parameters,
        List<MipsReg> savedVars,
        List<MipsReg> tempVars
    ) {
        this.name = name;
        this.parameters = parameters;
        this.usedSavedRegs = savedVars;
        this.usedTempRegs = tempVars;
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

        Set<String> seenVariables = new HashSet<>();
        for (IRVariableOperand irfp : irf.parameters) {
            MipsReg newReg = irfp.type instanceof IRArrayType
                ? new MipsReg(irfp.getName(), false, 4) // this 4 is ignored lol
                : new MipsReg(irfp.getName());
            irToMipsReg.put(irfp.getName(), newReg);
            parameters.add(newReg);
            seenVariables.add(irfp.getName());
        }

        for (IRVariableOperand irfv : irf.variables) {
            if (seenVariables.contains(irfv.getName())) continue;

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
        // appendTranslation needs an initial mapping of variables to MipsImmOp, which will then be updated during instruction selection
        for (MipsReg param : parameters) {
            symbolToStackOff.put(param, new MipsImmOp(0));
        }
        for (MipsReg var : variables) {
            symbolToStackOff.put(var, new MipsImmOp(0));
        }
        for (IRInstruction iri : sourceIr.instructions) {
            IrInstrToMipsInstr.appendTranslation(iri, this, allFuncs);
            if (iri.opCode == OpCode.CALL || iri.opCode == OpCode.CALLR) {
                usesCall = true;
            }
        }

        this.allocateStackPositions(this.symbolToStackOff);
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
    // 8 saved regs, 10 temp regs, then the ra and fp
    final int SAVEDREGSECTIONSIZE = 16 + 4 * 8 + 4 * 10 + 4 + 4;

    void allocateStackPositions(Map<MipsReg, MipsImmOp> spMap) {
        final int sfSize = getStackSize();
        int i = 0;
        for (MipsReg param : parameters) {
            spMap.get(param).setVal(sfSize + i);
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
            System.err.println("Mapping " + variable);
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
            spMap.get(variable).setVal(sfSize - i);
        }

        // saved regs like $ra and $s0 have implicit positions on the stack
        logStackMapping();
    }

    public void printPrologue() {
        // TODO
        // push saved variables to the stack
        System.out.println(name + ":");

        // Shift stack pointer down
        System.out.printf(
            "\taddi %s, %s, %d\n",
            MipsReg.SP,
            MipsReg.SP,
            -getStackSize()
        );
        int i = 0;
        for (MipsReg possibleSaved : MipsReg.S) {
            int savePos = 4 * i + 16;
            if (
                this.usedSavedRegs != null &&
                this.usedSavedRegs.contains(possibleSaved)
            ) {
                System.out.printf(
                    "\tsw %s, %d(%s)\n",
                    possibleSaved,
                    savePos,
                    MipsReg.SP
                );
            }
            i += 1;
        }
        // if a function call is made in the procedure and the function is not main, also push ra to the stack
        // main does not need to store caller stuff because it will syscall exit anyway.
        if (usesCall && !name.equals("main")) {
            int savePos = SAVEDREGSECTIONSIZE - 8;
            System.out.printf(
                "\tsw %s, %d(%s)\n",
                MipsReg.FP,
                savePos,
                MipsReg.SP
            );
            System.out.printf(
                "\tsw %s, %d(%s)\n",
                MipsReg.RA,
                savePos + 4,
                MipsReg.SP
            );
        }
    }

    public void printBody() {
        for (MipsInstr instr : instrs) {
            instr.printMipsRepresentation();
        }
    }

    public void printNaiveAllocation() {
        printPrologue();
        for (MipsInstr instr : BackendUtils.NaiveRegAlloc(this)) {
            instr.printMipsRepresentation();
        }
        printEpilogue();
    }

    public void printEpilogue() {
        // TODO
        // if a function call was made in the procedure, pop ra from the stack
        // pop saved variables to the stack
        System.out.printf("%s_epilogue:\n", this.name);
        if (usesCall && !name.equals("main")) {
            int savePos = SAVEDREGSECTIONSIZE - 8;
            System.out.printf(
                "\tlw %s, %d(%s)\n",
                MipsReg.RA,
                savePos + 4,
                MipsReg.SP
            );
            System.out.printf(
                "\tlw %s, %d(%s)\n",
                MipsReg.FP,
                savePos,
                MipsReg.SP
            );
        }

        for (int j = 7; j >= 0; --j) {
            MipsReg possibleSaved = MipsReg.S[j];
            int savePos = 4 * j + 16;
            if (
                this.usedSavedRegs != null &&
                this.usedSavedRegs.contains(possibleSaved)
            ) {
                System.out.printf(
                    "\tlw %s, %d(%s)\n",
                    possibleSaved,
                    savePos,
                    MipsReg.SP
                );
            }
        }

        // We have saved everything we needed from our current stack frame, so move stack back up
        System.out.printf(
            "\taddi %s, %s, %d\n",
            MipsReg.SP,
            MipsReg.SP,
            getStackSize()
        );

        if (name.equals("main")) {
            System.out.println("\tli $v0, 10");
            System.out.println("\tsyscall");
        } else {
            System.out.print("\tjr $ra\n");
        }
    }

    void logStackMapping() {
        System.err.println(name + " Stack:");
        symbolToStackOff
            .entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .forEach(entry -> {
                System.err.printf(
                    "\t%s: %d\n",
                    entry.getKey(),
                    entry.getValue().getVal()
                );
            });
    }

    public void print() {
        printPrologue();
        printBody();
        printEpilogue();
    }

    public MipsReg irVarToMipsReg(IRVariableOperand irvar) {
        MipsReg symbolic = irToMipsReg.get(irvar.getName());
        int parameterPos = parameters.indexOf(symbolic);
        if (parameterPos == -1) {
            return symbolic;
        } else {
            return MipsReg.A[parameterPos];
        }
    }
}
