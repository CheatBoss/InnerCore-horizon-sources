package com.microsoft.xbox.idp.util;

import android.app.*;
import com.microsoft.xbox.idp.toolkit.*;

public class ObjectLoaderInfo implements LoaderInfo
{
    private final LoaderManager$LoaderCallbacks<?> callbacks;
    
    public ObjectLoaderInfo(final LoaderManager$LoaderCallbacks<?> callbacks) {
        this.callbacks = callbacks;
    }
    
    @Override
    public void clearCache(final Object o) {
        final ObjectLoader.Cache objectLoaderCache = CacheUtil.getObjectLoaderCache();
        synchronized (objectLoaderCache) {
            objectLoaderCache.remove(o);
        }
    }
    
    @Override
    public LoaderManager$LoaderCallbacks<?> getLoaderCallbacks() {
        return this.callbacks;
    }
    
    @Override
    public boolean hasCachedData(final Object o) {
        while (true) {
            final ObjectLoader.Cache objectLoaderCache = CacheUtil.getObjectLoaderCache();
            synchronized (objectLoaderCache) {
                if (objectLoaderCache.get(o) != null) {
                    return true;
                }
            }
            return false;
        }
    }
}
