package com.android.dx.ssa;

import com.android.dx.util.*;
import com.android.dx.rop.code.*;
import java.util.*;

public class LocalVariableInfo extends MutabilityControl
{
    private final RegisterSpecSet[] blockStarts;
    private final RegisterSpecSet emptySet;
    private final HashMap<SsaInsn, RegisterSpec> insnAssignments;
    private final int regCount;
    
    public LocalVariableInfo(final SsaMethod ssaMethod) {
        if (ssaMethod == null) {
            throw new NullPointerException("method == null");
        }
        final ArrayList<SsaBasicBlock> blocks = ssaMethod.getBlocks();
        this.regCount = ssaMethod.getRegCount();
        this.emptySet = new RegisterSpecSet(this.regCount);
        this.blockStarts = new RegisterSpecSet[blocks.size()];
        this.insnAssignments = new HashMap<SsaInsn, RegisterSpec>();
        this.emptySet.setImmutable();
    }
    
    private RegisterSpecSet getStarts0(final int n) {
        try {
            return this.blockStarts[n];
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("bogus index");
        }
    }
    
    public void addAssignment(final SsaInsn ssaInsn, final RegisterSpec registerSpec) {
        this.throwIfImmutable();
        if (ssaInsn == null) {
            throw new NullPointerException("insn == null");
        }
        if (registerSpec == null) {
            throw new NullPointerException("spec == null");
        }
        this.insnAssignments.put(ssaInsn, registerSpec);
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
    
    public RegisterSpec getAssignment(final SsaInsn ssaInsn) {
        return this.insnAssignments.get(ssaInsn);
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
    
    public RegisterSpecSet getStarts(final SsaBasicBlock ssaBasicBlock) {
        return this.getStarts(ssaBasicBlock.getIndex());
    }
    
    public boolean mergeStarts(final int n, final RegisterSpecSet set) {
        final RegisterSpecSet starts0 = this.getStarts0(n);
        if (starts0 == null) {
            this.setStarts(n, set);
            return true;
        }
        final RegisterSpecSet mutableCopy = starts0.mutableCopy();
        mutableCopy.intersect(set, true);
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
            throw new IllegalArgumentException("bogus index");
        }
    }
}
