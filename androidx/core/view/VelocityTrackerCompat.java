package androidx.core.view;

import android.view.*;

@Deprecated
public final class VelocityTrackerCompat
{
    private VelocityTrackerCompat() {
    }
    
    @Deprecated
    public static float getXVelocity(final VelocityTracker velocityTracker, final int n) {
        return velocityTracker.getXVelocity(n);
    }
    
    @Deprecated
    public static float getYVelocity(final VelocityTracker velocityTracker, final int n) {
        return velocityTracker.getYVelocity(n);
    }
}
