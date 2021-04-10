package com.bumptech.glide.request.target;

import com.bumptech.glide.manager.*;
import com.bumptech.glide.request.*;
import android.graphics.drawable.*;
import com.bumptech.glide.request.animation.*;

public interface Target<R> extends LifecycleListener
{
    public static final int SIZE_ORIGINAL = Integer.MIN_VALUE;
    
    Request getRequest();
    
    void getSize(final SizeReadyCallback p0);
    
    void onLoadCleared(final Drawable p0);
    
    void onLoadFailed(final Exception p0, final Drawable p1);
    
    void onLoadStarted(final Drawable p0);
    
    void onResourceReady(final R p0, final GlideAnimation<? super R> p1);
    
    void setRequest(final Request p0);
}
