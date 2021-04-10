package com.android.dx.rop.cst;

import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class CstShort extends CstLiteral32
{
    public static final CstShort VALUE_0;
    
    static {
        VALUE_0 = make((short)0);
    }
    
    private CstShort(final short n) {
        super(n);
    }
    
    public static CstShort make(final int n) {
        final short n2 = (short)n;
        if (n2 != n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("bogus short value: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        return make(n2);
    }
    
    public static CstShort make(final short n) {
        return new CstShort(n);
    }
    
    @Override
    public Type getType() {
        return Type.SHORT;
    }
    
    public short getValue() {
        return (short)this.getIntBits();
    }
    
    @Override
    public String toHuman() {
        return Integer.toString(this.getIntBits());
    }
    
    @Override
    public String toString() {
        final int intBits = this.getIntBits();
        final StringBuilder sb = new StringBuilder();
        sb.append("short{0x");
        sb.append(Hex.u2(intBits));
        sb.append(" / ");
        sb.append(intBits);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public String typeName() {
        return "short";
    }
}
