package com.microsoft.xbox.xle.anim;

import org.simpleframework.xml.*;
import android.view.*;
import com.microsoft.xbox.toolkit.*;
import android.view.animation.*;
import com.microsoft.xbox.toolkit.anim.*;
import java.util.*;

public class XLEMAASAnimation extends MAASAnimation
{
    @ElementList(required = false)
    public ArrayList<XLEAnimationDefinition> animations;
    @Attribute(required = false)
    public boolean fillAfter;
    @Attribute(required = false)
    public int offsetMs;
    @Attribute(required = false)
    public TargetType target;
    @Attribute(required = false)
    public String targetId;
    
    public XLEMAASAnimation() {
        this.target = TargetType.View;
        this.targetId = null;
        this.fillAfter = true;
    }
    
    public XLEAnimation compile() {
        return this.compile(XLERValueHelper.findViewByString(this.targetId));
    }
    
    public XLEAnimation compile(final View targetView) {
        final ArrayList<XLEAnimationDefinition> animations = this.animations;
        Object o;
        if (animations != null && animations.size() > 0) {
            final AnimationSet set = new AnimationSet(false);
            final Iterator<XLEAnimationDefinition> iterator = this.animations.iterator();
            while (true) {
                o = set;
                if (!iterator.hasNext()) {
                    break;
                }
                final Animation animation = iterator.next().getAnimation();
                if (animation == null) {
                    continue;
                }
                set.addAnimation(animation);
            }
        }
        else {
            o = null;
        }
        final int n = XLEMAASAnimation$1.$SwitchMap$com$microsoft$xbox$xle$anim$XLEMAASAnimation$TargetType[this.target.ordinal()];
        XLEAnimation xleAnimation;
        if (n != 1) {
            if (n != 2 && n != 3) {
                throw new UnsupportedOperationException();
            }
            XLEAssert.assertNotNull(o);
            xleAnimation = new XLEAnimationAbsListView(new LayoutAnimationController((Animation)o, this.offsetMs / 1000.0f));
        }
        else {
            XLEAssert.assertNotNull(o);
            xleAnimation = new XLEAnimationView((Animation)o);
            ((XLEAnimationView)xleAnimation).setFillAfter(this.fillAfter);
        }
        xleAnimation.setTargetView(targetView);
        return xleAnimation;
    }
    
    public XLEAnimation compileWithRoot(final View view) {
        return this.compile(view.findViewById(XLERValueHelper.getIdRValue(this.targetId)));
    }
    
    public enum TargetType
    {
        GridView, 
        ListView, 
        View;
    }
}
