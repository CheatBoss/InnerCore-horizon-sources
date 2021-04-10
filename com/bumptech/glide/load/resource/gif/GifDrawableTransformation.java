package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.load.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import android.graphics.*;
import com.bumptech.glide.load.engine.*;
import com.bumptech.glide.load.resource.bitmap.*;

public class GifDrawableTransformation implements Transformation<GifDrawable>
{
    private final BitmapPool bitmapPool;
    private final Transformation<Bitmap> wrapped;
    
    public GifDrawableTransformation(final Transformation<Bitmap> wrapped, final BitmapPool bitmapPool) {
        this.wrapped = wrapped;
        this.bitmapPool = bitmapPool;
    }
    
    @Override
    public String getId() {
        return this.wrapped.getId();
    }
    
    @Override
    public Resource<GifDrawable> transform(final Resource<GifDrawable> resource, final int n, final int n2) {
        final GifDrawable gifDrawable = resource.get();
        final BitmapResource bitmapResource = new BitmapResource(resource.get().getFirstFrame(), this.bitmapPool);
        final Resource<Bitmap> transform = this.wrapped.transform(bitmapResource, n, n2);
        if (!bitmapResource.equals(transform)) {
            bitmapResource.recycle();
        }
        gifDrawable.setFrameTransformation(this.wrapped, transform.get());
        return resource;
    }
}
