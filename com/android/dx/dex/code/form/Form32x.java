package com.android.dx.dex.code.form;

import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.dex.code.*;
import com.android.dx.util.*;

public final class Form32x extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form32x();
    }
    
    private Form32x() {
    }
    
    @Override
    public int codeSize() {
        return 3;
    }
    
    @Override
    public BitSet compatibleRegs(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final BitSet set = new BitSet(2);
        set.set(0, InsnFormat.unsignedFitsInShort(registers.get(0).getReg()));
        set.set(1, InsnFormat.unsignedFitsInShort(registers.get(1).getReg()));
        return set;
    }
    
    @Override
    public String insnArgString(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final StringBuilder sb = new StringBuilder();
        sb.append(registers.get(0).regString());
        sb.append(", ");
        sb.append(registers.get(1).regString());
        return sb.toString();
    }
    
    @Override
    public String insnCommentString(final DalvInsn dalvInsn, final boolean b) {
        return "";
    }
    
    @Override
    public boolean isCompatible(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        return dalvInsn instanceof SimpleInsn && registers.size() == 2 && InsnFormat.unsignedFitsInShort(registers.get(0).getReg()) && InsnFormat.unsignedFitsInShort(registers.get(1).getReg());
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, 0), (short)registers.get(0).getReg(), (short)registers.get(1).getReg());
    }
}
