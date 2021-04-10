package com.microsoft.xbox.toolkit.anim;

public class SineInterpolator extends XLEInterpolator
{
    public SineInterpolator(final EasingMode easingMode) {
        super(easingMode);
    }
    
    @Override
    protected float getInterpolationCore(final float n) {
        final double n2 = n;
        Double.isNaN(n2);
        return (float)(1.0 - Math.sin((1.0 - n2) * 1.5707963267948966));
    }
}
