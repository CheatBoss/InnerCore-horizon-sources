package com.bumptech.glide.load.engine.bitmap_recycle;

import android.annotation.*;
import java.util.*;
import com.bumptech.glide.util.*;
import android.graphics.*;

@TargetApi(19)
class SizeStrategy implements LruPoolStrategy
{
    private static final int MAX_SIZE_MULTIPLE = 8;
    private final GroupedLinkedMap<Key, Bitmap> groupedMap;
    private final KeyPool keyPool;
    private final TreeMap<Integer, Integer> sortedSizes;
    
    SizeStrategy() {
        this.keyPool = new KeyPool();
        this.groupedMap = new GroupedLinkedMap<Key, Bitmap>();
        this.sortedSizes = new PrettyPrintTreeMap<Integer, Integer>();
    }
    
    private void decrementBitmapOfSize(final Integer n) {
        final Integer n2 = this.sortedSizes.get(n);
        if (n2 == 1) {
            this.sortedSizes.remove(n);
            return;
        }
        this.sortedSizes.put(n, n2 - 1);
    }
    
    private static String getBitmapString(final int n) {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(n);
        sb.append("]");
        return sb.toString();
    }
    
    private static String getBitmapString(final Bitmap bitmap) {
        return getBitmapString(Util.getBitmapByteSize(bitmap));
    }
    
    @Override
    public Bitmap get(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        final int bitmapByteSize = Util.getBitmapByteSize(n, n2, bitmap$Config);
        final Key value = this.keyPool.get(bitmapByteSize);
        final Integer n3 = this.sortedSizes.ceilingKey(bitmapByteSize);
        Poolable value2 = value;
        if (n3 != null) {
            value2 = value;
            if (n3 != bitmapByteSize) {
                value2 = value;
                if (n3 <= bitmapByteSize * 8) {
                    this.keyPool.offer(value);
                    value2 = this.keyPool.get(n3);
                }
            }
        }
        final Bitmap bitmap = this.groupedMap.get((Key)value2);
        if (bitmap != null) {
            bitmap.reconfigure(n, n2, bitmap$Config);
            this.decrementBitmapOfSize(n3);
        }
        return bitmap;
    }
    
    @Override
    public int getSize(final Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }
    
    @Override
    public String logBitmap(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        return getBitmapString(Util.getBitmapByteSize(n, n2, bitmap$Config));
    }
    
    @Override
    public String logBitmap(final Bitmap bitmap) {
        return getBitmapString(bitmap);
    }
    
    @Override
    public void put(final Bitmap bitmap) {
        final Key value = this.keyPool.get(Util.getBitmapByteSize(bitmap));
        this.groupedMap.put(value, bitmap);
        final Integer n = this.sortedSizes.get(value.size);
        final TreeMap<Integer, Integer> sortedSizes = this.sortedSizes;
        final int access$000 = value.size;
        int n2 = 1;
        if (n != null) {
            n2 = 1 + n;
        }
        sortedSizes.put(access$000, n2);
    }
    
    @Override
    public Bitmap removeLast() {
        final Bitmap bitmap = this.groupedMap.removeLast();
        if (bitmap != null) {
            this.decrementBitmapOfSize(Util.getBitmapByteSize(bitmap));
        }
        return bitmap;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SizeStrategy:\n  ");
        sb.append(this.groupedMap);
        sb.append("\n");
        sb.append("  SortedSizes");
        sb.append(this.sortedSizes);
        return sb.toString();
    }
    
    static final class Key implements Poolable
    {
        private final KeyPool pool;
        private int size;
        
        Key(final KeyPool pool) {
            this.pool = pool;
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof Key;
            boolean b2 = false;
            if (b) {
                if (this.size == ((Key)o).size) {
                    b2 = true;
                }
                return b2;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.size;
        }
        
        public void init(final int size) {
            this.size = size;
        }
        
        @Override
        public void offer() {
            this.pool.offer(this);
        }
        
        @Override
        public String toString() {
            return getBitmapString(this.size);
        }
    }
    
    static class KeyPool extends BaseKeyPool<Key>
    {
        @Override
        protected Key create() {
            return new Key(this);
        }
        
        public Key get(final int n) {
            final Key key = this.get();
            key.init(n);
            return key;
        }
    }
}
