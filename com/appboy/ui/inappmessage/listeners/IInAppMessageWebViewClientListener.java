package com.appboy.ui.inappmessage.listeners;

import com.appboy.models.*;
import android.os.*;

public interface IInAppMessageWebViewClientListener
{
    void onCloseAction(final IInAppMessage p0, final String p1, final Bundle p2);
    
    void onCustomEventAction(final IInAppMessage p0, final String p1, final Bundle p2);
    
    void onNewsfeedAction(final IInAppMessage p0, final String p1, final Bundle p2);
    
    void onOtherUrlAction(final IInAppMessage p0, final String p1, final Bundle p2);
}
