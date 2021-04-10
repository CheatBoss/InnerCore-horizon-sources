package com.android.dx.dex.code;

import com.android.dx.rop.code.*;
import com.android.dx.util.*;

public abstract class ZeroSizeInsn extends DalvInsn
{
    public ZeroSizeInsn(final SourcePosition sourcePosition) {
        super(Dops.SPECIAL_FORMAT, sourcePosition, RegisterSpecList.EMPTY);
    }
    
    @Override
    public final int codeSize() {
        return 0;
    }
    
    @Override
    public final DalvInsn withOpcode(final Dop dop) {
        throw new RuntimeException("unsupported");
    }
    
    @Override
    public DalvInsn withRegisterOffset(final int n) {
        return this.withRegisters(this.getRegisters().withOffset(n));
    }
    
    @Override
    public final void writeTo(final AnnotatedOutput annotatedOutput) {
    }
}
