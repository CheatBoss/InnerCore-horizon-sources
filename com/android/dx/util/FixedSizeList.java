package com.android.dx.util;

import java.util.*;

public class FixedSizeList extends MutabilityControl implements ToHuman
{
    private Object[] arr;
    
    public FixedSizeList(final int n) {
        super(n != 0);
        try {
            this.arr = new Object[n];
        }
        catch (NegativeArraySizeException ex) {
            throw new IllegalArgumentException("size < 0");
        }
    }
    
    private Object throwIndex(final int n) {
        if (n < 0) {
            throw new IndexOutOfBoundsException("n < 0");
        }
        throw new IndexOutOfBoundsException("n >= size()");
    }
    
    private String toString0(final String s, final String s2, final String s3, final boolean b) {
        final int length = this.arr.length;
        final StringBuffer sb = new StringBuffer(length * 10 + 10);
        if (s != null) {
            sb.append(s);
        }
        for (int i = 0; i < length; ++i) {
            if (i != 0 && s2 != null) {
                sb.append(s2);
            }
            if (b) {
                sb.append(((ToHuman)this.arr[i]).toHuman());
            }
            else {
                sb.append(this.arr[i]);
            }
        }
        if (s3 != null) {
            sb.append(s3);
        }
        return sb.toString();
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass() && Arrays.equals(this.arr, ((FixedSizeList)o).arr));
    }
    
    protected final Object get0(final int n) {
        try {
            final Object o = this.arr[n];
            if (o == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unset: ");
                sb.append(n);
                throw new NullPointerException(sb.toString());
            }
            return o;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            return this.throwIndex(n);
        }
    }
    
    protected final Object getOrNull0(final int n) {
        return this.arr[n];
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.arr);
    }
    
    protected final void set0(final int n, final Object o) {
        this.throwIfImmutable();
        try {
            this.arr[n] = o;
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            this.throwIndex(n);
        }
    }
    
    public void shrinkToFit() {
        final int length = this.arr.length;
        final int n = 0;
        int n2 = 0;
        int n3;
        for (int i = 0; i < length; ++i, n2 = n3) {
            n3 = n2;
            if (this.arr[i] != null) {
                n3 = n2 + 1;
            }
        }
        if (length == n2) {
            return;
        }
        this.throwIfImmutable();
        final Object[] arr = new Object[n2];
        int n4 = 0;
        int n5;
        for (int j = n; j < length; ++j, n4 = n5) {
            final Object o = this.arr[j];
            n5 = n4;
            if (o != null) {
                arr[n4] = o;
                n5 = n4 + 1;
            }
        }
        this.arr = arr;
        if (n2 == 0) {
            this.setImmutable();
        }
    }
    
    public final int size() {
        return this.arr.length;
    }
    
    @Override
    public String toHuman() {
        final String name = this.getClass().getName();
        final StringBuilder sb = new StringBuilder();
        sb.append(name.substring(name.lastIndexOf(46) + 1));
        sb.append('{');
        return this.toString0(sb.toString(), ", ", "}", true);
    }
    
    public String toHuman(final String s, final String s2, final String s3) {
        return this.toString0(s, s2, s3, true);
    }
    
    @Override
    public String toString() {
        final String name = this.getClass().getName();
        final StringBuilder sb = new StringBuilder();
        sb.append(name.substring(name.lastIndexOf(46) + 1));
        sb.append('{');
        return this.toString0(sb.toString(), ", ", "}", false);
    }
    
    public String toString(final String s, final String s2, final String s3) {
        return this.toString0(s, s2, s3, false);
    }
}
