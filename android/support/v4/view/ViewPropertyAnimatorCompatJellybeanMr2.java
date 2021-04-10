package android.support.v4.view;

import android.view.*;
import android.view.animation.*;

class ViewPropertyAnimatorCompatJellybeanMr2
{
    public static Interpolator getInterpolator(final View view) {
        return (Interpolator)view.animate().getInterpolator();
    }
}
