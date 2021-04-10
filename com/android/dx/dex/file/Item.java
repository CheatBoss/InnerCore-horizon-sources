package com.android.dx.dex.file;

import com.android.dx.util.*;

public abstract class Item
{
    public abstract void addContents(final DexFile p0);
    
    public abstract ItemType itemType();
    
    public final String typeName() {
        return this.itemType().toHuman();
    }
    
    public abstract int writeSize();
    
    public abstract void writeTo(final DexFile p0, final AnnotatedOutput p1);
}
