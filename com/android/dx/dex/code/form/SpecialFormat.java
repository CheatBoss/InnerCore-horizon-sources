package com.android.dx.dex.code.form;

import com.android.dx.dex.code.*;
import com.android.dx.util.*;

public final class SpecialFormat extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new SpecialFormat();
    }
    
    private SpecialFormat() {
    }
    
    @Override
    public int codeSize() {
        throw new RuntimeException("unsupported");
    }
    
    @Override
    public String insnArgString(final DalvInsn dalvInsn) {
        throw new RuntimeException("unsupported");
    }
    
    @Override
    public String insnCommentString(final DalvInsn dalvInsn, final boolean b) {
        throw new RuntimeException("unsupported");
    }
    
    @Override
    public boolean isCompatible(final DalvInsn dalvInsn) {
        return true;
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        throw new RuntimeException("unsupported");
    }
}
