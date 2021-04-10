package android.support.v4.graphics.drawable;

import android.graphics.drawable.*;
import android.util.*;
import java.io.*;
import org.xmlpull.v1.*;
import android.content.res.*;
import android.graphics.*;

class DrawableCompatBase
{
    public static void inflate(final Drawable drawable, final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws IOException, XmlPullParserException {
        drawable.inflate(resources, xmlPullParser, set);
    }
    
    public static void setTint(final Drawable drawable, final int compatTint) {
        if (drawable instanceof DrawableWrapper) {
            ((DrawableWrapper)drawable).setCompatTint(compatTint);
        }
    }
    
    public static void setTintList(final Drawable drawable, final ColorStateList compatTintList) {
        if (drawable instanceof DrawableWrapper) {
            ((DrawableWrapper)drawable).setCompatTintList(compatTintList);
        }
    }
    
    public static void setTintMode(final Drawable drawable, final PorterDuff$Mode compatTintMode) {
        if (drawable instanceof DrawableWrapper) {
            ((DrawableWrapper)drawable).setCompatTintMode(compatTintMode);
        }
    }
    
    public static Drawable wrapForTinting(final Drawable drawable) {
        Drawable drawable2 = drawable;
        if (!(drawable instanceof DrawableWrapperDonut)) {
            drawable2 = new DrawableWrapperDonut(drawable);
        }
        return drawable2;
    }
}
