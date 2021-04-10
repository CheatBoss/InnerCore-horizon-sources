package com.android.dx.dex.code.form;

import com.android.dx.rop.code.*;
import com.android.dx.rop.type.*;
import java.util.*;
import com.android.dx.dex.code.*;
import com.android.dx.rop.cst.*;
import com.android.dx.util.*;

public final class Form35c extends InsnFormat
{
    private static final int MAX_NUM_OPS = 5;
    public static final InsnFormat THE_ONE;
    
    static {
        THE_ONE = new Form35c();
    }
    
    private Form35c() {
    }
    
    private static RegisterSpecList explicitize(final RegisterSpecList list) {
        final int wordCount = wordCount(list);
        final int size = list.size();
        if (wordCount == size) {
            return list;
        }
        final RegisterSpecList list2 = new RegisterSpecList(wordCount);
        int n = 0;
        for (int i = 0; i < size; ++i) {
            final RegisterSpec value = list.get(i);
            list2.set(n, value);
            if (value.getCategory() == 2) {
                list2.set(n + 1, RegisterSpec.make(value.getReg() + 1, Type.VOID));
                n += 2;
            }
            else {
                ++n;
            }
        }
        list2.setImmutable();
        return list2;
    }
    
    private static int wordCount(final RegisterSpecList list) {
        final int size = list.size();
        final int n = -1;
        if (size > 5) {
            return -1;
        }
        int n2 = 0;
        for (int i = 0; i < size; ++i) {
            final RegisterSpec value = list.get(i);
            n2 += value.getCategory();
            if (!InsnFormat.unsignedFitsInNibble(value.getReg() + value.getCategory() - 1)) {
                return -1;
            }
        }
        int n3 = n;
        if (n2 <= 5) {
            n3 = n2;
        }
        return n3;
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
        for (int i = 0; i < size; ++i) {
            final RegisterSpec value = registers.get(i);
            set.set(i, InsnFormat.unsignedFitsInNibble(value.getReg() + value.getCategory() - 1));
        }
        return set;
    }
    
    @Override
    public String insnArgString(final DalvInsn dalvInsn) {
        final RegisterSpecList explicitize = explicitize(dalvInsn.getRegisters());
        final StringBuilder sb = new StringBuilder();
        sb.append(InsnFormat.regListString(explicitize));
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
        final boolean b = dalvInsn instanceof CstInsn;
        boolean b2 = false;
        if (!b) {
            return false;
        }
        final CstInsn cstInsn = (CstInsn)dalvInsn;
        if (!InsnFormat.unsignedFitsInShort(cstInsn.getIndex())) {
            return false;
        }
        final Constant constant = cstInsn.getConstant();
        if (!(constant instanceof CstMethodRef) && !(constant instanceof CstType)) {
            return false;
        }
        if (wordCount(cstInsn.getRegisters()) >= 0) {
            b2 = true;
        }
        return b2;
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput, final DalvInsn dalvInsn) {
        final int index = ((CstInsn)dalvInsn).getIndex();
        final RegisterSpecList explicitize = explicitize(dalvInsn.getRegisters());
        final int size = explicitize.size();
        int reg = 0;
        int reg2;
        if (size > 0) {
            reg2 = explicitize.get(0).getReg();
        }
        else {
            reg2 = 0;
        }
        int reg3;
        if (size > 1) {
            reg3 = explicitize.get(1).getReg();
        }
        else {
            reg3 = 0;
        }
        int reg4;
        if (size > 2) {
            reg4 = explicitize.get(2).getReg();
        }
        else {
            reg4 = 0;
        }
        int reg5;
        if (size > 3) {
            reg5 = explicitize.get(3).getReg();
        }
        else {
            reg5 = 0;
        }
        if (size > 4) {
            reg = explicitize.get(4).getReg();
        }
        InsnFormat.write(annotatedOutput, InsnFormat.opcodeUnit(dalvInsn, InsnFormat.makeByte(reg, size)), (short)index, InsnFormat.codeUnit(reg2, reg3, reg4, reg5));
    }
}
