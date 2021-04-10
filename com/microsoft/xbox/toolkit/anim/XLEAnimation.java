package com.microsoft.xbox.toolkit.anim;

import android.view.animation.*;
import com.microsoft.xbox.toolkit.*;
import android.view.*;

public abstract class XLEAnimation
{
    protected Runnable endRunnable;
    
    public abstract void clear();
    
    public abstract void setInterpolator(final Interpolator p0);
    
    public void setOnAnimationEnd(final Runnable runnable) {
        if (runnable != null) {
            this.endRunnable = new Runnable() {
                @Override
                public void run() {
                    ThreadManager.UIThreadPost(runnable);
                }
            };
            return;
        }
        this.endRunnable = null;
    }
    
    public abstract void setTargetView(final View p0);
    
    public abstract void start();
}
