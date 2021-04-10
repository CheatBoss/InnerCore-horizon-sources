package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.engine.cache.*;
import com.bumptech.glide.load.*;
import android.util.*;
import java.io.*;

class CacheLoader
{
    private static final String TAG = "CacheLoader";
    private final DiskCache diskCache;
    
    public CacheLoader(final DiskCache diskCache) {
        this.diskCache = diskCache;
    }
    
    public <Z> Resource<Z> load(final Key key, final ResourceDecoder<File, Z> resourceDecoder, final int n, final int n2) {
        final File value = this.diskCache.get(key);
        final Resource<Z> resource = null;
        if (value == null) {
            return null;
        }
        Resource<Z> decode;
        try {
            decode = resourceDecoder.decode(value, n, n2);
        }
        catch (IOException ex) {
            decode = resource;
            if (Log.isLoggable("CacheLoader", 3)) {
                Log.d("CacheLoader", "Exception decoding image from cache", (Throwable)ex);
                decode = resource;
            }
        }
        if (decode == null) {
            if (Log.isLoggable("CacheLoader", 3)) {
                Log.d("CacheLoader", "Failed to decode image from cache or not present in cache");
            }
            this.diskCache.delete(key);
        }
        return decode;
    }
}
