package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.*;
import com.bumptech.glide.load.engine.*;

public class MemoryCacheAdapter implements MemoryCache
{
    private ResourceRemovedListener listener;
    
    @Override
    public void clearMemory() {
    }
    
    @Override
    public int getCurrentSize() {
        return 0;
    }
    
    @Override
    public int getMaxSize() {
        return 0;
    }
    
    @Override
    public Resource<?> put(final Key key, final Resource<?> resource) {
        this.listener.onResourceRemoved(resource);
        return null;
    }
    
    @Override
    public Resource<?> remove(final Key key) {
        return null;
    }
    
    @Override
    public void setResourceRemovedListener(final ResourceRemovedListener listener) {
        this.listener = listener;
    }
    
    @Override
    public void setSizeMultiplier(final float n) {
    }
    
    @Override
    public void trimMemory(final int n) {
    }
}
