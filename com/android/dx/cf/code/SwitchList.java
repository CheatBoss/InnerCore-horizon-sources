package com.android.dx.cf.code;

import com.android.dx.util.*;

public final class SwitchList extends MutabilityControl
{
    private int size;
    private final IntList targets;
    private final IntList values;
    
    public SwitchList(final int size) {
        super(true);
        this.values = new IntList(size);
        this.targets = new IntList(size + 1);
        this.size = size;
    }
    
    public void add(final int n, final int n2) {
        this.throwIfImmutable();
        if (n2 < 0) {
            throw new IllegalArgumentException("target < 0");
        }
        this.values.add(n);
        this.targets.add(n2);
    }
    
    public int getDefaultTarget() {
        return this.targets.get(this.size);
    }
    
    public int getTarget(final int n) {
        return this.targets.get(n);
    }
    
    public IntList getTargets() {
        return this.targets;
    }
    
    public int getValue(final int n) {
        return this.values.get(n);
    }
    
    public IntList getValues() {
        return this.values;
    }
    
    public void removeSuperfluousDefaults() {
        this.throwIfImmutable();
        final int size = this.size;
        if (size != this.targets.size() - 1) {
            throw new IllegalArgumentException("incomplete instance");
        }
        final int value = this.targets.get(size);
        int size2 = 0;
        int n;
        for (int i = 0; i < size; ++i, size2 = n) {
            final int value2 = this.targets.get(i);
            n = size2;
            if (value2 != value) {
                if (i != size2) {
                    this.targets.set(size2, value2);
                    this.values.set(size2, this.values.get(i));
                }
                n = size2 + 1;
            }
        }
        if (size2 != size) {
            this.values.shrink(size2);
            this.targets.set(size2, value);
            this.targets.shrink(size2 + 1);
            this.size = size2;
        }
    }
    
    public void setDefaultTarget(final int n) {
        this.throwIfImmutable();
        if (n < 0) {
            throw new IllegalArgumentException("target < 0");
        }
        if (this.targets.size() != this.size) {
            throw new RuntimeException("non-default elements not all set");
        }
        this.targets.add(n);
    }
    
    @Override
    public void setImmutable() {
        this.values.setImmutable();
        this.targets.setImmutable();
        super.setImmutable();
    }
    
    public int size() {
        return this.size;
    }
}
