package android.support.v4.view;

import android.view.*;
import android.graphics.*;

class ViewCompatJellybeanMr2
{
    public static Rect getClipBounds(final View view) {
        return view.getClipBounds();
    }
    
    public static void setClipBounds(final View view, final Rect clipBounds) {
        view.setClipBounds(clipBounds);
    }
}
