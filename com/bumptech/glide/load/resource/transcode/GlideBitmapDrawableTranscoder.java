package com.bumptech.glide.load.resource.transcode;

import android.graphics.*;
import com.bumptech.glide.load.engine.bitmap_recycle.*;
import android.content.res.*;
import android.content.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.engine.*;
import com.bumptech.glide.load.resource.bitmap.*;

public class GlideBitmapDrawableTranscoder implements ResourceTranscoder<Bitmap, GlideBitmapDrawable>
{
    private final BitmapPool bitmapPool;
    private final Resources resources;
    
    public GlideBitmapDrawableTranscoder(final Context context) {
        this(context.getResources(), Glide.get(context).getBitmapPool());
    }
    
    public GlideBitmapDrawableTranscoder(final Resources resources, final BitmapPool bitmapPool) {
        this.resources = resources;
        this.bitmapPool = bitmapPool;
    }
    
    @Override
    public String getId() {
        return "GlideBitmapDrawableTranscoder.com.bumptech.glide.load.resource.transcode";
    }
    
    @Override
    public Resource<GlideBitmapDrawable> transcode(final Resource<Bitmap> resource) {
        return new GlideBitmapDrawableResource(new GlideBitmapDrawable(this.resources, resource.get()), this.bitmapPool);
    }
}
