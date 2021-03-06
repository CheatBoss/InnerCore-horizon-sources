package android.support.v4.graphics.drawable;

import android.support.annotation.*;
import android.content.res.*;
import android.graphics.drawable.*;
import android.graphics.*;

class DrawableWrapperDonut extends Drawable implements Drawable$Callback, DrawableWrapper
{
    static final PorterDuff$Mode DEFAULT_TINT_MODE;
    private boolean mColorFilterSet;
    private int mCurrentColor;
    private PorterDuff$Mode mCurrentMode;
    Drawable mDrawable;
    private boolean mMutated;
    DrawableWrapperState mState;
    
    static {
        DEFAULT_TINT_MODE = PorterDuff$Mode.SRC_IN;
    }
    
    DrawableWrapperDonut(@Nullable final Drawable wrappedDrawable) {
        if (wrappedDrawable != null && wrappedDrawable.getConstantState() != null) {
            this.mState = this.mutateConstantState();
        }
        this.setWrappedDrawable(wrappedDrawable);
    }
    
    DrawableWrapperDonut(@NonNull final DrawableWrapperState mState, @Nullable final Resources resources) {
        this.mState = mState;
        this.updateLocalState(resources);
    }
    
    private void updateLocalState(@Nullable final Resources resources) {
        if (this.mState != null && this.mState.mDrawableState != null) {
            this.setWrappedDrawable(this.newDrawableFromState(this.mState.mDrawableState, resources));
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
    
    public void draw(final Canvas canvas) {
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
    
    public boolean getPadding(final Rect rect) {
        return this.mDrawable.getPadding(rect);
    }
    
    public int[] getState() {
        return this.mDrawable.getState();
    }
    
    public Region getTransparentRegion() {
        return this.mDrawable.getTransparentRegion();
    }
    
    public final Drawable getWrappedDrawable() {
        return this.mDrawable;
    }
    
    public void invalidateDrawable(final Drawable drawable) {
        this.invalidateSelf();
    }
    
    protected boolean isCompatTintEnabled() {
        return true;
    }
    
    public boolean isStateful() {
        ColorStateList mTint;
        if (this.isCompatTintEnabled()) {
            mTint = this.mState.mTint;
        }
        else {
            mTint = null;
        }
        return (mTint != null && mTint.isStateful()) || this.mDrawable.isStateful();
    }
    
    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState = this.mutateConstantState();
            if (this.mDrawable != null) {
                this.mDrawable.mutate();
            }
            if (this.mState != null) {
                final DrawableWrapperState mState = this.mState;
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
    
    @NonNull
    DrawableWrapperState mutateConstantState() {
        return (DrawableWrapperState)new DrawableWrapperStateDonut(this.mState, null);
    }
    
    protected Drawable newDrawableFromState(@NonNull final Drawable$ConstantState drawable$ConstantState, @Nullable final Resources resources) {
        return drawable$ConstantState.newDrawable();
    }
    
    protected void onBoundsChange(final Rect bounds) {
        if (this.mDrawable != null) {
            this.mDrawable.setBounds(bounds);
        }
    }
    
    protected boolean onLevelChange(final int level) {
        return this.mDrawable.setLevel(level);
    }
    
    public void scheduleDrawable(final Drawable drawable, final Runnable runnable, final long n) {
        this.scheduleSelf(runnable, n);
    }
    
    public void setAlpha(final int alpha) {
        this.mDrawable.setAlpha(alpha);
    }
    
    public void setChangingConfigurations(final int changingConfigurations) {
        this.mDrawable.setChangingConfigurations(changingConfigurations);
    }
    
    public void setColorFilter(final ColorFilter colorFilter) {
        this.mDrawable.setColorFilter(colorFilter);
    }
    
    public void setCompatTint(final int n) {
        this.setCompatTintList(ColorStateList.valueOf(n));
    }
    
    public void setCompatTintList(final ColorStateList mTint) {
        this.mState.mTint = mTint;
        this.updateTint(this.getState());
    }
    
    public void setCompatTintMode(final PorterDuff$Mode mTintMode) {
        this.mState.mTintMode = mTintMode;
        this.updateTint(this.getState());
    }
    
    public void setDither(final boolean dither) {
        this.mDrawable.setDither(dither);
    }
    
    public void setFilterBitmap(final boolean filterBitmap) {
        this.mDrawable.setFilterBitmap(filterBitmap);
    }
    
    public boolean setState(final int[] state) {
        final boolean setState = this.mDrawable.setState(state);
        return this.updateTint(state) || setState;
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
            mDrawable.setVisible(this.isVisible(), true);
            mDrawable.setState(this.getState());
            mDrawable.setLevel(this.getLevel());
            mDrawable.setBounds(this.getBounds());
            if (this.mState != null) {
                this.mState.mDrawableState = mDrawable.getConstantState();
            }
        }
        this.invalidateSelf();
    }
    
    public void unscheduleDrawable(final Drawable drawable, final Runnable runnable) {
        this.unscheduleSelf(runnable);
    }
    
    protected abstract static class DrawableWrapperState extends Drawable$ConstantState
    {
        int mChangingConfigurations;
        Drawable$ConstantState mDrawableState;
        ColorStateList mTint;
        PorterDuff$Mode mTintMode;
        
        DrawableWrapperState(@Nullable final DrawableWrapperState drawableWrapperState, @Nullable final Resources resources) {
            this.mTint = null;
            this.mTintMode = DrawableWrapperDonut.DEFAULT_TINT_MODE;
            if (drawableWrapperState != null) {
                this.mChangingConfigurations = drawableWrapperState.mChangingConfigurations;
                this.mDrawableState = drawableWrapperState.mDrawableState;
                this.mTint = drawableWrapperState.mTint;
                this.mTintMode = drawableWrapperState.mTintMode;
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
        
        public Drawable newDrawable() {
            return this.newDrawable(null);
        }
        
        public abstract Drawable newDrawable(@Nullable final Resources p0);
    }
    
    private static class DrawableWrapperStateDonut extends DrawableWrapperState
    {
        DrawableWrapperStateDonut(@Nullable final DrawableWrapperState drawableWrapperState, @Nullable final Resources resources) {
            super(drawableWrapperState, resources);
        }
        
        @Override
        public Drawable newDrawable(@Nullable final Resources resources) {
            return new DrawableWrapperDonut((DrawableWrapperState)this, resources);
        }
    }
}
