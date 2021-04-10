package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.resource.gifbitmap.*;
import com.bumptech.glide.load.resource.drawable.*;
import android.graphics.*;
import com.bumptech.glide.load.resource.bitmap.*;
import com.bumptech.glide.load.engine.*;

public class GifBitmapWrapperDrawableTranscoder implements ResourceTranscoder<GifBitmapWrapper, GlideDrawable>
{
    private final ResourceTranscoder<Bitmap, GlideBitmapDrawable> bitmapDrawableResourceTranscoder;
    
    public GifBitmapWrapperDrawableTranscoder(final ResourceTranscoder<Bitmap, GlideBitmapDrawable> bitmapDrawableResourceTranscoder) {
        this.bitmapDrawableResourceTranscoder = bitmapDrawableResourceTranscoder;
    }
    
    @Override
    public String getId() {
        return "GifBitmapWrapperDrawableTranscoder.com.bumptech.glide.load.resource.transcode";
    }
    
    @Override
    public Resource<GlideDrawable> transcode(final Resource<GifBitmapWrapper> resource) {
        final GifBitmapWrapper gifBitmapWrapper = resource.get();
        final Resource<Bitmap> bitmapResource = gifBitmapWrapper.getBitmapResource();
        if (bitmapResource != null) {
            return (Resource<GlideDrawable>)this.bitmapDrawableResourceTranscoder.transcode(bitmapResource);
        }
        return (Resource<GlideDrawable>)gifBitmapWrapper.getGifResource();
    }
}
