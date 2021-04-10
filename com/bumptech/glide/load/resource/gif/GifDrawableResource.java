package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.load.resource.drawable.*;
import com.bumptech.glide.util.*;

public class GifDrawableResource extends DrawableResource<GifDrawable>
{
    public GifDrawableResource(final GifDrawable gifDrawable) {
        super(gifDrawable);
    }
    
    @Override
    public int getSize() {
        return ((GifDrawable)this.drawable).getData().length + Util.getBitmapByteSize(((GifDrawable)this.drawable).getFirstFrame());
    }
    
    @Override
    public void recycle() {
        ((GifDrawable)this.drawable).stop();
        ((GifDrawable)this.drawable).recycle();
    }
}
