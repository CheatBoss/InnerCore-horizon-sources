package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.util.*;

public abstract class UniformItemSection extends Section
{
    public UniformItemSection(final String s, final DexFile dexFile, final int n) {
        super(s, dexFile, n);
    }
    
    public abstract IndexedItem get(final Constant p0);
    
    @Override
    public final int getAbsoluteItemOffset(final Item item) {
        final IndexedItem indexedItem = (IndexedItem)item;
        return this.getAbsoluteOffset(indexedItem.getIndex() * indexedItem.writeSize());
    }
    
    protected abstract void orderItems();
    
    @Override
    protected final void prepare0() {
        final DexFile file = this.getFile();
        this.orderItems();
        final Iterator<? extends Item> iterator = this.items().iterator();
        while (iterator.hasNext()) {
            ((Item)iterator.next()).addContents(file);
        }
    }
    
    @Override
    public final int writeSize() {
        final Collection<? extends Item> items = this.items();
        final int size = items.size();
        if (size == 0) {
            return 0;
        }
        return items.iterator().next().writeSize() * size;
    }
    
    @Override
    protected final void writeTo0(final AnnotatedOutput annotatedOutput) {
        final DexFile file = this.getFile();
        final int alignment = this.getAlignment();
        final Iterator<? extends Item> iterator = this.items().iterator();
        while (iterator.hasNext()) {
            ((Item)iterator.next()).writeTo(file, annotatedOutput);
            annotatedOutput.alignTo(alignment);
        }
    }
}
