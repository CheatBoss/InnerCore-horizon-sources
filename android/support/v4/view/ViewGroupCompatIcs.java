package android.support.v4.view;

import android.view.*;
import android.view.accessibility.*;

class ViewGroupCompatIcs
{
    public static boolean onRequestSendAccessibilityEvent(final ViewGroup viewGroup, final View view, final AccessibilityEvent accessibilityEvent) {
        return viewGroup.onRequestSendAccessibilityEvent(view, accessibilityEvent);
    }
}
