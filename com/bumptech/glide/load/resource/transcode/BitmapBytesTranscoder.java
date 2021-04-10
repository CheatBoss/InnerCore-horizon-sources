package com.bumptech.glide.load.resource.transcode;

import android.graphics.*;
import com.bumptech.glide.load.engine.*;
import java.io.*;
import com.bumptech.glide.load.resource.bytes.*;

public class BitmapBytesTranscoder implements ResourceTranscoder<Bitmap, byte[]>
{
    private final Bitmap$CompressFormat compressFormat;
    private final int quality;
    
    public BitmapBytesTranscoder() {
        this(Bitmap$CompressFormat.JPEG, 100);
    }
    
    public BitmapBytesTranscoder(final Bitmap$CompressFormat compressFormat, final int quality) {
        this.compressFormat = compressFormat;
        this.quality = quality;
    }
    
    @Override
    public String getId() {
        return "BitmapBytesTranscoder.com.bumptech.glide.load.resource.transcode";
    }
    
    @Override
    public Resource<byte[]> transcode(final Resource<Bitmap> resource) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        resource.get().compress(this.compressFormat, this.quality, (OutputStream)byteArrayOutputStream);
        resource.recycle();
        return new BytesResource(byteArrayOutputStream.toByteArray());
    }
}
