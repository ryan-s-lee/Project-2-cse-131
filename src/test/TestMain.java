package test;

import backend.MipsProgram;
import ir.IRProgram;
import ir.IRReader;

public class TestMain {

    public static void main(String[] args) throws Exception {
        // Parse the IR file
        IRReader irReader = new IRReader();
        System.err.println("Reading from " + args[0]);
        if (args.length >= 2) {
            System.err.println("with option " + args[1]);
        }
        IRProgram program = irReader.parseIRFile(args[0]);

        MipsProgram mipsprogram = new MipsProgram(program);
        if (args.length >= 2 && args[1].equals("--naive")) {
            mipsprogram.printNaive();
        } else if (args.length >= 2 && args[1].equals("--symbolic")) {
            mipsprogram.print();
        } else {
            mipsprogram.print();
        }
    }
}
