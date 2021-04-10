package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.provider.*;
import android.os.*;
import android.graphics.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import com.bumptech.glide.load.resource.file.*;
import java.io.*;
import com.bumptech.glide.load.resource.*;
import com.bumptech.glide.load.*;

public class FileDescriptorBitmapDataLoadProvider implements DataLoadProvider<ParcelFileDescriptor, Bitmap>
{
    private final ResourceDecoder<File, Bitmap> cacheDecoder;
    private final BitmapEncoder encoder;
    private final FileDescriptorBitmapDecoder sourceDecoder;
    private final Encoder<ParcelFileDescriptor> sourceEncoder;
    
    public FileDescriptorBitmapDataLoadProvider(final BitmapPool bitmapPool, final DecodeFormat decodeFormat) {
        this.cacheDecoder = new FileToStreamDecoder<Bitmap>(new StreamBitmapDecoder(bitmapPool, decodeFormat));
        this.sourceDecoder = new FileDescriptorBitmapDecoder(bitmapPool, decodeFormat);
        this.encoder = new BitmapEncoder();
        this.sourceEncoder = NullEncoder.get();
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
    public ResourceDecoder<ParcelFileDescriptor, Bitmap> getSourceDecoder() {
        return this.sourceDecoder;
    }
    
    @Override
    public Encoder<ParcelFileDescriptor> getSourceEncoder() {
        return this.sourceEncoder;
    }
}
