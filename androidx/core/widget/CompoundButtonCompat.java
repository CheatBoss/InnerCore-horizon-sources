package androidx.core.widget;

import java.lang.reflect.*;
import android.widget.*;
import android.graphics.drawable.*;
import android.os.*;
import android.util.*;
import androidx.annotation.*;
import android.content.res.*;
import android.graphics.*;

public final class CompoundButtonCompat
{
    private static final String TAG = "CompoundButtonCompat";
    private static Field sButtonDrawableField;
    private static boolean sButtonDrawableFieldFetched;
    
    private CompoundButtonCompat() {
    }
    
    @Nullable
    public static Drawable getButtonDrawable(@NonNull final CompoundButton compoundButton) {
        if (Build$VERSION.SDK_INT >= 23) {
            return compoundButton.getButtonDrawable();
        }
        if (!CompoundButtonCompat.sButtonDrawableFieldFetched) {
            try {
                (CompoundButtonCompat.sButtonDrawableField = CompoundButton.class.getDeclaredField("mButtonDrawable")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {
                Log.i("CompoundButtonCompat", "Failed to retrieve mButtonDrawable field", (Throwable)ex);
            }
            CompoundButtonCompat.sButtonDrawableFieldFetched = true;
        }
        if (CompoundButtonCompat.sButtonDrawableField != null) {
            try {
                return (Drawable)CompoundButtonCompat.sButtonDrawableField.get(compoundButton);
            }
            catch (IllegalAccessException ex2) {
                Log.i("CompoundButtonCompat", "Failed to get button drawable via reflection", (Throwable)ex2);
                CompoundButtonCompat.sButtonDrawableField = null;
            }
        }
        return null;
    }
    
    @Nullable
    public static ColorStateList getButtonTintList(@NonNull final CompoundButton compoundButton) {
        if (Build$VERSION.SDK_INT >= 21) {
            return compoundButton.getButtonTintList();
        }
        if (compoundButton instanceof TintableCompoundButton) {
            return ((TintableCompoundButton)compoundButton).getSupportButtonTintList();
        }
        return null;
    }
    
    @Nullable
    public static PorterDuff$Mode getButtonTintMode(@NonNull final CompoundButton compoundButton) {
        if (Build$VERSION.SDK_INT >= 21) {
            return compoundButton.getButtonTintMode();
        }
        if (compoundButton instanceof TintableCompoundButton) {
            return ((TintableCompoundButton)compoundButton).getSupportButtonTintMode();
        }
        return null;
    }
    
    public static void setButtonTintList(@NonNull final CompoundButton compoundButton, @Nullable final ColorStateList list) {
        if (Build$VERSION.SDK_INT >= 21) {
            compoundButton.setButtonTintList(list);
            return;
        }
        if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintList(list);
        }
    }
    
    public static void setButtonTintMode(@NonNull final CompoundButton compoundButton, @Nullable final PorterDuff$Mode porterDuff$Mode) {
        if (Build$VERSION.SDK_INT >= 21) {
            compoundButton.setButtonTintMode(porterDuff$Mode);
            return;
        }
        if (compoundButton instanceof TintableCompoundButton) {
            ((TintableCompoundButton)compoundButton).setSupportButtonTintMode(porterDuff$Mode);
        }
    }
}
