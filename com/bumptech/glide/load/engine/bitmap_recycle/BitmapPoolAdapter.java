package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.*;

public class BitmapPoolAdapter implements BitmapPool
{
    @Override
    public void clearMemory() {
    }
    
    @Override
    public Bitmap get(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        return null;
    }
    
    @Override
    public Bitmap getDirty(final int n, final int n2, final Bitmap$Config bitmap$Config) {
        return null;
    }
    
    @Override
    public int getMaxSize() {
        return 0;
    }
    
    @Override
    public boolean put(final Bitmap bitmap) {
        return false;
    }
    
    @Override
    public void setSizeMultiplier(final float n) {
    }
    
    @Override
    public void trimMemory(final int n) {
    }
}
