package androidx.core.content.res;

import android.graphics.*;
import androidx.annotation.*;
import org.xmlpull.v1.*;
import android.content.res.*;
import java.io.*;
import android.util.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public final class ComplexColorCompat
{
    private static final String LOG_TAG = "ComplexColorCompat";
    private int mColor;
    private final ColorStateList mColorStateList;
    private final Shader mShader;
    
    private ComplexColorCompat(final Shader mShader, final ColorStateList mColorStateList, @ColorInt final int mColor) {
        this.mShader = mShader;
        this.mColorStateList = mColorStateList;
        this.mColor = mColor;
    }
    
    @NonNull
    private static ComplexColorCompat createFromXml(@NonNull final Resources resources, @ColorRes int n, @Nullable final Resources$Theme resources$Theme) throws IOException, XmlPullParserException {
        final XmlResourceParser xml = resources.getXml(n);
        final AttributeSet attributeSet = Xml.asAttributeSet((XmlPullParser)xml);
        int next;
        do {
            next = ((XmlPullParser)xml).next();
            n = 1;
        } while (next != 2 && next != 1);
        if (next != 2) {
            throw new XmlPullParserException("No start tag found");
        }
        final String name = ((XmlPullParser)xml).getName();
        final int hashCode = name.hashCode();
        Label_0112: {
            if (hashCode != 89650992) {
                if (hashCode == 1191572447) {
                    if (name.equals("selector")) {
                        n = 0;
                        break Label_0112;
                    }
                }
            }
            else if (name.equals("gradient")) {
                break Label_0112;
            }
            n = -1;
        }
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append(((XmlPullParser)xml).getPositionDescription());
                sb.append(": unsupported complex color tag ");
                sb.append(name);
                throw new XmlPullParserException(sb.toString());
            }
            case 1: {
                return from(GradientColorInflaterCompat.createFromXmlInner(resources, (XmlPullParser)xml, attributeSet, resources$Theme));
            }
            case 0: {
                return from(ColorStateListInflaterCompat.createFromXmlInner(resources, (XmlPullParser)xml, attributeSet, resources$Theme));
            }
        }
    }
    
    static ComplexColorCompat from(@ColorInt final int n) {
        return new ComplexColorCompat(null, null, n);
    }
    
    static ComplexColorCompat from(@NonNull final ColorStateList list) {
        return new ComplexColorCompat(null, list, list.getDefaultColor());
    }
    
    static ComplexColorCompat from(@NonNull final Shader shader) {
        return new ComplexColorCompat(shader, null, 0);
    }
    
    @Nullable
    public static ComplexColorCompat inflate(@NonNull final Resources resources, @ColorRes final int n, @Nullable final Resources$Theme resources$Theme) {
        try {
            return createFromXml(resources, n, resources$Theme);
        }
        catch (Exception ex) {
            Log.e("ComplexColorCompat", "Failed to inflate ComplexColor.", (Throwable)ex);
            return null;
        }
    }
    
    @ColorInt
    public int getColor() {
        return this.mColor;
    }
    
    @Nullable
    public Shader getShader() {
        return this.mShader;
    }
    
    public boolean isGradient() {
        return this.mShader != null;
    }
    
    public boolean isStateful() {
        return this.mShader == null && this.mColorStateList != null && this.mColorStateList.isStateful();
    }
    
    public boolean onStateChanged(final int[] array) {
        boolean b = false;
        if (this.isStateful()) {
            final int colorForState = this.mColorStateList.getColorForState(array, this.mColorStateList.getDefaultColor());
            b = b;
            if (colorForState != this.mColor) {
                b = true;
                this.mColor = colorForState;
            }
        }
        return b;
    }
    
    public void setColor(@ColorInt final int mColor) {
        this.mColor = mColor;
    }
    
    public boolean willDraw() {
        return this.isGradient() || this.mColor != 0;
    }
}
