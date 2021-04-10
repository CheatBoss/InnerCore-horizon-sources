package com.bumptech.glide.load.resource.bitmap;

import android.content.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import android.graphics.*;

public class CenterCrop extends BitmapTransformation
{
    public CenterCrop(final Context context) {
        super(context);
    }
    
    public CenterCrop(final BitmapPool bitmapPool) {
        super(bitmapPool);
    }
    
    @Override
    public String getId() {
        return "CenterCrop.com.bumptech.glide.load.resource.bitmap";
    }
    
    @Override
    protected Bitmap transform(final BitmapPool bitmapPool, Bitmap centerCrop, final int n, final int n2) {
        Bitmap$Config bitmap$Config;
        if (centerCrop.getConfig() != null) {
            bitmap$Config = centerCrop.getConfig();
        }
        else {
            bitmap$Config = Bitmap$Config.ARGB_8888;
        }
        final Bitmap value = bitmapPool.get(n, n2, bitmap$Config);
        centerCrop = TransformationUtils.centerCrop(value, centerCrop, n, n2);
        if (value != null && value != centerCrop && !bitmapPool.put(value)) {
            value.recycle();
        }
        return centerCrop;
    }
}
