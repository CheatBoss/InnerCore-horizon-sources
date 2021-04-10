package com.appboy.ui.inappmessage;

import com.appboy.models.*;

public class InAppMessageCloser
{
    private InAppMessageViewWrapper mInAppMessageViewWrapper;
    
    public InAppMessageCloser(final InAppMessageViewWrapper mInAppMessageViewWrapper) {
        this.mInAppMessageViewWrapper = mInAppMessageViewWrapper;
    }
    
    public void close(final boolean b) {
        IInAppMessage inAppMessage;
        boolean animateOut;
        if (b) {
            inAppMessage = this.mInAppMessageViewWrapper.getInAppMessage();
            animateOut = true;
        }
        else {
            inAppMessage = this.mInAppMessageViewWrapper.getInAppMessage();
            animateOut = false;
        }
        inAppMessage.setAnimateOut(animateOut);
        this.mInAppMessageViewWrapper.close();
    }
}
