package com.bumptech.glide.request.animation;

import android.content.*;
import android.view.animation.*;

public class ViewAnimationFactory<R> implements GlideAnimationFactory<R>
{
    private final ViewAnimation.AnimationFactory animationFactory;
    private GlideAnimation<R> glideAnimation;
    
    public ViewAnimationFactory(final Context context, final int n) {
        this(new ResourceAnimationFactory(context, n));
    }
    
    public ViewAnimationFactory(final Animation animation) {
        this(new ConcreteAnimationFactory(animation));
    }
    
    ViewAnimationFactory(final ViewAnimation.AnimationFactory animationFactory) {
        this.animationFactory = animationFactory;
    }
    
    @Override
    public GlideAnimation<R> build(final boolean b, final boolean b2) {
        if (!b && b2) {
            if (this.glideAnimation == null) {
                this.glideAnimation = new ViewAnimation<R>(this.animationFactory);
            }
            return this.glideAnimation;
        }
        return NoAnimation.get();
    }
    
    private static class ConcreteAnimationFactory implements AnimationFactory
    {
        private final Animation animation;
        
        public ConcreteAnimationFactory(final Animation animation) {
            this.animation = animation;
        }
        
        @Override
        public Animation build() {
            return this.animation;
        }
    }
    
    private static class ResourceAnimationFactory implements AnimationFactory
    {
        private final int animationId;
        private final Context context;
        
        public ResourceAnimationFactory(final Context context, final int animationId) {
            this.context = context.getApplicationContext();
            this.animationId = animationId;
        }
        
        @Override
        public Animation build() {
            return AnimationUtils.loadAnimation(this.context, this.animationId);
        }
    }
}
