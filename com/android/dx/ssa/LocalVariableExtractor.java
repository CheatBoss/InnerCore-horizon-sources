package com.android.dx.ssa;

import java.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.util.*;

public class LocalVariableExtractor
{
    private final ArrayList<SsaBasicBlock> blocks;
    private final SsaMethod method;
    private final LocalVariableInfo resultInfo;
    private final BitSet workSet;
    
    private LocalVariableExtractor(final SsaMethod method) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        final ArrayList<SsaBasicBlock> blocks = method.getBlocks();
        this.method = method;
        this.blocks = blocks;
        this.resultInfo = new LocalVariableInfo(method);
        this.workSet = new BitSet(blocks.size());
    }
    
    private LocalVariableInfo doit() {
        if (this.method.getRegCount() > 0) {
            for (int i = this.method.getEntryBlockIndex(); i >= 0; i = this.workSet.nextSetBit(0)) {
                this.workSet.clear(i);
                this.processBlock(i);
            }
        }
        this.resultInfo.setImmutable();
        return this.resultInfo;
    }
    
    public static LocalVariableInfo extract(final SsaMethod ssaMethod) {
        return new LocalVariableExtractor(ssaMethod).doit();
    }
    
    private void processBlock(int i) {
        final RegisterSpecSet mutableCopyOfStarts = this.resultInfo.mutableCopyOfStarts(i);
        final SsaBasicBlock ssaBasicBlock = this.blocks.get(i);
        final ArrayList<SsaInsn> insns = ssaBasicBlock.getInsns();
        final int size = insns.size();
        if (i == this.method.getExitBlockIndex()) {
            return;
        }
        final SsaInsn ssaInsn = insns.get(size - 1);
        i = ssaInsn.getOriginalRopInsn().getCatches().size();
        final int n = 1;
        if (i != 0) {
            i = 1;
        }
        else {
            i = 0;
        }
        if (i != 0 && ssaInsn.getResult() != null) {
            i = n;
        }
        else {
            i = 0;
        }
        RegisterSpecSet set = mutableCopyOfStarts;
        RegisterSpecSet mutableCopy;
        for (int j = 0; j < size; ++j, set = mutableCopy) {
            mutableCopy = set;
            if (i != 0) {
                mutableCopy = set;
                if (j == size - 1) {
                    set.setImmutable();
                    mutableCopy = set.mutableCopy();
                }
            }
            final SsaInsn ssaInsn2 = insns.get(j);
            final RegisterSpec localAssignment = ssaInsn2.getLocalAssignment();
            if (localAssignment == null) {
                final RegisterSpec result = ssaInsn2.getResult();
                if (result != null && mutableCopy.get(result.getReg()) != null) {
                    mutableCopy.remove(mutableCopy.get(result.getReg()));
                }
            }
            else {
                final RegisterSpec withSimpleType = localAssignment.withSimpleType();
                if (!withSimpleType.equals(mutableCopy.get(withSimpleType))) {
                    final RegisterSpec localItemToSpec = mutableCopy.localItemToSpec(withSimpleType.getLocalItem());
                    if (localItemToSpec != null && localItemToSpec.getReg() != withSimpleType.getReg()) {
                        mutableCopy.remove(localItemToSpec);
                    }
                    this.resultInfo.addAssignment(ssaInsn2, withSimpleType);
                    mutableCopy.put(withSimpleType);
                }
            }
        }
        set.setImmutable();
        final IntList successorList = ssaBasicBlock.getSuccessorList();
        final int size2 = successorList.size();
        final int primarySuccessorIndex = ssaBasicBlock.getPrimarySuccessorIndex();
        int value;
        RegisterSpecSet set2;
        for (i = 0; i < size2; ++i) {
            value = successorList.get(i);
            if (value == primarySuccessorIndex) {
                set2 = set;
            }
            else {
                set2 = mutableCopyOfStarts;
            }
            if (this.resultInfo.mergeStarts(value, set2)) {
                this.workSet.set(value);
            }
        }
    }
}
