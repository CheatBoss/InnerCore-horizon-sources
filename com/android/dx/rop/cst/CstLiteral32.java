package com.android.dx.rop.cst;

public abstract class CstLiteral32 extends CstLiteralBits
{
    private final int bits;
    
    CstLiteral32(final int bits) {
        this.bits = bits;
    }
    
    @Override
    protected int compareTo0(final Constant constant) {
        final int bits = ((CstLiteral32)constant).bits;
        if (this.bits < bits) {
            return -1;
        }
        if (this.bits > bits) {
            return 1;
        }
        return 0;
    }
    
    @Override
    public final boolean equals(final Object o) {
        return o != null && this.getClass() == o.getClass() && this.bits == ((CstLiteral32)o).bits;
    }
    
    @Override
    public final boolean fitsInInt() {
        return true;
    }
    
    @Override
    public final int getIntBits() {
        return this.bits;
    }
    
    @Override
    public final long getLongBits() {
        return this.bits;
    }
    
    @Override
    public final int hashCode() {
        return this.bits;
    }
    
    @Override
    public final boolean isCategory2() {
        return false;
    }
}
