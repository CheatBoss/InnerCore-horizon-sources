package com.bumptech.glide.load.resource.gifbitmap;

import com.bumptech.glide.load.engine.*;
import android.graphics.*;
import com.bumptech.glide.load.resource.gif.*;

public class GifBitmapWrapperResource implements Resource<GifBitmapWrapper>
{
    private final GifBitmapWrapper data;
    
    public GifBitmapWrapperResource(final GifBitmapWrapper data) {
        if (data == null) {
            throw new NullPointerException("Data must not be null");
        }
        this.data = data;
    }
    
    @Override
    public GifBitmapWrapper get() {
        return this.data;
    }
    
    @Override
    public int getSize() {
        return this.data.getSize();
    }
    
    @Override
    public void recycle() {
        final Resource<Bitmap> bitmapResource = this.data.getBitmapResource();
        if (bitmapResource != null) {
            bitmapResource.recycle();
        }
        final Resource<GifDrawable> gifResource = this.data.getGifResource();
        if (gifResource != null) {
            gifResource.recycle();
        }
    }
}
