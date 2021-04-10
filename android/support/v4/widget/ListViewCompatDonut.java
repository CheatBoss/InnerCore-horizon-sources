package android.support.v4.widget;

import android.widget.*;
import android.view.*;

class ListViewCompatDonut
{
    static void scrollListBy(final ListView listView, final int n) {
        final int firstVisiblePosition = listView.getFirstVisiblePosition();
        if (firstVisiblePosition == -1) {
            return;
        }
        final View child = listView.getChildAt(0);
        if (child != null) {
            listView.setSelectionFromTop(firstVisiblePosition, child.getTop() - n);
        }
    }
}
