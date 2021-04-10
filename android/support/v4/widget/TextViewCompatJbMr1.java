package android.support.v4.widget;

import android.widget.*;
import android.graphics.drawable.*;
import android.support.annotation.*;

class TextViewCompatJbMr1
{
    public static void setCompoundDrawablesRelative(@NonNull final TextView textView, @Nullable Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
        final int layoutDirection = textView.getLayoutDirection();
        boolean b = true;
        if (layoutDirection != 1) {
            b = false;
        }
        Drawable drawable5;
        if (b) {
            drawable5 = drawable3;
        }
        else {
            drawable5 = drawable;
        }
        if (!b) {
            drawable = drawable3;
        }
        textView.setCompoundDrawables(drawable5, drawable2, drawable, drawable4);
    }
    
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, int n, final int n2, final int n3, final int n4) {
        final int layoutDirection = textView.getLayoutDirection();
        boolean b = true;
        if (layoutDirection != 1) {
            b = false;
        }
        int n5;
        if (b) {
            n5 = n3;
        }
        else {
            n5 = n;
        }
        if (!b) {
            n = n3;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(n5, n2, n, n4);
    }
    
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @Nullable Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
        final int layoutDirection = textView.getLayoutDirection();
        boolean b = true;
        if (layoutDirection != 1) {
            b = false;
        }
        Drawable drawable5;
        if (b) {
            drawable5 = drawable3;
        }
        else {
            drawable5 = drawable;
        }
        if (!b) {
            drawable = drawable3;
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(drawable5, drawable2, drawable, drawable4);
    }
}
