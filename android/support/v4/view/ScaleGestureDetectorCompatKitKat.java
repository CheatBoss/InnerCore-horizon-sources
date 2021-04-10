package android.support.v4.view;

import android.view.*;

class ScaleGestureDetectorCompatKitKat
{
    private ScaleGestureDetectorCompatKitKat() {
    }
    
    public static boolean isQuickScaleEnabled(final Object o) {
        return ((ScaleGestureDetector)o).isQuickScaleEnabled();
    }
    
    public static void setQuickScaleEnabled(final Object o, final boolean quickScaleEnabled) {
        ((ScaleGestureDetector)o).setQuickScaleEnabled(quickScaleEnabled);
    }
}
