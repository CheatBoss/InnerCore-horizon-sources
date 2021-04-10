package com.appboy.ui.inappmessage.factories;

import com.appboy.ui.inappmessage.listeners.*;
import android.app.*;
import com.appboy.ui.inappmessage.views.*;
import com.appboy.models.*;
import com.appboy.ui.*;
import android.view.*;
import com.appboy.ui.inappmessage.*;

public class AppboyHtmlFullViewFactory implements IInAppMessageViewFactory
{
    private IInAppMessageWebViewClientListener mInAppMessageWebViewClientListener;
    
    public AppboyHtmlFullViewFactory(final IInAppMessageWebViewClientListener mInAppMessageWebViewClientListener) {
        this.mInAppMessageWebViewClientListener = mInAppMessageWebViewClientListener;
    }
    
    public AppboyInAppMessageHtmlFullView createInAppMessageView(final Activity activity, final IInAppMessage inAppMessage) {
        final InAppMessageHtmlFull inAppMessageHtmlFull = (InAppMessageHtmlFull)inAppMessage;
        final AppboyInAppMessageHtmlFullView appboyInAppMessageHtmlFullView = (AppboyInAppMessageHtmlFullView)activity.getLayoutInflater().inflate(R$layout.com_appboy_inappmessage_html_full, (ViewGroup)null);
        appboyInAppMessageHtmlFullView.setWebViewContent(inAppMessage.getMessage(), inAppMessageHtmlFull.getLocalAssetsDirectoryUrl());
        appboyInAppMessageHtmlFullView.setInAppMessageWebViewClient(new InAppMessageWebViewClient(activity.getApplicationContext(), inAppMessage, this.mInAppMessageWebViewClientListener));
        return appboyInAppMessageHtmlFullView;
    }
}
