package com.android.dx.dex.code;

import com.android.dx.rop.code.*;

public abstract class VariableSizeInsn extends DalvInsn
{
    public VariableSizeInsn(final SourcePosition sourcePosition, final RegisterSpecList list) {
        super(Dops.SPECIAL_FORMAT, sourcePosition, list);
    }
    
    @Override
    public final DalvInsn withOpcode(final Dop dop) {
        throw new RuntimeException("unsupported");
    }
    
    @Override
    public final DalvInsn withRegisterOffset(final int n) {
        return this.withRegisters(this.getRegisters().withOffset(n));
    }
}
