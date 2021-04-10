package androidx.core.content.res;

import android.content.*;
import android.graphics.drawable.*;
import org.xmlpull.v1.*;
import androidx.annotation.*;
import android.content.res.*;
import android.util.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public class TypedArrayUtils
{
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
    
    private TypedArrayUtils() {
    }
    
    public static int getAttr(@NonNull final Context context, final int n, final int n2) {
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(n, typedValue, true);
        if (typedValue.resourceId != 0) {
            return n;
        }
        return n2;
    }
    
    public static boolean getBoolean(@NonNull final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2, final boolean b) {
        return typedArray.getBoolean(n, typedArray.getBoolean(n2, b));
    }
    
    @Nullable
    public static Drawable getDrawable(@NonNull final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2) {
        Drawable drawable;
        if ((drawable = typedArray.getDrawable(n)) == null) {
            drawable = typedArray.getDrawable(n2);
        }
        return drawable;
    }
    
    public static int getInt(@NonNull final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2, final int n3) {
        return typedArray.getInt(n, typedArray.getInt(n2, n3));
    }
    
    public static boolean getNamedBoolean(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, @NonNull final String s, @StyleableRes final int n, final boolean b) {
        if (!hasAttribute(xmlPullParser, s)) {
            return b;
        }
        return typedArray.getBoolean(n, b);
    }
    
    @ColorInt
    public static int getNamedColor(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, @NonNull final String s, @StyleableRes final int n, @ColorInt final int n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getColor(n, n2);
    }
    
    @Nullable
    public static ColorStateList getNamedColorStateList(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, @Nullable final Resources$Theme resources$Theme, @NonNull final String s, @StyleableRes final int n) {
        if (!hasAttribute(xmlPullParser, s)) {
            return null;
        }
        final TypedValue typedValue = new TypedValue();
        typedArray.getValue(n, typedValue);
        if (typedValue.type == 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to resolve attribute at index ");
            sb.append(n);
            sb.append(": ");
            sb.append(typedValue);
            throw new UnsupportedOperationException(sb.toString());
        }
        if (typedValue.type >= 28 && typedValue.type <= 31) {
            return getNamedColorStateListFromInt(typedValue);
        }
        return ColorStateListInflaterCompat.inflate(typedArray.getResources(), typedArray.getResourceId(n, 0), resources$Theme);
    }
    
    @NonNull
    private static ColorStateList getNamedColorStateListFromInt(@NonNull final TypedValue typedValue) {
        return ColorStateList.valueOf(typedValue.data);
    }
    
    public static ComplexColorCompat getNamedComplexColor(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, @Nullable final Resources$Theme resources$Theme, @NonNull final String s, @StyleableRes final int n, @ColorInt final int n2) {
        if (hasAttribute(xmlPullParser, s)) {
            final TypedValue typedValue = new TypedValue();
            typedArray.getValue(n, typedValue);
            if (typedValue.type >= 28 && typedValue.type <= 31) {
                return ComplexColorCompat.from(typedValue.data);
            }
            final ComplexColorCompat inflate = ComplexColorCompat.inflate(typedArray.getResources(), typedArray.getResourceId(n, 0), resources$Theme);
            if (inflate != null) {
                return inflate;
            }
        }
        return ComplexColorCompat.from(n2);
    }
    
    public static float getNamedFloat(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, @NonNull final String s, @StyleableRes final int n, final float n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getFloat(n, n2);
    }
    
    public static int getNamedInt(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, @NonNull final String s, @StyleableRes final int n, final int n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getInt(n, n2);
    }
    
    @AnyRes
    public static int getNamedResourceId(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, @NonNull final String s, @StyleableRes final int n, @AnyRes final int n2) {
        if (!hasAttribute(xmlPullParser, s)) {
            return n2;
        }
        return typedArray.getResourceId(n, n2);
    }
    
    @Nullable
    public static String getNamedString(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, @NonNull final String s, @StyleableRes final int n) {
        if (!hasAttribute(xmlPullParser, s)) {
            return null;
        }
        return typedArray.getString(n);
    }
    
    @AnyRes
    public static int getResourceId(@NonNull final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2, @AnyRes final int n3) {
        return typedArray.getResourceId(n, typedArray.getResourceId(n2, n3));
    }
    
    @Nullable
    public static String getString(@NonNull final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2) {
        String s;
        if ((s = typedArray.getString(n)) == null) {
            s = typedArray.getString(n2);
        }
        return s;
    }
    
    @Nullable
    public static CharSequence getText(@NonNull final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2) {
        CharSequence charSequence;
        if ((charSequence = typedArray.getText(n)) == null) {
            charSequence = typedArray.getText(n2);
        }
        return charSequence;
    }
    
    @Nullable
    public static CharSequence[] getTextArray(@NonNull final TypedArray typedArray, @StyleableRes final int n, @StyleableRes final int n2) {
        CharSequence[] array;
        if ((array = typedArray.getTextArray(n)) == null) {
            array = typedArray.getTextArray(n2);
        }
        return array;
    }
    
    public static boolean hasAttribute(@NonNull final XmlPullParser xmlPullParser, @NonNull final String s) {
        return xmlPullParser.getAttributeValue("http://schemas.android.com/apk/res/android", s) != null;
    }
    
    @NonNull
    public static TypedArray obtainAttributes(@NonNull final Resources resources, @Nullable final Resources$Theme resources$Theme, @NonNull final AttributeSet set, @NonNull final int[] array) {
        if (resources$Theme == null) {
            return resources.obtainAttributes(set, array);
        }
        return resources$Theme.obtainStyledAttributes(set, array, 0, 0);
    }
    
    @Nullable
    public static TypedValue peekNamedValue(@NonNull final TypedArray typedArray, @NonNull final XmlPullParser xmlPullParser, @NonNull final String s, final int n) {
        if (!hasAttribute(xmlPullParser, s)) {
            return null;
        }
        return typedArray.peekValue(n);
    }
}
