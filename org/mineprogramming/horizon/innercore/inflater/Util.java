package org.mineprogramming.horizon.innercore.inflater;

import android.widget.*;
import android.graphics.*;
import android.graphics.drawable.*;

public final class Util
{
    private Util() {
    }
    
    public static void setProgressBarColor(final ProgressBar progressBar, final int n) {
        Drawable drawable;
        if (progressBar.isIndeterminate()) {
            drawable = progressBar.getIndeterminateDrawable();
        }
        else {
            drawable = progressBar.getProgressDrawable();
        }
        if (drawable == null) {
            return;
        }
        drawable.setColorFilter(n, PorterDuff$Mode.SRC_IN);
    }
}
