package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.provider.*;
import com.bumptech.glide.load.resource.file.*;
import com.bumptech.glide.load.model.*;
import android.content.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import java.io.*;
import com.bumptech.glide.load.*;

public class GifDrawableLoadProvider implements DataLoadProvider<InputStream, GifDrawable>
{
    private final FileToStreamDecoder<GifDrawable> cacheDecoder;
    private final GifResourceDecoder decoder;
    private final GifResourceEncoder encoder;
    private final StreamEncoder sourceEncoder;
    
    public GifDrawableLoadProvider(final Context context, final BitmapPool bitmapPool) {
        this.decoder = new GifResourceDecoder(context, bitmapPool);
        this.cacheDecoder = new FileToStreamDecoder<GifDrawable>(this.decoder);
        this.encoder = new GifResourceEncoder(bitmapPool);
        this.sourceEncoder = new StreamEncoder();
    }
    
    @Override
    public ResourceDecoder<File, GifDrawable> getCacheDecoder() {
        return this.cacheDecoder;
    }
    
    @Override
    public ResourceEncoder<GifDrawable> getEncoder() {
        return this.encoder;
    }
    
    @Override
    public ResourceDecoder<InputStream, GifDrawable> getSourceDecoder() {
        return this.decoder;
    }
    
    @Override
    public Encoder<InputStream> getSourceEncoder() {
        return this.sourceEncoder;
    }
}
