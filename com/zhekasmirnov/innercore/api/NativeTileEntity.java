package com.zhekasmirnov.innercore.api;

import com.zhekasmirnov.innercore.api.nbt.*;

public class NativeTileEntity
{
    public static final boolean isContainer = false;
    private long pointer;
    protected int size;
    protected int type;
    protected int x;
    protected int y;
    protected int z;
    
    public NativeTileEntity(final long pointer) {
        this.pointer = pointer;
        this.type = getType(pointer);
        this.size = getSize(pointer);
    }
    
    public static native long getCompoundTag(final long p0);
    
    public static native long getInWorld(final int p0, final int p1, final int p2);
    
    public static native int getSize(final long p0);
    
    public static native long getSlot(final long p0, final int p1);
    
    public static NativeTileEntity getTileEntity(final int n, final int n2, final int n3) {
        final long inWorld = getInWorld(n, n2, n3);
        if (inWorld == 0L) {
            return null;
        }
        return new NativeTileEntity(inWorld);
    }
    
    public static native int getType(final long p0);
    
    public static native void setCompoundTag(final long p0, final long p1);
    
    public static native void setSlot(final long p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static native void setSlot2(final long p0, final int p1, final long p2);
    
    public NativeCompoundTag getCompoundTag() {
        return new NativeCompoundTag(getCompoundTag(this.pointer));
    }
    
    public int getSize() {
        return this.size;
    }
    
    public NativeItemInstance getSlot(final int n) {
        if (n < 0) {
            return null;
        }
        if (n >= this.size) {
            return null;
        }
        final long slot = getSlot(this.pointer, n);
        if (slot == 0L) {
            return null;
        }
        return new NativeItemInstance(slot);
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setCompoundTag(final NativeCompoundTag nativeCompoundTag) {
        if (nativeCompoundTag != null) {
            setCompoundTag(this.pointer, nativeCompoundTag.pointer);
        }
    }
    
    public void setSlot(final int n, final int n2, final int n3, final int n4) {
        if (n < 0) {
            return;
        }
        if (n >= this.size) {
            return;
        }
        setSlot(this.pointer, n, n2, n3, n4, 0L);
    }
    
    public void setSlot(final int n, final int n2, final int n3, final int n4, final Object o) {
        if (n < 0) {
            return;
        }
        if (n >= this.size) {
            return;
        }
        setSlot(this.pointer, n, n2, n3, n4, NativeItemInstanceExtra.unwrapValue(o));
    }
    
    public void setSlot(final int n, final NativeItemInstance nativeItemInstance) {
        if (n < 0) {
            return;
        }
        if (n >= this.size) {
            return;
        }
        setSlot2(this.pointer, n, nativeItemInstance.getPointer());
    }
}
