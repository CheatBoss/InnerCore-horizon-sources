package com.microsoft.xbox.toolkit;

import android.util.*;

public class XLEMemoryCache<K, V>
{
    private int itemCount;
    private final LruCache<K, XLEMemoryCacheEntry<V>> lruCache;
    private final int maxFileSizeBytes;
    
    public XLEMemoryCache(final int n, final int maxFileSizeBytes) {
        this.itemCount = 0;
        if (n < 0) {
            throw new IllegalArgumentException("sizeInBytes");
        }
        if (maxFileSizeBytes >= 0) {
            this.maxFileSizeBytes = maxFileSizeBytes;
            LruCache<K, XLEMemoryCacheEntry<V>> lruCache;
            if (n == 0) {
                lruCache = null;
            }
            else {
                lruCache = new LruCache<K, XLEMemoryCacheEntry<V>>(n) {
                    protected void entryRemoved(final boolean b, final K k, final XLEMemoryCacheEntry<V> xleMemoryCacheEntry, final XLEMemoryCacheEntry<V> xleMemoryCacheEntry2) {
                        --XLEMemoryCache.this.itemCount;
                    }
                    
                    protected int sizeOf(final K k, final XLEMemoryCacheEntry<V> xleMemoryCacheEntry) {
                        return xleMemoryCacheEntry.getByteCount();
                    }
                };
            }
            this.lruCache = lruCache;
            return;
        }
        throw new IllegalArgumentException("maxFileSizeInBytes");
    }
    
    public boolean add(final K k, final V v, final int n) {
        if (n > this.maxFileSizeBytes) {
            return false;
        }
        if (this.lruCache == null) {
            return false;
        }
        final XLEMemoryCacheEntry xleMemoryCacheEntry = new XLEMemoryCacheEntry((V)v, n);
        ++this.itemCount;
        this.lruCache.put((Object)k, (Object)xleMemoryCacheEntry);
        return true;
    }
    
    public V get(final K k) {
        final LruCache<K, XLEMemoryCacheEntry<V>> lruCache = this.lruCache;
        if (lruCache != null) {
            final XLEMemoryCacheEntry xleMemoryCacheEntry = (XLEMemoryCacheEntry)lruCache.get((Object)k);
            if (xleMemoryCacheEntry != null) {
                return xleMemoryCacheEntry.getValue();
            }
        }
        return null;
    }
    
    public int getBytesCurrent() {
        final LruCache<K, XLEMemoryCacheEntry<V>> lruCache = this.lruCache;
        if (lruCache == null) {
            return 0;
        }
        return lruCache.size();
    }
    
    public int getBytesFree() {
        final LruCache<K, XLEMemoryCacheEntry<V>> lruCache = this.lruCache;
        if (lruCache == null) {
            return 0;
        }
        return lruCache.maxSize() - this.lruCache.size();
    }
    
    public int getItemsInCache() {
        return this.itemCount;
    }
}
