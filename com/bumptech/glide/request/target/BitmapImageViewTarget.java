package com.bumptech.glide.request.target;

import android.graphics.*;
import android.widget.*;

public class BitmapImageViewTarget extends ImageViewTarget<Bitmap>
{
    public BitmapImageViewTarget(final ImageView imageView) {
        super(imageView);
    }
    
    @Override
    protected void setResource(final Bitmap imageBitmap) {
        ((ImageView)this.view).setImageBitmap(imageBitmap);
    }
}
