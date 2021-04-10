package com.android.dx.dex.code.form;

import com.android.dx.dex.code.*;
import com.android.dx.util.*;

public final class Form10x extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form10x();
    }
    
    private Form10x() {
    }
    
    @Override
    public int codeSize() {
        return 1;
    }
    
    @Override
    public String insnArgString(final DalvInsn dalvInsn) {
        return "";
    }
    
    @Override
    public String insnCommentString(final DalvInsn dalvInsn, final boolean b) {
        return "";
    }
    
    @Override
    public boolean isCompatible(final DalvInsn dalvInsn) {
        return dalvInsn instanceof SimpleInsn && dalvInsn.getRegisters().size() == 0;
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, 0));
    }
}
