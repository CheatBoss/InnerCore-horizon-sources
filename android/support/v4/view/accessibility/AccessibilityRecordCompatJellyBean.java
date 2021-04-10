package android.support.v4.view.accessibility;

import android.view.*;
import android.view.accessibility.*;

class AccessibilityRecordCompatJellyBean
{
    public static void setSource(final Object o, final View view, final int n) {
        ((AccessibilityRecord)o).setSource(view, n);
    }
}
