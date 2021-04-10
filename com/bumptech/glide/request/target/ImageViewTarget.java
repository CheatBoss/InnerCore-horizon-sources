package com.bumptech.glide.request.target;

import android.widget.*;
import com.bumptech.glide.request.animation.*;
import android.view.*;
import android.graphics.drawable.*;

public abstract class ImageViewTarget<Z> extends ViewTarget<ImageView, Z> implements ViewAdapter
{
    public ImageViewTarget(final ImageView imageView) {
        super((View)imageView);
    }
    
    @Override
    public Drawable getCurrentDrawable() {
        return ((ImageView)this.view).getDrawable();
    }
    
    @Override
    public void onLoadCleared(final Drawable imageDrawable) {
        ((ImageView)this.view).setImageDrawable(imageDrawable);
    }
    
    @Override
    public void onLoadFailed(final Exception ex, final Drawable imageDrawable) {
        ((ImageView)this.view).setImageDrawable(imageDrawable);
    }
    
    @Override
    public void onLoadStarted(final Drawable imageDrawable) {
        ((ImageView)this.view).setImageDrawable(imageDrawable);
    }
    
    @Override
    public void onResourceReady(final Z resource, final GlideAnimation<? super Z> glideAnimation) {
        if (glideAnimation == null || !glideAnimation.animate(resource, (GlideAnimation.ViewAdapter)this)) {
            this.setResource(resource);
        }
    }
    
    @Override
    public void setDrawable(final Drawable imageDrawable) {
        ((ImageView)this.view).setImageDrawable(imageDrawable);
    }
    
    protected abstract void setResource(final Z p0);
}
