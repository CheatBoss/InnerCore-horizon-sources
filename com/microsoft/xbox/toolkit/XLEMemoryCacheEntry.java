package com.microsoft.xbox.toolkit;

public class XLEMemoryCacheEntry<V>
{
    private int byteCount;
    private V data;
    
    public XLEMemoryCacheEntry(final V data, final int byteCount) {
        if (data == null) {
            throw new IllegalArgumentException("data");
        }
        if (byteCount > 0) {
            this.data = data;
            this.byteCount = byteCount;
            return;
        }
        throw new IllegalArgumentException("byteCount");
    }
    
    public int getByteCount() {
        return this.byteCount;
    }
    
    public V getValue() {
        return this.data;
    }
}
