package com.android.dx.rop.cst;

import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class CstDouble extends CstLiteral64
{
    public static final CstDouble VALUE_0;
    public static final CstDouble VALUE_1;
    
    static {
        VALUE_0 = new CstDouble(Double.doubleToLongBits(0.0));
        VALUE_1 = new CstDouble(Double.doubleToLongBits(1.0));
    }
    
    private CstDouble(final long n) {
        super(n);
    }
    
    public static CstDouble make(final long n) {
        return new CstDouble(n);
    }
    
    @Override
    public Type getType() {
        return Type.DOUBLE;
    }
    
    public double getValue() {
        return Double.longBitsToDouble(this.getLongBits());
    }
    
    @Override
    public String toHuman() {
        return Double.toString(Double.longBitsToDouble(this.getLongBits()));
    }
    
    @Override
    public String toString() {
        final long longBits = this.getLongBits();
        final StringBuilder sb = new StringBuilder();
        sb.append("double{0x");
        sb.append(Hex.u8(longBits));
        sb.append(" / ");
        sb.append(Double.longBitsToDouble(longBits));
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public String typeName() {
        return "double";
    }
}
