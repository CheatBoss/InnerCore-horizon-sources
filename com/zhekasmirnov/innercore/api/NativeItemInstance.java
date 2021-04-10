package com.zhekasmirnov.innercore.api;

public class NativeItemInstance
{
    public int count;
    public int data;
    public NativeItemInstanceExtra extra;
    public int id;
    public boolean isValid;
    private long pointer;
    
    public NativeItemInstance(final int id, final int count, final int data) {
        this.isValid = false;
        this.pointer = createItemInstanceData(id, count, data);
        this.id = id;
        this.count = count;
        this.data = data;
        this.isValid = true;
    }
    
    public NativeItemInstance(long extra) {
        this.isValid = false;
        this.pointer = extra;
        final long pointer = this.pointer;
        NativeItemInstanceExtra extra2 = null;
        if (pointer == 0L) {
            this.pointer = createItemInstanceData(0, 0, 0);
            this.data = 0;
            this.count = 0;
            this.id = 0;
            this.extra = null;
        }
        else {
            this.id = getId(extra);
            this.count = getCount(extra);
            this.data = getData(extra);
            extra = getExtra(extra);
            if (extra != 0L) {
                extra2 = new NativeItemInstanceExtra(extra);
            }
            this.extra = extra2;
        }
        this.isValid = true;
    }
    
    public static native long createItemInstanceData(final int p0, final int p1, final int p2);
    
    public static native int destroy(final long p0);
    
    public static native int getCount(final long p0);
    
    public static native int getData(final long p0);
    
    public static native long getExtra(final long p0);
    
    public static native int getId(final long p0);
    
    public static native void setExtra(final long p0, final int p1);
    
    public static native int setItemInstance(final long p0, final int p1, final int p2, final int p3);
    
    public void destroy() {
        this.isValid = false;
    }
    
    public long getPointer() {
        setItemInstance(this.pointer, this.id, this.count, this.data);
        return this.pointer;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[item=");
        sb.append(this.id);
        sb.append(",");
        sb.append(this.count);
        sb.append(",");
        sb.append(this.data);
        sb.append("]");
        return sb.toString();
    }
}
