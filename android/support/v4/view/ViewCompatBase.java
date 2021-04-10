package android.support.v4.view;

import java.lang.reflect.*;
import android.content.res.*;
import android.graphics.*;
import android.view.*;

class ViewCompatBase
{
    private static final String TAG = "ViewCompatBase";
    private static Field sMinHeightField;
    private static boolean sMinHeightFieldFetched;
    private static Field sMinWidthField;
    private static boolean sMinWidthFieldFetched;
    
    static ColorStateList getBackgroundTintList(final View view) {
        if (view instanceof TintableBackgroundView) {
            return ((TintableBackgroundView)view).getSupportBackgroundTintList();
        }
        return null;
    }
    
    static PorterDuff$Mode getBackgroundTintMode(final View view) {
        if (view instanceof TintableBackgroundView) {
            return ((TintableBackgroundView)view).getSupportBackgroundTintMode();
        }
        return null;
    }
    
    static int getMinimumHeight(final View view) {
        if (!ViewCompatBase.sMinHeightFieldFetched) {
            try {
                (ViewCompatBase.sMinHeightField = View.class.getDeclaredField("mMinHeight")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {}
            ViewCompatBase.sMinHeightFieldFetched = true;
        }
        if (ViewCompatBase.sMinHeightField != null) {
            try {
                return (int)ViewCompatBase.sMinHeightField.get(view);
            }
            catch (Exception ex2) {}
        }
        return 0;
    }
    
    static int getMinimumWidth(final View view) {
        if (!ViewCompatBase.sMinWidthFieldFetched) {
            try {
                (ViewCompatBase.sMinWidthField = View.class.getDeclaredField("mMinWidth")).setAccessible(true);
            }
            catch (NoSuchFieldException ex) {}
            ViewCompatBase.sMinWidthFieldFetched = true;
        }
        if (ViewCompatBase.sMinWidthField != null) {
            try {
                return (int)ViewCompatBase.sMinWidthField.get(view);
            }
            catch (Exception ex2) {}
        }
        return 0;
    }
    
    static boolean isAttachedToWindow(final View view) {
        return view.getWindowToken() != null;
    }
    
    static boolean isLaidOut(final View view) {
        return view.getWidth() > 0 && view.getHeight() > 0;
    }
    
    static void offsetLeftAndRight(final View view, int abs) {
        final int left = view.getLeft();
        view.offsetLeftAndRight(abs);
        if (abs != 0) {
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                abs = Math.abs(abs);
                ((View)parent).invalidate(left - abs, view.getTop(), view.getWidth() + left + abs, view.getBottom());
                return;
            }
            view.invalidate();
        }
    }
    
    static void offsetTopAndBottom(final View view, int abs) {
        final int top = view.getTop();
        view.offsetTopAndBottom(abs);
        if (abs != 0) {
            final ViewParent parent = view.getParent();
            if (parent instanceof View) {
                abs = Math.abs(abs);
                ((View)parent).invalidate(view.getLeft(), top - abs, view.getRight(), view.getHeight() + top + abs);
                return;
            }
            view.invalidate();
        }
    }
    
    static void setBackgroundTintList(final View view, final ColorStateList supportBackgroundTintList) {
        if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView)view).setSupportBackgroundTintList(supportBackgroundTintList);
        }
    }
    
    static void setBackgroundTintMode(final View view, final PorterDuff$Mode supportBackgroundTintMode) {
        if (view instanceof TintableBackgroundView) {
            ((TintableBackgroundView)view).setSupportBackgroundTintMode(supportBackgroundTintMode);
        }
    }
}
