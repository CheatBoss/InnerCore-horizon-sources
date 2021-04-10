package com.appboy.ui.inappmessage.listeners;

import com.appboy.ui.inappmessage.*;
import android.os.*;
import com.appboy.models.*;
import com.appboy.models.outgoing.*;
import java.util.*;
import com.appboy.*;
import android.content.*;
import com.appboy.ui.*;
import com.appboy.enums.*;
import com.appboy.support.*;
import com.appboy.ui.actions.*;
import android.net.*;

public class AppboyInAppMessageWebViewClientListener implements IInAppMessageWebViewClientListener
{
    private static final String HTML_IAM_CUSTOM_EVENT_NAME_KEY = "name";
    private static final String TAG;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyInAppMessageWebViewClientListener.class);
    }
    
    private AppboyInAppMessageManager getInAppMessageManager() {
        return AppboyInAppMessageManager.getInstance();
    }
    
    private void logHtmlInAppMessageClick(final IInAppMessage inAppMessage, final Bundle bundle) {
        if (bundle != null && bundle.containsKey("abButtonId")) {
            ((IInAppMessageHtml)inAppMessage).logButtonClick(bundle.getString("abButtonId"));
            return;
        }
        inAppMessage.logClick();
    }
    
    static String parseCustomEventNameFromQueryBundle(final Bundle bundle) {
        return bundle.getString("name");
    }
    
    static AppboyProperties parsePropertiesFromQueryBundle(final Bundle bundle) {
        final AppboyProperties appboyProperties = new AppboyProperties();
        for (final String s : bundle.keySet()) {
            if (!s.equals("name")) {
                final String string = bundle.getString(s, (String)null);
                if (StringUtils.isNullOrBlank(string)) {
                    continue;
                }
                appboyProperties.addProperty(s, string);
            }
        }
        return appboyProperties;
    }
    
    static boolean parseUseWebViewFromQueryBundle(final IInAppMessage inAppMessage, final Bundle bundle) {
        boolean boolean1;
        boolean b;
        if (bundle.containsKey("abDeepLink")) {
            boolean1 = Boolean.parseBoolean(bundle.getString("abDeepLink"));
            b = true;
        }
        else {
            b = false;
            boolean1 = false;
        }
        boolean boolean2;
        if (bundle.containsKey("abExternalOpen")) {
            boolean2 = Boolean.parseBoolean(bundle.getString("abExternalOpen"));
            b = true;
        }
        else {
            boolean2 = false;
        }
        boolean openUriInWebView = inAppMessage.getOpenUriInWebView();
        if (b) {
            if (!boolean1 && !boolean2) {
                return true;
            }
            openUriInWebView = false;
        }
        return openUriInWebView;
    }
    
    @Override
    public void onCloseAction(final IInAppMessage inAppMessage, final String s, final Bundle bundle) {
        AppboyLogger.d(AppboyInAppMessageWebViewClientListener.TAG, "IInAppMessageWebViewClientListener.onCloseAction called.");
        this.logHtmlInAppMessageClick(inAppMessage, bundle);
        this.getInAppMessageManager().hideCurrentlyDisplayingInAppMessage(true);
        this.getInAppMessageManager().getHtmlInAppMessageActionListener().onCloseClicked(inAppMessage, s, bundle);
    }
    
    @Override
    public void onCustomEventAction(final IInAppMessage inAppMessage, final String s, final Bundle bundle) {
        AppboyLogger.d(AppboyInAppMessageWebViewClientListener.TAG, "IInAppMessageWebViewClientListener.onCustomEventAction called.");
        if (this.getInAppMessageManager().getActivity() == null) {
            AppboyLogger.w(AppboyInAppMessageWebViewClientListener.TAG, "Can't perform custom event action because the activity is null.");
            return;
        }
        if (!this.getInAppMessageManager().getHtmlInAppMessageActionListener().onCustomEventFired(inAppMessage, s, bundle)) {
            final String customEventNameFromQueryBundle = parseCustomEventNameFromQueryBundle(bundle);
            if (StringUtils.isNullOrBlank(customEventNameFromQueryBundle)) {
                return;
            }
            Appboy.getInstance((Context)this.getInAppMessageManager().getActivity()).logCustomEvent(customEventNameFromQueryBundle, parsePropertiesFromQueryBundle(bundle));
        }
    }
    
    @Override
    public void onNewsfeedAction(final IInAppMessage inAppMessage, final String s, final Bundle bundle) {
        AppboyLogger.d(AppboyInAppMessageWebViewClientListener.TAG, "IInAppMessageWebViewClientListener.onNewsfeedAction called.");
        if (this.getInAppMessageManager().getActivity() == null) {
            AppboyLogger.w(AppboyInAppMessageWebViewClientListener.TAG, "Can't perform news feed action because the cached activity is null.");
            return;
        }
        this.logHtmlInAppMessageClick(inAppMessage, bundle);
        if (!this.getInAppMessageManager().getHtmlInAppMessageActionListener().onNewsfeedClicked(inAppMessage, s, bundle)) {
            inAppMessage.setAnimateOut(false);
            this.getInAppMessageManager().hideCurrentlyDisplayingInAppMessage(false);
            AppboyNavigator.getAppboyNavigator().gotoNewsFeed((Context)this.getInAppMessageManager().getActivity(), new NewsfeedAction(BundleUtils.mapToBundle(inAppMessage.getExtras()), Channel.INAPP_MESSAGE));
        }
    }
    
    @Override
    public void onOtherUrlAction(final IInAppMessage inAppMessage, String string, final Bundle bundle) {
        AppboyLogger.d(AppboyInAppMessageWebViewClientListener.TAG, "IInAppMessageWebViewClientListener.onOtherUrlAction called.");
        String s = null;
        Label_0026: {
            if (this.getInAppMessageManager().getActivity() != null) {
                this.logHtmlInAppMessageClick(inAppMessage, bundle);
                if (!this.getInAppMessageManager().getHtmlInAppMessageActionListener().onOtherUrlAction(inAppMessage, string, bundle)) {
                    final boolean useWebViewFromQueryBundle = parseUseWebViewFromQueryBundle(inAppMessage, bundle);
                    final Bundle mapToBundle = BundleUtils.mapToBundle(inAppMessage.getExtras());
                    mapToBundle.putAll(bundle);
                    final UriAction uriActionFromUrlString = ActionFactory.createUriActionFromUrlString(string, mapToBundle, useWebViewFromQueryBundle, Channel.INAPP_MESSAGE);
                    final Uri uri = uriActionFromUrlString.getUri();
                    if (uri != null && AppboyFileUtils.isLocalUri(uri)) {
                        s = AppboyInAppMessageWebViewClientListener.TAG;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Not passing local URI to AppboyNavigator. Got local uri: ");
                        sb.append(uri);
                        string = sb.toString();
                        break Label_0026;
                    }
                    inAppMessage.setAnimateOut(false);
                    this.getInAppMessageManager().hideCurrentlyDisplayingInAppMessage(false);
                    if (uriActionFromUrlString != null) {
                        AppboyNavigator.getAppboyNavigator().gotoUri(this.getInAppMessageManager().getApplicationContext(), uriActionFromUrlString);
                    }
                }
                return;
            }
            s = AppboyInAppMessageWebViewClientListener.TAG;
            string = "Can't perform other url action because the cached activity is null.";
        }
        AppboyLogger.w(s, string);
    }
}
