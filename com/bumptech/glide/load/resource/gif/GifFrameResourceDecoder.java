package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.load.*;
import com.bumptech.glide.gifdecoder.*;
import android.graphics.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import com.bumptech.glide.load.engine.*;
import com.bumptech.glide.load.resource.bitmap.*;
import java.io.*;

class GifFrameResourceDecoder implements ResourceDecoder<GifDecoder, Bitmap>
{
    private final BitmapPool bitmapPool;
    
    public GifFrameResourceDecoder(final BitmapPool bitmapPool) {
        this.bitmapPool = bitmapPool;
    }
    
    @Override
    public Resource<Bitmap> decode(final GifDecoder gifDecoder, final int n, final int n2) {
        return BitmapResource.obtain(gifDecoder.getNextFrame(), this.bitmapPool);
    }
    
    @Override
    public String getId() {
        return "GifFrameResourceDecoder.com.bumptech.glide.load.resource.gif";
    }
}
