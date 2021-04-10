package com.bumptech.glide.load.engine.prefill;

import com.bumptech.glide.load.engine.bitmap_recycle.*;
import com.bumptech.glide.load.*;
import com.bumptech.glide.load.engine.cache.*;
import android.os.*;
import com.bumptech.glide.util.*;
import java.util.*;
import android.graphics.*;

public final class BitmapPreFiller
{
    private final BitmapPool bitmapPool;
    private BitmapPreFillRunner current;
    private final DecodeFormat defaultFormat;
    private final Handler handler;
    private final MemoryCache memoryCache;
    
    public BitmapPreFiller(final MemoryCache memoryCache, final BitmapPool bitmapPool, final DecodeFormat defaultFormat) {
        this.handler = new Handler(Looper.getMainLooper());
        this.memoryCache = memoryCache;
        this.bitmapPool = bitmapPool;
        this.defaultFormat = defaultFormat;
    }
    
    private static int getSizeInBytes(final PreFillType preFillType) {
        return Util.getBitmapByteSize(preFillType.getWidth(), preFillType.getHeight(), preFillType.getConfig());
    }
    
    PreFillQueue generateAllocationOrder(final PreFillType[] array) {
        final int maxSize = this.memoryCache.getMaxSize();
        final int currentSize = this.memoryCache.getCurrentSize();
        final int maxSize2 = this.bitmapPool.getMaxSize();
        final int length = array.length;
        final int n = 0;
        int n2 = 0;
        for (int i = 0; i < length; ++i) {
            n2 += array[i].getWeight();
        }
        final float n3 = (maxSize - currentSize + maxSize2) / (float)n2;
        final HashMap<PreFillType, Integer> hashMap = new HashMap<PreFillType, Integer>();
        for (int length2 = array.length, j = n; j < length2; ++j) {
            final PreFillType preFillType = array[j];
            hashMap.put(preFillType, Math.round(preFillType.getWeight() * n3) / getSizeInBytes(preFillType));
        }
        return new PreFillQueue(hashMap);
    }
    
    public void preFill(final PreFillType.Builder... array) {
        if (this.current != null) {
            this.current.cancel();
        }
        final PreFillType[] array2 = new PreFillType[array.length];
        for (int i = 0; i < array.length; ++i) {
            final PreFillType.Builder builder = array[i];
            if (builder.getConfig() == null) {
                Bitmap$Config config;
                if (this.defaultFormat != DecodeFormat.ALWAYS_ARGB_8888 && this.defaultFormat != DecodeFormat.PREFER_ARGB_8888) {
                    config = Bitmap$Config.RGB_565;
                }
                else {
                    config = Bitmap$Config.ARGB_8888;
                }
                builder.setConfig(config);
            }
            array2[i] = builder.build();
        }
        this.current = new BitmapPreFillRunner(this.bitmapPool, this.memoryCache, this.generateAllocationOrder(array2));
        this.handler.post((Runnable)this.current);
    }
}
