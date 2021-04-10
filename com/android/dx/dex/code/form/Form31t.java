package com.android.dx.dex.code.form;

import com.android.dx.dex.code.*;
import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.util.*;

public final class Form31t extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form31t();
    }
    
    private Form31t() {
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
    public BitSet compatibleRegs(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final BitSet set = new BitSet(1);
        set.set(0, InsnFormat.unsignedFitsInByte(registers.get(0).getReg()));
        return set;
    }
    
    @Override
    public String insnArgString(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final StringBuilder sb = new StringBuilder();
        sb.append(registers.get(0).regString());
        sb.append(", ");
        sb.append(InsnFormat.branchString(dalvInsn));
        return sb.toString();
    }
    
    @Override
    public String insnCommentString(final DalvInsn dalvInsn, final boolean b) {
        return InsnFormat.branchComment(dalvInsn);
    }
    
    @Override
    public boolean isCompatible(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        return dalvInsn instanceof TargetInsn && registers.size() == 1 && InsnFormat.unsignedFitsInByte(registers.get(0).getReg());
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, dalvInsn.getRegisters().get(0).getReg()), ((TargetInsn)dalvInsn).getTargetOffset());
    }
}
