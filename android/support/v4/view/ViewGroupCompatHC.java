package android.support.v4.view;

import android.view.*;

class ViewGroupCompatHC
{
    private ViewGroupCompatHC() {
    }
    
    public static void setMotionEventSplittingEnabled(final ViewGroup viewGroup, final boolean motionEventSplittingEnabled) {
        viewGroup.setMotionEventSplittingEnabled(motionEventSplittingEnabled);
    }
}
