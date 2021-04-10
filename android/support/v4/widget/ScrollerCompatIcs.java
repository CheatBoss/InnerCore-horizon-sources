package android.support.v4.widget;

import android.widget.*;

class ScrollerCompatIcs
{
    public static float getCurrVelocity(final Object o) {
        return ((OverScroller)o).getCurrVelocity();
    }
}
