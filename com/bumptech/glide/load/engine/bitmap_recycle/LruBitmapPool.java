package com.bumptech.glide.load.engine.bitmap_recycle;

import android.util.*;
import android.os.*;
import java.util.*;
import android.graphics.*;
import android.annotation.*;

public class LruBitmapPool implements BitmapPool
{
    private static final Bitmap$Config DEFAULT_CONFIG;
    private static final String TAG = "LruBitmapPool";
    private final Set<Bitmap$Config> allowedConfigs;
    private int currentSize;
    private int evictions;
    private int hits;
    private final int initialMaxSize;
    private int maxSize;
    private int misses;
    private int puts;
    private final LruPoolStrategy strategy;
    private final BitmapTracker tracker;
    
    static {
        DEFAULT_CONFIG = Bitmap$Config.ARGB_8888;
    }
    
    public LruBitmapPool(final int n) {
        this(n, getDefaultStrategy(), getDefaultAllowedConfigs());
    }
    
    LruBitmapPool(final int n, final LruPoolStrategy strategy, final Set<Bitmap$Config> allowedConfigs) {
        this.initialMaxSize = n;
        this.maxSize = n;
        this.strategy = strategy;
        this.allowedConfigs = allowedConfigs;
        this.tracker = (BitmapTracker)new NullBitmapTracker();
    }
    
    public LruBitmapPool(final int n, final Set<Bitmap$Config> set) {
        this(n, getDefaultStrategy(), set);
    }
    
    private void dump() {
        if (Log.isLoggable("LruBitmapPool", 2)) {
            this.dumpUnchecked();
        }
    }
    
