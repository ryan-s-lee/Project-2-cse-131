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
        for (MipsFunction function : functions) {
            function.print();
            System.out.println();
        }
    }
}
