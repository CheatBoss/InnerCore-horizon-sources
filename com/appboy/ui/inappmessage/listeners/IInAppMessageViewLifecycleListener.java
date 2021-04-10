package com.appboy.ui.inappmessage.listeners;

import android.view.*;
import com.appboy.ui.inappmessage.*;
import com.appboy.models.*;

public interface IInAppMessageViewLifecycleListener
{
    void afterClosed(final IInAppMessage p0);
    
    void afterOpened(final View p0, final IInAppMessage p1);
    
    void beforeClosed(final View p0, final IInAppMessage p1);
    
    void beforeOpened(final View p0, final IInAppMessage p1);
    
    void onButtonClicked(final InAppMessageCloser p0, final MessageButton p1, final IInAppMessageImmersive p2);
    
    void onClicked(final InAppMessageCloser p0, final View p1, final IInAppMessage p2);
    
    void onDismissed(final View p0, final IInAppMessage p1);
}
