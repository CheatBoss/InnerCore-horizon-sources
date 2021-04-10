package com.android.dx.rop.code;

import com.android.dx.util.*;
import java.util.*;

public final class LocalVariableInfo extends MutabilityControl
{
    private final RegisterSpecSet[] blockStarts;
    private final RegisterSpecSet emptySet;
    private final HashMap<Insn, RegisterSpec> insnAssignments;
    private final int regCount;
    
    public LocalVariableInfo(final RopMethod ropMethod) {
        if (ropMethod == null) {
            throw new NullPointerException("method == null");
        }
        final BasicBlockList blocks = ropMethod.getBlocks();
        final int maxLabel = blocks.getMaxLabel();
        this.regCount = blocks.getRegCount();
        this.emptySet = new RegisterSpecSet(this.regCount);
        this.blockStarts = new RegisterSpecSet[maxLabel];
        this.insnAssignments = new HashMap<Insn, RegisterSpec>(blocks.getInstructionCount());
        this.emptySet.setImmutable();
    }
    
    private RegisterSpecSet getStarts0(final int n) {
        try {
            return this.blockStarts[n];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("bogus label");
        }
    }
    
    public void addAssignment(final Insn insn, final RegisterSpec registerSpec) {
        this.throwIfImmutable();
        if (insn == null) {
            throw new NullPointerException("insn == null");
        }
        if (registerSpec == null) {
            throw new NullPointerException("spec == null");
        }
        this.insnAssignments.put(insn, registerSpec);
    }
    
    public void debugDump() {
        for (int i = 0; i < this.blockStarts.length; ++i) {
            if (this.blockStarts[i] != null) {
                if (this.blockStarts[i] == this.emptySet) {
                    System.out.printf("%04x: empty set\n", i);
                }
                else {
                    System.out.printf("%04x: %s\n", i, this.blockStarts[i]);
                }
            }
        }
    }
    
    public RegisterSpec getAssignment(final Insn insn) {
        return this.insnAssignments.get(insn);
    }
    
    public int getAssignmentCount() {
        return this.insnAssignments.size();
    }
    
    public RegisterSpecSet getStarts(final int n) {
        final RegisterSpecSet starts0 = this.getStarts0(n);
        if (starts0 != null) {
            return starts0;
        }
        return this.emptySet;
    }
    
    public RegisterSpecSet getStarts(final BasicBlock basicBlock) {
        return this.getStarts(basicBlock.getLabel());
    }
    
    public boolean mergeStarts(final int n, RegisterSpecSet mutableCopy) {
        final RegisterSpecSet starts0 = this.getStarts0(n);
        if (starts0 == null) {
            this.setStarts(n, mutableCopy);
            return true;
        }
        final RegisterSpecSet mutableCopy2 = starts0.mutableCopy();
        if (starts0.size() != 0) {
            mutableCopy2.intersect(mutableCopy, true);
            mutableCopy = mutableCopy2;
        }
        else {
            mutableCopy = mutableCopy.mutableCopy();
        }
        if (starts0.equals(mutableCopy)) {
            return false;
        }
        mutableCopy.setImmutable();
        this.setStarts(n, mutableCopy);
        return true;
    }
    
    public RegisterSpecSet mutableCopyOfStarts(final int n) {
        final RegisterSpecSet starts0 = this.getStarts0(n);
        if (starts0 != null) {
            return starts0.mutableCopy();
        }
        return new RegisterSpecSet(this.regCount);
    }
    
    public void setStarts(final int n, final RegisterSpecSet set) {
        this.throwIfImmutable();
        if (set == null) {
            throw new NullPointerException("specs == null");
        }
        try {
            this.blockStarts[n] = set;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("bogus label");
        }
    }
}
