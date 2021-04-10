package com.android.dx.util;

import java.util.*;

public class BitIntSet implements IntSet
{
    int[] bits;
    
    public BitIntSet(final int n) {
        this.bits = Bits.makeBitSet(n);
    }
    
    private void ensureCapacity(final int n) {
        if (n >= Bits.getMax(this.bits)) {
            final int[] bitSet = Bits.makeBitSet(Math.max(n + 1, Bits.getMax(this.bits) * 2));
            System.arraycopy(this.bits, 0, bitSet, 0, this.bits.length);
            this.bits = bitSet;
        }
    }
    
    @Override
    public void add(final int n) {
        this.ensureCapacity(n);
        Bits.set(this.bits, n, true);
    }
    
    @Override
    public int elements() {
        return Bits.bitCount(this.bits);
    }
    
    @Override
    public boolean has(final int n) {
        return n < Bits.getMax(this.bits) && Bits.get(this.bits, n);
    }
    
    @Override
    public IntIterator iterator() {
        return new IntIterator() {
            private int idx = Bits.findFirst(BitIntSet.this.bits, 0);
            
            @Override
            public boolean hasNext() {
                return this.idx >= 0;
            }
            
            @Override
            public int next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final int idx = this.idx;
                this.idx = Bits.findFirst(BitIntSet.this.bits, this.idx + 1);
                return idx;
            }
        };
    }
    
    @Override
    public void merge(final IntSet set) {
        if (set instanceof BitIntSet) {
            final BitIntSet set2 = (BitIntSet)set;
            this.ensureCapacity(Bits.getMax(set2.bits) + 1);
            Bits.or(this.bits, set2.bits);
            return;
        }
        if (set instanceof ListIntSet) {
            final ListIntSet set3 = (ListIntSet)set;
            final int size = set3.ints.size();
            if (size > 0) {
                this.ensureCapacity(set3.ints.get(size - 1));
            }
            for (int i = 0; i < set3.ints.size(); ++i) {
                Bits.set(this.bits, set3.ints.get(i), true);
            }
            return;
        }
        final IntIterator iterator = set.iterator();
        while (iterator.hasNext()) {
            this.add(iterator.next());
        }
    }
    
    @Override
    public void remove(final int n) {
        if (n < Bits.getMax(this.bits)) {
            Bits.set(this.bits, n, false);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append('{');
        int n = 1;
        for (int i = Bits.findFirst(this.bits, 0); i >= 0; i = Bits.findFirst(this.bits, i + 1)) {
            if (n == 0) {
                sb.append(", ");
            }
            n = 0;
            sb.append(i);
        }
        sb.append('}');
        return sb.toString();
    }
}
