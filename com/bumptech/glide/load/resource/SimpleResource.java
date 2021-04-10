package com.bumptech.glide.load.resource;

import com.bumptech.glide.load.engine.*;

public class SimpleResource<T> implements Resource<T>
{
    protected final T data;
    
    public SimpleResource(final T data) {
        if (data == null) {
            throw new NullPointerException("Data must not be null");
        }
        this.data = data;
    }
    
    @Override
    public final T get() {
        return this.data;
    }
    
    @Override
    public final int getSize() {
        return 1;
    }
    
    @Override
    public void recycle() {
    }
}
