package com.zhekasmirnov.apparatus.adapter.innercore.game.entity;

import com.zhekasmirnov.apparatus.adapter.innercore.game.item.*;
import com.zhekasmirnov.innercore.api.*;

public class EntityActor
{
    private final long uid;
    
    public EntityActor(final long uid) {
        this.uid = uid;
    }
    
    public ItemStack getArmorSlot(final int n) {
        return ItemStack.fromPtr(NativeAPI.getEntityArmor(this.uid, n));
    }
    
    public ItemStack getCarriedItem() {
        return ItemStack.fromPtr(NativeAPI.getEntityCarriedItem(this.uid));
    }
    
    public int getDimension() {
        return NativeAPI.getEntityDimension(this.uid);
    }
    
    public int getHealth() {
        return NativeAPI.getHealth(this.uid);
    }
    
    public int getMaxHealth() {
        return NativeAPI.getMaxHealth(this.uid);
    }
    
    public ItemStack getOffhandItem() {
        return ItemStack.fromPtr(NativeAPI.getEntityOffhandItem(this.uid));
    }
    
    public long getUid() {
        return this.uid;
    }
    
    public void setArmorSlot(final int n, final ItemStack itemStack) {
        NativeAPI.setEntityArmor(this.uid, n, itemStack.id, itemStack.count, itemStack.data, itemStack.getExtraPtr());
    }
}
