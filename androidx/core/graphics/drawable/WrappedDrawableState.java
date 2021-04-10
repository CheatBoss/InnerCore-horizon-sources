package androidx.core.graphics.drawable;

import android.graphics.*;
import android.graphics.drawable.*;
import android.content.res.*;
import androidx.annotation.*;
import android.os.*;

final class WrappedDrawableState extends Drawable$ConstantState
{
    int mChangingConfigurations;
    Drawable$ConstantState mDrawableState;
    ColorStateList mTint;
    PorterDuff$Mode mTintMode;
    
    WrappedDrawableState(@Nullable final WrappedDrawableState wrappedDrawableState) {
        this.mTint = null;
        this.mTintMode = WrappedDrawableApi14.DEFAULT_TINT_MODE;
        if (wrappedDrawableState != null) {
            this.mChangingConfigurations = wrappedDrawableState.mChangingConfigurations;
            this.mDrawableState = wrappedDrawableState.mDrawableState;
            this.mTint = wrappedDrawableState.mTint;
            this.mTintMode = wrappedDrawableState.mTintMode;
        }
    }
    
    boolean canConstantState() {
        return this.mDrawableState != null;
    }
    
    public int getChangingConfigurations() {
        final int mChangingConfigurations = this.mChangingConfigurations;
        int changingConfigurations;
        if (this.mDrawableState != null) {
            changingConfigurations = this.mDrawableState.getChangingConfigurations();
        }
        else {
            changingConfigurations = 0;
        }
        return mChangingConfigurations | changingConfigurations;
    }
    
    @NonNull
    public Drawable newDrawable() {
        return this.newDrawable(null);
    }
    
    @NonNull
    public Drawable newDrawable(@Nullable final Resources resources) {
        if (Build$VERSION.SDK_INT >= 21) {
            return new WrappedDrawableApi21(this, resources);
        }
        return new WrappedDrawableApi14(this, resources);
    }
}
