package com.android.dx.dex.code;

import com.android.dx.rop.code.*;
import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class HighRegisterPrefix extends VariableSizeInsn
{
    private SimpleInsn[] insns;
    
    public HighRegisterPrefix(final SourcePosition sourcePosition, final RegisterSpecList list) {
        super(sourcePosition, list);
        if (list.size() == 0) {
            throw new IllegalArgumentException("registers.size() == 0");
        }
        this.insns = null;
    }
    
    private void calculateInsnsIfNecessary() {
        if (this.insns != null) {
            return;
        }
        final RegisterSpecList registers = this.getRegisters();
        final int size = registers.size();
        this.insns = new SimpleInsn[size];
        int i = 0;
        int n = 0;
        while (i < size) {
            final RegisterSpec value = registers.get(i);
            this.insns[i] = moveInsnFor(value, n);
            n += value.getCategory();
            ++i;
        }
    }
    
    private static SimpleInsn moveInsnFor(final RegisterSpec registerSpec, final int n) {
        return DalvInsn.makeMove(SourcePosition.NO_INFO, RegisterSpec.make(n, registerSpec.getType()), registerSpec);
    }
    
    @Override
    protected String argString() {
        return null;
    }
    
    @Override
    public int codeSize() {
        int n = 0;
        this.calculateInsnsIfNecessary();
        final SimpleInsn[] insns = this.insns;
        for (int length = insns.length, i = 0; i < length; ++i) {
            n += insns[i].codeSize();
        }
        return n;
    }
    
    @Override
    protected String listingString0(final boolean b) {
        final RegisterSpecList registers = this.getRegisters();
        final int size = registers.size();
        final StringBuffer sb = new StringBuffer(100);
        int i = 0;
        int n = 0;
        while (i < size) {
            final RegisterSpec value = registers.get(i);
            final SimpleInsn moveInsn = moveInsnFor(value, n);
            if (i != 0) {
                sb.append('\n');
            }
            sb.append(moveInsn.listingString0(b));
            n += value.getCategory();
            ++i;
        }
        return sb.toString();
    }
    
    @Override
    public DalvInsn withRegisters(final RegisterSpecList list) {
        return new HighRegisterPrefix(this.getPosition(), list);
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput) {
        this.calculateInsnsIfNecessary();
        final SimpleInsn[] insns = this.insns;
        for (int length = insns.length, i = 0; i < length; ++i) {
            insns[i].writeTo(annotatedOutput);
        }
    }
}
