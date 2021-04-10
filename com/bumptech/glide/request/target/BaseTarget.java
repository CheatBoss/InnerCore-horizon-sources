package com.bumptech.glide.request.target;

import com.bumptech.glide.request.*;
import android.graphics.drawable.*;

public abstract class BaseTarget<Z> implements Target<Z>
{
    private Request request;
    
    @Override
    public Request getRequest() {
        return this.request;
    }
    
    @Override
    public void onDestroy() {
    }
    
    @Override
    public void onLoadCleared(final Drawable drawable) {
    }
    
    @Override
    public void onLoadFailed(final Exception ex, final Drawable drawable) {
    }
    
    @Override
    public void onLoadStarted(final Drawable drawable) {
    }
    
    @Override
    public void onStart() {
    }
    
    @Override
    public void onStop() {
    }
    
    @Override
    public void setRequest(final Request request) {
        this.request = request;
    }
}
