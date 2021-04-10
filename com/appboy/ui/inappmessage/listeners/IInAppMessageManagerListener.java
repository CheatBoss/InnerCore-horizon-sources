package com.appboy.ui.inappmessage.listeners;

import com.appboy.models.*;
import com.appboy.ui.inappmessage.*;

public interface IInAppMessageManagerListener
{
    InAppMessageOperation beforeInAppMessageDisplayed(final IInAppMessage p0);
    
    boolean onInAppMessageButtonClicked(final MessageButton p0, final InAppMessageCloser p1);
    
    boolean onInAppMessageClicked(final IInAppMessage p0, final InAppMessageCloser p1);
    
    void onInAppMessageDismissed(final IInAppMessage p0);
    
    @Deprecated
    boolean onInAppMessageReceived(final IInAppMessage p0);
}
