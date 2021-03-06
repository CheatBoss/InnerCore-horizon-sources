package androidx.core.view;

import android.view.*;
import android.os.*;

public final class ScaleGestureDetectorCompat
{
    private ScaleGestureDetectorCompat() {
    }
    
    public static boolean isQuickScaleEnabled(final ScaleGestureDetector scaleGestureDetector) {
        return Build$VERSION.SDK_INT >= 19 && scaleGestureDetector.isQuickScaleEnabled();
    }
    
    @Deprecated
    public static boolean isQuickScaleEnabled(final Object o) {
        return isQuickScaleEnabled((ScaleGestureDetector)o);
    }
    
    public static void setQuickScaleEnabled(final ScaleGestureDetector scaleGestureDetector, final boolean quickScaleEnabled) {
        if (Build$VERSION.SDK_INT >= 19) {
            scaleGestureDetector.setQuickScaleEnabled(quickScaleEnabled);
        }
    }
    
    @Deprecated
    public static void setQuickScaleEnabled(final Object o, final boolean b) {
        setQuickScaleEnabled((ScaleGestureDetector)o, b);
    }
}
