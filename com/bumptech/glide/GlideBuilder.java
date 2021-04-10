package com.bumptech.glide;

import android.content.*;
import com.bumptech.glide.load.*;
import java.util.concurrent.*;
import com.bumptech.glide.load.engine.*;
import com.bumptech.glide.load.engine.executor.*;
import android.os.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import com.bumptech.glide.load.engine.cache.*;

public class GlideBuilder
{
    private BitmapPool bitmapPool;
    private final Context context;
    private DecodeFormat decodeFormat;
    private DiskCache.Factory diskCacheFactory;
    private ExecutorService diskCacheService;
    private Engine engine;
    private MemoryCache memoryCache;
    private ExecutorService sourceService;
    
    public GlideBuilder(final Context context) {
        this.context = context.getApplicationContext();
    }
    
    Glide createGlide() {
        if (this.sourceService == null) {
            this.sourceService = new FifoPriorityThreadPoolExecutor(Math.max(1, Runtime.getRuntime().availableProcessors()));
        }
        if (this.diskCacheService == null) {
            this.diskCacheService = new FifoPriorityThreadPoolExecutor(1);
        }
        final MemorySizeCalculator memorySizeCalculator = new MemorySizeCalculator(this.context);
        if (this.bitmapPool == null) {
            if (Build$VERSION.SDK_INT >= 11) {
                this.bitmapPool = new LruBitmapPool(memorySizeCalculator.getBitmapPoolSize());
            }
            else {
                this.bitmapPool = new BitmapPoolAdapter();
            }
        }
        if (this.memoryCache == null) {
            this.memoryCache = new LruResourceCache(memorySizeCalculator.getMemoryCacheSize());
        }
        if (this.diskCacheFactory == null) {
            this.diskCacheFactory = new InternalCacheDiskCacheFactory(this.context);
        }
        if (this.engine == null) {
            this.engine = new Engine(this.memoryCache, this.diskCacheFactory, this.diskCacheService, this.sourceService);
        }
        if (this.decodeFormat == null) {
            this.decodeFormat = DecodeFormat.DEFAULT;
        }
        return new Glide(this.engine, this.memoryCache, this.bitmapPool, this.context, this.decodeFormat);
    }
    
    public GlideBuilder setBitmapPool(final BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
        return this;
    }
    
    public GlideBuilder setDecodeFormat(final DecodeFormat decodeFormat) {
        this.decodeFormat = decodeFormat;
        return this;
    }
    
    public GlideBuilder setDiskCache(final DiskCache.Factory diskCacheFactory) {
        this.diskCacheFactory = diskCacheFactory;
        return this;
    }
    
    @Deprecated
    public GlideBuilder setDiskCache(final DiskCache diskCache) {
        return this.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                return diskCache;
            }
        });
    }
    
    public GlideBuilder setDiskCacheService(final ExecutorService diskCacheService) {
        this.diskCacheService = diskCacheService;
        return this;
    }
    
    GlideBuilder setEngine(final Engine engine) {
        this.engine = engine;
        return this;
    }
    
    public GlideBuilder setMemoryCache(final MemoryCache memoryCache) {
        this.memoryCache = memoryCache;
        return this;
    }
    
    public GlideBuilder setResizeService(final ExecutorService sourceService) {
        this.sourceService = sourceService;
        return this;
    }
}
