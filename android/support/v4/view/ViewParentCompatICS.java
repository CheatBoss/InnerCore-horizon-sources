package android.support.v4.view;

import android.view.*;
import android.view.accessibility.*;

class ViewParentCompatICS
{
    public static boolean requestSendAccessibilityEvent(final ViewParent viewParent, final View view, final AccessibilityEvent accessibilityEvent) {
        return viewParent.requestSendAccessibilityEvent(view, accessibilityEvent);
    }
}
