package com.bumptech.glide.request.animation;

import android.view.*;
import android.view.animation.*;

public class ViewAnimation<R> implements GlideAnimation<R>
{
    private final AnimationFactory animationFactory;
    
    ViewAnimation(final AnimationFactory animationFactory) {
        this.animationFactory = animationFactory;
    }
    
    @Override
    public boolean animate(final R r, final ViewAdapter viewAdapter) {
        final View view = viewAdapter.getView();
        if (view != null) {
            view.clearAnimation();
            view.startAnimation(this.animationFactory.build());
        }
        return false;
    }
    
    interface AnimationFactory
    {
        Animation build();
    }
}
