package com.microsoft.xbox.toolkit.anim;

import android.widget.*;
import com.microsoft.xbox.toolkit.*;
import android.view.animation.*;
import android.view.*;

public class XLEAnimationAbsListView extends XLEAnimation
{
    private LayoutAnimationController layoutAnimationController;
    private AbsListView layoutView;
    
    public XLEAnimationAbsListView(final LayoutAnimationController layoutAnimationController) {
        this.layoutAnimationController = null;
        this.layoutView = null;
        this.layoutAnimationController = layoutAnimationController;
        XLEAssert.assertTrue(layoutAnimationController != null);
    }
    
    @Override
    public void clear() {
        this.layoutView.setLayoutAnimationListener((Animation$AnimationListener)null);
        this.layoutView.clearAnimation();
    }
    
    @Override
    public void setInterpolator(final Interpolator interpolator) {
        this.layoutAnimationController.setInterpolator(interpolator);
    }
    
    @Override
    public void setTargetView(final View view) {
        XLEAssert.assertNotNull(view);
        XLEAssert.assertTrue(view instanceof AbsListView);
        this.layoutView = (AbsListView)view;
    }
    
    @Override
    public void start() {
        this.layoutView.setLayoutAnimation(this.layoutAnimationController);
        if (this.endRunnable != null) {
            this.endRunnable.run();
        }
    }
}
