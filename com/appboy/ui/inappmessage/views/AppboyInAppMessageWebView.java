package com.appboy.ui.inappmessage.views;

import android.webkit.*;
import android.content.*;
import android.util.*;
import android.view.*;

public class AppboyInAppMessageWebView extends WebView
{
    public AppboyInAppMessageWebView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        if (n == 4) {
            InAppMessageViewUtils.closeInAppMessageOnKeycodeBack();
            return true;
        }
        return super.onKeyDown(n, keyEvent);
    }
}
