package android.support.v4.content;

import android.content.*;
import android.content.res.*;

class ContextCompatApi23
{
    public static int getColor(final Context context, final int n) {
        return context.getColor(n);
    }
    
    public static ColorStateList getColorStateList(final Context context, final int n) {
        return context.getColorStateList(n);
    }
}
