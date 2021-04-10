package com.bumptech.glide.util;

import com.bumptech.glide.*;
import android.view.*;
import java.util.*;
import com.bumptech.glide.request.target.*;
import com.bumptech.glide.request.animation.*;

public class ViewPreloadSizeProvider<T> implements PreloadSizeProvider<T>, SizeReadyCallback
{
    private int[] size;
    private SizeViewTarget viewTarget;
    
    public ViewPreloadSizeProvider() {
    }
    
    public ViewPreloadSizeProvider(final View view) {
        this.setView(view);
    }
    
    @Override
    public int[] getPreloadSize(final T t, final int n, final int n2) {
        if (this.size == null) {
            return null;
        }
        return Arrays.copyOf(this.size, this.size.length);
    }
    
    @Override
    public void onSizeReady(final int n, final int n2) {
        this.size = new int[] { n, n2 };
        this.viewTarget = null;
    }
    
    public void setView(final View view) {
        if (this.size != null) {
            return;
        }
        if (this.viewTarget != null) {
            return;
        }
        this.viewTarget = new SizeViewTarget(view, this);
    }
    
    private static final class SizeViewTarget extends ViewTarget<View, Object>
    {
        public SizeViewTarget(final View view, final SizeReadyCallback sizeReadyCallback) {
            super(view);
            this.getSize(sizeReadyCallback);
        }
        
        @Override
        public void onResourceReady(final Object o, final GlideAnimation glideAnimation) {
        }
    }
}
