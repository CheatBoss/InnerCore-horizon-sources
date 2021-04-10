package android.support.v4.widget;

import java.lang.reflect.*;
import android.widget.*;
import android.util.*;

class TextViewCompatDonut
{
    private static final int LINES = 1;
    private static final String LOG_TAG = "TextViewCompatDonut";
    private static Field sMaxModeField;
    private static boolean sMaxModeFieldFetched;
    private static Field sMaximumField;
    private static boolean sMaximumFieldFetched;
    private static Field sMinModeField;
    private static boolean sMinModeFieldFetched;
    private static Field sMinimumField;
    private static boolean sMinimumFieldFetched;
    
    static int getMaxLines(final TextView textView) {
        if (!TextViewCompatDonut.sMaxModeFieldFetched) {
            TextViewCompatDonut.sMaxModeField = retrieveField("mMaxMode");
            TextViewCompatDonut.sMaxModeFieldFetched = true;
        }
        if (TextViewCompatDonut.sMaxModeField != null && retrieveIntFromField(TextViewCompatDonut.sMaxModeField, textView) == 1) {
            if (!TextViewCompatDonut.sMaximumFieldFetched) {
                TextViewCompatDonut.sMaximumField = retrieveField("mMaximum");
                TextViewCompatDonut.sMaximumFieldFetched = true;
            }
            if (TextViewCompatDonut.sMaximumField != null) {
                return retrieveIntFromField(TextViewCompatDonut.sMaximumField, textView);
            }
        }
        return -1;
    }
    
    static int getMinLines(final TextView textView) {
        if (!TextViewCompatDonut.sMinModeFieldFetched) {
            TextViewCompatDonut.sMinModeField = retrieveField("mMinMode");
            TextViewCompatDonut.sMinModeFieldFetched = true;
        }
        if (TextViewCompatDonut.sMinModeField != null && retrieveIntFromField(TextViewCompatDonut.sMinModeField, textView) == 1) {
            if (!TextViewCompatDonut.sMinimumFieldFetched) {
                TextViewCompatDonut.sMinimumField = retrieveField("mMinimum");
                TextViewCompatDonut.sMinimumFieldFetched = true;
            }
            if (TextViewCompatDonut.sMinimumField != null) {
                return retrieveIntFromField(TextViewCompatDonut.sMinimumField, textView);
            }
        }
        return -1;
    }
    
    private static Field retrieveField(final String s) {
        Field field = null;
        try {
            final Field declaredField = TextView.class.getDeclaredField(s);
            try {
                declaredField.setAccessible(true);
                return declaredField;
            }
            catch (NoSuchFieldException field) {
                field = declaredField;
            }
        }
        catch (NoSuchFieldException ex) {}
        final StringBuilder sb = new StringBuilder();
        sb.append("Could not retrieve ");
        sb.append(s);
        sb.append(" field.");
        Log.e("TextViewCompatDonut", sb.toString());
        return field;
    }
    
    private static int retrieveIntFromField(final Field field, final TextView textView) {
        try {
            return field.getInt(textView);
        }
        catch (IllegalAccessException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not retrieve value of ");
            sb.append(field.getName());
            sb.append(" field.");
            Log.d("TextViewCompatDonut", sb.toString());
            return -1;
        }
    }
    
    static void setTextAppearance(final TextView textView, final int n) {
        textView.setTextAppearance(textView.getContext(), n);
    }
}
