package com.android.dx.cf.code;

import com.android.dx.util.*;

public final class ByteBlock implements LabeledItem
{
    private final ByteCatchList catches;
    private final int end;
    private final int label;
    private final int start;
    private final IntList successors;
    
    public ByteBlock(final int label, final int start, final int end, final IntList successors, final ByteCatchList catches) {
        if (label < 0) {
            throw new IllegalArgumentException("label < 0");
        }
        if (start < 0) {
            throw new IllegalArgumentException("start < 0");
        }
        if (end <= start) {
            throw new IllegalArgumentException("end <= start");
        }
        if (successors == null) {
            throw new NullPointerException("targets == null");
        }
        for (int size = successors.size(), i = 0; i < size; ++i) {
            if (successors.get(i) < 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("successors[");
                sb.append(i);
                sb.append("] == ");
                sb.append(successors.get(i));
                throw new IllegalArgumentException(sb.toString());
            }
        }
        if (catches == null) {
            throw new NullPointerException("catches == null");
        }
        this.label = label;
        this.start = start;
        this.end = end;
        this.successors = successors;
        this.catches = catches;
    }
    
    public ByteCatchList getCatches() {
        return this.catches;
    }
    
    public int getEnd() {
        return this.end;
    }
    
    @Override
    public int getLabel() {
        return this.label;
    }
    
    public int getStart() {
        return this.start;
    }
    
    public IntList getSuccessors() {
        return this.successors;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append(Hex.u2(this.label));
        sb.append(": ");
        sb.append(Hex.u2(this.start));
        sb.append("..");
        sb.append(Hex.u2(this.end));
        sb.append('}');
        return sb.toString();
    }
}
