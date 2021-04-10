package com.appboy.ui.inappmessage.listeners;

import com.appboy.models.*;
import com.appboy.ui.inappmessage.*;

public class AppboyDefaultInAppMessageManagerListener implements IInAppMessageManagerListener
{
    @Override
    public InAppMessageOperation beforeInAppMessageDisplayed(final IInAppMessage inAppMessage) {
        return InAppMessageOperation.DISPLAY_NOW;
    }
    
    @Override
    public boolean onInAppMessageButtonClicked(final MessageButton messageButton, final InAppMessageCloser inAppMessageCloser) {
        return false;
    }
    
    @Override
    public boolean onInAppMessageClicked(final IInAppMessage inAppMessage, final InAppMessageCloser inAppMessageCloser) {
        return false;
    }
    
    @Override
    public void onInAppMessageDismissed(final IInAppMessage inAppMessage) {
    }
    
    @Deprecated
    @Override
    public boolean onInAppMessageReceived(final IInAppMessage inAppMessage) {
        return false;
    }
}
