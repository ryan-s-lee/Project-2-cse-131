package backend.operand;

// need the ability to create a symbolic register, and a set of special, singleton
// registers for the architectural registers.
// Symbolic registers can have an arbitrary name.
public class MipsReg {

    static public MipsReg ZERO = new MipsReg("$zero", Type.ZERO);
    static public MipsReg V0 = new MipsReg("$v0", Type.RET);
    static public MipsReg V1 = new MipsReg("$v1", Type.RET);
    static public MipsReg A0 = new MipsReg("$a0", Type.ARG);
    static public MipsReg A1 = new MipsReg("$a1", Type.ARG);
    static public MipsReg A2 = new MipsReg("$a2", Type.ARG);
    static public MipsReg A3 = new MipsReg("$a3", Type.ARG);
    static public MipsReg T0 = new MipsReg("$t0", Type.TEMP);
    static public MipsReg T1 = new MipsReg("$t1", Type.TEMP);
    static public MipsReg T2 = new MipsReg("$t2", Type.TEMP);
    static public MipsReg T3 = new MipsReg("$t3", Type.TEMP);
    static public MipsReg T4 = new MipsReg("$t4", Type.TEMP);
    static public MipsReg T5 = new MipsReg("$t5", Type.TEMP);
    static public MipsReg T6 = new MipsReg("$t6", Type.TEMP);
    static public MipsReg T7 = new MipsReg("$t7", Type.TEMP);
    static public MipsReg T8 = new MipsReg("$t8", Type.TEMP);
    static public MipsReg T9 = new MipsReg("$t9", Type.TEMP);
    static public MipsReg S0 = new MipsReg("$s0", Type.SAVED);
    static public MipsReg S1 = new MipsReg("$s1", Type.SAVED);
    static public MipsReg S2 = new MipsReg("$s2", Type.SAVED);
    static public MipsReg S3 = new MipsReg("$s3", Type.SAVED);
    static public MipsReg S4 = new MipsReg("$s4", Type.SAVED);
    static public MipsReg S5 = new MipsReg("$s5", Type.SAVED);
    static public MipsReg S6 = new MipsReg("$s6", Type.SAVED);
    static public MipsReg S7 = new MipsReg("$s7", Type.SAVED);
    static public MipsReg RA = new MipsReg("$ra", Type.RET);
    static public MipsReg SP = new MipsReg("$sp", Type.SP);

    static public MipsReg[] A = {A0, A1, A2, A3};
    static public MipsReg[] V = {V0, V1};
    static public MipsReg[] T = {T0, T1, T2, T3, T4, T5, T6, T7, T8, T9};

    public enum Type {
        SYMBOLIC,
        SYMBOLICARR,
        SYMBOLICPTR,
        ZERO,
        TEMP,
        SAVED,
        ARG,
        RET,
        SP;
    }

    String name;
    Type t;
    int size;

    public MipsReg(String name) {
        this.name = "$" + name;
        this.t = Type.SYMBOLIC;
        this.size = 4;
    }

    public MipsReg(String name, boolean local, int size) {
        this.name = "$" + name;
        this.t = local ? Type.SYMBOLICARR : Type.SYMBOLICPTR;
        this.size = local ? 4 * size : 4;
    }

    private MipsReg(
        String name,
        Type t
    ) {
        this.name = name;
        this.t = t;
    }

    public Type getType() {
        return t;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        // This one's kinda goofy: only the name really matters for equality. However,
        // if the type is different, the register will be treated differently by
        // backend passes!
        return obj instanceof MipsReg && this.name == ((MipsReg)obj).getName();
    }

    public void replace(MipsReg other) {
        this.name = other.name;
        this.t = other.t;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean isSymbolic() {
        return t == Type.SYMBOLIC || t == Type.SYMBOLICARR || t == Type.SYMBOLICPTR;
    }

    public int getSize() {
        return this.size;
    }
}
