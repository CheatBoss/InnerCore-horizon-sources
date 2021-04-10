package com.microsoft.xbox.idp.util;

import com.microsoft.xbox.idp.toolkit.*;
import android.util.*;
import android.graphics.*;

public class BitmapLoaderCache implements Cache
{
    private final LruCache<Object, Bitmap> cache;
    
    public BitmapLoaderCache(final int n) {
        this.cache = (LruCache<Object, Bitmap>)new LruCache(n);
    }
    
    @Override
    public void clear() {
        this.cache.evictAll();
    }
    
    @Override
    public Bitmap get(final Object o) {
        return (Bitmap)this.cache.get(o);
    }
    
    @Override
    public Bitmap put(final Object o, final Bitmap bitmap) {
        return (Bitmap)this.cache.put(o, (Object)bitmap);
    }
    
    @Override
    public Bitmap remove(final Object o) {
        return (Bitmap)this.cache.remove(o);
    }
}
