package com.microsoft.xbox.idp.util;

import android.app.*;
import com.microsoft.xbox.idp.toolkit.*;

public class BitmapLoaderInfo implements LoaderInfo
{
    private final LoaderManager$LoaderCallbacks<?> callbacks;
    
    public BitmapLoaderInfo(final LoaderManager$LoaderCallbacks<?> callbacks) {
        this.callbacks = callbacks;
    }
    
    @Override
    public void clearCache(final Object o) {
        final BitmapLoader.Cache bitmapCache = CacheUtil.getBitmapCache();
        synchronized (bitmapCache) {
            bitmapCache.remove(o);
        }
    }
    
    @Override
    public LoaderManager$LoaderCallbacks<?> getLoaderCallbacks() {
        return this.callbacks;
    }
    
    @Override
    public boolean hasCachedData(final Object o) {
        while (true) {
            final BitmapLoader.Cache bitmapCache = CacheUtil.getBitmapCache();
            synchronized (bitmapCache) {
                if (bitmapCache.get(o) != null) {
                    return true;
                }
            }
            return false;
        }
    }
}
