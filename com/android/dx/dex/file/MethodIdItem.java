package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;

public final class MethodIdItem extends MemberIdItem
{
    public MethodIdItem(final CstBaseMethodRef cstBaseMethodRef) {
        super(cstBaseMethodRef);
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        super.addContents(dexFile);
        dexFile.getProtoIds().intern(this.getMethodRef().getPrototype());
    }
    
    public CstBaseMethodRef getMethodRef() {
        return (CstBaseMethodRef)this.getRef();
    }
    
    @Override
    protected int getTypoidIdx(final DexFile dexFile) {
        return dexFile.getProtoIds().indexOf(this.getMethodRef().getPrototype());
    }
    
    @Override
    protected String getTypoidName() {
        return "proto_idx";
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_METHOD_ID_ITEM;
    }
}
