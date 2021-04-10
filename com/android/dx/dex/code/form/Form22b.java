package com.android.dx.dex.code.form;

import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.dex.code.*;
import com.android.dx.rop.cst.*;
import com.android.dx.util.*;

public final class Form22b extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form22b();
    }
    
    private Form22b() {
    }
    
    @Override
    public int codeSize() {
        return 2;
    }
    
    @Override
    public BitSet compatibleRegs(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final BitSet set = new BitSet(2);
        set.set(0, InsnFormat.unsignedFitsInByte(registers.get(0).getReg()));
        set.set(1, InsnFormat.unsignedFitsInByte(registers.get(1).getReg()));
        return set;
    }
    
    @Override
    public String insnArgString(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final CstLiteralBits cstLiteralBits = (CstLiteralBits)((CstInsn)dalvInsn).getConstant();
        final StringBuilder sb = new StringBuilder();
        sb.append(registers.get(0).regString());
        sb.append(", ");
        sb.append(registers.get(1).regString());
        sb.append(", ");
        sb.append(InsnFormat.literalBitsString(cstLiteralBits));
        return sb.toString();
    }
    
    @Override
    public String insnCommentString(final DalvInsn dalvInsn, final boolean b) {
        return InsnFormat.literalBitsComment((CstLiteralBits)((CstInsn)dalvInsn).getConstant(), 8);
    }
    
    @Override
    public boolean isCompatible(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        if (!(dalvInsn instanceof CstInsn) || registers.size() != 2 || !InsnFormat.unsignedFitsInByte(registers.get(0).getReg())) {
            return false;
        }
        if (!InsnFormat.unsignedFitsInByte(registers.get(1).getReg())) {
            return false;
        }
        final Constant constant = ((CstInsn)dalvInsn).getConstant();
        if (!(constant instanceof CstLiteralBits)) {
            return false;
        }
        final CstLiteralBits cstLiteralBits = (CstLiteralBits)constant;
        return cstLiteralBits.fitsInInt() && InsnFormat.signedFitsInByte(cstLiteralBits.getIntBits());
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, registers.get(0).getReg()), InsnFormat.codeUnit(registers.get(1).getReg(), ((CstLiteralBits)((CstInsn)dalvInsn).getConstant()).getIntBits() & 0xFF));
    }
}
