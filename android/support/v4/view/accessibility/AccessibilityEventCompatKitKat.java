package android.support.v4.view.accessibility;

import android.view.accessibility.*;

class AccessibilityEventCompatKitKat
{
    public static int getContentChangeTypes(final AccessibilityEvent accessibilityEvent) {
        return accessibilityEvent.getContentChangeTypes();
    }
    
    public static void setContentChangeTypes(final AccessibilityEvent accessibilityEvent, final int contentChangeTypes) {
        accessibilityEvent.setContentChangeTypes(contentChangeTypes);
    }
}
