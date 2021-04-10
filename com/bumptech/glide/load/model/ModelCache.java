package com.bumptech.glide.load.model;

import java.util.*;
import com.bumptech.glide.util.*;

public class ModelCache<A, B>
{
    private static final int DEFAULT_SIZE = 250;
    private final LruCache<ModelKey<A>, B> cache;
    
    public ModelCache() {
        this(250);
    }
    
    public ModelCache(final int n) {
        this.cache = new LruCache<ModelKey<A>, B>(n) {
            @Override
            protected void onItemEvicted(final ModelKey<A> modelKey, final B b) {
                modelKey.release();
            }
        };
    }
    
    public B get(final A a, final int n, final int n2) {
        final ModelKey<A> value = ModelKey.get(a, n, n2);
        final B value2 = this.cache.get(value);
        value.release();
        return value2;
    }
    
    public void put(final A a, final int n, final int n2, final B b) {
        this.cache.put(ModelKey.get(a, n, n2), b);
    }
    
    static final class ModelKey<A>
    {
        private static final Queue<ModelKey<?>> KEY_QUEUE;
        private int height;
        private A model;
        private int width;
        
        static {
            KEY_QUEUE = Util.createQueue(0);
        }
        
        private ModelKey() {
        }
        
        static <A> ModelKey<A> get(final A a, final int n, final int n2) {
            ModelKey<?> modelKey;
            if ((modelKey = ModelKey.KEY_QUEUE.poll()) == null) {
                modelKey = new ModelKey<A>();
            }
            modelKey.init(a, n, n2);
            return (ModelKey<A>)modelKey;
        }
        
        private void init(final A model, final int width, final int height) {
            this.model = model;
            this.width = width;
            this.height = height;
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof ModelKey;
            final boolean b2 = false;
            if (b) {
                final ModelKey modelKey = (ModelKey)o;
                boolean b3 = b2;
                if (this.width == modelKey.width) {
                    b3 = b2;
                    if (this.height == modelKey.height) {
                        b3 = b2;
                        if (this.model.equals(modelKey.model)) {
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
            return (this.height * 31 + this.width) * 31 + this.model.hashCode();
        }
        
        public void release() {
            ModelKey.KEY_QUEUE.offer(this);
        }
    }
}
