package androidx.core.widget;

import java.lang.reflect.*;
import android.widget.*;
import androidx.annotation.*;
import android.os.*;
import android.util.*;
import android.view.*;
import androidx.core.view.*;

public final class PopupWindowCompat
{
    private static final String TAG = "PopupWindowCompatApi21";
    private static Method sGetWindowLayoutTypeMethod;
    private static boolean sGetWindowLayoutTypeMethodAttempted;
    private static Field sOverlapAnchorField;
    private static boolean sOverlapAnchorFieldAttempted;
    private static Method sSetWindowLayoutTypeMethod;
    private static boolean sSetWindowLayoutTypeMethodAttempted;
    
    private PopupWindowCompat() {
    }
    
    public static boolean getOverlapAnchor(@NonNull final PopupWindow popupWindow) {
        if (Build$VERSION.SDK_INT >= 23) {
            return popupWindow.getOverlapAnchor();
        }
        if (Build$VERSION.SDK_INT >= 21) {
            if (!PopupWindowCompat.sOverlapAnchorFieldAttempted) {
                try {
                    (PopupWindowCompat.sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor")).setAccessible(true);
                }
                catch (NoSuchFieldException ex) {
                    Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", (Throwable)ex);
                }
                PopupWindowCompat.sOverlapAnchorFieldAttempted = true;
            }
            if (PopupWindowCompat.sOverlapAnchorField != null) {
                try {
                    return (boolean)PopupWindowCompat.sOverlapAnchorField.get(popupWindow);
                }
                catch (IllegalAccessException ex2) {
                    Log.i("PopupWindowCompatApi21", "Could not get overlap anchor field in PopupWindow", (Throwable)ex2);
                }
            }
        }
        return false;
    }
    
    public static int getWindowLayoutType(@NonNull final PopupWindow popupWindow) {
        if (Build$VERSION.SDK_INT >= 23) {
            return popupWindow.getWindowLayoutType();
        }
        if (!PopupWindowCompat.sGetWindowLayoutTypeMethodAttempted) {
            try {
                (PopupWindowCompat.sGetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("getWindowLayoutType", (Class<?>[])new Class[0])).setAccessible(true);
            }
            catch (Exception ex) {}
            PopupWindowCompat.sGetWindowLayoutTypeMethodAttempted = true;
        }
        if (PopupWindowCompat.sGetWindowLayoutTypeMethod != null) {
            try {
                return (int)PopupWindowCompat.sGetWindowLayoutTypeMethod.invoke(popupWindow, new Object[0]);
            }
            catch (Exception ex2) {}
        }
        return 0;
    }
    
    public static void setOverlapAnchor(@NonNull final PopupWindow popupWindow, final boolean overlapAnchor) {
        if (Build$VERSION.SDK_INT >= 23) {
            popupWindow.setOverlapAnchor(overlapAnchor);
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            if (!PopupWindowCompat.sOverlapAnchorFieldAttempted) {
                try {
                    (PopupWindowCompat.sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor")).setAccessible(true);
                }
                catch (NoSuchFieldException ex) {
                    Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", (Throwable)ex);
                }
                PopupWindowCompat.sOverlapAnchorFieldAttempted = true;
            }
            if (PopupWindowCompat.sOverlapAnchorField != null) {
                try {
                    PopupWindowCompat.sOverlapAnchorField.set(popupWindow, overlapAnchor);
                }
                catch (IllegalAccessException ex2) {
                    Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", (Throwable)ex2);
                }
            }
        }
    }
    
    public static void setWindowLayoutType(@NonNull final PopupWindow popupWindow, final int windowLayoutType) {
        if (Build$VERSION.SDK_INT >= 23) {
            popupWindow.setWindowLayoutType(windowLayoutType);
            return;
        }
        if (!PopupWindowCompat.sSetWindowLayoutTypeMethodAttempted) {
            try {
                (PopupWindowCompat.sSetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", Integer.TYPE)).setAccessible(true);
            }
            catch (Exception ex) {}
            PopupWindowCompat.sSetWindowLayoutTypeMethodAttempted = true;
        }
        if (PopupWindowCompat.sSetWindowLayoutTypeMethod != null) {
            try {
                PopupWindowCompat.sSetWindowLayoutTypeMethod.invoke(popupWindow, windowLayoutType);
            }
            catch (Exception ex2) {}
        }
    }
    
    public static void showAsDropDown(@NonNull final PopupWindow popupWindow, @NonNull final View view, final int n, final int n2, final int n3) {
        if (Build$VERSION.SDK_INT >= 19) {
            popupWindow.showAsDropDown(view, n, n2, n3);
            return;
        }
        int n4 = n;
        if ((GravityCompat.getAbsoluteGravity(n3, ViewCompat.getLayoutDirection(view)) & 0x7) == 0x5) {
            n4 = n - (popupWindow.getWidth() - view.getWidth());
        }
        popupWindow.showAsDropDown(view, n4, n2);
    }
}
