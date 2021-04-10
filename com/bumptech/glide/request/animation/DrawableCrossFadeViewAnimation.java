package com.bumptech.glide.request.animation;

import android.graphics.drawable.*;

public class DrawableCrossFadeViewAnimation<T extends Drawable> implements GlideAnimation<T>
{
    private final GlideAnimation<T> defaultAnimation;
    private final int duration;
    
    public DrawableCrossFadeViewAnimation(final GlideAnimation<T> defaultAnimation, final int duration) {
        this.defaultAnimation = defaultAnimation;
        this.duration = duration;
    }
    
    @Override
    public boolean animate(final T t, final ViewAdapter viewAdapter) {
        final Drawable currentDrawable = viewAdapter.getCurrentDrawable();
        if (currentDrawable != null) {
            final TransitionDrawable drawable = new TransitionDrawable(new Drawable[] { currentDrawable, t });
            drawable.setCrossFadeEnabled(true);
            drawable.startTransition(this.duration);
            viewAdapter.setDrawable((Drawable)drawable);
            return true;
        }
        this.defaultAnimation.animate(t, viewAdapter);
        return false;
    }
}
