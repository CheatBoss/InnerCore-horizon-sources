package com.android.dx.rop.code;

import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class BasicBlock implements LabeledItem
{
    private final InsnList insns;
    private final int label;
    private final int primarySuccessor;
    private final IntList successors;
    
    public BasicBlock(final int label, final InsnList insns, final IntList successors, final int primarySuccessor) {
        if (label < 0) {
            throw new IllegalArgumentException("label < 0");
        }
        try {
            insns.throwIfMutable();
            final int size = insns.size();
            if (size == 0) {
                throw new IllegalArgumentException("insns.size() == 0");
            }
            for (int i = size - 2; i >= 0; --i) {
                if (insns.get(i).getOpcode().getBranchingness() != 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("insns[");
                    sb.append(i);
                    sb.append("] is a ");
                    sb.append("branch or can throw");
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            if (insns.get(size - 1).getOpcode().getBranchingness() == 1) {
                throw new IllegalArgumentException("insns does not end with a branch or throwing instruction");
            }
            try {
                successors.throwIfMutable();
                if (primarySuccessor < -1) {
                    throw new IllegalArgumentException("primarySuccessor < -1");
                }
                if (primarySuccessor >= 0 && !successors.contains(primarySuccessor)) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("primarySuccessor ");
                    sb2.append(primarySuccessor);
                    sb2.append(" not in successors ");
                    sb2.append(successors);
                    throw new IllegalArgumentException(sb2.toString());
                }
                this.label = label;
                this.insns = insns;
                this.successors = successors;
                this.primarySuccessor = primarySuccessor;
            }
            catch (NullPointerException ex) {
                throw new NullPointerException("successors == null");
            }
        }
        catch (NullPointerException ex2) {
            throw new NullPointerException("insns == null");
        }
    }
    
    public boolean canThrow() {
        return this.insns.getLast().canThrow();
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o;
    }
    
    public TypeList getExceptionHandlerTypes() {
        return this.insns.getLast().getCatches();
    }
    
    public Insn getFirstInsn() {
        return this.insns.get(0);
    }
    
    public InsnList getInsns() {
        return this.insns;
    }
    
    @Override
    public int getLabel() {
        return this.label;
    }
    
    public Insn getLastInsn() {
        return this.insns.getLast();
    }
    
    public int getPrimarySuccessor() {
        return this.primarySuccessor;
    }
    
    public int getSecondarySuccessor() {
        if (this.successors.size() != 2) {
            throw new UnsupportedOperationException("block doesn't have exactly two successors");
        }
        int n;
        if ((n = this.successors.get(0)) == this.primarySuccessor) {
            n = this.successors.get(1);
        }
        return n;
    }
    
    public IntList getSuccessors() {
        return this.successors;
    }
    
    public boolean hasExceptionHandlers() {
        return this.insns.getLast().getCatches().size() != 0;
    }
    
    @Override
    public int hashCode() {
        return System.identityHashCode(this);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append(Hex.u2(this.label));
        sb.append('}');
        return sb.toString();
    }
    
    public BasicBlock withRegisterOffset(final int n) {
        return new BasicBlock(this.label, this.insns.withRegisterOffset(n), this.successors, this.primarySuccessor);
    }
    
    public interface Visitor
    {
        void visitBlock(final BasicBlock p0);
    }
}
