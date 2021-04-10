package com.bumptech.glide.load.engine.bitmap_recycle;

import android.annotation.*;
import android.graphics.*;
import com.bumptech.glide.util.*;
import java.util.*;

@TargetApi(19)
public class SizeConfigStrategy implements LruPoolStrategy
{
    private static final Bitmap$Config[] ALPHA_8_IN_CONFIGS;
    private static final Bitmap$Config[] ARGB_4444_IN_CONFIGS;
    private static final Bitmap$Config[] ARGB_8888_IN_CONFIGS;
    private static final int MAX_SIZE_MULTIPLE = 8;
    private static final Bitmap$Config[] RGB_565_IN_CONFIGS;
    private final GroupedLinkedMap<Key, Bitmap> groupedMap;
    private final KeyPool keyPool;
    private final Map<Bitmap$Config, NavigableMap<Integer, Integer>> sortedSizes;
    
    static {
        ARGB_8888_IN_CONFIGS = new Bitmap$Config[] { Bitmap$Config.ARGB_8888, null };
        RGB_565_IN_CONFIGS = new Bitmap$Config[] { Bitmap$Config.RGB_565 };
        ARGB_4444_IN_CONFIGS = new Bitmap$Config[] { Bitmap$Config.ARGB_4444 };
        ALPHA_8_IN_CONFIGS = new Bitmap$Config[] { Bitmap$Config.ALPHA_8 };
    }
    
    public SizeConfigStrategy() {
        this.keyPool = new KeyPool();
        this.groupedMap = new GroupedLinkedMap<Key, Bitmap>();
        this.sortedSizes = new HashMap<Bitmap$Config, NavigableMap<Integer, Integer>>();
    }
    
    private void decrementBitmapOfSize(final Integer n, final Bitmap$Config bitmap$Config) {
        final NavigableMap<Integer, Integer> sizesForConfig = this.getSizesForConfig(bitmap$Config);
        final Integer n2 = sizesForConfig.get(n);
        if (n2 == 1) {
            sizesForConfig.remove(n);
            return;
        }
        sizesForConfig.put(n, n2 - 1);
    }
    
    private Key findBestKey(final Key key, final int n, final Bitmap$Config bitmap$Config) {
        final Bitmap$Config[] inConfigs = getInConfigs(bitmap$Config);
        for (int length = inConfigs.length, i = 0; i < length; ++i) {
            final Bitmap$Config bitmap$Config2 = inConfigs[i];
            final Integer n2 = this.getSizesForConfig(bitmap$Config2).ceilingKey(n);
            if (n2 != null && n2 <= n * 8) {
                if (n2 == n) {
                    if (bitmap$Config2 == null) {
                        if (bitmap$Config == null) {
                            break;
                        }
                    }
                    else if (bitmap$Config2.equals((Object)bitmap$Config)) {
                        break;
                    }
                }
                this.keyPool.offer(key);
                return this.keyPool.get(n2, bitmap$Config2);
            }
        }
        return key;
    }
    
