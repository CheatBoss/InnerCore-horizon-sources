package com.android.dx.dex.code;

import com.android.dx.util.*;
import com.android.dx.rop.cst.*;

public final class CatchHandlerList extends FixedSizeList implements Comparable<CatchHandlerList>
{
    public static final CatchHandlerList EMPTY;
    
    static {
        EMPTY = new CatchHandlerList(0);
    }
    
    public CatchHandlerList(final int n) {
        super(n);
    }
    
    public boolean catchesAll() {
        final int size = this.size();
        return size != 0 && this.get(size - 1).getExceptionType().equals(CstType.OBJECT);
    }
    
    @Override
    public int compareTo(final CatchHandlerList list) {
        if (this == list) {
            return 0;
        }
        final int size = this.size();
        final int size2 = list.size();
        for (int min = Math.min(size, size2), i = 0; i < min; ++i) {
            final int compareTo = this.get(i).compareTo(list.get(i));
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
    
    public Entry get(final int n) {
        return (Entry)this.get0(n);
    }
    
    public void set(final int n, final Entry entry) {
        this.set0(n, entry);
    }
    
    public void set(final int n, final CstType cstType, final int n2) {
        this.set0(n, new Entry(cstType, n2));
    }
    
    @Override
    public String toHuman() {
        return this.toHuman("", "");
    }
    
    public String toHuman(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder(100);
        final int size = this.size();
        sb.append(s);
        sb.append(s2);
        sb.append("catch ");
        for (int i = 0; i < size; ++i) {
            final Entry value = this.get(i);
            if (i != 0) {
                sb.append(",\n");
                sb.append(s);
                sb.append("  ");
            }
            if (i == size - 1 && this.catchesAll()) {
                sb.append("<any>");
            }
            else {
                sb.append(value.getExceptionType().toHuman());
            }
            sb.append(" -> ");
            sb.append(Hex.u2or4(value.getHandler()));
        }
        return sb.toString();
    }
    
    public static class Entry implements Comparable<Entry>
    {
        private final CstType exceptionType;
        private final int handler;
        
        public Entry(final CstType exceptionType, final int handler) {
            if (handler < 0) {
                throw new IllegalArgumentException("handler < 0");
            }
            if (exceptionType == null) {
                throw new NullPointerException("exceptionType == null");
            }
            this.handler = handler;
            this.exceptionType = exceptionType;
        }
        
        @Override
        public int compareTo(final Entry entry) {
            if (this.handler < entry.handler) {
                return -1;
            }
            if (this.handler > entry.handler) {
                return 1;
            }
            return this.exceptionType.compareTo((Constant)entry.exceptionType);
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof Entry;
            boolean b2 = false;
            if (b) {
                if (this.compareTo((Entry)o) == 0) {
                    b2 = true;
                }
                return b2;
            }
            return false;
        }
        
        public CstType getExceptionType() {
            return this.exceptionType;
        }
        
        public int getHandler() {
            return this.handler;
        }
        
        @Override
        public int hashCode() {
            return this.handler * 31 + this.exceptionType.hashCode();
        }
    }
}
