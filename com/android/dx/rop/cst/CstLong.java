package com.android.dx.rop.cst;

import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class CstLong extends CstLiteral64
{
    public static final CstLong VALUE_0;
    public static final CstLong VALUE_1;
    
    static {
        VALUE_0 = make(0L);
        VALUE_1 = make(1L);
    }
    
    private CstLong(final long n) {
        super(n);
    }
    
    public static CstLong make(final long n) {
        return new CstLong(n);
    }
    
    @Override
    public Type getType() {
        return Type.LONG;
    }
    
    public long getValue() {
        return this.getLongBits();
    }
    
    @Override
    public String toHuman() {
        return Long.toString(this.getLongBits());
    }
    
    @Override
    public String toString() {
        final long longBits = this.getLongBits();
        final StringBuilder sb = new StringBuilder();
        sb.append("long{0x");
        sb.append(Hex.u8(longBits));
        sb.append(" / ");
        sb.append(longBits);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public String typeName() {
        return "long";
    }
}
