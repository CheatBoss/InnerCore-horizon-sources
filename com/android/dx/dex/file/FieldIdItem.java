package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;

public final class FieldIdItem extends MemberIdItem
{
    public FieldIdItem(final CstFieldRef cstFieldRef) {
        super(cstFieldRef);
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        super.addContents(dexFile);
        dexFile.getTypeIds().intern(this.getFieldRef().getType());
    }
    
    public CstFieldRef getFieldRef() {
        return (CstFieldRef)this.getRef();
    }
    
    @Override
    protected int getTypoidIdx(final DexFile dexFile) {
        return dexFile.getTypeIds().indexOf(this.getFieldRef().getType());
    }
    
    @Override
    protected String getTypoidName() {
        return "type_idx";
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_FIELD_ID_ITEM;
    }
}
