package com.android.dx.dex.code;

import com.android.dx.rop.code.*;

public final class BlockAddresses
{
    private final CodeAddress[] ends;
    private final CodeAddress[] lasts;
    private final CodeAddress[] starts;
    
    public BlockAddresses(final RopMethod ropMethod) {
        final int maxLabel = ropMethod.getBlocks().getMaxLabel();
        this.starts = new CodeAddress[maxLabel];
        this.lasts = new CodeAddress[maxLabel];
        this.ends = new CodeAddress[maxLabel];
        this.setupArrays(ropMethod);
    }
    
    private void setupArrays(final RopMethod ropMethod) {
        final BasicBlockList blocks = ropMethod.getBlocks();
        for (int size = blocks.size(), i = 0; i < size; ++i) {
            final BasicBlock value = blocks.get(i);
            final int label = value.getLabel();
            this.starts[label] = new CodeAddress(value.getInsns().get(0).getPosition());
            final SourcePosition position = value.getLastInsn().getPosition();
            this.lasts[label] = new CodeAddress(position);
            this.ends[label] = new CodeAddress(position);
        }
    }
    
    public CodeAddress getEnd(final int n) {
        return this.ends[n];
    }
    
    public CodeAddress getEnd(final BasicBlock basicBlock) {
        return this.ends[basicBlock.getLabel()];
    }
    
    public CodeAddress getLast(final int n) {
        return this.lasts[n];
    }
    
    public CodeAddress getLast(final BasicBlock basicBlock) {
        return this.lasts[basicBlock.getLabel()];
    }
    
    public CodeAddress getStart(final int n) {
        return this.starts[n];
    }
    
    public CodeAddress getStart(final BasicBlock basicBlock) {
        return this.starts[basicBlock.getLabel()];
    }
}
