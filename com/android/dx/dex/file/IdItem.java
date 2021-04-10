package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;

public abstract class IdItem extends IndexedItem
{
    private final CstType type;
    
    public IdItem(final CstType type) {
        if (type == null) {
            throw new NullPointerException("type == null");
        }
        this.type = type;
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        dexFile.getTypeIds().intern(this.type);
    }
    
    public final CstType getDefiningClass() {
        return this.type;
    }
}
