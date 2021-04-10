package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.engine.bitmap_recycle.*;
import com.bumptech.glide.load.*;
import android.graphics.*;

public interface BitmapDecoder<T>
{
    Bitmap decode(final T p0, final BitmapPool p1, final int p2, final int p3, final DecodeFormat p4) throws Exception;
    
    String getId();
}
