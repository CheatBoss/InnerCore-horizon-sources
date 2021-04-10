package com.android.dx.dex.code.form;

import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.dex.code.*;
import com.android.dx.rop.cst.*;
import com.android.dx.util.*;

public final class Form11n extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form11n();
    }
    
    private Form11n() {
    }
    
    @Override
    public int codeSize() {
        return 1;
    }
    
    @Override
    public BitSet compatibleRegs(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final BitSet set = new BitSet(1);
        set.set(0, InsnFormat.unsignedFitsInNibble(registers.get(0).getReg()));
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
        return InsnFormat.literalBitsComment((CstLiteralBits)((CstInsn)dalvInsn).getConstant(), 4);
    }
    
    @Override
    public boolean isCompatible(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final boolean b = dalvInsn instanceof CstInsn;
        final boolean b2 = false;
        if (!b || registers.size() != 1) {
            return false;
        }
        if (!InsnFormat.unsignedFitsInNibble(registers.get(0).getReg())) {
            return false;
        }
        final Constant constant = ((CstInsn)dalvInsn).getConstant();
        if (!(constant instanceof CstLiteralBits)) {
            return false;
        }
        final CstLiteralBits cstLiteralBits = (CstLiteralBits)constant;
        boolean b3 = b2;
        if (cstLiteralBits.fitsInInt()) {
            b3 = b2;
            if (InsnFormat.signedFitsInNibble(cstLiteralBits.getIntBits())) {
                b3 = true;
            }
        }
        return b3;
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, InsnFormat.makeByte(dalvInsn.getRegisters().get(0).getReg(), ((CstLiteralBits)((CstInsn)dalvInsn).getConstant()).getIntBits() & 0xF)));
    }
}
