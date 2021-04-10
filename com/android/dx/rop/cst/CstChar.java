package com.android.dx.rop.cst;

import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class CstChar extends CstLiteral32
{
    public static final CstChar VALUE_0;
    
    static {
        VALUE_0 = make('\0');
    }
    
    private CstChar(final char c) {
        super(c);
    }
    
    public static CstChar make(final char c) {
        return new CstChar(c);
    }
    
    public static CstChar make(final int n) {
        final char c = (char)n;
        if (c != n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("bogus char value: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        return make(c);
    }
    
    @Override
    public Type getType() {
        return Type.CHAR;
    }
    
    public char getValue() {
        return (char)this.getIntBits();
    }
    
    @Override
    public String toHuman() {
        return Integer.toString(this.getIntBits());
    }
    
    @Override
    public String toString() {
        final int intBits = this.getIntBits();
        final StringBuilder sb = new StringBuilder();
        sb.append("char{0x");
        sb.append(Hex.u2(intBits));
        sb.append(" / ");
        sb.append(intBits);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public String typeName() {
        return "char";
    }
}