    private void dumpUnchecked() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Hits=");
        sb.append(this.hits);
        sb.append(", misses=");
        sb.append(this.misses);
        sb.append(", puts=");
        sb.append(this.puts);
        sb.append(", evictions=");
        sb.append(this.evictions);
        sb.append(", currentSize=");
        sb.append(this.currentSize);
        sb.append(", maxSize=");
        sb.append(this.maxSize);
        sb.append("\nStrategy=");
        sb.append(this.strategy);
        Log.v("LruBitmapPool", sb.toString());
    }
    
    private void evict() {
        this.trimToSize(this.maxSize);
    }
    
    private static Set<Bitmap$Config> getDefaultAllowedConfigs() {
        final HashSet<Bitmap$Config> set = new HashSet<Bitmap$Config>();
        set.addAll((Collection<?>)Arrays.asList(Bitmap$Config.values()));
        if (Build$VERSION.SDK_INT >= 19) {
            set.add(null);
        }
        return (Set<Bitmap$Config>)Collections.unmodifiableSet((Set<?>)set);
    }
    
    private static LruPoolStrategy getDefaultStrategy() {
        if (Build$VERSION.SDK_INT >= 19) {
            return new SizeConfigStrategy();
        }
        return new AttributeStrategy();
    }
    
    private void trimToSize(final int n) {
        synchronized (this) {
            while (this.currentSize > n) {
                final Bitmap removeLast = this.strategy.removeLast();
                if (removeLast == null) {
                    if (Log.isLoggable("LruBitmapPool", 5)) {
                        Log.w("LruBitmapPool", "Size mismatch, resetting");
                        this.dumpUnchecked();
                    }
                    this.currentSize = 0;
                    return;
                }
                this.tracker.remove(removeLast);
                this.currentSize -= this.strategy.getSize(removeLast);
                removeLast.recycle();
                ++this.evictions;
                if (Log.isLoggable("LruBitmapPool", 3)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Evicting bitmap=");
                    sb.append(this.strategy.logBitmap(removeLast));
                    Log.d("LruBitmapPool", sb.toString());
                }
                this.dump();
            }
        }
    }
    
    @Override
    public void clearMemory() {
        if (Log.isLoggable("LruBitmapPool", 3)) {
            Log.d("LruBitmapPool", "clearMemory");
        }
        this.trimToSize(0);
    }
    
    @Override
    public Bitmap get(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        synchronized (this) {
            final Bitmap dirty = this.getDirty(n, n2, bitmap$Config);
            if (dirty != null) {
                dirty.eraseColor(0);
            }
            return dirty;
        }
    }
    
    @TargetApi(12)
    @Override
    public Bitmap getDirty(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        synchronized (this) {
            final LruPoolStrategy strategy = this.strategy;
            Bitmap$Config default_CONFIG;
            if (bitmap$Config != null) {
                default_CONFIG = bitmap$Config;
            }
            else {
                default_CONFIG = LruBitmapPool.DEFAULT_CONFIG;
            }
            final Bitmap value = strategy.get(n, n2, default_CONFIG);
            if (value == null) {
                if (Log.isLoggable("LruBitmapPool", 3)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Missing bitmap=");
                    sb.append(this.strategy.logBitmap(n, n2, bitmap$Config));
                    Log.d("LruBitmapPool", sb.toString());
                }
                ++this.misses;
            }
            else {
                ++this.hits;
                this.currentSize -= this.strategy.getSize(value);
                this.tracker.remove(value);
                if (Build$VERSION.SDK_INT >= 12) {
                    value.setHasAlpha(true);
                }
            }
            if (Log.isLoggable("LruBitmapPool", 2)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Get bitmap=");
                sb2.append(this.strategy.logBitmap(n, n2, bitmap$Config));
                Log.v("LruBitmapPool", sb2.toString());
            }
            this.dump();
            return value;
        }
    }
    
    @Override
    public int getMaxSize() {
        return this.maxSize;
    }
    
    @Override
    public boolean put(final Bitmap bitmap) {
        // monitorenter(this)
        Label_0016: {
            if (bitmap != null) {
                break Label_0016;
            }
            while (true) {
                try {
                    throw new NullPointerException("Bitmap must not be null");
                    // monitorexit(this)
                    // monitorexit(this)
                    // iftrue(Label_0265:, !Log.isLoggable("LruBitmapPool", 2))
                    // iftrue(Label_0160:, !Log.isLoggable("LruBitmapPool", 2))
                    while (true) {
                        while (true) {
                            while (true) {
                                while (true) {
                                    return false;
                                    this.dump();
                                    this.evict();
                                    return true;
                                    final StringBuilder sb;
                                    Label_0172: {
                                        sb = new StringBuilder();
                                    }
                                    sb.append("Reject bitmap from pool, bitmap: ");
                                    sb.append(this.strategy.logBitmap(bitmap));
                                    sb.append(", is mutable: ");
                                    sb.append(bitmap.isMutable());
                                    sb.append(", is allowed config: ");
                                    sb.append(this.allowedConfigs.contains(bitmap.getConfig()));
                                    Log.v("LruBitmapPool", sb.toString());
                                    continue;
                                }
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append("Put bitmap in pool=");
                                sb2.append(this.strategy.logBitmap(bitmap));
                                Log.v("LruBitmapPool", sb2.toString());
                                continue;
                            }
                            final int size = this.strategy.getSize(bitmap);
                            this.strategy.put(bitmap);
                            this.tracker.add(bitmap);
                            ++this.puts;
                            this.currentSize += size;
                            continue;
                        }
                        throw;
                        continue;
                    }
                }
                // monitorexit(this)
                // iftrue(Label_0172:, !bitmap.isMutable() || this.strategy.getSize(bitmap) > this.maxSize || !this.allowedConfigs.contains((Object)bitmap.getConfig()))
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    @Override
    public void setSizeMultiplier(final float n) {
        synchronized (this) {
            this.maxSize = Math.round(this.initialMaxSize * n);
            this.evict();
        }
    }
    
    @SuppressLint({ "InlinedApi" })
    @Override
    public void trimMemory(final int n) {
        if (Log.isLoggable("LruBitmapPool", 3)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("trimMemory, level=");
            sb.append(n);
            Log.d("LruBitmapPool", sb.toString());
        }
        if (n >= 60) {
            this.clearMemory();
            return;
        }
        if (n >= 40) {
            this.trimToSize(this.maxSize / 2);
        }
    }
    
    private interface BitmapTracker
    {
        void add(final Bitmap p0);
        
        void remove(final Bitmap p0);
    }
    
    private static class NullBitmapTracker implements BitmapTracker
    {
        @Override
        public void add(final Bitmap bitmap) {
        }
        
        @Override
        public void remove(final Bitmap bitmap) {
        }
    }
    
    private static class ThrowingBitmapTracker implements BitmapTracker
    {
        private final Set<Bitmap> bitmaps;
        
        private ThrowingBitmapTracker() {
            this.bitmaps = Collections.synchronizedSet(new HashSet<Bitmap>());
        }
        
        @Override
        public void add(final Bitmap bitmap) {
            if (this.bitmaps.contains(bitmap)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Can't add already added bitmap: ");
                sb.append(bitmap);
                sb.append(" [");
                sb.append(bitmap.getWidth());
                sb.append("x");
                sb.append(bitmap.getHeight());
                sb.append("]");
                throw new IllegalStateException(sb.toString());
            }
            this.bitmaps.add(bitmap);
        }
        
        @Override
        public void remove(final Bitmap bitmap) {
            if (!this.bitmaps.contains(bitmap)) {
                throw new IllegalStateException("Cannot remove bitmap not in tracker");
            }
            this.bitmaps.remove(bitmap);
        }
    }
}
