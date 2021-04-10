package com.android.dx.dex.code.form;

import com.android.dx.dex.code.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;
import com.android.dx.util.*;

public final class Form3rc extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form3rc();
    }
    
    private Form3rc() {
    }
    
    @Override
    public int codeSize() {
        return 3;
    }
    
    @Override
    public String insnArgString(final DalvInsn dalvInsn) {
        final StringBuilder sb = new StringBuilder();
        sb.append(InsnFormat.regRangeString(dalvInsn.getRegisters()));
        sb.append(", ");
        sb.append(InsnFormat.cstString(dalvInsn));
        return sb.toString();
    }
    
    @Override
    public String insnCommentString(final DalvInsn dalvInsn, final boolean b) {
        if (b) {
            return InsnFormat.cstComment(dalvInsn);
        }
        return "";
    }
    
    @Override
    public boolean isCompatible(final DalvInsn dalvInsn) {
        if (!(dalvInsn instanceof CstInsn)) {
            return false;
        }
        final CstInsn cstInsn = (CstInsn)dalvInsn;
        final int index = cstInsn.getIndex();
        final Constant constant = cstInsn.getConstant();
        if (!InsnFormat.unsignedFitsInShort(index)) {
            return false;
        }
        if (!(constant instanceof CstMethodRef) && !(constant instanceof CstType)) {
            return false;
        }
        final RegisterSpecList registers = cstInsn.getRegisters();
        registers.size();
        return registers.size() == 0 || (InsnFormat.isRegListSequential(registers) && InsnFormat.unsignedFitsInShort(registers.get(0).getReg()) && InsnFormat.unsignedFitsInByte(registers.getWordCount()));
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final int index = ((CstInsn)dalvInsn).getIndex();
        final int size = registers.size();
        int reg = 0;
        if (size != 0) {
            reg = registers.get(0).getReg();
        }
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, registers.getWordCount()), (short)index, (short)reg);
    }
}
