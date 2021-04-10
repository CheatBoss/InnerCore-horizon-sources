package com.zhekasmirnov.innercore.api.mod.ui.memory;

import java.util.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;

public abstract class BitmapWrap
{
    public static final Bitmap MISSING_BITMAP;
    private static HashMap<String, BitmapWrap> namedWrapCache;
    protected Bitmap bitmap;
    protected Bitmap$Config config;
    protected int height;
    protected boolean isRecycled;
    protected boolean isStored;
    private Bitmap resizeCacheBmp;
    private int resizeCacheH;
    private int resizeCacheW;
    private int useId;
    protected int width;
    
    static {
        MISSING_BITMAP = TextureSource.instance.getSafe("missing_texture");
        BitmapWrap.namedWrapCache = new HashMap<String, BitmapWrap>();
    }
    
    BitmapWrap() {
        this.useId = -1;
        this.isRecycled = false;
        this.isStored = false;
        this.config = Bitmap$Config.ARGB_8888;
        BitmapCache.registerWrap(this);
    }
    
    public static BitmapWrap wrap(final Bitmap bitmap) {
        return new RandomBitmapWrap(bitmap);
    }
    
    public static BitmapWrap wrap(final String s) {
        final Bitmap safe = TextureSource.instance.getSafe(s);
        return wrap(s, safe.getWidth(), safe.getHeight());
    }
    
    public static BitmapWrap wrap(final String s, final int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(n);
        sb.append(".");
        sb.append(n2);
        final String string = sb.toString();
        final BitmapWrap bitmapWrap = BitmapWrap.namedWrapCache.get(string);
        if (bitmapWrap != null) {
            return bitmapWrap;
        }
        final SourceBitmapWrap sourceBitmapWrap = new SourceBitmapWrap(s, n, n2);
        BitmapWrap.namedWrapCache.put(string, sourceBitmapWrap);
        return sourceBitmapWrap;
    }
    
    public Bitmap get() {
        synchronized (this) {
            this.useId = BitmapCache.getUseId();
            this.restoreIfNeeded();
            Bitmap bitmap;
            if (this.bitmap != null && !this.bitmap.isRecycled()) {
                bitmap = this.bitmap;
            }
            else {
                bitmap = BitmapWrap.MISSING_BITMAP;
            }
            return bitmap;
        }
    }
    
    public Bitmap$Config getConfig() {
        return this.config;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public Bitmap getResizedCache(final int resizeCacheW, final int resizeCacheH) {
        synchronized (this) {
            if (this.resizeCacheBmp != null && this.resizeCacheW == resizeCacheW && this.resizeCacheH == resizeCacheH) {
                return this.resizeCacheBmp;
            }
            this.removeCache();
            final Bitmap value = this.get();
            this.resizeCacheBmp = Bitmap.createScaledBitmap(value, resizeCacheW, resizeCacheH, false);
            if (this.resizeCacheBmp == value) {
                this.resizeCacheBmp = value.copy(this.getConfig(), true);
            }
            this.resizeCacheW = resizeCacheW;
            this.resizeCacheH = resizeCacheH;
            return this.resizeCacheBmp;
        }
    }
    
    public int getStackPos() {
        return BitmapCache.getStackPos(this.useId);
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public boolean isRecycled() {
        return this.isRecycled;
    }
    
    public void recycle() {
        synchronized (this) {
            this.removeCache();
            if (!this.isRecycled && this.bitmap != null) {
                this.bitmap.recycle();
                this.bitmap = null;
            }
            this.isRecycled = true;
            BitmapCache.unregisterWrap(this);
        }
    }
    
    public void removeCache() {
        synchronized (this) {
            if (this.resizeCacheBmp != null) {
                this.resizeCacheBmp.recycle();
                this.resizeCacheBmp = null;
            }
        }
    }
    
    public abstract BitmapWrap resize(final int p0, final int p1);
    
    public abstract boolean restore();
    
    public void restoreIfNeeded() {
        synchronized (this) {
            if (this.isStored && !this.isRecycled) {
                this.isStored = (this.restore() ^ true);
            }
        }
    }
    
    public abstract boolean store();
    
    public void storeIfNeeded() {
        synchronized (this) {
            this.removeCache();
            if (!this.isStored && !this.isRecycled) {
                this.isStored = this.store();
            }
        }
    }
}
