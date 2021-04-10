package com.bumptech.glide.request.animation;

import android.view.*;

public class ViewPropertyAnimation<R> implements GlideAnimation<R>
{
    private final Animator animator;
    
    public ViewPropertyAnimation(final Animator animator) {
        this.animator = animator;
    }
    
    @Override
    public boolean animate(final R r, final ViewAdapter viewAdapter) {
        if (viewAdapter.getView() != null) {
            this.animator.animate(viewAdapter.getView());
        }
        return false;
    }
    
    public interface Animator
    {
        void animate(final View p0);
    }
}
