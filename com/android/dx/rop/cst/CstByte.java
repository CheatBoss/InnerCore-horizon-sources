package com.android.dx.rop.cst;

import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class CstByte extends CstLiteral32
{
    public static final CstByte VALUE_0;
    
    static {
        VALUE_0 = make((byte)0);
    }
    
    private CstByte(final byte b) {
        super(b);
    }
    
    public static CstByte make(final byte b) {
        return new CstByte(b);
    }
    
    public static CstByte make(final int n) {
        final byte b = (byte)n;
        if (b != n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("bogus byte value: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        return make(b);
    }
    
    @Override
    public Type getType() {
        return Type.BYTE;
    }
    
    public byte getValue() {
        return (byte)this.getIntBits();
    }
    
    @Override
    public String toHuman() {
        return Integer.toString(this.getIntBits());
    }
    
    @Override
    public String toString() {
        final int intBits = this.getIntBits();
        final StringBuilder sb = new StringBuilder();
        sb.append("byte{0x");
        sb.append(Hex.u1(intBits));
        sb.append(" / ");
        sb.append(intBits);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public String typeName() {
        return "byte";
    }
}
