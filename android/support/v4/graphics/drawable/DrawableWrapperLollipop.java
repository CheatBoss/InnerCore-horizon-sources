package android.support.v4.graphics.drawable;

import android.os.*;
import android.graphics.drawable.*;
import android.content.res.*;
import android.graphics.*;
import android.support.annotation.*;

class DrawableWrapperLollipop extends DrawableWrapperKitKat
{
    DrawableWrapperLollipop(final Drawable drawable) {
        super(drawable);
    }
    
    DrawableWrapperLollipop(final DrawableWrapperState drawableWrapperState, final Resources resources) {
        super(drawableWrapperState, resources);
    }
    
    public Rect getDirtyBounds() {
        return this.mDrawable.getDirtyBounds();
    }
    
    public void getOutline(final Outline outline) {
        this.mDrawable.getOutline(outline);
    }
    
    @Override
    protected boolean isCompatTintEnabled() {
        if (Build$VERSION.SDK_INT == 21) {
            final Drawable mDrawable = this.mDrawable;
            if (mDrawable instanceof GradientDrawable || mDrawable instanceof DrawableContainer || mDrawable instanceof InsetDrawable) {
                return true;
            }
        }
        return false;
    }
    
    @NonNull
    @Override
    DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperStateLollipop(this.mState, null);
    }
    
    public void setHotspot(final float n, final float n2) {
        this.mDrawable.setHotspot(n, n2);
    }
    
    public void setHotspotBounds(final int n, final int n2, final int n3, final int n4) {
        this.mDrawable.setHotspotBounds(n, n2, n3, n4);
    }
    
    @Override
    public boolean setState(final int[] state) {
        if (super.setState(state)) {
            this.invalidateSelf();
            return true;
        }
        return false;
    }
    
    public void setTint(final int n) {
        if (this.isCompatTintEnabled()) {
            this.setCompatTint(n);
            return;
        }
        this.mDrawable.setTint(n);
    }
    
    public void setTintList(final ColorStateList list) {
        if (this.isCompatTintEnabled()) {
            this.setCompatTintList(list);
            return;
        }
        this.mDrawable.setTintList(list);
    }
    
    public void setTintMode(final PorterDuff$Mode porterDuff$Mode) {
        if (this.isCompatTintEnabled()) {
            this.setCompatTintMode(porterDuff$Mode);
            return;
        }
        this.mDrawable.setTintMode(porterDuff$Mode);
    }
    
    private static class DrawableWrapperStateLollipop extends DrawableWrapperState
    {
        DrawableWrapperStateLollipop(@Nullable final DrawableWrapperState drawableWrapperState, @Nullable final Resources resources) {
            super(drawableWrapperState, resources);
        }
        
        @Override
        public Drawable newDrawable(@Nullable final Resources resources) {
            return new DrawableWrapperLollipop(this, resources);
        }
    }
}
