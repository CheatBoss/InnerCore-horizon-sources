package com.microsoft.xbox.xle.anim;

import org.simpleframework.xml.*;
import com.microsoft.xbox.toolkit.*;
import com.microsoft.xboxtcui.*;
import com.microsoft.xbox.toolkit.anim.*;
import android.view.animation.*;

public class XLEAnimationDefinition
{
    @Attribute(required = false)
    public int delayMs;
    @Attribute(required = false)
    public String dimen;
    @Attribute(required = false)
    public int durationMs;
    @Attribute(required = false)
    public EasingMode easing;
    @Attribute(required = false)
    public float from;
    @Attribute(required = false)
    public int fromXType;
    @Attribute(required = false)
    public int fromYType;
    @Attribute(required = false)
    public float parameter;
    @Attribute(required = false)
    public float pivotX;
    @Attribute(required = false)
    public float pivotY;
    @Attribute(required = false)
    public AnimationProperty property;
    @Attribute(required = false)
    public boolean scaleRelativeToSelf;
    @Attribute(required = false)
    public float to;
    @Attribute(required = false)
    public int toXType;
    @Attribute(required = false)
    public int toYType;
    @Attribute(required = false)
    public AnimationFunctionType type;
    
    public XLEAnimationDefinition() {
        this.easing = EasingMode.EaseIn;
        this.pivotX = 0.5f;
        this.pivotY = 0.5f;
        this.scaleRelativeToSelf = true;
        this.fromXType = 1;
        this.toXType = 1;
        this.fromYType = 1;
        this.toYType = 1;
    }
    
    private Interpolator getInterpolator() {
        final int n = XLEAnimationDefinition$1.$SwitchMap$com$microsoft$xbox$toolkit$anim$AnimationFunctionType[this.type.ordinal()];
        if (n == 1) {
            return (Interpolator)new SineInterpolator(this.easing);
        }
        if (n == 2) {
            return (Interpolator)new ExponentialInterpolator(this.parameter, this.easing);
        }
        if (n != 3) {
            return (Interpolator)new XLEInterpolator(this.easing);
        }
        return (Interpolator)new BackEaseInterpolator(this.parameter, this.easing);
    }
    
    public Animation getAnimation() {
        final Interpolator interpolator = this.getInterpolator();
        final int n = XLEAnimationDefinition$1.$SwitchMap$com$microsoft$xbox$toolkit$anim$AnimationProperty[this.property.ordinal()];
        Object o;
        if (n != 1) {
            if (n != 2) {
                if (n != 3) {
                    if (n != 4) {
                        if (n != 5) {
                            o = null;
                        }
                        else {
                            final int dimensionIdByName = XLERValueHelper.findDimensionIdByName(this.dimen);
                            int dimensionPixelSize;
                            if (dimensionIdByName >= 0) {
                                dimensionPixelSize = XboxTcuiSdk.getResources().getDimensionPixelSize(dimensionIdByName);
                            }
                            else {
                                dimensionPixelSize = 0;
                            }
                            o = new HeightAnimation(0, dimensionPixelSize);
                        }
                    }
                    else {
                        o = new TranslateAnimation(1, 0.0f, 1, 0.0f, this.fromYType, this.from, this.toYType, this.to);
                    }
                }
                else {
                    o = new TranslateAnimation(this.fromXType, this.from, this.toXType, this.to, 1, 0.0f, 1, 0.0f);
                }
            }
            else {
                final float from = this.from;
                final float to = this.to;
                int n2;
                if (this.scaleRelativeToSelf) {
                    n2 = 1;
                }
                else {
                    n2 = 2;
                }
                final float pivotX = this.pivotX;
                int n3;
                if (this.scaleRelativeToSelf) {
                    n3 = 1;
                }
                else {
                    n3 = 2;
                }
                o = new ScaleAnimation(from, to, from, to, n2, pivotX, n3, this.pivotY);
            }
        }
        else {
            o = new AlphaAnimation(this.from, this.to);
        }
        if (o != null) {
            ((Animation)o).setDuration((long)this.durationMs);
            ((Animation)o).setInterpolator(interpolator);
            ((Animation)o).setStartOffset((long)this.delayMs);
            return (Animation)o;
        }
        return null;
    }
}
