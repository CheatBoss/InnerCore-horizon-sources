package com.bumptech.glide.load.resource.bitmap;

import android.os.*;
import android.graphics.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import com.bumptech.glide.load.*;
import android.content.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.engine.*;
import java.io.*;

public class FileDescriptorBitmapDecoder implements ResourceDecoder<ParcelFileDescriptor, Bitmap>
{
    private final VideoBitmapDecoder bitmapDecoder;
    private final BitmapPool bitmapPool;
    private DecodeFormat decodeFormat;
    
    public FileDescriptorBitmapDecoder(final Context context) {
        this(Glide.get(context).getBitmapPool(), DecodeFormat.DEFAULT);
    }
    
    public FileDescriptorBitmapDecoder(final Context context, final DecodeFormat decodeFormat) {
        this(Glide.get(context).getBitmapPool(), decodeFormat);
    }
    
    public FileDescriptorBitmapDecoder(final BitmapPool bitmapPool, final DecodeFormat decodeFormat) {
        this(new VideoBitmapDecoder(), bitmapPool, decodeFormat);
    }
    
    public FileDescriptorBitmapDecoder(final VideoBitmapDecoder bitmapDecoder, final BitmapPool bitmapPool, final DecodeFormat decodeFormat) {
        this.bitmapDecoder = bitmapDecoder;
        this.bitmapPool = bitmapPool;
        this.decodeFormat = decodeFormat;
    }
    
    @Override
    public Resource<Bitmap> decode(final ParcelFileDescriptor parcelFileDescriptor, final int n, final int n2) throws IOException {
        return BitmapResource.obtain(this.bitmapDecoder.decode(parcelFileDescriptor, this.bitmapPool, n, n2, this.decodeFormat), this.bitmapPool);
    }
    
    @Override
    public String getId() {
        return "FileDescriptorBitmapDecoder.com.bumptech.glide.load.data.bitmap";
    }
}
