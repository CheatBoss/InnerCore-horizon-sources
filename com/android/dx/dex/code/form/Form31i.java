package com.android.dx.dex.code.form;

import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.dex.code.*;
import com.android.dx.rop.cst.*;
import com.android.dx.util.*;

public final class Form31i extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form31i();
    }
    
    private Form31i() {
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
        final CstLiteralBits cstLiteralBits = (CstLiteralBits)((CstInsn)dalvInsn).getConstant();
        final StringBuilder sb = new StringBuilder();
        sb.append(registers.get(0).regString());
        sb.append(", ");
        sb.append(InsnFormat.literalBitsString(cstLiteralBits));
        return sb.toString();
    }
    
    @Override
    public String insnCommentString(final DalvInsn dalvInsn, final boolean b) {
        return InsnFormat.literalBitsComment((CstLiteralBits)((CstInsn)dalvInsn).getConstant(), 32);
    }
    
    @Override
    public boolean isCompatible(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        if (!(dalvInsn instanceof CstInsn) || registers.size() != 1) {
            return false;
        }
        if (!InsnFormat.unsignedFitsInByte(registers.get(0).getReg())) {
            return false;
        }
        final Constant constant = ((CstInsn)dalvInsn).getConstant();
        return constant instanceof CstLiteralBits && ((CstLiteralBits)constant).fitsInInt();
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, dalvInsn.getRegisters().get(0).getReg()), ((CstLiteralBits)((CstInsn)dalvInsn).getConstant()).getIntBits());
    }
}
