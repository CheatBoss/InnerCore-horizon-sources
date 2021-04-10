package android.support.v4.graphics.drawable;

import android.content.res.*;
import android.graphics.drawable.*;
import android.support.annotation.*;

class DrawableWrapperEclair extends DrawableWrapperDonut
{
    DrawableWrapperEclair(final Drawable drawable) {
        super(drawable);
    }
    
    DrawableWrapperEclair(final DrawableWrapperState drawableWrapperState, final Resources resources) {
        super(drawableWrapperState, resources);
    }
    
    @Override
    DrawableWrapperState mutateConstantState() {
        return new DrawableWrapperStateEclair(this.mState, null);
    }
    
    @Override
    protected Drawable newDrawableFromState(final Drawable$ConstantState drawable$ConstantState, final Resources resources) {
        return drawable$ConstantState.newDrawable(resources);
    }
    
    private static class DrawableWrapperStateEclair extends DrawableWrapperState
    {
        DrawableWrapperStateEclair(@Nullable final DrawableWrapperState drawableWrapperState, @Nullable final Resources resources) {
            super(drawableWrapperState, resources);
        }
        
        @Override
        public Drawable newDrawable(@Nullable final Resources resources) {
            return new DrawableWrapperEclair(this, resources);
        }
    }
}
