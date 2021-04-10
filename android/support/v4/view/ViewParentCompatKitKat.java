package android.support.v4.view;

import android.view.*;

class ViewParentCompatKitKat
{
    public static void notifySubtreeAccessibilityStateChanged(final ViewParent viewParent, final View view, final View view2, final int n) {
        viewParent.notifySubtreeAccessibilityStateChanged(view, view2, n);
    }
}
