package com.android.dx.rop.cst;

public abstract class CstLiteral64 extends CstLiteralBits
{
    private final long bits;
    
    CstLiteral64(final long bits) {
        this.bits = bits;
    }
    
    @Override
    protected int compareTo0(final Constant constant) {
        final long bits = ((CstLiteral64)constant).bits;
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
        return o != null && this.getClass() == o.getClass() && this.bits == ((CstLiteral64)o).bits;
    }
    
    @Override
    public final boolean fitsInInt() {
        return (int)this.bits == this.bits;
    }
    
    @Override
    public final int getIntBits() {
        return (int)this.bits;
    }
    
    @Override
    public final long getLongBits() {
        return this.bits;
    }
    
    @Override
    public final int hashCode() {
        return (int)this.bits ^ (int)(this.bits >> 32);
    }
    
    @Override
    public final boolean isCategory2() {
        return true;
    }
}
