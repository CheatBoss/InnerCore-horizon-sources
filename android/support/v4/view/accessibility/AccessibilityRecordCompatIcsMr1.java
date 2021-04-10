package android.support.v4.view.accessibility;

import android.view.accessibility.*;

class AccessibilityRecordCompatIcsMr1
{
    public static int getMaxScrollX(final Object o) {
        return ((AccessibilityRecord)o).getMaxScrollX();
    }
    
    public static int getMaxScrollY(final Object o) {
        return ((AccessibilityRecord)o).getMaxScrollY();
    }
    
    public static void setMaxScrollX(final Object o, final int maxScrollX) {
        ((AccessibilityRecord)o).setMaxScrollX(maxScrollX);
    }
    
    public static void setMaxScrollY(final Object o, final int maxScrollY) {
        ((AccessibilityRecord)o).setMaxScrollY(maxScrollY);
    }
}
