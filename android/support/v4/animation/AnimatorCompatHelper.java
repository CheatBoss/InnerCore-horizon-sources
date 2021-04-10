package android.support.v4.animation;

import android.os.*;
import android.view.*;

public final class AnimatorCompatHelper
{
    private static final AnimatorProvider IMPL;
    
    static {
        AnimatorProvider impl;
        if (Build$VERSION.SDK_INT >= 12) {
            impl = new HoneycombMr1AnimatorCompatProvider();
        }
        else {
            impl = new DonutAnimatorCompatProvider();
        }
        IMPL = impl;
    }
    
    private AnimatorCompatHelper() {
    }
    
    public static void clearInterpolator(final View view) {
        AnimatorCompatHelper.IMPL.clearInterpolator(view);
    }
    
    public static ValueAnimatorCompat emptyValueAnimator() {
        return AnimatorCompatHelper.IMPL.emptyValueAnimator();
    }
}
