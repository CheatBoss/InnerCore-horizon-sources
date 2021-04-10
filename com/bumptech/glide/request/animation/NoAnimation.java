package com.bumptech.glide.request.animation;

public class NoAnimation<R> implements GlideAnimation<R>
{
    private static final NoAnimation<?> NO_ANIMATION;
    private static final GlideAnimationFactory<?> NO_ANIMATION_FACTORY;
    
    static {
        NO_ANIMATION = new NoAnimation<Object>();
        NO_ANIMATION_FACTORY = new NoAnimationFactory<Object>();
    }
    
    public static <R> GlideAnimation<R> get() {
        return (GlideAnimation<R>)NoAnimation.NO_ANIMATION;
    }
    
    public static <R> GlideAnimationFactory<R> getFactory() {
        return (GlideAnimationFactory<R>)NoAnimation.NO_ANIMATION_FACTORY;
    }
    
    @Override
    public boolean animate(final Object o, final ViewAdapter viewAdapter) {
        return false;
    }
    
    public static class NoAnimationFactory<R> implements GlideAnimationFactory<R>
    {
        @Override
        public GlideAnimation<R> build(final boolean b, final boolean b2) {
            return (GlideAnimation<R>)NoAnimation.NO_ANIMATION;
        }
    }
}
