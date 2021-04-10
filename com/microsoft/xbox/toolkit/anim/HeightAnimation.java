package com.microsoft.xbox.toolkit.anim;

import android.view.*;
import android.view.animation.*;

public class HeightAnimation extends Animation
{
    private int fromValue;
    private int toValue;
    private View view;
    
    public HeightAnimation(final int fromValue, final int toValue) {
        this.fromValue = fromValue;
        this.toValue = toValue;
    }
    
    protected void applyTransformation(final float n, final Transformation transformation) {
        this.view.getLayoutParams().height = this.fromValue + (int)((this.toValue - this.fromValue) * n);
        this.view.requestLayout();
    }
    
    public void setTargetView(final View view) {
        this.view = view;
        this.fromValue = view.getHeight();
    }
    
    public boolean willChangeBounds() {
        return true;
    }
}
