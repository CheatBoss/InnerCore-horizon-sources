package android.support.v4.view;

import android.os.*;
import android.view.*;

public final class VelocityTrackerCompat
{
    static final VelocityTrackerVersionImpl IMPL;
    
    static {
        VelocityTrackerVersionImpl impl;
        if (Build$VERSION.SDK_INT >= 11) {
            impl = new HoneycombVelocityTrackerVersionImpl();
        }
        else {
            impl = new BaseVelocityTrackerVersionImpl();
        }
        IMPL = impl;
    }
    
    private VelocityTrackerCompat() {
    }
    
    public static float getXVelocity(final VelocityTracker velocityTracker, final int n) {
        return VelocityTrackerCompat.IMPL.getXVelocity(velocityTracker, n);
    }
    
    public static float getYVelocity(final VelocityTracker velocityTracker, final int n) {
        return VelocityTrackerCompat.IMPL.getYVelocity(velocityTracker, n);
    }
    
    static class BaseVelocityTrackerVersionImpl implements VelocityTrackerVersionImpl
    {
        @Override
        public float getXVelocity(final VelocityTracker velocityTracker, final int n) {
            return velocityTracker.getXVelocity();
        }
        
        @Override
        public float getYVelocity(final VelocityTracker velocityTracker, final int n) {
            return velocityTracker.getYVelocity();
        }
    }
    
    static class HoneycombVelocityTrackerVersionImpl implements VelocityTrackerVersionImpl
    {
        @Override
        public float getXVelocity(final VelocityTracker velocityTracker, final int n) {
            return VelocityTrackerCompatHoneycomb.getXVelocity(velocityTracker, n);
        }
        
        @Override
        public float getYVelocity(final VelocityTracker velocityTracker, final int n) {
            return VelocityTrackerCompatHoneycomb.getYVelocity(velocityTracker, n);
        }
    }
    
    interface VelocityTrackerVersionImpl
    {
        float getXVelocity(final VelocityTracker p0, final int p1);
        
        float getYVelocity(final VelocityTracker p0, final int p1);
    }
}
