package com.android.dx.cf.iface;

import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;

public final class StdMethod extends StdMember implements Method
{
    private final Prototype effectiveDescriptor;
    
    public StdMethod(final CstType cstType, final int n, final CstNat cstNat, final AttributeList list) {
        super(cstType, n, cstNat, list);
        this.effectiveDescriptor = Prototype.intern(this.getDescriptor().getString(), cstType.getClassType(), AccessFlags.isStatic(n), cstNat.isInstanceInit());
    }
    
    @Override
    public Prototype getEffectiveDescriptor() {
        return this.effectiveDescriptor;
    }
}
