package com.android.dx.dex.code;

import com.android.dx.rop.code.*;
import com.android.dx.util.*;

public final class OddSpacer extends VariableSizeInsn
{
    public OddSpacer(final SourcePosition sourcePosition) {
        super(sourcePosition, RegisterSpecList.EMPTY);
    }
    
    @Override
    protected String argString() {
        return null;
    }
    
    @Override
    public int codeSize() {
        return this.getAddress() & 0x1;
    }
    
    @Override
    protected String listingString0(final boolean b) {
        if (this.codeSize() == 0) {
            return null;
        }
        return "nop // spacer";
    }
    
    @Override
    public DalvInsn withRegisters(final RegisterSpecList list) {
        return new OddSpacer(this.getPosition());
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput) {
        if (this.codeSize() != 0) {
            annotatedOutput.writeShort(InsnFormat.codeUnit(0, 0));
        }
    }
}
