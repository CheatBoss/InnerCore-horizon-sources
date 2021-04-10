package android.support.v4.widget;

import android.os.*;
import android.widget.*;
import android.graphics.drawable.*;
import android.support.annotation.*;
import android.content.res.*;
import android.graphics.*;

public final class CompoundButtonCompat
{
    private static final CompoundButtonCompatImpl IMPL;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        CompoundButtonCompatImpl impl;
        if (sdk_INT >= 23) {
            impl = new Api23CompoundButtonImpl();
        }
        else if (sdk_INT >= 21) {
            impl = new LollipopCompoundButtonImpl();
        }
        else {
            impl = new BaseCompoundButtonCompat();
        }
        IMPL = impl;
    }
    
    private CompoundButtonCompat() {
    }
    
    @Nullable
    public static Drawable getButtonDrawable(@NonNull final CompoundButton compoundButton) {
        return CompoundButtonCompat.IMPL.getButtonDrawable(compoundButton);
    }
    
    @Nullable
    public static ColorStateList getButtonTintList(@NonNull final CompoundButton compoundButton) {
        return CompoundButtonCompat.IMPL.getButtonTintList(compoundButton);
    }
    
    @Nullable
    public static PorterDuff$Mode getButtonTintMode(@NonNull final CompoundButton compoundButton) {
        return CompoundButtonCompat.IMPL.getButtonTintMode(compoundButton);
    }
    
    public static void setButtonTintList(@NonNull final CompoundButton compoundButton, @Nullable final ColorStateList list) {
        CompoundButtonCompat.IMPL.setButtonTintList(compoundButton, list);
    }
    
    public static void setButtonTintMode(@NonNull final CompoundButton compoundButton, @Nullable final PorterDuff$Mode porterDuff$Mode) {
        CompoundButtonCompat.IMPL.setButtonTintMode(compoundButton, porterDuff$Mode);
    }
    
    static class Api23CompoundButtonImpl extends LollipopCompoundButtonImpl
    {
        @Override
        public Drawable getButtonDrawable(final CompoundButton compoundButton) {
            return CompoundButtonCompatApi23.getButtonDrawable(compoundButton);
        }
    }
    
    static class BaseCompoundButtonCompat implements CompoundButtonCompatImpl
    {
        @Override
        public Drawable getButtonDrawable(final CompoundButton compoundButton) {
            return CompoundButtonCompatDonut.getButtonDrawable(compoundButton);
        }
        
        @Override
        public ColorStateList getButtonTintList(final CompoundButton compoundButton) {
            return CompoundButtonCompatDonut.getButtonTintList(compoundButton);
        }
        
        @Override
        public PorterDuff$Mode getButtonTintMode(final CompoundButton compoundButton) {
            return CompoundButtonCompatDonut.getButtonTintMode(compoundButton);
        }
        
        @Override
        public void setButtonTintList(final CompoundButton compoundButton, final ColorStateList list) {
            CompoundButtonCompatDonut.setButtonTintList(compoundButton, list);
        }
        
        @Override
        public void setButtonTintMode(final CompoundButton compoundButton, final PorterDuff$Mode porterDuff$Mode) {
            CompoundButtonCompatDonut.setButtonTintMode(compoundButton, porterDuff$Mode);
        }
    }
    
    interface CompoundButtonCompatImpl
    {
        Drawable getButtonDrawable(final CompoundButton p0);
        
        ColorStateList getButtonTintList(final CompoundButton p0);
        
        PorterDuff$Mode getButtonTintMode(final CompoundButton p0);
        
        void setButtonTintList(final CompoundButton p0, final ColorStateList p1);
        
        void setButtonTintMode(final CompoundButton p0, final PorterDuff$Mode p1);
    }
    
    static class LollipopCompoundButtonImpl extends BaseCompoundButtonCompat
    {
        @Override
        public ColorStateList getButtonTintList(final CompoundButton compoundButton) {
            return CompoundButtonCompatLollipop.getButtonTintList(compoundButton);
        }
        
        @Override
        public PorterDuff$Mode getButtonTintMode(final CompoundButton compoundButton) {
            return CompoundButtonCompatLollipop.getButtonTintMode(compoundButton);
        }
        
        @Override
        public void setButtonTintList(final CompoundButton compoundButton, final ColorStateList list) {
            CompoundButtonCompatLollipop.setButtonTintList(compoundButton, list);
        }
        
        @Override
        public void setButtonTintMode(final CompoundButton compoundButton, final PorterDuff$Mode porterDuff$Mode) {
            CompoundButtonCompatLollipop.setButtonTintMode(compoundButton, porterDuff$Mode);
        }
    }
}
