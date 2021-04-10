package com.android.dx.dex.code.form;

import com.android.dx.dex.code.*;
import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.util.*;

public final class Form22t extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form22t();
    }
    
    private Form22t() {
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
        if (!(dalvInsn instanceof TargetInsn) || registers.size() != 2 || !InsnFormat.unsignedFitsInNibble(registers.get(0).getReg())) {
            return false;
        }
        boolean branchFits = true;
        if (!InsnFormat.unsignedFitsInNibble(registers.get(1).getReg())) {
            return false;
        }
        final TargetInsn targetInsn = (TargetInsn)dalvInsn;
        if (targetInsn.hasTargetOffset()) {
            branchFits = this.branchFits(targetInsn);
        }
        return branchFits;
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, InsnFormat.makeByte(registers.get(0).getReg(), registers.get(1).getReg())), (short)((TargetInsn)dalvInsn).getTargetOffset());
    }
}
