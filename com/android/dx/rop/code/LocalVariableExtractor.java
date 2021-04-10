package com.android.dx.rop.code;

import com.android.dx.util.*;

public final class LocalVariableExtractor
{
    private final BasicBlockList blocks;
    private final RopMethod method;
    private final LocalVariableInfo resultInfo;
    private final int[] workSet;
    
    private LocalVariableExtractor(final RopMethod method) {
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        final BasicBlockList blocks = method.getBlocks();
        final int maxLabel = blocks.getMaxLabel();
        this.method = method;
        this.blocks = blocks;
        this.resultInfo = new LocalVariableInfo(method);
        this.workSet = Bits.makeBitSet(maxLabel);
    }
    
    private LocalVariableInfo doit() {
        for (int i = this.method.getFirstLabel(); i >= 0; i = Bits.findFirst(this.workSet, 0)) {
            Bits.clear(this.workSet, i);
            this.processBlock(i);
        }
        this.resultInfo.setImmutable();
        return this.resultInfo;
    }
    
    public static LocalVariableInfo extract(final RopMethod ropMethod) {
        return new LocalVariableExtractor(ropMethod).doit();
    }
    
    private void processBlock(int i) {
        final RegisterSpecSet mutableCopyOfStarts = this.resultInfo.mutableCopyOfStarts(i);
        final BasicBlock labelToBlock = this.blocks.labelToBlock(i);
        final InsnList insns = labelToBlock.getInsns();
        final int size = insns.size();
        if (labelToBlock.hasExceptionHandlers() && insns.getLast().getResult() != null) {
            i = 1;
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
            final Insn value = insns.get(j);
            final RegisterSpec localAssignment = value.getLocalAssignment();
            if (localAssignment == null) {
                final RegisterSpec result = value.getResult();
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
                    this.resultInfo.addAssignment(value, withSimpleType);
                    mutableCopy.put(withSimpleType);
                }
            }
        }
        set.setImmutable();
        final IntList successors = labelToBlock.getSuccessors();
        final int size2 = successors.size();
        final int primarySuccessor = labelToBlock.getPrimarySuccessor();
        int value2;
        RegisterSpecSet set2;
        for (i = 0; i < size2; ++i) {
            value2 = successors.get(i);
            if (value2 == primarySuccessor) {
                set2 = set;
            }
            else {
                set2 = mutableCopyOfStarts;
            }
            if (this.resultInfo.mergeStarts(value2, set2)) {
                Bits.set(this.workSet, value2);
            }
        }
    }
}
