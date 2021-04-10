package com.android.dx.util;

import java.util.*;

public class ListIntSet implements IntSet
{
    final IntList ints;
    
    public ListIntSet() {
        (this.ints = new IntList()).sort();
    }
    
    @Override
    public void add(final int n) {
        final int binarysearch = this.ints.binarysearch(n);
        if (binarysearch < 0) {
            this.ints.insert(-(binarysearch + 1), n);
        }
    }
    
    @Override
    public int elements() {
        return this.ints.size();
    }
    
    @Override
    public boolean has(final int n) {
        return this.ints.indexOf(n) >= 0;
    }
    
    @Override
    public IntIterator iterator() {
        return new IntIterator() {
            private int idx = 0;
            
            @Override
            public boolean hasNext() {
                return this.idx < ListIntSet.this.ints.size();
            }
            
            @Override
            public int next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return ListIntSet.this.ints.get(this.idx++);
            }
        };
    }
    
    @Override
    public void merge(final IntSet set) {
        final boolean b = set instanceof ListIntSet;
        final int n = 0;
        int n2 = 0;
        if (b) {
            final ListIntSet set2 = (ListIntSet)set;
            final int size = this.ints.size();
            final int size2 = set2.ints.size();
            int n3 = 0;
            int i;
            while ((i = n2) < size2) {
                i = n2;
                if (n3 >= size) {
                    break;
                }
                for (i = n2; i < size2 && set2.ints.get(i) < this.ints.get(n3); ++i) {
                    this.add(set2.ints.get(i));
                }
                int n4 = n3;
                if (i == size2) {
                    break;
                }
                while (true) {
                    n2 = i;
                    if ((n3 = n4) >= size) {
                        break;
                    }
                    n2 = i;
                    n3 = n4;
                    if (set2.ints.get(i) < this.ints.get(n4)) {
                        break;
                    }
                    ++n4;
                }
            }
            while (i < size2) {
                this.add(set2.ints.get(i));
                ++i;
            }
            this.ints.sort();
            return;
        }
        if (set instanceof BitIntSet) {
            final BitIntSet set3 = (BitIntSet)set;
            for (int j = n; j >= 0; j = Bits.findFirst(set3.bits, j + 1)) {
                this.ints.add(j);
            }
            this.ints.sort();
            return;
        }
        final IntIterator iterator = set.iterator();
        while (iterator.hasNext()) {
            this.add(iterator.next());
        }
    }
    
    @Override
    public void remove(int index) {
        index = this.ints.indexOf(index);
        if (index >= 0) {
            this.ints.removeIndex(index);
        }
    }
    
    @Override
    public String toString() {
        return this.ints.toString();
    }
}
