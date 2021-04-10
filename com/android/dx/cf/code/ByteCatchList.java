package com.android.dx.cf.code;

import com.android.dx.rop.cst.*;
import com.android.dx.rop.type.*;
import com.android.dx.util.*;

public final class ByteCatchList extends FixedSizeList
{
    public static final ByteCatchList EMPTY;
    
    static {
        EMPTY = new ByteCatchList(0);
    }
    
    public ByteCatchList(final int n) {
        super(n);
    }
    
    private static boolean typeNotFound(final Item item, final Item[] array, final int n) {
        final CstType exceptionClass = item.getExceptionClass();
        for (int i = 0; i < n; ++i) {
            final CstType exceptionClass2 = array[i].getExceptionClass();
            if (exceptionClass2 == exceptionClass) {
                return false;
            }
            if (exceptionClass2 == CstType.OBJECT) {
                return false;
            }
        }
        return true;
    }
    
    public int byteLength() {
        return this.size() * 8 + 2;
    }
    
    public Item get(final int n) {
        return (Item)this.get0(n);
    }
    
    public ByteCatchList listFor(int i) {
        final int size = this.size();
        final Item[] array = new Item[size];
        final int n = 0;
        int n2 = 0;
        int n3;
        for (int j = 0; j < size; ++j, n2 = n3) {
            final Item value = this.get(j);
            n3 = n2;
            if (value.covers(i)) {
                n3 = n2;
                if (typeNotFound(value, array, n2)) {
                    array[n2] = value;
                    n3 = n2 + 1;
                }
            }
        }
        if (n2 == 0) {
            return ByteCatchList.EMPTY;
        }
        final ByteCatchList list = new ByteCatchList(n2);
        for (i = n; i < n2; ++i) {
            list.set(i, array[i]);
        }
        list.setImmutable();
        return list;
    }
    
    public void set(final int n, final int n2, final int n3, final int n4, final CstType cstType) {
        this.set0(n, new Item(n2, n3, n4, cstType));
    }
    
    public void set(final int n, final Item item) {
        if (item == null) {
            throw new NullPointerException("item == null");
        }
        this.set0(n, item);
    }
    
    public TypeList toRopCatchList() {
        final int size = this.size();
        if (size == 0) {
            return StdTypeList.EMPTY;
        }
        final StdTypeList list = new StdTypeList(size);
        for (int i = 0; i < size; ++i) {
            list.set(i, this.get(i).getExceptionClass().getClassType());
        }
        list.setImmutable();
        return list;
    }
    
    public IntList toTargetList(final int n) {
        if (n < -1) {
            throw new IllegalArgumentException("noException < -1");
        }
        final int n2 = 0;
        int n3 = 1;
        final boolean b = n >= 0;
        final int size = this.size();
        if (size != 0) {
            if (!b) {
                n3 = 0;
            }
            final IntList list = new IntList(n3 + size);
            for (int i = n2; i < size; ++i) {
                list.add(this.get(i).getHandlerPc());
            }
            if (b) {
                list.add(n);
            }
            list.setImmutable();
            return list;
        }
        if (b) {
            return IntList.makeImmutable(n);
        }
        return IntList.EMPTY;
    }
    
    public static class Item
    {
        private final int endPc;
        private final CstType exceptionClass;
        private final int handlerPc;
        private final int startPc;
        
        public Item(final int startPc, final int endPc, final int handlerPc, final CstType exceptionClass) {
            if (startPc < 0) {
                throw new IllegalArgumentException("startPc < 0");
            }
            if (endPc < startPc) {
                throw new IllegalArgumentException("endPc < startPc");
            }
            if (handlerPc < 0) {
                throw new IllegalArgumentException("handlerPc < 0");
            }
            this.startPc = startPc;
            this.endPc = endPc;
            this.handlerPc = handlerPc;
            this.exceptionClass = exceptionClass;
        }
        
        public boolean covers(final int n) {
            return n >= this.startPc && n < this.endPc;
        }
        
        public int getEndPc() {
            return this.endPc;
        }
        
        public CstType getExceptionClass() {
            if (this.exceptionClass != null) {
                return this.exceptionClass;
            }
            return CstType.OBJECT;
        }
        
        public int getHandlerPc() {
            return this.handlerPc;
        }
        
        public int getStartPc() {
            return this.startPc;
        }
    }
}
