package com.bumptech.glide.util;

import java.util.*;

public class LruCache<T, Y>
{
    private final LinkedHashMap<T, Y> cache;
    private int currentSize;
    private final int initialMaxSize;
    private int maxSize;
    
    public LruCache(final int n) {
        this.cache = new LinkedHashMap<T, Y>(100, 0.75f, true);
        this.currentSize = 0;
        this.initialMaxSize = n;
        this.maxSize = n;
    }
    
    private void evict() {
        this.trimToSize(this.maxSize);
    }
    
    public void clearMemory() {
        this.trimToSize(0);
    }
    
    public boolean contains(final T t) {
        return this.cache.containsKey(t);
    }
    
    public Y get(final T t) {
        return this.cache.get(t);
    }
    
    public int getCurrentSize() {
        return this.currentSize;
    }
    
    public int getMaxSize() {
        return this.maxSize;
    }
    
    protected int getSize(final Y y) {
        return 1;
    }
    
    protected void onItemEvicted(final T t, final Y y) {
    }
    
    public Y put(final T t, final Y y) {
        if (this.getSize(y) >= this.maxSize) {
            this.onItemEvicted(t, y);
            return null;
        }
        final Y put = this.cache.put(t, y);
        if (y != null) {
            this.currentSize += this.getSize(y);
        }
        if (put != null) {
            this.currentSize -= this.getSize(put);
        }
        this.evict();
        return put;
    }
    
    public Y remove(final T t) {
        final Y remove = this.cache.remove(t);
        if (remove != null) {
            this.currentSize -= this.getSize(remove);
        }
        return remove;
    }
    
    public void setSizeMultiplier(final float n) {
        if (n < 0.0f) {
            throw new IllegalArgumentException("Multiplier must be >= 0");
        }
        this.maxSize = Math.round(this.initialMaxSize * n);
        this.evict();
    }
    
    protected void trimToSize(final int n) {
        while (this.currentSize > n) {
            final Map.Entry<T, Y> entry = this.cache.entrySet().iterator().next();
            final Y value = entry.getValue();
            this.currentSize -= this.getSize(value);
            final T key = entry.getKey();
            this.cache.remove(key);
            this.onItemEvicted(key, value);
        }
    }
}
