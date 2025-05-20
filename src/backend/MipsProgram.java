package backend;

import java.util.ArrayList;
import java.util.List;

import ir.IRFunction;
import ir.IRProgram;

public class MipsProgram {

    List<MipsFunction> functions;

    public MipsProgram(IRProgram source) {
        functions = new ArrayList<>();
        for (IRFunction sourceFunc : source.functions) {
            functions.add(new MipsFunction(sourceFunc));
        }

        for (MipsFunction function : functions) {
            function.populateInstrs(functions);
        }
    }

    public void print() {
        System.out.println(".text\n");
        System.out.println(".globl main\n");
        for (MipsFunction function : functions) {
            function.print();
            System.out.println();
        }
    }

    public void printNaive() {
        for (MipsFunction function : functions) {
            // mark functions as naively allocated by assigning temp/saved regs
            function.usedTempRegs = new ArrayList<>();
            function.usedSavedRegs = new ArrayList<>();
        }
        for (MipsFunction function : functions) {
            function.printNaiveAllocation();
            System.out.println();
        }
    }
}
