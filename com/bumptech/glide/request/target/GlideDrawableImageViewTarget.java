package com.bumptech.glide.request.target;

import com.bumptech.glide.load.resource.drawable.*;
import android.widget.*;
import com.bumptech.glide.request.animation.*;
import android.graphics.drawable.*;

public class GlideDrawableImageViewTarget extends ImageViewTarget<GlideDrawable>
{
    private static final float SQUARE_RATIO_MARGIN = 0.05f;
    private int maxLoopCount;
    private GlideDrawable resource;
    
    public GlideDrawableImageViewTarget(final ImageView imageView) {
        this(imageView, -1);
    }
    
    public GlideDrawableImageViewTarget(final ImageView imageView, final int maxLoopCount) {
        super(imageView);
        this.maxLoopCount = maxLoopCount;
    }
    
    @Override
    public void onResourceReady(final GlideDrawable glideDrawable, final GlideAnimation<? super GlideDrawable> glideAnimation) {
        GlideDrawable resource = glideDrawable;
        if (!glideDrawable.isAnimated()) {
            final float n = ((ImageView)this.view).getWidth() / (float)((ImageView)this.view).getHeight();
            final float n2 = glideDrawable.getIntrinsicWidth() / (float)glideDrawable.getIntrinsicHeight();
            resource = glideDrawable;
            if (Math.abs(n - 1.0f) <= 0.05f) {
                resource = glideDrawable;
                if (Math.abs(n2 - 1.0f) <= 0.05f) {
                    resource = new SquaringDrawable(glideDrawable, ((ImageView)this.view).getWidth());
                }
            }
        }
        super.onResourceReady(resource, glideAnimation);
        (this.resource = resource).setLoopCount(this.maxLoopCount);
        resource.start();
    }
    
    @Override
    public void onStart() {
        if (this.resource != null) {
            this.resource.start();
        }
    }
    
    @Override
    public void onStop() {
        if (this.resource != null) {
            this.resource.stop();
        }
    }
    
    @Override
    protected void setResource(final GlideDrawable imageDrawable) {
        ((ImageView)this.view).setImageDrawable((Drawable)imageDrawable);
    }
}
