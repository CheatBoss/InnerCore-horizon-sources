package com.microsoft.xbox.toolkit.anim;

public class BackEaseInterpolator extends XLEInterpolator
{
    private float amplitude;
    
    public BackEaseInterpolator(final float amplitude, final EasingMode easingMode) {
        super(easingMode);
        this.amplitude = amplitude;
    }
    
    @Override
    protected float getInterpolationCore(float n) {
        n = (float)Math.max(n, 0.0);
        final double n2 = n * n * n;
        final double n3 = this.amplitude * n;
        final double n4 = n;
        Double.isNaN(n4);
        final double sin = Math.sin(n4 * 3.141592653589793);
        Double.isNaN(n3);
        Double.isNaN(n2);
        return (float)(n2 - n3 * sin);
    }
}
