package com.bumptech.glide.load.resource.gifbitmap;

import com.bumptech.glide.load.*;
import android.graphics.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import com.bumptech.glide.load.resource.gif.*;
import com.bumptech.glide.load.engine.*;

public class GifBitmapWrapperTransformation implements Transformation<GifBitmapWrapper>
{
    private final Transformation<Bitmap> bitmapTransformation;
    private final Transformation<GifDrawable> gifDataTransformation;
    
    GifBitmapWrapperTransformation(final Transformation<Bitmap> bitmapTransformation, final Transformation<GifDrawable> gifDataTransformation) {
        this.bitmapTransformation = bitmapTransformation;
        this.gifDataTransformation = gifDataTransformation;
    }
    
    public GifBitmapWrapperTransformation(final BitmapPool bitmapPool, final Transformation<Bitmap> transformation) {
        this(transformation, new GifDrawableTransformation(transformation, bitmapPool));
    }
    
    @Override
    public String getId() {
        return this.bitmapTransformation.getId();
    }
    
    @Override
    public Resource<GifBitmapWrapper> transform(final Resource<GifBitmapWrapper> resource, final int n, final int n2) {
        final Resource<Bitmap> bitmapResource = resource.get().getBitmapResource();
        final Resource<GifDrawable> gifResource = resource.get().getGifResource();
        if (bitmapResource == null || this.bitmapTransformation == null) {
            if (gifResource != null && this.gifDataTransformation != null) {
                final Resource<GifDrawable> transform = this.gifDataTransformation.transform(gifResource, n, n2);
                if (!gifResource.equals(transform)) {
                    return new GifBitmapWrapperResource(new GifBitmapWrapper(resource.get().getBitmapResource(), transform));
                }
            }
            return resource;
        }
        final Resource<Bitmap> transform2 = this.bitmapTransformation.transform(bitmapResource, n, n2);
        if (!bitmapResource.equals(transform2)) {
            return new GifBitmapWrapperResource(new GifBitmapWrapper(transform2, resource.get().getGifResource()));
        }
        return resource;
    }
}
