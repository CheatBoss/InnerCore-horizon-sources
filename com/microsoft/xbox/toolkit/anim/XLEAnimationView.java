package com.microsoft.xbox.toolkit.anim;

import android.view.*;
import android.graphics.*;
import com.microsoft.xbox.toolkit.*;
import android.view.animation.*;
import java.util.*;

public class XLEAnimationView extends XLEAnimation
{
    private Animation anim;
    private View animtarget;
    
    public XLEAnimationView(final Animation anim) {
        (this.anim = anim).setFillAfter(true);
        this.anim.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener() {
            public void onAnimationEnd(final Animation animation) {
                XLEAnimationView.this.onViewAnimationEnd();
                if (XLEAnimationView.this.endRunnable != null) {
                    XLEAnimationView.this.endRunnable.run();
                }
            }
            
            public void onAnimationRepeat(final Animation animation) {
            }
            
            public void onAnimationStart(final Animation animation) {
                XLEAnimationView.this.onViewAnimationStart();
            }
        });
    }
    
    private void onViewAnimationEnd() {
        ThreadManager.UIThreadPost(new Runnable() {
            @Override
            public void run() {
                XLEAnimationView.this.animtarget.setLayerType(0, (Paint)null);
            }
        });
    }
    
    private void onViewAnimationStart() {
        this.animtarget.setLayerType(2, (Paint)null);
    }
    
    @Override
    public void clear() {
        this.anim.setAnimationListener((Animation$AnimationListener)null);
        this.animtarget.clearAnimation();
    }
    
    public void setFillAfter(final boolean fillAfter) {
        this.anim.setFillAfter(fillAfter);
    }
    
    @Override
    public void setInterpolator(final Interpolator interpolator) {
        this.anim.setInterpolator(interpolator);
    }
    
    @Override
    public void setTargetView(final View view) {
        XLEAssert.assertNotNull(view);
        this.animtarget = view;
        final Animation anim = this.anim;
        if (anim instanceof AnimationSet) {
            for (final Animation animation : ((AnimationSet)anim).getAnimations()) {
                if (animation instanceof HeightAnimation) {
                    ((HeightAnimation)animation).setTargetView(view);
                }
            }
        }
    }
    
    @Override
    public void start() {
        this.animtarget.startAnimation(this.anim);
    }
}
