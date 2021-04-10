package com.zhekasmirnov.apparatus.mcpe;

import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.item.*;

public class NativePlayer
{
    private final long pointer;
    
    public NativePlayer(final long n) {
        this.pointer = constructNew(n);
    }
    
    private static native void addExperience(final long p0, final int p1);
    
    private static native void addItemToInventory(final long p0, final int p1, final int p2, final int p3, final long p4, final boolean p5);
    
    private static native void addItemToInventoryPtr(final long p0, final long p1, final boolean p2);
    
    private static native long constructNew(final long p0);
    
    private static native long getArmor(final long p0, final int p1);
    
    private static native int getDimension(final long p0);
    
    private static native float getExhaustion(final long p0);
    
    private static native float getExperience(final long p0);
    
    private static native int getGameMode(final long p0);
    
    private static native float getHunger(final long p0);
    
    private static native long getInventorySlot(final long p0, final int p1);
    
    private static native float getLevel(final long p0);
    
    private static native float getSaturation(final long p0);
    
    private static native int getScore(final long p0);
    
    private static native int getSelectedSlot(final long p0);
    
    private static native void invokeUseItemNoTarget(final long p0, final int p1, final int p2, final int p3, final long p4);
    
    private static native boolean isSneaking(final long p0);
    
    private static native boolean isValid(final long p0);
    
    private static native void setArmor(final long p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    private static native void setExhaustion(final long p0, final float p1);
    
    private static native void setExperience(final long p0, final float p1);
    
    private static native void setHunger(final long p0, final float p1);
    
    private static native void setInventorySlot(final long p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    private static native void setLevel(final long p0, final float p1);
    
    private static native void setRespawnCoords(final long p0, final int p1, final int p2, final int p3);
    
    private static native void setSaturation(final long p0, final float p1);
    
    private static native void setSelectedSlot(final long p0, final int p1);
    
    private static native void setSneaking(final long p0, final boolean p1);
    
    private static native void spawnExpOrbs(final long p0, final float p1, final float p2, final float p3, final int p4);
    
    public void addExperience(final int n) {
        addExperience(this.pointer, n);
    }
    
    public void addItemToInventory(final int n, final int n2, final int n3) {
        this.addItemToInventory(n, n2, n3, null, true);
    }
    
    public void addItemToInventory(final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        this.addItemToInventory(n, n2, n3, nativeItemInstanceExtra, true);
    }
    
    public void addItemToInventory(final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra, final boolean b) {
        addItemToInventory(this.pointer, n, n2, n3, NativeItemInstanceExtra.getValueOrNullPtr(nativeItemInstanceExtra), b);
    }
    
    public void addItemToInventoryPtr(final long n, final boolean b) {
        addItemToInventoryPtr(this.pointer, n, b);
    }
    
    public ItemStack getArmor(final int n) {
        return ItemStack.fromPtr(getArmor(this.pointer, n));
    }
    
    public int getDimension() {
        return getDimension(this.pointer);
    }
    
    public float getExhaustion() {
        return getExhaustion(this.pointer);
    }
    
    public float getExperience() {
        return getExperience(this.pointer);
    }
    
    public int getGameMode() {
        return getGameMode(this.pointer);
    }
    
    public float getHunger() {
        return getHunger(this.pointer);
    }
    
    public ItemStack getInventorySlot(final int n) {
        return ItemStack.fromPtr(getInventorySlot(this.pointer, n));
    }
    
    public float getLevel() {
        return getLevel(this.pointer);
    }
    
    public float getSaturation() {
        return getSaturation(this.pointer);
    }
    
    public int getScore() {
        return getScore(this.pointer);
    }
    
    public int getSelectedSlot() {
        return getSelectedSlot(this.pointer);
    }
    
    public void invokeUseItemNoTarget(final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        invokeUseItemNoTarget(this.pointer, n, n2, n3, NativeItemInstanceExtra.getValueOrNullPtr(nativeItemInstanceExtra));
    }
    
    public boolean isSneaking() {
        return isSneaking(this.pointer);
    }
    
    public boolean isValid() {
        try {
            return isValid(this.pointer);
        }
        catch (NoSuchMethodError noSuchMethodError) {
            return this.pointer != 0L;
        }
    }
    
    public void setArmor(final int n, final int n2, final int n3, final int n4, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        setArmor(this.pointer, n, n2, n3, n4, NativeItemInstanceExtra.getValueOrNullPtr(nativeItemInstanceExtra));
    }
    
    public void setExhaustion(final float n) {
        setExhaustion(this.pointer, n);
    }
    
    public void setExperience(final float n) {
        setExperience(this.pointer, n);
    }
    
    public void setHunger(final float n) {
        setHunger(this.pointer, n);
    }
    
    public void setInventorySlot(final int n, final int n2, final int n3, final int n4, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        setInventorySlot(this.pointer, n, n2, n3, n4, NativeItemInstanceExtra.getValueOrNullPtr(nativeItemInstanceExtra));
    }
    
    public void setLevel(final float n) {
        setLevel(this.pointer, n);
    }
    
    public void setRespawnCoords(final int n, final int n2, final int n3) {
        setRespawnCoords(this.pointer, n, n2, n3);
    }
    
    public void setSaturation(final float n) {
        setSaturation(this.pointer, n);
    }
    
    public void setSelectedSlot(final int n) {
        setSelectedSlot(this.pointer, n);
    }
    
    public void setSneaking(final boolean b) {
        setSneaking(this.pointer, b);
    }
    
    public void spawnExpOrbs(final float n, final float n2, final float n3, final int n4) {
        spawnExpOrbs(this.pointer, n, n2, n3, n4);
    }
}
