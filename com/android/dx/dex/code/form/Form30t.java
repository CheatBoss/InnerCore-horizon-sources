package com.android.dx.dex.code.form;

import com.android.dx.dex.code.*;
import com.android.dx.util.*;

public final class Form30t extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form30t();
    }
    
    private Form30t() {
    }
    
    @Override
    public boolean branchFits(final TargetInsn targetInsn) {
        return true;
    }
    
    @Override
    public int codeSize() {
        return 3;
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
        return dalvInsn instanceof TargetInsn && dalvInsn.getRegisters().size() == 0;
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, 0), ((TargetInsn)dalvInsn).getTargetOffset());
    }
}
