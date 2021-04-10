package android.support.v4.graphics.drawable;

import android.graphics.drawable.*;

class DrawableCompatApi23
{
    public static int getLayoutDirection(final Drawable drawable) {
        return drawable.getLayoutDirection();
    }
    
    public static void setLayoutDirection(final Drawable drawable, final int layoutDirection) {
        drawable.setLayoutDirection(layoutDirection);
    }
}
