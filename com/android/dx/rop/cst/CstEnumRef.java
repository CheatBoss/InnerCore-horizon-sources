package com.android.dx.rop.cst;

import com.android.dx.rop.type.*;

public final class CstEnumRef extends CstMemberRef
{
    private CstFieldRef fieldRef;
    
    public CstEnumRef(final CstNat cstNat) {
        super(new CstType(cstNat.getFieldType()), cstNat);
        this.fieldRef = null;
    }
    
    public CstFieldRef getFieldRef() {
        if (this.fieldRef == null) {
            this.fieldRef = new CstFieldRef(this.getDefiningClass(), this.getNat());
        }
        return this.fieldRef;
    }
    
    @Override
    public Type getType() {
        return this.getDefiningClass().getClassType();
    }
    
    @Override
    public String typeName() {
        return "enum";
    }
}
