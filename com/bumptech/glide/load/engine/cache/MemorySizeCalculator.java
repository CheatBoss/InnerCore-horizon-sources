package com.bumptech.glide.load.engine.cache;

import android.content.*;
import android.app.*;
import android.os.*;
import android.annotation.*;
import android.text.format.*;
import android.util.*;

public class MemorySizeCalculator
{
    static final int BITMAP_POOL_TARGET_SCREENS = 4;
    static final int BYTES_PER_ARGB_8888_PIXEL = 4;
    static final float LOW_MEMORY_MAX_SIZE_MULTIPLIER = 0.33f;
    static final float MAX_SIZE_MULTIPLIER = 0.4f;
    static final int MEMORY_CACHE_TARGET_SCREENS = 2;
    private static final String TAG = "MemorySizeCalculator";
    private final int bitmapPoolSize;
    private final Context context;
    private final int memoryCacheSize;
    
    public MemorySizeCalculator(final Context context) {
        this(context, (ActivityManager)context.getSystemService("activity"), (ScreenDimensions)new DisplayMetricsScreenDimensions(context.getResources().getDisplayMetrics()));
    }
    
    MemorySizeCalculator(final Context context, final ActivityManager activityManager, final ScreenDimensions screenDimensions) {
        this.context = context;
        final int maxSize = getMaxSize(activityManager);
        final int n = screenDimensions.getWidthPixels() * screenDimensions.getHeightPixels() * 4;
        final int bitmapPoolSize = n * 4;
        final int memoryCacheSize = n * 2;
        if (memoryCacheSize + bitmapPoolSize <= maxSize) {
            this.memoryCacheSize = memoryCacheSize;
            this.bitmapPoolSize = bitmapPoolSize;
        }
        else {
            final int round = Math.round(maxSize / 6.0f);
            this.memoryCacheSize = round * 2;
            this.bitmapPoolSize = round * 4;
        }
        if (Log.isLoggable("MemorySizeCalculator", 3)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Calculated memory cache size: ");
            sb.append(this.toMb(this.memoryCacheSize));
            sb.append(" pool size: ");
            sb.append(this.toMb(this.bitmapPoolSize));
            sb.append(" memory class limited? ");
            sb.append(memoryCacheSize + bitmapPoolSize > maxSize);
            sb.append(" max size: ");
            sb.append(this.toMb(maxSize));
            sb.append(" memoryClass: ");
            sb.append(activityManager.getMemoryClass());
            sb.append(" isLowMemoryDevice: ");
            sb.append(isLowMemoryDevice(activityManager));
            Log.d("MemorySizeCalculator", sb.toString());
        }
    }
    
    private static int getMaxSize(final ActivityManager activityManager) {
        final int memoryClass = activityManager.getMemoryClass();
        final boolean lowMemoryDevice = isLowMemoryDevice(activityManager);
        final float n = (float)(memoryClass * 1024 * 1024);
        float n2;
        if (lowMemoryDevice) {
            n2 = 0.33f;
        }
        else {
            n2 = 0.4f;
        }
        return Math.round(n * n2);
    }
    
    @TargetApi(19)
    private static boolean isLowMemoryDevice(final ActivityManager activityManager) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        return sdk_INT < 11 || (sdk_INT >= 19 && activityManager.isLowRamDevice());
    }
    
    private String toMb(final int n) {
        return Formatter.formatFileSize(this.context, (long)n);
    }
    
    public int getBitmapPoolSize() {
        return this.bitmapPoolSize;
    }
    
    public int getMemoryCacheSize() {
        return this.memoryCacheSize;
    }
    
    private static class DisplayMetricsScreenDimensions implements ScreenDimensions
    {
        private final DisplayMetrics displayMetrics;
        
        public DisplayMetricsScreenDimensions(final DisplayMetrics displayMetrics) {
            this.displayMetrics = displayMetrics;
        }
        
        @Override
        public int getHeightPixels() {
            return this.displayMetrics.heightPixels;
        }
        
        @Override
        public int getWidthPixels() {
            return this.displayMetrics.widthPixels;
        }
    }
    
    interface ScreenDimensions
    {
        int getHeightPixels();
        
        int getWidthPixels();
    }
}
