package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.*;
import android.graphics.*;
import com.bumptech.glide.load.engine.*;
import java.io.*;
import android.util.*;
import com.bumptech.glide.util.*;

public class BitmapEncoder implements ResourceEncoder<Bitmap>
{
    private static final int DEFAULT_COMPRESSION_QUALITY = 90;
    private static final String TAG = "BitmapEncoder";
    private Bitmap$CompressFormat compressFormat;
    private int quality;
    
    public BitmapEncoder() {
        this(null, 90);
    }
    
    public BitmapEncoder(final Bitmap$CompressFormat compressFormat, final int quality) {
        this.compressFormat = compressFormat;
        this.quality = quality;
    }
    
    private Bitmap$CompressFormat getFormat(final Bitmap bitmap) {
        if (this.compressFormat != null) {
            return this.compressFormat;
        }
        if (bitmap.hasAlpha()) {
            return Bitmap$CompressFormat.PNG;
        }
        return Bitmap$CompressFormat.JPEG;
    }
    
    @Override
    public boolean encode(final Resource<Bitmap> resource, final OutputStream outputStream) {
        final Bitmap bitmap = resource.get();
        final long logTime = LogTime.getLogTime();
        final Bitmap$CompressFormat format = this.getFormat(bitmap);
        bitmap.compress(format, this.quality, outputStream);
        if (Log.isLoggable("BitmapEncoder", 2)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Compressed with type: ");
            sb.append(format);
            sb.append(" of size ");
            sb.append(Util.getBitmapByteSize(bitmap));
            sb.append(" in ");
            sb.append(LogTime.getElapsedMillis(logTime));
            Log.v("BitmapEncoder", sb.toString());
        }
        return true;
    }
    
    @Override
    public String getId() {
        return "BitmapEncoder.com.bumptech.glide.load.resource.bitmap";
    }
}
