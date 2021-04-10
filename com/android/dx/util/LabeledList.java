package com.android.dx.util;

import java.util.*;

public class LabeledList extends FixedSizeList
{
    private final IntList labelToIndex;
    
    public LabeledList(final int n) {
        super(n);
        this.labelToIndex = new IntList(n);
    }
    
    public LabeledList(final LabeledList list) {
        super(list.size());
        this.labelToIndex = list.labelToIndex.mutableCopy();
        for (int size = list.size(), i = 0; i < size; ++i) {
            final Object get0 = list.get0(i);
            if (get0 != null) {
                this.set0(i, get0);
            }
        }
    }
    
    private void addLabelIndex(final int n, final int n2) {
        for (int size = this.labelToIndex.size(), i = 0; i <= n - size; ++i) {
            this.labelToIndex.add(-1);
        }
        this.labelToIndex.set(n, n2);
    }
    
    private void rebuildLabelToIndex() {
        for (int size = this.size(), i = 0; i < size; ++i) {
            final LabeledItem labeledItem = (LabeledItem)this.get0(i);
            if (labeledItem != null) {
                this.labelToIndex.set(labeledItem.getLabel(), i);
            }
        }
    }
    
    private void removeLabel(final int n) {
        this.labelToIndex.set(n, -1);
    }
    
    public final int[] getLabelsInOrder() {
        final int size = this.size();
        final int[] array = new int[size];
        for (int i = 0; i < size; ++i) {
            final LabeledItem labeledItem = (LabeledItem)this.get0(i);
            if (labeledItem == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("null at index ");
                sb.append(i);
                throw new NullPointerException(sb.toString());
            }
            array[i] = labeledItem.getLabel();
        }
        Arrays.sort(array);
        return array;
    }
    
    public final int getMaxLabel() {
        int n;
        for (n = this.labelToIndex.size() - 1; n >= 0 && this.labelToIndex.get(n) < 0; --n) {}
        final int n2 = n + 1;
        this.labelToIndex.shrink(n2);
        return n2;
    }
    
    public final int indexOfLabel(final int n) {
        if (n >= this.labelToIndex.size()) {
            return -1;
        }
        return this.labelToIndex.get(n);
    }
    
    protected void set(final int n, final LabeledItem labeledItem) {
        final LabeledItem labeledItem2 = (LabeledItem)this.getOrNull0(n);
        this.set0(n, labeledItem);
        if (labeledItem2 != null) {
            this.removeLabel(labeledItem2.getLabel());
        }
        if (labeledItem != null) {
            this.addLabelIndex(labeledItem.getLabel(), n);
        }
    }
    
    @Override
    public void shrinkToFit() {
        super.shrinkToFit();
        this.rebuildLabelToIndex();
    }
}
