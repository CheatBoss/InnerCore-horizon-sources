package com.android.dx.cf.code;

import com.android.dx.util.*;

public final class LineNumberList extends FixedSizeList
{
    public static final LineNumberList EMPTY;
    
    static {
        EMPTY = new LineNumberList(0);
    }
    
    public LineNumberList(final int n) {
        super(n);
    }
    
    public static LineNumberList concat(final LineNumberList list, final LineNumberList list2) {
        if (list == LineNumberList.EMPTY) {
            return list2;
        }
        final int size = list.size();
        final int size2 = list2.size();
        final LineNumberList list3 = new LineNumberList(size + size2);
        final int n = 0;
        for (int i = 0; i < size; ++i) {
            list3.set(i, list.get(i));
        }
        for (int j = n; j < size2; ++j) {
            list3.set(size + j, list2.get(j));
        }
        return list3;
    }
    
    public Item get(final int n) {
        return (Item)this.get0(n);
    }
    
    public int pcToLine(final int n) {
        final int size = this.size();
        int n2 = -1;
        int n3 = -1;
        int n4;
        int n5;
        for (int i = 0; i < size; ++i, n2 = n4, n3 = n5) {
            final Item value = this.get(i);
            final int startPc = value.getStartPc();
            n4 = n2;
            n5 = n3;
            if (startPc <= n) {
                n4 = n2;
                n5 = n3;
                if (startPc > n2) {
                    final int n6 = startPc;
                    final int lineNumber = value.getLineNumber();
                    n4 = n6;
                    n5 = lineNumber;
                    if (n6 == n) {
                        return lineNumber;
                    }
                }
            }
        }
        return n3;
    }
    
    public void set(final int n, final int n2, final int n3) {
        this.set0(n, new Item(n2, n3));
    }
    
    public void set(final int n, final Item item) {
        if (item == null) {
            throw new NullPointerException("item == null");
        }
        this.set0(n, item);
    }
    
    public static class Item
    {
        private final int lineNumber;
        private final int startPc;
        
        public Item(final int startPc, final int lineNumber) {
            if (startPc < 0) {
                throw new IllegalArgumentException("startPc < 0");
            }
            if (lineNumber < 0) {
                throw new IllegalArgumentException("lineNumber < 0");
            }
            this.startPc = startPc;
            this.lineNumber = lineNumber;
        }
        
        public int getLineNumber() {
            return this.lineNumber;
        }
        
        public int getStartPc() {
            return this.startPc;
        }
    }
}
