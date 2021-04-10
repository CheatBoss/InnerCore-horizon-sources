package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.*;

public abstract class GlideDrawable extends Drawable implements Animatable
{
    public static final int LOOP_FOREVER = -1;
    public static final int LOOP_INTRINSIC = 0;
    
    public abstract boolean isAnimated();
    
    public abstract void setLoopCount(final int p0);
}
