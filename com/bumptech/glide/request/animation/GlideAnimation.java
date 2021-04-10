package com.bumptech.glide.request.animation;

import android.graphics.drawable.*;
import android.view.*;

public interface GlideAnimation<R>
{
    boolean animate(final R p0, final ViewAdapter p1);
    
    public interface ViewAdapter
    {
        Drawable getCurrentDrawable();
        
        View getView();
        
        void setDrawable(final Drawable p0);
    }
}
