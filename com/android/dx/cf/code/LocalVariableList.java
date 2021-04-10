package com.android.dx.cf.code;

import com.android.dx.util.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;
import com.android.dx.rop.type.*;

public final class LocalVariableList extends FixedSizeList
{
    public static final LocalVariableList EMPTY;
    
    static {
        EMPTY = new LocalVariableList(0);
    }
    
    public LocalVariableList(final int n) {
        super(n);
    }
    
    public static LocalVariableList concat(final LocalVariableList list, final LocalVariableList list2) {
        if (list == LocalVariableList.EMPTY) {
            return list2;
        }
        final int size = list.size();
        final int size2 = list2.size();
        final LocalVariableList list3 = new LocalVariableList(size + size2);
        final int n = 0;
        for (int i = 0; i < size; ++i) {
            list3.set(i, list.get(i));
        }
        for (int j = n; j < size2; ++j) {
            list3.set(size + j, list2.get(j));
        }
        list3.setImmutable();
        return list3;
    }
    
    public static LocalVariableList mergeDescriptorsAndSignatures(final LocalVariableList list, final LocalVariableList list2) {
        final int size = list.size();
        final LocalVariableList list3 = new LocalVariableList(size);
        for (int i = 0; i < size; ++i) {
            final Item value = list.get(i);
            final Item itemToLocal = list2.itemToLocal(value);
            Item withSignature = value;
            if (itemToLocal != null) {
                withSignature = value.withSignature(itemToLocal.getSignature());
            }
            list3.set(i, withSignature);
        }
        list3.setImmutable();
        return list3;
    }
    
    public Item get(final int n) {
        return (Item)this.get0(n);
    }
    
    public Item itemToLocal(final Item item) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            final Item item2 = (Item)this.get0(i);
            if (item2 != null && item2.matchesAllButType(item)) {
                return item2;
            }
        }
        return null;
    }
    
    public Item pcAndIndexToLocal(final int n, final int n2) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            final Item item = (Item)this.get0(i);
            if (item != null && item.matchesPcAndIndex(n, n2)) {
                return item;
            }
        }
        return null;
    }
    
    public void set(final int n, final int n2, final int n3, final CstString cstString, final CstString cstString2, final CstString cstString3, final int n4) {
        this.set0(n, new Item(n2, n3, cstString, cstString2, cstString3, n4));
    }
    
    public void set(final int n, final Item item) {
        if (item == null) {
            throw new NullPointerException("item == null");
        }
        this.set0(n, item);
    }
    
    public static class Item
    {
        private final CstString descriptor;
        private final int index;
        private final int length;
        private final CstString name;
        private final CstString signature;
        private final int startPc;
        
        public Item(final int startPc, final int length, final CstString name, final CstString descriptor, final CstString signature, final int index) {
            if (startPc < 0) {
                throw new IllegalArgumentException("startPc < 0");
            }
            if (length < 0) {
                throw new IllegalArgumentException("length < 0");
            }
            if (name == null) {
                throw new NullPointerException("name == null");
            }
            if (descriptor == null && signature == null) {
                throw new NullPointerException("(descriptor == null) && (signature == null)");
            }
            if (index < 0) {
                throw new IllegalArgumentException("index < 0");
            }
            this.startPc = startPc;
            this.length = length;
            this.name = name;
            this.descriptor = descriptor;
            this.signature = signature;
            this.index = index;
        }
        
        private CstString getSignature() {
            return this.signature;
        }
        
        public CstString getDescriptor() {
            return this.descriptor;
        }
        
        public int getIndex() {
            return this.index;
        }
        
        public int getLength() {
            return this.length;
        }
        
        public LocalItem getLocalItem() {
            return LocalItem.make(this.name, this.signature);
        }
        
        public int getStartPc() {
            return this.startPc;
        }
        
        public Type getType() {
            return Type.intern(this.descriptor.getString());
        }
        
        public boolean matchesAllButType(final Item item) {
            return this.startPc == item.startPc && this.length == item.length && this.index == item.index && this.name.equals(item.name);
        }
        
        public boolean matchesPcAndIndex(final int n, final int n2) {
            return n2 == this.index && n >= this.startPc && n < this.startPc + this.length;
        }
        
        public Item withSignature(final CstString cstString) {
            return new Item(this.startPc, this.length, this.name, this.descriptor, cstString, this.index);
        }
    }
}
