package com.microsoft.xbox.xle.anim;

import org.simpleframework.xml.*;
import android.view.*;
import com.microsoft.xbox.toolkit.anim.*;

@Root
public class XLEMAASAnimationPackageNavigationManager extends MAASAnimation
{
    @Element(required = false)
    public XLEMAASAnimationPackageDirection backward;
    @Element(required = false)
    public XLEMAASAnimationPackageDirection forward;
    
    public XLEAnimation compile(final MAAS.MAASAnimationType maasAnimationType, final boolean b, final View view) {
        XLEMAASAnimationPackageDirection xlemaasAnimationPackageDirection;
        if (b) {
            xlemaasAnimationPackageDirection = this.backward;
        }
        else {
            xlemaasAnimationPackageDirection = this.forward;
        }
        if (xlemaasAnimationPackageDirection == null) {
            return null;
        }
        return xlemaasAnimationPackageDirection.compile(maasAnimationType, view);
    }
}
