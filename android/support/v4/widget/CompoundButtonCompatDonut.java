package android.support.v4.widget;

import java.lang.reflect.*;
import android.widget.*;
import android.graphics.drawable.*;
import android.util.*;
import android.content.res.*;
import android.graphics.*;

class CompoundButtonCompatDonut
{
    private static final String TAG = "CompoundButtonCompatDonut";
    private static Field sButtonDrawableField;
    private static boolean sButtonDrawableFieldFetched;
    
    static Drawable getButtonDrawable(final CompoundButton compoundButton) {
        if (!CompoundButtonCompatDonut.sButtonDrawableFieldFetched) {
            try {
                (CompoundButtonCompatDonut.sButtonDrawableField = CompoundButton.class.getDeclaredField("mButtonDrawable")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {
                Log.i("CompoundButtonCompatDonut", "Failed to retrieve mButtonDrawable field", (Throwable)ex);
            }
            CompoundButtonCompatDonut.sButtonDrawableFieldFetched = true;
        }
        if (CompoundButtonCompatDonut.sButtonDrawableField != null) {
            try {
                return (Drawable)CompoundButtonCompatDonut.sButtonDrawableField.get(compoundButton);
            }
            catch (IllegalAccessException ex2) {
                Log.i("CompoundButtonCompatDonut", "Failed to get button drawable via reflection", (Throwable)ex2);
                CompoundButtonCompatDonut.sButtonDrawableField = null;
            }
        }
        return null;
    }
    
    static ColorStateList getButtonTintList(final CompoundButton compoundButton) {
        if (compoundButton instanceof TintableCompoundButton) {
            return ((TintableCompoundButton)compoundButton).getSupportButtonTintList();
        }
        return null;
    }
    
    static PorterDuff$Mode getButtonTintMode(final CompoundButton compoundButton) {
        if (compoundButton instanceof TintableCompoundButton) {
            return ((TintableCompoundButton)compoundButton).getSupportButtonTintMode();
        }
        return null;
    }
    
    static void setButtonTintList(final CompoundButton compoundButton, final ColorStateList supportButtonTintList) {
        if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintList(supportButtonTintList);
        }
    }
    
    static void setButtonTintMode(final CompoundButton compoundButton, final PorterDuff$Mode supportButtonTintMode) {
        if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintMode(supportButtonTintMode);
        }
    }
}
