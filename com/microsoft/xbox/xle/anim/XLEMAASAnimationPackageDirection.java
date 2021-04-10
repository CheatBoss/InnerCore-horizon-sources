package com.microsoft.xbox.xle.anim;

import org.simpleframework.xml.*;
import android.view.*;
import com.microsoft.xbox.toolkit.anim.*;

@Root
public class XLEMAASAnimationPackageDirection extends MAASAnimation
{
    @Element(required = false)
    public XLEMAASAnimation inAnimation;
    @Element(required = false)
    public XLEMAASAnimation outAnimation;
    
    public XLEAnimation compile(final MAAS.MAASAnimationType maasAnimationType, final View view) {
        XLEMAASAnimation xlemaasAnimation;
        if (maasAnimationType == MAAS.MAASAnimationType.ANIMATE_IN) {
            xlemaasAnimation = this.inAnimation;
        }
        else {
            xlemaasAnimation = this.outAnimation;
        }
        if (xlemaasAnimation == null) {
            return null;
        }
        return xlemaasAnimation.compile(view);
    }
}
