package backend;

import ir.IRFunction;
import ir.IRProgram;
import java.util.ArrayList;
import java.util.List;

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
        System.out.println(".globl main\n");
        System.out.println(".text\n");

        for (MipsFunction function : functions) {
            // mark functions as naively allocated by assigning temp/saved regs
            function.usedTempRegs = new ArrayList<>();
            function.usedSavedRegs = new ArrayList<>();
        }

        // first print main
        for (MipsFunction function : functions) {
            if (function.name.equals("main")) {
                function.printNaiveAllocation();
                System.out.println();
            }
        }
        for (MipsFunction function : functions) {
            if (!function.name.equals("main")) {
                function.printNaiveAllocation();
                System.out.println();
            }
        }
    }

    public void printGreedy() {
        System.out.println(".globl main\n");
        System.out.println(".text\n");

        for (MipsFunction function : functions) {
            // mark functions as naively allocated by assigning temp/saved regs
            function.usedTempRegs = new ArrayList<>();
            function.usedSavedRegs = new ArrayList<>();
        }

        // first print main
        for (MipsFunction function : functions) {
            if (function.name.equals("main")) {
                function.printNaiveAllocation();
                System.out.println();
            }
        }
        for (MipsFunction function : functions) {
            if (!function.name.equals("main")) {
                function.printNaiveAllocation();
                System.out.println();
            }
        }
    }
}
