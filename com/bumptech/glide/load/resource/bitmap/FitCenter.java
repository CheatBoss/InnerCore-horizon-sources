package com.bumptech.glide.load.resource.bitmap;

import android.content.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import android.graphics.*;

public class FitCenter extends BitmapTransformation
{
    public FitCenter(final Context context) {
        super(context);
    }
    
    public FitCenter(final BitmapPool bitmapPool) {
        super(bitmapPool);
    }
    
    @Override
    public String getId() {
        return "FitCenter.com.bumptech.glide.load.resource.bitmap";
    }
    
    @Override
    protected Bitmap transform(final BitmapPool bitmapPool, final Bitmap bitmap, final int n, final int n2) {
        return TransformationUtils.fitCenter(bitmap, bitmapPool, n, n2);
    }
}
