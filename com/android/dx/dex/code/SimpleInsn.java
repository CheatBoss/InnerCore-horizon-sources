package com.android.dx.dex.code;

import com.android.dx.rop.code.*;

public final class SimpleInsn extends FixedSizeInsn
{
    public SimpleInsn(final Dop dop, final SourcePosition sourcePosition, final RegisterSpecList list) {
        super(dop, sourcePosition, list);
    }
    
    @Override
    protected String argString() {
        return null;
    }
    
    @Override
    public DalvInsn withOpcode(final Dop dop) {
        return new SimpleInsn(dop, this.getPosition(), this.getRegisters());
    }
    
    @Override
    public DalvInsn withRegisters(final RegisterSpecList list) {
        return new SimpleInsn(this.getOpcode(), this.getPosition(), list);
    }
}
