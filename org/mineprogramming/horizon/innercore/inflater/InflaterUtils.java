package org.mineprogramming.horizon.innercore.inflater;

import android.view.*;
import android.text.style.*;
import android.os.*;
import android.text.*;
import android.widget.*;
import android.graphics.*;
import android.graphics.drawable.*;

public final class InflaterUtils
{
    private InflaterUtils() {
    }
    
    public static MenuItem addMenuItem(final Menu menu, final String s) {
        final SpannableString spannableString = new SpannableString((CharSequence)s);
        spannableString.setSpan((Object)new ForegroundColorSpan(-16777216), 0, spannableString.length(), 0);
        return menu.add((CharSequence)spannableString);
    }
    
    public static Spanned fromHtml(final String s) {
        if (Build$VERSION.SDK_INT >= 24) {
            return Html.fromHtml(s, 63);
        }
        return Html.fromHtml(s);
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
