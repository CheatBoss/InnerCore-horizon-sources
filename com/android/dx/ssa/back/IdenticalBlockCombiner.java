package com.android.dx.ssa.back;

import com.android.dx.util.*;
import com.android.dx.rop.code.*;
import java.util.*;

public class IdenticalBlockCombiner
{
    private final BasicBlockList blocks;
    private final BasicBlockList newBlocks;
    private final RopMethod ropMethod;
    
    public IdenticalBlockCombiner(final RopMethod ropMethod) {
        this.ropMethod = ropMethod;
        this.blocks = this.ropMethod.getBlocks();
        this.newBlocks = this.blocks.getMutableCopy();
    }
    
    private void combineBlocks(final int n, final IntList list) {
        for (int size = list.size(), i = 0; i < size; ++i) {
            final int value = list.get(i);
            final IntList labelToPredecessors = this.ropMethod.labelToPredecessors(this.blocks.labelToBlock(value).getLabel());
            for (int size2 = labelToPredecessors.size(), j = 0; j < size2; ++j) {
                this.replaceSucc(this.newBlocks.labelToBlock(labelToPredecessors.get(j)), value, n);
            }
        }
    }
    
    private static boolean compareInsns(final BasicBlock basicBlock, final BasicBlock basicBlock2) {
        return basicBlock.getInsns().contentEquals(basicBlock2.getInsns());
    }
    
    private void replaceSucc(final BasicBlock basicBlock, final int n, final int n2) {
        final IntList mutableCopy = basicBlock.getSuccessors().mutableCopy();
        mutableCopy.set(mutableCopy.indexOf(n), n2);
        int primarySuccessor;
        if ((primarySuccessor = basicBlock.getPrimarySuccessor()) == n) {
            primarySuccessor = n2;
        }
        mutableCopy.setImmutable();
        this.newBlocks.set(this.newBlocks.indexOfLabel(basicBlock.getLabel()), new BasicBlock(basicBlock.getLabel(), basicBlock.getInsns(), mutableCopy, primarySuccessor));
    }
    
    public RopMethod process() {
        final int size = this.blocks.size();
        final BitSet set = new BitSet(this.blocks.getMaxLabel());
        for (int i = 0; i < size; ++i) {
            final BasicBlock value = this.blocks.get(i);
            if (!set.get(value.getLabel())) {
                final IntList labelToPredecessors = this.ropMethod.labelToPredecessors(value.getLabel());
                for (int size2 = labelToPredecessors.size(), j = 0; j < size2; ++j) {
                    final int value2 = labelToPredecessors.get(j);
                    final BasicBlock labelToBlock = this.blocks.labelToBlock(value2);
                    if (!set.get(value2) && labelToBlock.getSuccessors().size() <= 1) {
                        if (labelToBlock.getFirstInsn().getOpcode().getOpcode() != 55) {
                            final IntList list = new IntList();
                            for (int k = j + 1; k < size2; ++k) {
                                final int value3 = labelToPredecessors.get(k);
                                final BasicBlock labelToBlock2 = this.blocks.labelToBlock(value3);
                                if (labelToBlock2.getSuccessors().size() == 1 && compareInsns(labelToBlock, labelToBlock2)) {
                                    list.add(value3);
                                    set.set(value3);
                                }
                            }
                            this.combineBlocks(value2, list);
                        }
                    }
                }
            }
        }
        for (int l = size - 1; l >= 0; --l) {
            if (set.get(this.newBlocks.get(l).getLabel())) {
                this.newBlocks.set(l, null);
            }
        }
        this.newBlocks.shrinkToFit();
        this.newBlocks.setImmutable();
        return new RopMethod(this.newBlocks, this.ropMethod.getFirstLabel());
    }
}
