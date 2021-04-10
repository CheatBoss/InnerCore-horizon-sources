package com.android.dx.rop.cst;

import com.android.dx.rop.type.*;

public final class CstFieldRef extends CstMemberRef
{
    public CstFieldRef(final CstType cstType, final CstNat cstNat) {
        super(cstType, cstNat);
    }
    
    public static CstFieldRef forPrimitiveType(final Type type) {
        return new CstFieldRef(CstType.forBoxedPrimitiveType(type), CstNat.PRIMITIVE_TYPE_NAT);
    }
    
    @Override
    protected int compareTo0(final Constant constant) {
        final int compareTo0 = super.compareTo0(constant);
        if (compareTo0 != 0) {
            return compareTo0;
        }
        return this.getNat().getDescriptor().compareTo((Constant)((CstFieldRef)constant).getNat().getDescriptor());
    }
    
    @Override
    public Type getType() {
        return this.getNat().getFieldType();
    }
    
    @Override
    public String typeName() {
        return "field";
    }
}
