package com.bumptech.glide.load.engine.bitmap_recycle;

import java.util.*;
import com.bumptech.glide.util.*;

abstract class BaseKeyPool<T extends Poolable>
{
    private static final int MAX_SIZE = 20;
    private final Queue<T> keyPool;
    
    BaseKeyPool() {
        this.keyPool = Util.createQueue(20);
    }
    
    protected abstract T create();
    
    protected T get() {
        Poolable create;
        if ((create = this.keyPool.poll()) == null) {
            create = this.create();
        }
        return (T)create;
    }
    
    public void offer(final T t) {
        if (this.keyPool.size() < 20) {
            this.keyPool.offer(t);
        }
    }
}
