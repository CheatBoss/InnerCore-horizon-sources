package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import java.util.*;

public final class HeaderSection extends UniformItemSection
{
    private final List<HeaderItem> list;
    
    public HeaderSection(final DexFile dexFile) {
        super(null, dexFile, 4);
        final HeaderItem headerItem = new HeaderItem();
        headerItem.setIndex(0);
        this.list = Collections.singletonList(headerItem);
    }
    
    @Override
    public IndexedItem get(final Constant constant) {
        return null;
    }
    
    @Override
    public Collection<? extends Item> items() {
        return this.list;
    }
    
    @Override
    protected void orderItems() {
    }
}
