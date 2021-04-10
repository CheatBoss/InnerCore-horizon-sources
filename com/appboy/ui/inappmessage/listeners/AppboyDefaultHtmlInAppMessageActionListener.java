package com.appboy.ui.inappmessage.listeners;

import com.appboy.models.*;
import android.os.*;

public class AppboyDefaultHtmlInAppMessageActionListener implements IHtmlInAppMessageActionListener
{
    @Override
    public void onCloseClicked(final IInAppMessage inAppMessage, final String s, final Bundle bundle) {
    }
    
    @Override
    public boolean onCustomEventFired(final IInAppMessage inAppMessage, final String s, final Bundle bundle) {
        return false;
    }
    
    @Override
    public boolean onNewsfeedClicked(final IInAppMessage inAppMessage, final String s, final Bundle bundle) {
        return false;
    }
    
    @Override
    public boolean onOtherUrlAction(final IInAppMessage inAppMessage, final String s, final Bundle bundle) {
        return false;
    }
}
