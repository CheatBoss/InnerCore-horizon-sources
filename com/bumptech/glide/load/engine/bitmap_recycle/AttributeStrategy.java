package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.*;
import com.bumptech.glide.util.*;

class AttributeStrategy implements LruPoolStrategy
{
    private final GroupedLinkedMap<Key, Bitmap> groupedMap;
    private final KeyPool keyPool;
    
    AttributeStrategy() {
        this.keyPool = new KeyPool();
        this.groupedMap = new GroupedLinkedMap<Key, Bitmap>();
    }
    
    private static String getBitmapString(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(n);
        sb.append("x");
        sb.append(n2);
        sb.append("], ");
        sb.append(bitmap$Config);
        return sb.toString();
    }
    
    private static String getBitmapString(final Bitmap bitmap) {
        return getBitmapString(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
    }
    
    @Override
    public Bitmap get(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        return this.groupedMap.get(this.keyPool.get(n, n2, bitmap$Config));
    }
    
    @Override
    public int getSize(final Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }
    
    @Override
    public String logBitmap(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        return getBitmapString(n, n2, bitmap$Config);
    }
    
    @Override
    public String logBitmap(final Bitmap bitmap) {
        return getBitmapString(bitmap);
    }
    
    @Override
    public void put(final Bitmap bitmap) {
        this.groupedMap.put(this.keyPool.get(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig()), bitmap);
    }
    
    @Override
    public Bitmap removeLast() {
        return this.groupedMap.removeLast();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AttributeStrategy:\n  ");
        sb.append(this.groupedMap);
        return sb.toString();
    }
    
    static class Key implements Poolable
    {
        private Bitmap$Config config;
        private int height;
        private final KeyPool pool;
        private int width;
        
        public Key(final KeyPool pool) {
            this.pool = pool;
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof Key;
            final boolean b2 = false;
            if (b) {
                final Key key = (Key)o;
                boolean b3 = b2;
                if (this.width == key.width) {
                    b3 = b2;
                    if (this.height == key.height) {
                        b3 = b2;
                        if (this.config == key.config) {
                            b3 = true;
                        }
                    }
                }
                return b3;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            final int width = this.width;
            final int height = this.height;
            int hashCode;
            if (this.config != null) {
                hashCode = this.config.hashCode();
            }
            else {
                hashCode = 0;
            }
            return (width * 31 + height) * 31 + hashCode;
        }
        
        public void init(final int width, final int height, final Bitmap$Config config) {
            this.width = width;
            this.height = height;
            this.config = config;
        }
        
        @Override
        public void offer() {
            this.pool.offer(this);
        }
        
        @Override
        public String toString() {
            return getBitmapString(this.width, this.height, this.config);
        }
    }
    
    static class KeyPool extends BaseKeyPool<Key>
    {
        @Override
        protected Key create() {
            return new Key(this);
        }
        
        public Key get(final int n, final int n2, final Bitmap$Config bitmap$Config) {
            final Key key = this.get();
            key.init(n, n2, bitmap$Config);
            return key;
        }
    }
}
