package com.bumptech.glide.request.animation;

public class ViewPropertyAnimationFactory<R> implements GlideAnimationFactory<R>
{
    private ViewPropertyAnimation<R> animation;
    private final ViewPropertyAnimation.Animator animator;
    
    public ViewPropertyAnimationFactory(final ViewPropertyAnimation.Animator animator) {
        this.animator = animator;
    }
    
    @Override
    public GlideAnimation<R> build(final boolean b, final boolean b2) {
        if (!b && b2) {
            if (this.animation == null) {
                this.animation = new ViewPropertyAnimation<R>(this.animator);
            }
            return this.animation;
        }
        return NoAnimation.get();
    }
}
