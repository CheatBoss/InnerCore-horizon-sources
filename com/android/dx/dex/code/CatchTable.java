package com.android.dx.dex.code;

import com.android.dx.util.*;

public final class CatchTable extends FixedSizeList implements Comparable<CatchTable>
{
    public static final CatchTable EMPTY;
    
    static {
        EMPTY = new CatchTable(0);
    }
    
    public CatchTable(final int n) {
        super(n);
    }
    
    @Override
    public int compareTo(final CatchTable catchTable) {
        if (this == catchTable) {
            return 0;
        }
        final int size = this.size();
        final int size2 = catchTable.size();
        for (int min = Math.min(size, size2), i = 0; i < min; ++i) {
            final int compareTo = this.get(i).compareTo(catchTable.get(i));
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
    
    public static class Entry implements Comparable<Entry>
    {
        private final int end;
        private final CatchHandlerList handlers;
        private final int start;
        
        public Entry(final int start, final int end, final CatchHandlerList handlers) {
            if (start < 0) {
                throw new IllegalArgumentException("start < 0");
            }
            if (end <= start) {
                throw new IllegalArgumentException("end <= start");
            }
            if (handlers.isMutable()) {
                throw new IllegalArgumentException("handlers.isMutable()");
            }
            this.start = start;
            this.end = end;
            this.handlers = handlers;
        }
        
        @Override
        public int compareTo(final Entry entry) {
            if (this.start < entry.start) {
                return -1;
            }
            if (this.start > entry.start) {
                return 1;
            }
            if (this.end < entry.end) {
                return -1;
            }
            if (this.end > entry.end) {
                return 1;
            }
            return this.handlers.compareTo(entry.handlers);
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
        
        public int getEnd() {
            return this.end;
        }
        
        public CatchHandlerList getHandlers() {
            return this.handlers;
        }
        
        public int getStart() {
            return this.start;
        }
        
        @Override
        public int hashCode() {
            return (this.start * 31 + this.end) * 31 + this.handlers.hashCode();
        }
    }
}
