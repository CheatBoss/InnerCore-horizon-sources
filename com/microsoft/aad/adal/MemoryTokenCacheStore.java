package com.microsoft.aad.adal;

import java.io.*;
import java.util.*;

public class MemoryTokenCacheStore implements ITokenCacheStore
{
    private static final String TAG = "MemoryTokenCacheStore";
    private static final long serialVersionUID = 3465700945655867086L;
    private final Map<String, TokenCacheItem> mCache;
    private transient Object mCacheLock;
    
    public MemoryTokenCacheStore() {
        this.mCache = new HashMap<String, TokenCacheItem>();
        this.mCacheLock = new Object();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.mCacheLock = new Object();
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        synchronized (this) {
            objectOutputStream.defaultWriteObject();
        }
    }
    
    @Override
    public boolean contains(final String s) {
        if (s != null) {
            while (true) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Key: ");
                sb.append(s);
                Logger.i("MemoryTokenCacheStore", "contains Item from cache.", sb.toString());
                synchronized (this.mCacheLock) {
                    if (this.mCache.get(s) != null) {
                        return true;
                    }
                    return false;
                }
                break;
                return false;
            }
        }
        throw new IllegalArgumentException("key");
    }
    
    @Override
    public Iterator<TokenCacheItem> getAll() {
        Logger.v("MemoryTokenCacheStore", "Retrieving all items from cache. ");
        synchronized (this.mCacheLock) {
            return this.mCache.values().iterator();
        }
    }
    
    @Override
    public TokenCacheItem getItem(final String s) {
        if (s != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Key:");
            sb.append(s);
            Logger.i("MemoryTokenCacheStore", "Get Item from cache. ", sb.toString());
            synchronized (this.mCacheLock) {
                return this.mCache.get(s);
            }
        }
        throw new IllegalArgumentException("The input key is null.");
    }
    
    @Override
    public void removeAll() {
        Logger.v("MemoryTokenCacheStore", "Remove all items from cache.");
        synchronized (this.mCacheLock) {
            this.mCache.clear();
        }
    }
    
    @Override
    public void removeItem(final String s) {
        if (s != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Key:");
            sb.append(s.hashCode());
            Logger.i("MemoryTokenCacheStore", "Remove Item from cache. ", sb.toString());
            synchronized (this.mCacheLock) {
                this.mCache.remove(s);
                return;
            }
        }
        throw new IllegalArgumentException("key");
    }
    
    @Override
    public void setItem(final String s, final TokenCacheItem tokenCacheItem) {
        if (tokenCacheItem != null) {
            if (s != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Key: ");
                sb.append(s);
                Logger.i("MemoryTokenCacheStore", "Set Item to cache. ", sb.toString());
                synchronized (this.mCacheLock) {
                    this.mCache.put(s, tokenCacheItem);
                    return;
                }
            }
            throw new IllegalArgumentException("key");
        }
        throw new IllegalArgumentException("item");
    }
}
