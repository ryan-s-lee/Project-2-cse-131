package test;

import backend.MipsProgram;
import ir.IRProgram;
import ir.IRReader;

public class TestMain {

    public static void main(String[] args) throws Exception{
        // Parse the IR file
        IRReader irReader = new IRReader();
        IRProgram program = irReader.parseIRFile(args[0]);

        MipsProgram mipsprogram = new MipsProgram(program);
        mipsprogram.print();
    }
}
