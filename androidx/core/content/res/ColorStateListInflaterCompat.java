package androidx.core.content.res;

import org.xmlpull.v1.*;
import java.io.*;
import androidx.core.*;
import android.util.*;
import android.content.res.*;
import androidx.annotation.*;
import android.graphics.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public final class ColorStateListInflaterCompat
{
    private ColorStateListInflaterCompat() {
    }
    
    @NonNull
    public static ColorStateList createFromXml(@NonNull final Resources resources, @NonNull final XmlPullParser xmlPullParser, @Nullable final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final AttributeSet attributeSet = Xml.asAttributeSet(xmlPullParser);
        int next;
        do {
            next = xmlPullParser.next();
        } while (next != 2 && next != 1);
        if (next != 2) {
            throw new XmlPullParserException("No start tag found");
        }
        return createFromXmlInner(resources, xmlPullParser, attributeSet, resources$Theme);
    }
    
    @NonNull
    public static ColorStateList createFromXmlInner(@NonNull final Resources resources, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final String name = xmlPullParser.getName();
        if (!name.equals("selector")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(xmlPullParser.getPositionDescription());
            sb.append(": invalid color state list tag ");
            sb.append(name);
            throw new XmlPullParserException(sb.toString());
        }
        return inflate(resources, xmlPullParser, set, resources$Theme);
    }
    
    @Nullable
    public static ColorStateList inflate(@NonNull final Resources resources, @XmlRes final int n, @Nullable final Resources$Theme resources$Theme) {
        try {
            return createFromXml(resources, (XmlPullParser)resources.getXml(n), resources$Theme);
        }
        catch (Exception ex) {
            Log.e("CSLCompat", "Failed to inflate ColorStateList.", (Throwable)ex);
            return null;
        }
    }
    
    private static ColorStateList inflate(@NonNull final Resources resources, @NonNull final XmlPullParser xmlPullParser, @NonNull final AttributeSet set, @Nullable final Resources$Theme resources$Theme) throws XmlPullParserException, IOException {
        final int n = xmlPullParser.getDepth() + 1;
        int[][] array = new int[20][];
        int[] append = new int[array.length];
        int n2 = 0;
        while (true) {
            final int next = xmlPullParser.next();
            if (next == 1) {
                break;
            }
            final int depth = xmlPullParser.getDepth();
            if (depth < n && next == 3) {
                break;
            }
            if (next != 2 || depth > n) {
                continue;
            }
            if (!xmlPullParser.getName().equals("item")) {
                continue;
            }
            final TypedArray obtainAttributes = obtainAttributes(resources, resources$Theme, set, R$styleable.ColorStateListItem);
            final int color = obtainAttributes.getColor(R$styleable.ColorStateListItem_android_color, -65281);
            float n3 = 1.0f;
            if (obtainAttributes.hasValue(R$styleable.ColorStateListItem_android_alpha)) {
                n3 = obtainAttributes.getFloat(R$styleable.ColorStateListItem_android_alpha, 1.0f);
            }
            else if (obtainAttributes.hasValue(R$styleable.ColorStateListItem_alpha)) {
                n3 = obtainAttributes.getFloat(R$styleable.ColorStateListItem_alpha, 1.0f);
            }
            obtainAttributes.recycle();
            final int attributeCount = set.getAttributeCount();
            final int[] array2 = new int[attributeCount];
            int n4 = 0;
            int n5;
            for (int i = 0; i < attributeCount; ++i, n4 = n5) {
                final int attributeNameResource = set.getAttributeNameResource(i);
                n5 = n4;
                if (attributeNameResource != 16843173) {
                    n5 = n4;
                    if (attributeNameResource != 16843551) {
                        n5 = n4;
                        if (attributeNameResource != R$attr.alpha) {
                            int n6;
                            if (set.getAttributeBooleanValue(i, false)) {
                                n6 = attributeNameResource;
                            }
                            else {
                                n6 = -attributeNameResource;
                            }
                            array2[n4] = n6;
                            n5 = n4 + 1;
                        }
                    }
                }
            }
            final int[] trimStateSet = StateSet.trimStateSet(array2, n4);
            append = GrowingArrayUtils.append(append, n2, modulateColorAlpha(color, n3));
            array = GrowingArrayUtils.append(array, n2, trimStateSet);
            ++n2;
        }
        final int[] array3 = new int[n2];
        final int[][] array4 = new int[n2][];
        System.arraycopy(append, 0, array3, 0, n2);
        System.arraycopy(array, 0, array4, 0, n2);
        return new ColorStateList(array4, array3);
    }
    
    @ColorInt
    private static int modulateColorAlpha(@ColorInt final int n, @FloatRange(from = 0.0, to = 1.0) final float n2) {
        return (0xFFFFFF & n) | Math.round(Color.alpha(n) * n2) << 24;
    }
    
    private static TypedArray obtainAttributes(final Resources resources, final Resources$Theme resources$Theme, final AttributeSet set, final int[] array) {
        if (resources$Theme == null) {
            return resources.obtainAttributes(set, array);
        }
        return resources$Theme.obtainStyledAttributes(set, array, 0, 0);
    }
}
