package android.support.v4.animation;

import android.view.*;

public interface ValueAnimatorCompat
{
    void addListener(final AnimatorListenerCompat p0);
    
    void addUpdateListener(final AnimatorUpdateListenerCompat p0);
    
    void cancel();
    
    float getAnimatedFraction();
    
    void setDuration(final long p0);
    
    void setTarget(final View p0);
    
    void start();
}
