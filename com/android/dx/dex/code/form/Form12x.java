package com.android.dx.dex.code.form;

import java.util.*;
import com.android.dx.dex.code.*;
import com.android.dx.rop.code.*;
import com.android.dx.util.*;

public final class Form12x extends InsnFormat
{
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form12x();
    }
    
    private Form12x() {
    }
    
    @Override
    public int codeSize() {
        return 1;
    }
    
    @Override
    public BitSet compatibleRegs(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final BitSet set = new BitSet(2);
        final int reg = registers.get(0).getReg();
        final int reg2 = registers.get(1).getReg();
        switch (registers.size()) {
            default: {
                throw new AssertionError();
            }
            case 3: {
                if (reg != reg2) {
                    set.set(0, false);
                    set.set(1, false);
                }
                else {
                    final boolean unsignedFitsInNibble = InsnFormat.unsignedFitsInNibble(reg2);
                    set.set(0, unsignedFitsInNibble);
                    set.set(1, unsignedFitsInNibble);
                }
                set.set(2, InsnFormat.unsignedFitsInNibble(registers.get(2).getReg()));
                return set;
            }
            case 2: {
                set.set(0, InsnFormat.unsignedFitsInNibble(reg));
                set.set(1, InsnFormat.unsignedFitsInNibble(reg2));
                return set;
            }
        }
    }
    
    @Override
    public String insnArgString(final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final int size = registers.size();
        final StringBuilder sb = new StringBuilder();
        sb.append(registers.get(size - 2).regString());
        sb.append(", ");
        sb.append(registers.get(size - 1).regString());
        return sb.toString();
    }
    
    @Override
    public String insnCommentString(final DalvInsn dalvInsn, final boolean b) {
        return "";
    }
    
    @Override
    public boolean isCompatible(final DalvInsn dalvInsn) {
        final boolean b = dalvInsn instanceof SimpleInsn;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final RegisterSpecList registers = dalvInsn.getRegisters();
        RegisterSpec registerSpec = null;
        RegisterSpec value2 = null;
        switch (registers.size()) {
            default: {
                return false;
            }
            case 3: {
                final RegisterSpec value = registers.get(1);
                registerSpec = registers.get(2);
                value2 = value;
                if (value.getReg() != registers.get(0).getReg()) {
                    return false;
                }
                break;
            }
            case 2: {
                value2 = registers.get(0);
                registerSpec = registers.get(1);
                break;
            }
        }
        boolean b3 = b2;
        if (InsnFormat.unsignedFitsInNibble(value2.getReg())) {
            b3 = b2;
            if (InsnFormat.unsignedFitsInNibble(registerSpec.getReg())) {
                b3 = true;
            }
        }
        return b3;
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        final RegisterSpecList registers = dalvInsn.getRegisters();
        final int size = registers.size();
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, InsnFormat.makeByte(registers.get(size - 2).getReg(), registers.get(size - 1).getReg())));
    }
}
