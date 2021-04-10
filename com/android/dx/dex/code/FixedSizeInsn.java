package com.android.dx.dex.code;

import com.android.dx.rop.code.*;
import com.android.dx.util.*;

public abstract class FixedSizeInsn extends DalvInsn
{
    public FixedSizeInsn(final Dop dop, final SourcePosition sourcePosition, final RegisterSpecList list) {
        super(dop, sourcePosition, list);
    }
    
    @Override
    public final int codeSize() {
        return this.getOpcode().getFormat().codeSize();
    }
    
    @Override
    protected final String listingString0(final boolean b) {
        return this.getOpcode().getFormat().listingString(this, b);
    }
    
    @Override
    public final DalvInsn withRegisterOffset(final int n) {
        return this.withRegisters(this.getRegisters().withOffset(n));
    }
    
    @Override
    public final void writeTo(final AnnotatedOutput annotatedOutput) {
        this.getOpcode().getFormat().writeTo(annotatedOutput, this);
    }
}
