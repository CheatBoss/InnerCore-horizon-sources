package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.resource.drawable.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import android.graphics.drawable.*;
import com.bumptech.glide.util.*;

public class BitmapDrawableResource extends DrawableResource<BitmapDrawable>
{
    private final BitmapPool bitmapPool;
    
    public BitmapDrawableResource(final BitmapDrawable bitmapDrawable, final BitmapPool bitmapPool) {
        super((Drawable)bitmapDrawable);
        this.bitmapPool = bitmapPool;
    }
    
    @Override
    public int getSize() {
        return Util.getBitmapByteSize(((BitmapDrawable)this.drawable).getBitmap());
    }
    
    @Override
    public void recycle() {
        this.bitmapPool.put(((BitmapDrawable)this.drawable).getBitmap());
    }
}
