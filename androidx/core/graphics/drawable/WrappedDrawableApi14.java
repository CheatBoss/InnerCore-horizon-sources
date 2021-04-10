package androidx.core.graphics.drawable;

import android.content.res.*;
import android.graphics.drawable.*;
import androidx.annotation.*;
import android.graphics.*;

class WrappedDrawableApi14 extends Drawable implements Drawable$Callback, WrappedDrawable, TintAwareDrawable
{
    static final PorterDuff$Mode DEFAULT_TINT_MODE;
    private boolean mColorFilterSet;
    private int mCurrentColor;
    private PorterDuff$Mode mCurrentMode;
    Drawable mDrawable;
    private boolean mMutated;
    WrappedDrawableState mState;
    
    static {
        DEFAULT_TINT_MODE = PorterDuff$Mode.SRC_IN;
    }
    
    WrappedDrawableApi14(@Nullable final Drawable wrappedDrawable) {
        this.mState = this.mutateConstantState();
        this.setWrappedDrawable(wrappedDrawable);
    }
    
    WrappedDrawableApi14(@NonNull final WrappedDrawableState mState, @Nullable final Resources resources) {
        this.mState = mState;
        this.updateLocalState(resources);
    }
    
    @NonNull
    private WrappedDrawableState mutateConstantState() {
        return new WrappedDrawableState(this.mState);
    }
    
    private void updateLocalState(@Nullable final Resources resources) {
        if (this.mState != null && this.mState.mDrawableState != null) {
            this.setWrappedDrawable(this.mState.mDrawableState.newDrawable(resources));
        }
    }
    
    private boolean updateTint(final int[] array) {
        if (!this.isCompatTintEnabled()) {
            return false;
        }
        final ColorStateList mTint = this.mState.mTint;
        final PorterDuff$Mode mTintMode = this.mState.mTintMode;
        if (mTint == null || mTintMode == null) {
            this.mColorFilterSet = false;
            this.clearColorFilter();
            return false;
        }
        final int colorForState = mTint.getColorForState(array, mTint.getDefaultColor());
        if (this.mColorFilterSet && colorForState == this.mCurrentColor && mTintMode == this.mCurrentMode) {
            return false;
        }
        this.setColorFilter(colorForState, mTintMode);
        this.mCurrentColor = colorForState;
        this.mCurrentMode = mTintMode;
        return this.mColorFilterSet = true;
    }
    
    public void draw(@NonNull final Canvas canvas) {
        this.mDrawable.draw(canvas);
    }
    
    public int getChangingConfigurations() {
        final int changingConfigurations = super.getChangingConfigurations();
        int changingConfigurations2;
        if (this.mState != null) {
            changingConfigurations2 = this.mState.getChangingConfigurations();
        }
        else {
            changingConfigurations2 = 0;
        }
        return changingConfigurations | changingConfigurations2 | this.mDrawable.getChangingConfigurations();
    }
    
    @Nullable
    public Drawable$ConstantState getConstantState() {
        if (this.mState != null && this.mState.canConstantState()) {
            this.mState.mChangingConfigurations = this.getChangingConfigurations();
            return this.mState;
        }
        return null;
    }
    
    @NonNull
    public Drawable getCurrent() {
        return this.mDrawable.getCurrent();
    }
    
    public int getIntrinsicHeight() {
        return this.mDrawable.getIntrinsicHeight();
    }
    
    public int getIntrinsicWidth() {
        return this.mDrawable.getIntrinsicWidth();
    }
    
    public int getMinimumHeight() {
        return this.mDrawable.getMinimumHeight();
    }
    
    public int getMinimumWidth() {
        return this.mDrawable.getMinimumWidth();
    }
    
    public int getOpacity() {
        return this.mDrawable.getOpacity();
    }
    
    public boolean getPadding(@NonNull final Rect rect) {
        return this.mDrawable.getPadding(rect);
    }
    
    @NonNull
    public int[] getState() {
        return this.mDrawable.getState();
    }
    
    public Region getTransparentRegion() {
        return this.mDrawable.getTransparentRegion();
    }
    
