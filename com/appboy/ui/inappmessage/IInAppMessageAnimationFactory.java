package com.appboy.ui.inappmessage;

import com.appboy.models.*;
import android.view.animation.*;

public interface IInAppMessageAnimationFactory
{
    Animation getClosingAnimation(final IInAppMessage p0);
    
    Animation getOpeningAnimation(final IInAppMessage p0);
}
