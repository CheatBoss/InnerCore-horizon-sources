package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.provider.*;
import android.graphics.*;
import com.bumptech.glide.load.resource.file.*;
import com.bumptech.glide.load.model.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import java.io.*;
import com.bumptech.glide.load.*;

public class StreamBitmapDataLoadProvider implements DataLoadProvider<InputStream, Bitmap>
{
    private final FileToStreamDecoder<Bitmap> cacheDecoder;
    private final StreamBitmapDecoder decoder;
    private final BitmapEncoder encoder;
    private final StreamEncoder sourceEncoder;
    
    public StreamBitmapDataLoadProvider(final BitmapPool bitmapPool, final DecodeFormat decodeFormat) {
        this.sourceEncoder = new StreamEncoder();
        this.decoder = new StreamBitmapDecoder(bitmapPool, decodeFormat);
        this.encoder = new BitmapEncoder();
        this.cacheDecoder = new FileToStreamDecoder<Bitmap>(this.decoder);
    }
    
    @Override
    public ResourceDecoder<File, Bitmap> getCacheDecoder() {
        return this.cacheDecoder;
    }
    
    @Override
    public ResourceEncoder<Bitmap> getEncoder() {
        return this.encoder;
    }
    
    @Override
    public ResourceDecoder<InputStream, Bitmap> getSourceDecoder() {
        return this.decoder;
    }
    
    @Override
    public Encoder<InputStream> getSourceEncoder() {
        return this.sourceEncoder;
    }
}
