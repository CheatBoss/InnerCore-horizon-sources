package com.android.dx.rop.cst;

import com.android.dx.rop.type.*;

public final class CstKnownNull extends CstLiteralBits
{
    public static final CstKnownNull THE_ONE;
    
    static {
        THE_ONE = new CstKnownNull();
    }
    
    private CstKnownNull() {
    }
    
    @Override
    protected int compareTo0(final Constant constant) {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof CstKnownNull;
    }
    
    @Override
    public boolean fitsInInt() {
        return true;
    }
    
    @Override
    public int getIntBits() {
        return 0;
    }
    
    @Override
    public long getLongBits() {
        return 0L;
    }
    
    @Override
    public Type getType() {
        return Type.KNOWN_NULL;
    }
    
    @Override
    public int hashCode() {
        return 1147565434;
    }
    
    @Override
    public boolean isCategory2() {
        return false;
    }
    
    @Override
    public String toHuman() {
        return "null";
    }
    
    @Override
    public String toString() {
        return "known-null";
    }
    
    @Override
    public String typeName() {
        return "known-null";
    }
}
