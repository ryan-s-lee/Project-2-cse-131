package backend.operand;

import java.util.HashMap;
import java.util.Map;

public class MipsImmOp {

    short immediate;

    // constructor is used when you want to modify the immediate post-instantiation.
    public MipsImmOp(int immediate) {
        if (immediate < Short.MIN_VALUE || immediate > Short.MAX_VALUE) {
            throw new RuntimeException("Passed immediate to instruction that was too large");
        }
        this.immediate = (short)immediate;
    }

    @Override
    public String toString() {
        return "" + immediate;
    }

    static Map<Integer, MipsImmOp> cache = new HashMap<>();

    public static MipsImmOp of(int immediate) {
        return cache.computeIfAbsent(immediate, e -> new MipsImmOp(e));
    }

    public short getVal() { return immediate; }
    public void setVal(int immediate) {
        if (immediate < Short.MIN_VALUE || immediate > Short.MAX_VALUE) {
            throw new RuntimeException("Passed immediate to instruction that was too large");
        }
        this.immediate = (short)immediate;
    }
}
