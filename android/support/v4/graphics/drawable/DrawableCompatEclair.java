package android.support.v4.graphics.drawable;

import android.graphics.drawable.*;

class DrawableCompatEclair
{
    public static Drawable wrapForTinting(final Drawable drawable) {
        Drawable drawable2 = drawable;
        if (!(drawable instanceof DrawableWrapperEclair)) {
            drawable2 = new DrawableWrapperEclair(drawable);
        }
        return drawable2;
    }
}