    private static String getBitmapString(final int n, final Bitmap$Config bitmap$Config) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(n);
        sb.append("](");
        sb.append(bitmap$Config);
        sb.append(")");
        return sb.toString();
    }
    
    private static Bitmap$Config[] getInConfigs(final Bitmap$Config bitmap$Config) {
        switch (bitmap$Config) {
            default: {
                return new Bitmap$Config[] { bitmap$Config };
            }
            case ALPHA_8: {
                return SizeConfigStrategy.ALPHA_8_IN_CONFIGS;
            }
            case ARGB_4444: {
                return SizeConfigStrategy.ARGB_4444_IN_CONFIGS;
            }
            case RGB_565: {
                return SizeConfigStrategy.RGB_565_IN_CONFIGS;
            }
            case ARGB_8888: {
                return SizeConfigStrategy.ARGB_8888_IN_CONFIGS;
            }
        }
    }
    
    private NavigableMap<Integer, Integer> getSizesForConfig(final Bitmap$Config bitmap$Config) {
        NavigableMap<Integer, Integer> navigableMap;
        if ((navigableMap = this.sortedSizes.get(bitmap$Config)) == null) {
            navigableMap = new TreeMap<Integer, Integer>();
            this.sortedSizes.put(bitmap$Config, navigableMap);
        }
        return navigableMap;
    }
    
    @Override
    public Bitmap get(final int n, final int n2, Bitmap$Config bitmap$Config) {
        final int bitmapByteSize = Util.getBitmapByteSize(n, n2, bitmap$Config);
        final Bitmap bitmap = this.groupedMap.get(this.findBestKey(this.keyPool.get(bitmapByteSize, bitmap$Config), bitmapByteSize, bitmap$Config));
        if (bitmap != null) {
            this.decrementBitmapOfSize(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
            if (bitmap.getConfig() != null) {
                bitmap$Config = bitmap.getConfig();
            }
            else {
                bitmap$Config = Bitmap$Config.ARGB_8888;
            }
            bitmap.reconfigure(n, n2, bitmap$Config);
        }
        return bitmap;
    }
    
    @Override
    public int getSize(final Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }
    
    @Override
    public String logBitmap(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        return getBitmapString(Util.getBitmapByteSize(n, n2, bitmap$Config), bitmap$Config);
    }
    
    @Override
    public String logBitmap(final Bitmap bitmap) {
        return getBitmapString(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
    }
    
    @Override
    public void put(final Bitmap bitmap) {
        final Key value = this.keyPool.get(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
        this.groupedMap.put(value, bitmap);
        final NavigableMap<Integer, Integer> sizesForConfig = this.getSizesForConfig(bitmap.getConfig());
        final Integer n = sizesForConfig.get(value.size);
        final int access$000 = value.size;
        int n2 = 1;
        if (n != null) {
            n2 = 1 + n;
        }
        sizesForConfig.put(access$000, n2);
    }
    
    @Override
    public Bitmap removeLast() {
        final Bitmap bitmap = this.groupedMap.removeLast();
        if (bitmap != null) {
            this.decrementBitmapOfSize(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
        }
        return bitmap;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SizeConfigStrategy{groupedMap=");
        sb.append(this.groupedMap);
        final StringBuilder append = sb.append(", sortedSizes=(");
        for (final Map.Entry<Bitmap$Config, NavigableMap<Integer, Integer>> entry : this.sortedSizes.entrySet()) {
            append.append(entry.getKey());
            append.append('[');
            append.append(entry.getValue());
            append.append("], ");
        }
        if (!this.sortedSizes.isEmpty()) {
            append.replace(append.length() - 2, append.length(), "");
        }
        append.append(")}");
        return append.toString();
    }
    
    static final class Key implements Poolable
    {
        private Bitmap$Config config;
        private final KeyPool pool;
        private int size;
        
        public Key(final KeyPool pool) {
            this.pool = pool;
        }
        
        Key(final KeyPool keyPool, final int n, final Bitmap$Config bitmap$Config) {
            this(keyPool);
            this.init(n, bitmap$Config);
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof Key;
            final boolean b2 = false;
            if (b) {
                final Key key = (Key)o;
                boolean b3 = b2;
                if (this.size == key.size) {
                    if (this.config == null) {
                        b3 = b2;
                        if (key.config != null) {
                            return b3;
                        }
                    }
                    else {
                        b3 = b2;
                        if (!this.config.equals((Object)key.config)) {
                            return b3;
                        }
                    }
                    b3 = true;
                }
                return b3;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            final int size = this.size;
            int hashCode;
            if (this.config != null) {
                hashCode = this.config.hashCode();
            }
            else {
                hashCode = 0;
            }
            return size * 31 + hashCode;
        }
        
        public void init(final int size, final Bitmap$Config config) {
            this.size = size;
            this.config = config;
        }
        
        @Override
        public void offer() {
            this.pool.offer(this);
        }
        
        @Override
        public String toString() {
            return getBitmapString(this.size, this.config);
        }
    }
    
    static class KeyPool extends BaseKeyPool<Key>
    {
        @Override
        protected Key create() {
            return new Key(this);
        }
        
        public Key get(final int n, final Bitmap$Config bitmap$Config) {
            final Key key = this.get();
            key.init(n, bitmap$Config);
            return key;
        }
    }
}
