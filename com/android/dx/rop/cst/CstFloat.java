package com.android.dx.rop.cst;

import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class CstFloat extends CstLiteral32
{
    public static final CstFloat VALUE_0;
    public static final CstFloat VALUE_1;
    public static final CstFloat VALUE_2;
    
    static {
        VALUE_0 = make(Float.floatToIntBits(0.0f));
        VALUE_1 = make(Float.floatToIntBits(1.0f));
        VALUE_2 = make(Float.floatToIntBits(2.0f));
    }
    
    private CstFloat(final int n) {
        super(n);
    }
    
    public static CstFloat make(final int n) {
        return new CstFloat(n);
    }
    
    @Override
    public Type getType() {
        return Type.FLOAT;
    }
    
    public float getValue() {
        return Float.intBitsToFloat(this.getIntBits());
    }
    
    @Override
    public String toHuman() {
        return Float.toString(Float.intBitsToFloat(this.getIntBits()));
    }
    
    @Override
    public String toString() {
        final int intBits = this.getIntBits();
        final StringBuilder sb = new StringBuilder();
        sb.append("float{0x");
        sb.append(Hex.u4(intBits));
        sb.append(" / ");
        sb.append(Float.intBitsToFloat(intBits));
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public String typeName() {
        return "float";
    }
}
