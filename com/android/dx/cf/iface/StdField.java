package com.android.dx.cf.iface;

import com.android.dx.rop.cst.*;
import com.android.dx.cf.attrib.*;

public final class StdField extends StdMember implements Field
{
    public StdField(final CstType cstType, final int n, final CstNat cstNat, final AttributeList list) {
        super(cstType, n, cstNat, list);
    }
    
    @Override
    public TypedConstant getConstantValue() {
        final AttConstantValue attConstantValue = (AttConstantValue)this.getAttributes().findFirst("ConstantValue");
        if (attConstantValue == null) {
            return null;
        }
        return attConstantValue.getConstantValue();
    }
}
