package com.android.dx.util;

import java.util.*;

public final class IntList extends MutabilityControl
{
    public static final IntList EMPTY;
    private int size;
    private boolean sorted;
    private int[] values;
    
    static {
        (EMPTY = new IntList(0)).setImmutable();
    }
    
    public IntList() {
        this(4);
    }
    
    public IntList(final int n) {
        super(true);
        try {
            this.values = new int[n];
            this.size = 0;
            this.sorted = true;
        }
        catch (NegativeArraySizeException ex) {
            throw new IllegalArgumentException("size < 0");
        }
    }
    
    private void growIfNeeded() {
        if (this.size == this.values.length) {
            final int[] values = new int[this.size * 3 / 2 + 10];
            System.arraycopy(this.values, 0, values, 0, this.size);
            this.values = values;
        }
    }
    
    public static IntList makeImmutable(final int n) {
        final IntList list = new IntList(1);
        list.add(n);
        list.setImmutable();
        return list;
    }
    
    public static IntList makeImmutable(final int n, final int n2) {
        final IntList list = new IntList(2);
        list.add(n);
        list.add(n2);
        list.setImmutable();
        return list;
    }
    
    public void add(final int n) {
        this.throwIfImmutable();
        this.growIfNeeded();
        this.values[this.size++] = n;
        if (this.sorted) {
            final int size = this.size;
            boolean sorted = true;
            if (size > 1) {
                if (n < this.values[this.size - 2]) {
                    sorted = false;
                }
                this.sorted = sorted;
            }
        }
    }
    
    public int binarysearch(final int n) {
        final int size = this.size;
        if (!this.sorted) {
            for (int i = 0; i < size; ++i) {
                if (this.values[i] == n) {
                    return i;
                }
            }
            return -size;
        }
        int n2 = -1;
        int j = size;
        while (j > n2 + 1) {
            final int n3 = (j - n2 >> 1) + n2;
            if (n <= this.values[n3]) {
                j = n3;
            }
            else {
                n2 = n3;
            }
        }
        if (j == size) {
            return -size - 1;
        }
        if (n == this.values[j]) {
            return j;
        }
        return -j - 1;
    }
    
    public boolean contains(final int n) {
        return this.indexOf(n) >= 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof IntList)) {
            return false;
        }
        final IntList list = (IntList)o;
        if (this.sorted != list.sorted) {
            return false;
        }
        if (this.size != list.size) {
            return false;
        }
        for (int i = 0; i < this.size; ++i) {
            if (this.values[i] != list.values[i]) {
                return false;
            }
        }
        return true;
    }
    
    public int get(int n) {
        if (n >= this.size) {
            throw new IndexOutOfBoundsException("n >= size()");
        }
        try {
            n = this.values[n];
            return n;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            throw new IndexOutOfBoundsException("n < 0");
        }
    }
    
    @Override
    public int hashCode() {
        int n = 0;
        for (int i = 0; i < this.size; ++i) {
            n = n * 31 + this.values[i];
        }
        return n;
    }
    
    public int indexOf(int binarysearch) {
        binarysearch = this.binarysearch(binarysearch);
        if (binarysearch >= 0) {
            return binarysearch;
        }
        return -1;
    }
    
    public void insert(final int n, final int n2) {
        if (n > this.size) {
            throw new IndexOutOfBoundsException("n > size()");
        }
        this.growIfNeeded();
        System.arraycopy(this.values, n, this.values, n + 1, this.size - n);
        this.values[n] = n2;
        final int size = this.size;
        boolean sorted = true;
        this.size = size + 1;
        if (!this.sorted || (n != 0 && n2 <= this.values[n - 1]) || (n != this.size - 1 && n2 >= this.values[n + 1])) {
            sorted = false;
        }
        this.sorted = sorted;
    }
    
    public IntList mutableCopy() {
        final int size = this.size;
        final IntList list = new IntList(size);
        for (int i = 0; i < size; ++i) {
            list.add(this.values[i]);
        }
        return list;
    }
    
    public int pop() {
        this.throwIfImmutable();
        final int value = this.get(this.size - 1);
        --this.size;
        return value;
    }
    
    public void pop(final int n) {
        this.throwIfImmutable();
        this.size -= n;
    }
    
    public void removeIndex(final int n) {
        if (n >= this.size) {
            throw new IndexOutOfBoundsException("n >= size()");
        }
        System.arraycopy(this.values, n + 1, this.values, n, this.size - n - 1);
        --this.size;
    }
    
    public void set(final int n, final int n2) {
        this.throwIfImmutable();
        if (n >= this.size) {
            throw new IndexOutOfBoundsException("n >= size()");
        }
        try {
            this.values[n] = n2;
            this.sorted = false;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            if (n < 0) {
                throw new IllegalArgumentException("n < 0");
            }
        }
    }
    
    public void shrink(final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("newSize < 0");
        }
        if (size > this.size) {
            throw new IllegalArgumentException("newSize > size");
        }
        this.throwIfImmutable();
        this.size = size;
    }
    
    public int size() {
        return this.size;
    }
    
    public void sort() {
        this.throwIfImmutable();
        if (!this.sorted) {
            Arrays.sort(this.values, 0, this.size);
            this.sorted = true;
        }
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(this.size * 5 + 10);
        sb.append('{');
        for (int i = 0; i < this.size; ++i) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append(this.values[i]);
        }
        sb.append('}');
        return sb.toString();
    }
    
    public int top() {
        return this.get(this.size - 1);
    }
}
