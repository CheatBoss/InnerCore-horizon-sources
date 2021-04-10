package com.android.dx.dex.code.form;

import com.android.dx.dex.code.*;
import com.android.dx.util.*;

public final class Form10t extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form10t();
    }
    
    private Form10t() {
    }
    
    @Override
    public boolean branchFits(final TargetInsn targetInsn) {
        final int targetOffset = targetInsn.getTargetOffset();
        return targetOffset != 0 && InsnFormat.signedFitsInByte(targetOffset);
    }
    
    @Override
    public int codeSize() {
        return 1;
    }
    
    @Override
    public String insnArgString(final DalvInsn dalvInsn) {
        return InsnFormat.branchString(dalvInsn);
    }
    
    @Override
    public String insnCommentString(final DalvInsn dalvInsn, final boolean b) {
        return InsnFormat.branchComment(dalvInsn);
    }
    
    @Override
    public boolean isCompatible(final DalvInsn dalvInsn) {
        if (dalvInsn instanceof TargetInsn && dalvInsn.getRegisters().size() == 0) {
            final TargetInsn targetInsn = (TargetInsn)dalvInsn;
            return !targetInsn.hasTargetOffset() || this.branchFits(targetInsn);
        }
        return false;
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, ((TargetInsn)dalvInsn).getTargetOffset() & 0xFF));
    }
}
