package com.appboy.ui.support;

import android.view.animation.*;

public class AnimationUtils
{
    private static Interpolator sAccelerateInterpolator;
    private static Interpolator sDecelerateInterpolator;
    
    static {
        AnimationUtils.sAccelerateInterpolator = (Interpolator)new AccelerateInterpolator();
        AnimationUtils.sDecelerateInterpolator = (Interpolator)new DecelerateInterpolator();
    }
    
    public static Animation createHorizontalAnimation(final float n, final float n2, final long n3, final boolean b) {
        return setAnimationParams((Animation)new TranslateAnimation(1, n, 1, n2, 2, 0.0f, 2, 0.0f), n3, b);
    }
    
    public static Animation createVerticalAnimation(final float n, final float n2, final long n3, final boolean b) {
        return setAnimationParams((Animation)new TranslateAnimation(2, 0.0f, 2, 0.0f, 1, n, 1, n2), n3, b);
    }
    
    public static Animation setAnimationParams(final Animation animation, final long duration, final boolean b) {
        animation.setDuration(duration);
        Interpolator interpolator;
        if (b) {
            interpolator = AnimationUtils.sAccelerateInterpolator;
        }
        else {
            interpolator = AnimationUtils.sDecelerateInterpolator;
        }
        animation.setInterpolator(interpolator);
        return animation;
    }
}
