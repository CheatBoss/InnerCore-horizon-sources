package com.bumptech.glide.load.engine.cache;

import java.io.*;

public class DiskLruCacheFactory implements Factory
{
    private final CacheDirectoryGetter cacheDirectoryGetter;
    private final int diskCacheSize;
    
    public DiskLruCacheFactory(final CacheDirectoryGetter cacheDirectoryGetter, final int diskCacheSize) {
        this.diskCacheSize = diskCacheSize;
        this.cacheDirectoryGetter = cacheDirectoryGetter;
    }
    
    public DiskLruCacheFactory(final String s, final int n) {
        this((CacheDirectoryGetter)new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                return new File(s);
            }
        }, n);
    }
    
    public DiskLruCacheFactory(final String s, final String s2, final int n) {
        this((CacheDirectoryGetter)new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                return new File(s, s2);
            }
        }, n);
    }
    
    @Override
    public DiskCache build() {
        final File cacheDirectory = this.cacheDirectoryGetter.getCacheDirectory();
        if (cacheDirectory == null) {
            return null;
        }
        if (!cacheDirectory.mkdirs() && (!cacheDirectory.exists() || !cacheDirectory.isDirectory())) {
            return null;
        }
        return DiskLruCacheWrapper.get(cacheDirectory, this.diskCacheSize);
    }
    
    public interface CacheDirectoryGetter
    {
        File getCacheDirectory();
    }
}
