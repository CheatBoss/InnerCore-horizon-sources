package com.microsoft.xbox.toolkit.anim;

import android.view.animation.*;

public class XLEInterpolator implements Interpolator
{
    private EasingMode easingMode;
    
    public XLEInterpolator(final EasingMode easingMode) {
        this.easingMode = easingMode;
    }
    
    public float getInterpolation(final float n) {
        if (n < 0.0f || n > 1.0f) {
            throw new IllegalArgumentException("should respect 0<=normalizedTime<=1");
        }
        final int n2 = XLEInterpolator$1.$SwitchMap$com$microsoft$xbox$toolkit$anim$EasingMode[this.easingMode.ordinal()];
        if (n2 == 1) {
            return this.getInterpolationCore(n);
        }
        if (n2 == 2) {
            return 1.0f - this.getInterpolationCore(1.0f - n);
        }
        if (n2 != 3) {
            return n;
        }
        if (n < 0.5) {
            return this.getInterpolationCore(n * 2.0f) / 2.0f;
        }
        return (1.0f - this.getInterpolationCore(2.0f - n * 2.0f)) / 2.0f + 0.5f;
    }
    
    protected float getInterpolationCore(final float n) {
        return n;
    }
}
