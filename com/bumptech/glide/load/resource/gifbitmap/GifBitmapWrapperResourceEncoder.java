package com.bumptech.glide.load.resource.gifbitmap;

import com.bumptech.glide.load.*;
import android.graphics.*;
import com.bumptech.glide.load.resource.gif.*;
import com.bumptech.glide.load.engine.*;
import java.io.*;

public class GifBitmapWrapperResourceEncoder implements ResourceEncoder<GifBitmapWrapper>
{
    private final ResourceEncoder<Bitmap> bitmapEncoder;
    private final ResourceEncoder<GifDrawable> gifEncoder;
    private String id;
    
    public GifBitmapWrapperResourceEncoder(final ResourceEncoder<Bitmap> bitmapEncoder, final ResourceEncoder<GifDrawable> gifEncoder) {
        this.bitmapEncoder = bitmapEncoder;
        this.gifEncoder = gifEncoder;
    }
    
    @Override
    public boolean encode(final Resource<GifBitmapWrapper> resource, final OutputStream outputStream) {
        final GifBitmapWrapper gifBitmapWrapper = resource.get();
        final Resource<Bitmap> bitmapResource = gifBitmapWrapper.getBitmapResource();
        if (bitmapResource != null) {
            return this.bitmapEncoder.encode(bitmapResource, outputStream);
        }
        return this.gifEncoder.encode(gifBitmapWrapper.getGifResource(), outputStream);
    }
    
    @Override
    public String getId() {
        if (this.id == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.bitmapEncoder.getId());
            sb.append(this.gifEncoder.getId());
            this.id = sb.toString();
        }
        return this.id;
    }
}
