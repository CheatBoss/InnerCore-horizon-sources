package com.android.dx.dex.code.form;

import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.dex.code.*;
import com.android.dx.rop.cst.*;
import com.android.dx.util.*;

public final class Form22c extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form22c();
    }
    
    private Form22c() {
    }
    
    @Override
    public int codeSize() {
        return 2;
    }
    
    @Override
    public BitSet compatibleRegs(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final BitSet set = new BitSet(2);
        set.set(0, InsnFormat.unsignedFitsInNibble(registers.get(0).getReg()));
        set.set(1, InsnFormat.unsignedFitsInNibble(registers.get(1).getReg()));
        return set;
    }
    
    @Override
    public String insnArgString(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final StringBuilder sb = new StringBuilder();
        sb.append(registers.get(0).regString());
        sb.append(", ");
        sb.append(registers.get(1).regString());
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
        final RegisterSpecList registers = dalvInsn.getRegisters();
        if (!(dalvInsn instanceof CstInsn) || registers.size() != 2 || !InsnFormat.unsignedFitsInNibble(registers.get(0).getReg())) {
            return false;
        }
        boolean b = true;
        if (!InsnFormat.unsignedFitsInNibble(registers.get(1).getReg())) {
            return false;
        }
        final CstInsn cstInsn = (CstInsn)dalvInsn;
        if (!InsnFormat.unsignedFitsInShort(cstInsn.getIndex())) {
            return false;
        }
        final Constant constant = cstInsn.getConstant();
        if (!(constant instanceof CstType)) {
            if (constant instanceof CstFieldRef) {
                return true;
            }
            b = false;
        }
        return b;
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, InsnFormat.makeByte(registers.get(0).getReg(), registers.get(1).getReg())), (short)((CstInsn)dalvInsn).getIndex());
    }
}
