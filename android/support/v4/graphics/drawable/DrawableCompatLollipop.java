package android.support.v4.graphics.drawable;

import android.graphics.drawable.*;
import android.util.*;
import java.io.*;
import org.xmlpull.v1.*;
import android.content.res.*;
import android.graphics.*;

class DrawableCompatLollipop
{
    public static void applyTheme(final Drawable drawable, final Resources$Theme resources$Theme) {
        drawable.applyTheme(resources$Theme);
    }
    
    public static boolean canApplyTheme(final Drawable drawable) {
        return drawable.canApplyTheme();
    }
    
    public static ColorFilter getColorFilter(final Drawable drawable) {
        return drawable.getColorFilter();
    }
    
    public static void inflate(final Drawable drawable, final Resources resources, final XmlPullParser xmlPullParser, final AttributeSet set, final Resources$Theme resources$Theme) throws IOException, XmlPullParserException {
        drawable.inflate(resources, xmlPullParser, set, resources$Theme);
    }
    
    public static void setHotspot(final Drawable drawable, final float n, final float n2) {
        drawable.setHotspot(n, n2);
    }
    
    public static void setHotspotBounds(final Drawable drawable, final int n, final int n2, final int n3, final int n4) {
        drawable.setHotspotBounds(n, n2, n3, n4);
    }
    
    public static void setTint(final Drawable drawable, final int tint) {
        drawable.setTint(tint);
    }
    
    public static void setTintList(final Drawable drawable, final ColorStateList tintList) {
        drawable.setTintList(tintList);
    }
    
    public static void setTintMode(final Drawable drawable, final PorterDuff$Mode tintMode) {
        drawable.setTintMode(tintMode);
    }
    
    public static Drawable wrapForTinting(final Drawable drawable) {
        Drawable drawable2 = drawable;
        if (!(drawable instanceof DrawableWrapperLollipop)) {
            drawable2 = new DrawableWrapperLollipop(drawable);
        }
        return drawable2;
    }
}
