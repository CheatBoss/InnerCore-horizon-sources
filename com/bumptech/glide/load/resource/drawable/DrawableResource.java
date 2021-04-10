package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.*;
import com.bumptech.glide.load.engine.*;

public abstract class DrawableResource<T extends Drawable> implements Resource<T>
{
    protected final T drawable;
    
    public DrawableResource(final T drawable) {
        if (drawable == null) {
            throw new NullPointerException("Drawable must not be null!");
        }
        this.drawable = drawable;
    }
    
    @Override
    public final T get() {
        return (T)this.drawable.getConstantState().newDrawable();
    }
}
