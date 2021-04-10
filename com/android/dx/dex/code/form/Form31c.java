package com.android.dx.dex.code.form;

import java.util.*;
import com.android.dx.dex.code.*;
import com.android.dx.rop.code.*;
import com.android.dx.rop.cst.*;
import com.android.dx.util.*;

public final class Form31c extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form31c();
    }
    
    private Form31c() {
    }
    
    @Override
    public int codeSize() {
        return 3;
    }
    
    @Override
    public BitSet compatibleRegs(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final int size = registers.size();
        final BitSet set = new BitSet(size);
        final boolean unsignedFitsInByte = InsnFormat.unsignedFitsInByte(registers.get(0).getReg());
        if (size == 1) {
            set.set(0, unsignedFitsInByte);
            return set;
        }
        if (registers.get(0).getReg() == registers.get(1).getReg()) {
            set.set(0, unsignedFitsInByte);
            set.set(1, unsignedFitsInByte);
        }
        return set;
    }
    
    @Override
    public String insnArgString(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final StringBuilder sb = new StringBuilder();
        sb.append(registers.get(0).regString());
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
        final RegisterSpecList registers = dalvInsn.getRegisters();
        RegisterSpec registerSpec = null;
        switch (registers.size()) {
            default: {
                return false;
            }
            case 2: {
                if ((registerSpec = registers.get(0)).getReg() != registers.get(1).getReg()) {
                    return false;
                }
                break;
            }
            case 1: {
                registerSpec = registers.get(0);
                break;
            }
        }
        if (!InsnFormat.unsignedFitsInByte(registerSpec.getReg())) {
            return false;
        }
        final Constant constant = ((CstInsn)dalvInsn).getConstant();
        return constant instanceof CstType || constant instanceof CstFieldRef || constant instanceof CstString;
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, dalvInsn.getRegisters().get(0).getReg()), ((CstInsn)dalvInsn).getIndex());
    }
}
