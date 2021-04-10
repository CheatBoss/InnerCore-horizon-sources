package android.support.v4.widget;

import android.view.*;
import android.widget.*;

class ListPopupWindowCompatKitKat
{
    public static View$OnTouchListener createDragToOpenListener(final Object o, final View view) {
        return ((ListPopupWindow)o).createDragToOpenListener(view);
    }
}
