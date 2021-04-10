package com.android.dx.rop.cst;

import com.android.dx.util.*;

public final class CstArray extends Constant
{
    private final List list;
    
    public CstArray(final List list) {
        if (list == null) {
            throw new NullPointerException("list == null");
        }
        list.throwIfMutable();
        this.list = list;
    }
    
    @Override
    protected int compareTo0(final Constant constant) {
        return this.list.compareTo(((CstArray)constant).list);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof CstArray && this.list.equals(((CstArray)o).list);
    }
    
    public List getList() {
        return this.list;
    }
    
    @Override
    public int hashCode() {
        return this.list.hashCode();
    }
    
    @Override
    public boolean isCategory2() {
        return false;
    }
    
    @Override
    public String toHuman() {
        return this.list.toHuman("{", ", ", "}");
    }
    
    @Override
    public String toString() {
        return this.list.toString("array{", ", ", "}");
    }
    
    @Override
    public String typeName() {
        return "array";
    }
    
    public static final class List extends FixedSizeList implements Comparable<List>
    {
        public List(final int n) {
            super(n);
        }
        
        @Override
        public int compareTo(final List list) {
            final int size = this.size();
            final int size2 = list.size();
            int n;
            if (size < size2) {
                n = size;
            }
            else {
                n = size2;
            }
            for (int i = 0; i < n; ++i) {
                final int compareTo = ((Constant)this.get0(i)).compareTo((Constant)list.get0(i));
                if (compareTo != 0) {
                    return compareTo;
                }
            }
            if (size < size2) {
                return -1;
            }
            if (size > size2) {
                return 1;
            }
            return 0;
        }
        
        public Constant get(final int n) {
            return (Constant)this.get0(n);
        }
        
        public void set(final int n, final Constant constant) {
            this.set0(n, constant);
        }
    }
}
