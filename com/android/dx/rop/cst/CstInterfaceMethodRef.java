package com.android.dx.rop.cst;

public final class CstInterfaceMethodRef extends CstBaseMethodRef
{
    private CstMethodRef methodRef;
    
    public CstInterfaceMethodRef(final CstType cstType, final CstNat cstNat) {
        super(cstType, cstNat);
        this.methodRef = null;
    }
    
    public CstMethodRef toMethodRef() {
        if (this.methodRef == null) {
            this.methodRef = new CstMethodRef(this.getDefiningClass(), this.getNat());
        }
        return this.methodRef;
    }
    
    @Override
    public String typeName() {
        return "ifaceMethod";
    }
}
