package com.microsoft.xbox.idp.util;

import com.microsoft.xbox.idp.toolkit.*;
import android.util.*;

public final class CacheUtil
{
    private static final String TAG;
    private static final BitmapLoader.Cache bitmapCache;
    private static final ObjectLoader.Cache objectLoaderCache;
    
    static {
        TAG = CacheUtil.class.getSimpleName();
        objectLoaderCache = new ObjectLoaderCache();
        bitmapCache = new BitmapLoaderCache(50);
    }
    
    public static void clearCaches() {
        Log.d(CacheUtil.TAG, "clearCaches");
        synchronized (CacheUtil.objectLoaderCache) {
            CacheUtil.objectLoaderCache.clear();
            // monitorexit(CacheUtil.objectLoaderCache)
            final BitmapLoader.Cache bitmapCache = CacheUtil.bitmapCache;
            synchronized (CacheUtil.objectLoaderCache) {
                CacheUtil.bitmapCache.clear();
            }
        }
    }
    
    public static BitmapLoader.Cache getBitmapCache() {
        return CacheUtil.bitmapCache;
    }
    
    public static ObjectLoader.Cache getObjectLoaderCache() {
        return CacheUtil.objectLoaderCache;
    }
}
