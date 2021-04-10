package com.android.dx.rop.cst;

import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class CstInteger extends CstLiteral32
{
    public static final CstInteger VALUE_0;
    public static final CstInteger VALUE_1;
    public static final CstInteger VALUE_2;
    public static final CstInteger VALUE_3;
    public static final CstInteger VALUE_4;
    public static final CstInteger VALUE_5;
    public static final CstInteger VALUE_M1;
    private static final CstInteger[] cache;
    
    static {
        cache = new CstInteger[511];
        VALUE_M1 = make(-1);
        VALUE_0 = make(0);
        VALUE_1 = make(1);
        VALUE_2 = make(2);
        VALUE_3 = make(3);
        VALUE_4 = make(4);
        VALUE_5 = make(5);
    }
    
    private CstInteger(final int n) {
        super(n);
    }
    
    public static CstInteger make(final int n) {
        final int n2 = (Integer.MAX_VALUE & n) % CstInteger.cache.length;
        final CstInteger cstInteger = CstInteger.cache[n2];
        if (cstInteger != null && cstInteger.getValue() == n) {
            return cstInteger;
        }
        return CstInteger.cache[n2] = new CstInteger(n);
    }
    
    @Override
    public Type getType() {
        return Type.INT;
    }
    
    public int getValue() {
        return this.getIntBits();
    }
    
    @Override
    public String toHuman() {
        return Integer.toString(this.getIntBits());
    }
    
    @Override
    public String toString() {
        final int intBits = this.getIntBits();
        final StringBuilder sb = new StringBuilder();
        sb.append("int{0x");
        sb.append(Hex.u4(intBits));
        sb.append(" / ");
        sb.append(intBits);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public String typeName() {
        return "int";
    }
}
