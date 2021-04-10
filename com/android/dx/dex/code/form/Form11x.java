package com.android.dx.dex.code.form;

import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.dex.code.*;
import com.android.dx.util.*;

public final class Form11x extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form11x();
    }
    
    private Form11x() {
    }
    
    @Override
    public int codeSize() {
        return 1;
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
        return dalvInsn.getRegisters().get(0).regString();
    }
    
    @Override
    public String insnCommentString(final DalvInsn dalvInsn, final boolean b) {
        return "";
    }
    
    @Override
    public boolean isCompatible(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final boolean b = dalvInsn instanceof SimpleInsn;
        boolean b3;
        final boolean b2 = b3 = false;
        if (b) {
            b3 = b2;
            if (registers.size() == 1) {
                b3 = b2;
                if (InsnFormat.unsignedFitsInByte(registers.get(0).getReg())) {
                    b3 = true;
                }
            }
        }
        return b3;
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, dalvInsn.getRegisters().get(0).getReg()));
    }
}
