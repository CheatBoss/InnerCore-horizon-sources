package com.android.dx.cf.attrib;

import com.android.dx.util.*;
import com.android.dx.rop.cst.*;

public final class InnerClassList extends FixedSizeList
{
    public InnerClassList(final int n) {
        super(n);
    }
    
    public Item get(final int n) {
        return (Item)this.get0(n);
    }
    
    public void set(final int n, final CstType cstType, final CstType cstType2, final CstString cstString, final int n2) {
        this.set0(n, new Item(cstType, cstType2, cstString, n2));
    }
    
    public static class Item
    {
        private final int accessFlags;
        private final CstType innerClass;
        private final CstString innerName;
        private final CstType outerClass;
        
        public Item(final CstType innerClass, final CstType outerClass, final CstString innerName, final int accessFlags) {
            if (innerClass == null) {
                throw new NullPointerException("innerClass == null");
            }
            this.innerClass = innerClass;
            this.outerClass = outerClass;
            this.innerName = innerName;
            this.accessFlags = accessFlags;
        }
        
        public int getAccessFlags() {
            return this.accessFlags;
        }
        
        public CstType getInnerClass() {
            return this.innerClass;
        }
        
        public CstString getInnerName() {
            return this.innerName;
        }
        
        public CstType getOuterClass() {
            return this.outerClass;
        }
    }
}
