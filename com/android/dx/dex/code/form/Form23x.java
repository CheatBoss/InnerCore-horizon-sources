package com.android.dx.dex.code.form;

import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.dex.code.*;
import com.android.dx.util.*;

public final class Form23x extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form23x();
    }
    
    private Form23x() {
    }
    
    @Override
    public int codeSize() {
        return 2;
    }
    
    @Override
    public BitSet compatibleRegs(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final BitSet set = new BitSet(3);
        set.set(0, InsnFormat.unsignedFitsInByte(registers.get(0).getReg()));
        set.set(1, InsnFormat.unsignedFitsInByte(registers.get(1).getReg()));
        set.set(2, InsnFormat.unsignedFitsInByte(registers.get(2).getReg()));
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
        sb.append(registers.get(2).regString());
        return sb.toString();
    }
    
    @Override
    public String insnCommentString(final DalvInsn dalvInsn, final boolean b) {
        return "";
    }
    
    @Override
    public boolean isCompatible(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        return dalvInsn instanceof SimpleInsn && registers.size() == 3 && InsnFormat.unsignedFitsInByte(registers.get(0).getReg()) && InsnFormat.unsignedFitsInByte(registers.get(1).getReg()) && InsnFormat.unsignedFitsInByte(registers.get(2).getReg());
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, registers.get(0).getReg()), InsnFormat.codeUnit(registers.get(1).getReg(), registers.get(2).getReg()));
    }
}
