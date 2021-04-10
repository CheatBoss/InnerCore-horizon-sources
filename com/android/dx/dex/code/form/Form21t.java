package com.android.dx.dex.code.form;

import com.android.dx.dex.code.*;
import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.util.*;

public final class Form21t extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form21t();
    }
    
    private Form21t() {
    }
    
    @Override
    public boolean branchFits(final TargetInsn targetInsn) {
        final int targetOffset = targetInsn.getTargetOffset();
        return targetOffset != 0 && InsnFormat.signedFitsInShort(targetOffset);
    }
    
    @Override
    public int codeSize() {
        return 2;
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
        if (dalvInsn instanceof TargetInsn) {
            final int size = registers.size();
            boolean branchFits = true;
            if (size == 1) {
                if (!InsnFormat.unsignedFitsInByte(registers.get(0).getReg())) {
                    return false;
                }
                final TargetInsn targetInsn = (TargetInsn)dalvInsn;
                if (targetInsn.hasTargetOffset()) {
                    branchFits = this.branchFits(targetInsn);
                }
                return branchFits;
            }
        }
        return false;
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, dalvInsn.getRegisters().get(0).getReg()), (short)((TargetInsn)dalvInsn).getTargetOffset());
    }
}
