package com.bumptech.glide.load.resource.transcode;

import android.graphics.*;
import com.bumptech.glide.load.resource.drawable.*;
import android.content.*;
import com.bumptech.glide.load.engine.*;

public class BitmapToGlideDrawableTranscoder implements ResourceTranscoder<Bitmap, GlideDrawable>
{
    private final GlideBitmapDrawableTranscoder glideBitmapDrawableTranscoder;
    
    public BitmapToGlideDrawableTranscoder(final Context context) {
        this(new GlideBitmapDrawableTranscoder(context));
    }
    
    public BitmapToGlideDrawableTranscoder(final GlideBitmapDrawableTranscoder glideBitmapDrawableTranscoder) {
        this.glideBitmapDrawableTranscoder = glideBitmapDrawableTranscoder;
    }
    
    @Override
    public String getId() {
        return this.glideBitmapDrawableTranscoder.getId();
    }
    
    @Override
    public Resource<GlideDrawable> transcode(final Resource<Bitmap> resource) {
        return (Resource<GlideDrawable>)this.glideBitmapDrawableTranscoder.transcode(resource);
    }
}
