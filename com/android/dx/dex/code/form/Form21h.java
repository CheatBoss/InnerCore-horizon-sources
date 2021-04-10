package com.android.dx.dex.code.form;

import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.dex.code.*;
import com.android.dx.rop.cst.*;
import com.android.dx.util.*;

public final class Form21h extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form21h();
    }
    
    private Form21h() {
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
        final CstLiteralBits cstLiteralBits = (CstLiteralBits)((CstInsn)dalvInsn).getConstant();
        final StringBuilder sb = new StringBuilder();
        sb.append(registers.get(0).regString());
        sb.append(", ");
        sb.append(InsnFormat.literalBitsString(cstLiteralBits));
        return sb.toString();
    }
    
    @Override
    public String insnCommentString(final DalvInsn dalvInsn, final boolean b) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final CstLiteralBits cstLiteralBits = (CstLiteralBits)((CstInsn)dalvInsn).getConstant();
        int n;
        if (registers.get(0).getCategory() == 1) {
            n = 32;
        }
        else {
            n = 64;
        }
        return InsnFormat.literalBitsComment(cstLiteralBits, n);
    }
    
    @Override
    public boolean isCompatible(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final boolean b = dalvInsn instanceof CstInsn;
        final boolean b2 = false;
        boolean b3 = false;
        if (!b || registers.size() != 1) {
            return false;
        }
        if (!InsnFormat.unsignedFitsInByte(registers.get(0).getReg())) {
            return false;
        }
        final Constant constant = ((CstInsn)dalvInsn).getConstant();
        if (!(constant instanceof CstLiteralBits)) {
            return false;
        }
        final CstLiteralBits cstLiteralBits = (CstLiteralBits)constant;
        if (registers.get(0).getCategory() == 1) {
            if ((0xFFFF & cstLiteralBits.getIntBits()) == 0x0) {
                b3 = true;
            }
            return b3;
        }
        boolean b4 = b2;
        if ((cstLiteralBits.getLongBits() & 0xFFFFFFFFFFFFL) == 0x0L) {
            b4 = true;
        }
        return b4;
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final CstLiteralBits cstLiteralBits = (CstLiteralBits)((CstInsn)dalvInsn).getConstant();
        short n;
        if (registers.get(0).getCategory() == 1) {
            n = (short)(cstLiteralBits.getIntBits() >>> 16);
        }
        else {
            n = (short)(cstLiteralBits.getLongBits() >>> 48);
        }
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, registers.get(0).getReg()), n);
    }
}