    public final Drawable getWrappedDrawable() {
        return this.mDrawable;
    }
    
    public void invalidateDrawable(@NonNull final Drawable drawable) {
        this.invalidateSelf();
    }
    
    @RequiresApi(19)
    public boolean isAutoMirrored() {
        return this.mDrawable.isAutoMirrored();
    }
    
    protected boolean isCompatTintEnabled() {
        return true;
    }
    
    public boolean isStateful() {
        ColorStateList mTint;
        if (this.isCompatTintEnabled() && this.mState != null) {
            mTint = this.mState.mTint;
        }
        else {
            mTint = null;
        }
        return (mTint != null && mTint.isStateful()) || this.mDrawable.isStateful();
    }
    
    public void jumpToCurrentState() {
        this.mDrawable.jumpToCurrentState();
    }
    
    @NonNull
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState = this.mutateConstantState();
            if (this.mDrawable != null) {
                this.mDrawable.mutate();
            }
            if (this.mState != null) {
                final WrappedDrawableState mState = this.mState;
                Drawable$ConstantState constantState;
                if (this.mDrawable != null) {
                    constantState = this.mDrawable.getConstantState();
                }
                else {
                    constantState = null;
                }
                mState.mDrawableState = constantState;
            }
            this.mMutated = true;
        }
        return this;
    }
    
    protected void onBoundsChange(final Rect bounds) {
        if (this.mDrawable != null) {
            this.mDrawable.setBounds(bounds);
        }
    }
    
    protected boolean onLevelChange(final int level) {
        return this.mDrawable.setLevel(level);
    }
    
    public void scheduleDrawable(@NonNull final Drawable drawable, @NonNull final Runnable runnable, final long n) {
        this.scheduleSelf(runnable, n);
    }
    
    public void setAlpha(final int alpha) {
        this.mDrawable.setAlpha(alpha);
    }
    
    @RequiresApi(19)
    public void setAutoMirrored(final boolean autoMirrored) {
        this.mDrawable.setAutoMirrored(autoMirrored);
    }
    
    public void setChangingConfigurations(final int changingConfigurations) {
        this.mDrawable.setChangingConfigurations(changingConfigurations);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mDrawable.setColorFilter(colorFilter);
    }
    
    public void setDither(final boolean dither) {
        this.mDrawable.setDither(dither);
    }
    
    public void setFilterBitmap(final boolean filterBitmap) {
        this.mDrawable.setFilterBitmap(filterBitmap);
    }
    
    public boolean setState(@NonNull final int[] state) {
        final boolean setState = this.mDrawable.setState(state);
        return this.updateTint(state) || setState;
    }
    
    public void setTint(final int n) {
        this.setTintList(ColorStateList.valueOf(n));
    }
    
    public void setTintList(final ColorStateList mTint) {
        this.mState.mTint = mTint;
        this.updateTint(this.getState());
    }
    
    public void setTintMode(@NonNull final PorterDuff$Mode mTintMode) {
        this.mState.mTintMode = mTintMode;
        this.updateTint(this.getState());
    }
    
    public boolean setVisible(final boolean b, final boolean b2) {
        return super.setVisible(b, b2) || this.mDrawable.setVisible(b, b2);
    }
    
    public final void setWrappedDrawable(final Drawable mDrawable) {
        if (this.mDrawable != null) {
            this.mDrawable.setCallback((Drawable$Callback)null);
        }
        if ((this.mDrawable = mDrawable) != null) {
            mDrawable.setCallback((Drawable$Callback)this);
            this.setVisible(mDrawable.isVisible(), true);
            this.setState(mDrawable.getState());
            this.setLevel(mDrawable.getLevel());
            this.setBounds(mDrawable.getBounds());
            if (this.mState != null) {
                this.mState.mDrawableState = mDrawable.getConstantState();
            }
        }
        this.invalidateSelf();
    }
    
    public void unscheduleDrawable(@NonNull final Drawable drawable, @NonNull final Runnable runnable) {
        this.unscheduleSelf(runnable);
    }
}
