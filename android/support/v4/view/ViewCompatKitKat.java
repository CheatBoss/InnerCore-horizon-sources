package android.support.v4.view;

import android.view.*;

class ViewCompatKitKat
{
    public static int getAccessibilityLiveRegion(final View view) {
        return view.getAccessibilityLiveRegion();
    }
    
    public static boolean isAttachedToWindow(final View view) {
        return view.isAttachedToWindow();
    }
    
    public static boolean isLaidOut(final View view) {
        return view.isLaidOut();
    }
    
    public static void setAccessibilityLiveRegion(final View view, final int accessibilityLiveRegion) {
        view.setAccessibilityLiveRegion(accessibilityLiveRegion);
    }
}
