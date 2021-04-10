package com.microsoft.xbox.toolkit.anim;

public class ExponentialInterpolator extends XLEInterpolator
{
    private float exponent;
    
    public ExponentialInterpolator(final float exponent, final EasingMode easingMode) {
        super(easingMode);
        this.exponent = exponent;
    }
    
    @Override
    protected float getInterpolationCore(final float n) {
        return (float)((Math.pow(2.718281828459045, this.exponent * n) - 1.0) / (Math.pow(2.718281828459045, this.exponent) - 1.0));
    }
}
