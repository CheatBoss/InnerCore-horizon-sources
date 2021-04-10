package com.android.dx.rop.code;

import com.android.dx.util.*;

public final class RopMethod
{
    private final BasicBlockList blocks;
    private IntList exitPredecessors;
    private final int firstLabel;
    private IntList[] predecessors;
    
    public RopMethod(final BasicBlockList blocks, final int firstLabel) {
        if (blocks == null) {
            throw new NullPointerException("blocks == null");
        }
        if (firstLabel < 0) {
            throw new IllegalArgumentException("firstLabel < 0");
        }
        this.blocks = blocks;
        this.firstLabel = firstLabel;
        this.predecessors = null;
        this.exitPredecessors = null;
    }
    
    private void calcPredecessors() {
        final int maxLabel = this.blocks.getMaxLabel();
        final IntList[] predecessors = new IntList[maxLabel];
        final IntList exitPredecessors = new IntList(10);
        final int size = this.blocks.size();
        final int n = 0;
        for (int i = 0; i < size; ++i) {
            final BasicBlock value = this.blocks.get(i);
            final int label = value.getLabel();
            final IntList successors = value.getSuccessors();
            final int size2 = successors.size();
            if (size2 == 0) {
                exitPredecessors.add(label);
            }
            else {
                for (int j = 0; j < size2; ++j) {
                    final int value2 = successors.get(j);
                    IntList list;
                    if ((list = predecessors[value2]) == null) {
                        list = new IntList(10);
                        predecessors[value2] = list;
                    }
                    list.add(label);
                }
            }
        }
        for (int k = n; k < maxLabel; ++k) {
            final IntList list2 = predecessors[k];
            if (list2 != null) {
                list2.sort();
                list2.setImmutable();
            }
        }
        exitPredecessors.sort();
        exitPredecessors.setImmutable();
        if (predecessors[this.firstLabel] == null) {
            predecessors[this.firstLabel] = IntList.EMPTY;
        }
        this.predecessors = predecessors;
        this.exitPredecessors = exitPredecessors;
    }
    
    public BasicBlockList getBlocks() {
        return this.blocks;
    }
    
    public IntList getExitPredecessors() {
        if (this.exitPredecessors == null) {
            this.calcPredecessors();
        }
        return this.exitPredecessors;
    }
    
    public int getFirstLabel() {
        return this.firstLabel;
    }
    
    public IntList labelToPredecessors(final int n) {
        if (this.exitPredecessors == null) {
            this.calcPredecessors();
        }
        final IntList list = this.predecessors[n];
        if (list == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("no such block: ");
            sb.append(Hex.u2(n));
            throw new RuntimeException(sb.toString());
        }
        return list;
    }
    
    public RopMethod withRegisterOffset(final int n) {
        final RopMethod ropMethod = new RopMethod(this.blocks.withRegisterOffset(n), this.firstLabel);
        if (this.exitPredecessors != null) {
            ropMethod.exitPredecessors = this.exitPredecessors;
            ropMethod.predecessors = this.predecessors;
        }
        return ropMethod;
    }
}
