package com.bumptech.glide.request.target;

import android.graphics.drawable.*;
import android.widget.*;

public class DrawableImageViewTarget extends ImageViewTarget<Drawable>
{
    public DrawableImageViewTarget(final ImageView imageView) {
        super(imageView);
    }
    
    @Override
    protected void setResource(final Drawable imageDrawable) {
        ((ImageView)this.view).setImageDrawable(imageDrawable);
    }
}
