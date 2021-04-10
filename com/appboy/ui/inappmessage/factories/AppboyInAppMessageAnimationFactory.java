package com.appboy.ui.inappmessage.factories;

import com.appboy.ui.inappmessage.*;
import android.content.res.*;
import com.appboy.models.*;
import android.view.animation.*;
import com.appboy.ui.support.*;
import com.appboy.enums.inappmessage.*;

public class AppboyInAppMessageAnimationFactory implements IInAppMessageAnimationFactory
{
    private final int mShortAnimationDurationMillis;
    
    public AppboyInAppMessageAnimationFactory() {
        this.mShortAnimationDurationMillis = Resources.getSystem().getInteger(17694720);
    }
    
    @Override
    public Animation getClosingAnimation(final IInAppMessage inAppMessage) {
        if (!(inAppMessage instanceof InAppMessageSlideup)) {
            return AnimationUtils.setAnimationParams((Animation)new AlphaAnimation(1.0f, 0.0f), this.mShortAnimationDurationMillis, false);
        }
        if (((InAppMessageSlideup)inAppMessage).getSlideFrom() == SlideFrom.TOP) {
            return AnimationUtils.createVerticalAnimation(0.0f, -1.0f, this.mShortAnimationDurationMillis, false);
        }
        return AnimationUtils.createVerticalAnimation(0.0f, 1.0f, this.mShortAnimationDurationMillis, false);
    }
    
    @Override
    public Animation getOpeningAnimation(final IInAppMessage inAppMessage) {
        if (!(inAppMessage instanceof InAppMessageSlideup)) {
            return AnimationUtils.setAnimationParams((Animation)new AlphaAnimation(0.0f, 1.0f), this.mShortAnimationDurationMillis, true);
        }
        if (((InAppMessageSlideup)inAppMessage).getSlideFrom() == SlideFrom.TOP) {
            return AnimationUtils.createVerticalAnimation(-1.0f, 0.0f, this.mShortAnimationDurationMillis, false);
        }
        return AnimationUtils.createVerticalAnimation(1.0f, 0.0f, this.mShortAnimationDurationMillis, false);
    }
}
