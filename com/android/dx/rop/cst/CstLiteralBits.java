package com.android.dx.rop.cst;

public abstract class CstLiteralBits extends TypedConstant
{
    public boolean fitsIn16Bits() {
        final boolean fitsInInt = this.fitsInInt();
        boolean b = false;
        if (!fitsInInt) {
            return false;
        }
        final int intBits = this.getIntBits();
        if ((short)intBits == intBits) {
            b = true;
        }
        return b;
    }
    
    public boolean fitsIn8Bits() {
        final boolean fitsInInt = this.fitsInInt();
        boolean b = false;
        if (!fitsInInt) {
            return false;
        }
        final int intBits = this.getIntBits();
        if ((byte)intBits == intBits) {
            b = true;
        }
        return b;
    }
    
    public abstract boolean fitsInInt();
    
    public abstract int getIntBits();
    
    public abstract long getLongBits();
}
