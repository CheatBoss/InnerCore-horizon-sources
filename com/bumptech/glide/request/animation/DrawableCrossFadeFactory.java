package com.bumptech.glide.request.animation;

import android.graphics.drawable.*;
import android.content.*;
import android.view.animation.*;

public class DrawableCrossFadeFactory<T extends Drawable> implements GlideAnimationFactory<T>
{
    private static final int DEFAULT_DURATION_MS = 300;
    private final ViewAnimationFactory<T> animationFactory;
    private final int duration;
    private DrawableCrossFadeViewAnimation<T> firstResourceAnimation;
    private DrawableCrossFadeViewAnimation<T> secondResourceAnimation;
    
    public DrawableCrossFadeFactory() {
        this(300);
    }
    
    public DrawableCrossFadeFactory(final int n) {
        this((ViewAnimationFactory)new ViewAnimationFactory(new DefaultAnimationFactory(n)), n);
    }
    
    public DrawableCrossFadeFactory(final Context context, final int n, final int n2) {
        this((ViewAnimationFactory)new ViewAnimationFactory(context, n), n2);
    }
    
    public DrawableCrossFadeFactory(final Animation animation, final int n) {
        this((ViewAnimationFactory)new ViewAnimationFactory(animation), n);
    }
    
    DrawableCrossFadeFactory(final ViewAnimationFactory<T> animationFactory, final int duration) {
        this.animationFactory = animationFactory;
        this.duration = duration;
    }
    
    private GlideAnimation<T> getFirstResourceAnimation() {
        if (this.firstResourceAnimation == null) {
            this.firstResourceAnimation = new DrawableCrossFadeViewAnimation<T>(this.animationFactory.build(false, true), this.duration);
        }
        return this.firstResourceAnimation;
    }
    
    private GlideAnimation<T> getSecondResourceAnimation() {
        if (this.secondResourceAnimation == null) {
            this.secondResourceAnimation = new DrawableCrossFadeViewAnimation<T>(this.animationFactory.build(false, false), this.duration);
        }
        return this.secondResourceAnimation;
    }
    
    @Override
    public GlideAnimation<T> build(final boolean b, final boolean b2) {
        if (b) {
            return NoAnimation.get();
        }
        if (b2) {
            return this.getFirstResourceAnimation();
        }
        return this.getSecondResourceAnimation();
    }
    
    private static class DefaultAnimationFactory implements AnimationFactory
    {
        private final int duration;
        
        DefaultAnimationFactory(final int duration) {
            this.duration = duration;
        }
        
        @Override
        public Animation build() {
            final AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration((long)this.duration);
            return (Animation)alphaAnimation;
        }
    }
}
