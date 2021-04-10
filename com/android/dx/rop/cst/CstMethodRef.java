package com.android.dx.rop.cst;

public final class CstMethodRef extends CstBaseMethodRef
{
    public CstMethodRef(final CstType cstType, final CstNat cstNat) {
        super(cstType, cstNat);
    }
    
    @Override
    public String typeName() {
        return "method";
    }
}
