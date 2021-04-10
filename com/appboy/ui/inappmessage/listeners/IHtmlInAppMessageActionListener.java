package com.appboy.ui.inappmessage.listeners;

import com.appboy.models.*;
import android.os.*;

public interface IHtmlInAppMessageActionListener
{
    void onCloseClicked(final IInAppMessage p0, final String p1, final Bundle p2);
    
    boolean onCustomEventFired(final IInAppMessage p0, final String p1, final Bundle p2);
    
    boolean onNewsfeedClicked(final IInAppMessage p0, final String p1, final Bundle p2);
    
    boolean onOtherUrlAction(final IInAppMessage p0, final String p1, final Bundle p2);
}
