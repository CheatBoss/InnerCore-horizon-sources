package android.support.v4.widget;

import android.os.*;
import android.widget.*;
import android.graphics.drawable.*;
import android.support.annotation.*;

public final class TextViewCompat
{
    static final TextViewCompatImpl IMPL;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        TextViewCompatImpl impl;
        if (sdk_INT >= 23) {
            impl = new Api23TextViewCompatImpl();
        }
        else if (sdk_INT >= 18) {
            impl = new JbMr2TextViewCompatImpl();
        }
        else if (sdk_INT >= 17) {
            impl = new JbMr1TextViewCompatImpl();
        }
        else if (sdk_INT >= 16) {
            impl = new JbTextViewCompatImpl();
        }
        else {
            impl = new BaseTextViewCompatImpl();
        }
        IMPL = impl;
    }
    
    private TextViewCompat() {
    }
    
    public static int getMaxLines(@NonNull final TextView textView) {
        return TextViewCompat.IMPL.getMaxLines(textView);
    }
    
    public static int getMinLines(@NonNull final TextView textView) {
        return TextViewCompat.IMPL.getMinLines(textView);
    }
    
    public static void setCompoundDrawablesRelative(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
        TextViewCompat.IMPL.setCompoundDrawablesRelative(textView, drawable, drawable2, drawable3, drawable4);
    }
    
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @DrawableRes final int n, @DrawableRes final int n2, @DrawableRes final int n3, @DrawableRes final int n4) {
        TextViewCompat.IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, n, n2, n3, n4);
    }
    
    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
        TextViewCompat.IMPL.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, drawable, drawable2, drawable3, drawable4);
    }
    
    public static void setTextAppearance(@NonNull final TextView textView, @StyleRes final int n) {
        TextViewCompat.IMPL.setTextAppearance(textView, n);
    }
    
    static class Api23TextViewCompatImpl extends JbMr2TextViewCompatImpl
    {
        @Override
        public void setTextAppearance(@NonNull final TextView textView, @StyleRes final int n) {
            TextViewCompatApi23.setTextAppearance(textView, n);
        }
    }
    
    static class BaseTextViewCompatImpl implements TextViewCompatImpl
    {
        @Override
        public int getMaxLines(final TextView textView) {
            return TextViewCompatDonut.getMaxLines(textView);
        }
        
        @Override
        public int getMinLines(final TextView textView) {
            return TextViewCompatDonut.getMinLines(textView);
        }
        
        @Override
        public void setCompoundDrawablesRelative(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
            textView.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        }
        
        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @DrawableRes final int n, @DrawableRes final int n2, @DrawableRes final int n3, @DrawableRes final int n4) {
            textView.setCompoundDrawablesWithIntrinsicBounds(n, n2, n3, n4);
        }
        
        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        }
        
        @Override
        public void setTextAppearance(final TextView textView, @StyleRes final int n) {
            TextViewCompatDonut.setTextAppearance(textView, n);
        }
    }
    
    static class JbMr1TextViewCompatImpl extends JbTextViewCompatImpl
    {
        @Override
        public void setCompoundDrawablesRelative(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
            TextViewCompatJbMr1.setCompoundDrawablesRelative(textView, drawable, drawable2, drawable3, drawable4);
        }
        
        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @DrawableRes final int n, @DrawableRes final int n2, @DrawableRes final int n3, @DrawableRes final int n4) {
            TextViewCompatJbMr1.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, n, n2, n3, n4);
        }
        
        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
            TextViewCompatJbMr1.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, drawable, drawable2, drawable3, drawable4);
        }
    }
    
    static class JbMr2TextViewCompatImpl extends JbMr1TextViewCompatImpl
    {
        @Override
        public void setCompoundDrawablesRelative(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
            TextViewCompatJbMr2.setCompoundDrawablesRelative(textView, drawable, drawable2, drawable3, drawable4);
        }
        
        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @DrawableRes final int n, @DrawableRes final int n2, @DrawableRes final int n3, @DrawableRes final int n4) {
            TextViewCompatJbMr2.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, n, n2, n3, n4);
        }
        
        @Override
        public void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView textView, @Nullable final Drawable drawable, @Nullable final Drawable drawable2, @Nullable final Drawable drawable3, @Nullable final Drawable drawable4) {
            TextViewCompatJbMr2.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, drawable, drawable2, drawable3, drawable4);
        }
    }
    
    static class JbTextViewCompatImpl extends BaseTextViewCompatImpl
    {
        @Override
        public int getMaxLines(final TextView textView) {
            return TextViewCompatJb.getMaxLines(textView);
        }
        
        @Override
        public int getMinLines(final TextView textView) {
            return TextViewCompatJb.getMinLines(textView);
        }
    }
    
    interface TextViewCompatImpl
    {
        int getMaxLines(final TextView p0);
        
        int getMinLines(final TextView p0);
        
        void setCompoundDrawablesRelative(@NonNull final TextView p0, @Nullable final Drawable p1, @Nullable final Drawable p2, @Nullable final Drawable p3, @Nullable final Drawable p4);
        
        void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView p0, @DrawableRes final int p1, @DrawableRes final int p2, @DrawableRes final int p3, @DrawableRes final int p4);
        
        void setCompoundDrawablesRelativeWithIntrinsicBounds(@NonNull final TextView p0, @Nullable final Drawable p1, @Nullable final Drawable p2, @Nullable final Drawable p3, @Nullable final Drawable p4);
        
        void setTextAppearance(@NonNull final TextView p0, @StyleRes final int p1);
    }
}
