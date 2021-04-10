package com.appboy.ui.inappmessage;

import android.content.*;
import com.appboy.models.*;
import com.appboy.ui.inappmessage.listeners.*;
import android.webkit.*;
import android.os.*;
import com.appboy.support.*;
import android.net.*;
import com.appboy.ui.support.*;
import java.util.*;

public class InAppMessageWebViewClient extends WebViewClient
{
    private static final String APPBOY_INAPP_MESSAGE_SCHEME = "appboy";
    private static final String AUTHORITY_NAME_CLOSE = "close";
    private static final String AUTHORITY_NAME_CUSTOM_EVENT = "customEvent";
    private static final String AUTHORITY_NAME_NEWSFEED = "feed";
    public static final String JAVASCRIPT_PREFIX = "javascript:";
    public static final String QUERY_NAME_BUTTON_ID = "abButtonId";
    public static final String QUERY_NAME_DEEPLINK = "abDeepLink";
    public static final String QUERY_NAME_EXTERNAL_OPEN = "abExternalOpen";
    private static final String TAG;
    private Context mContext;
    private final IInAppMessage mInAppMessage;
    private IInAppMessageWebViewClientListener mInAppMessageWebViewClientListener;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(InAppMessageWebViewClient.class);
    }
    
    public InAppMessageWebViewClient(final Context mContext, final IInAppMessage mInAppMessage, final IInAppMessageWebViewClientListener mInAppMessageWebViewClientListener) {
        this.mInAppMessageWebViewClientListener = mInAppMessageWebViewClientListener;
        this.mInAppMessage = mInAppMessage;
        this.mContext = mContext;
    }
    
    private void appendBridgeJavascript(final WebView webView) {
        final String assetFileStringContents = AppboyFileUtils.getAssetFileStringContents(this.mContext.getAssets(), "appboy-html-in-app-message-javascript-component.js");
        if (assetFileStringContents == null) {
            AppboyInAppMessageManager.getInstance().hideCurrentlyDisplayingInAppMessage(false);
            AppboyLogger.e(InAppMessageWebViewClient.TAG, "Failed to get HTML in-app message javascript additions");
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("javascript:");
        sb.append(assetFileStringContents);
        webView.loadUrl(sb.toString());
    }
    
    static Bundle getBundleFromUrl(final String s) {
        final Bundle bundle = new Bundle();
        if (StringUtils.isNullOrBlank(s)) {
            return bundle;
        }
        final Map<String, String> queryParameters = UriUtils.getQueryParameters(Uri.parse(s));
        for (final String s2 : queryParameters.keySet()) {
            bundle.putString(s2, (String)queryParameters.get(s2));
        }
        return bundle;
    }
    
    public void onPageFinished(final WebView webView, final String s) {
        this.appendBridgeJavascript(webView);
    }
    
    public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
        if (this.mInAppMessageWebViewClientListener == null) {
            AppboyLogger.i(InAppMessageWebViewClient.TAG, "InAppMessageWebViewClient was given null IInAppMessageWebViewClientListener listener. Returning true.");
            return true;
        }
        if (StringUtils.isNullOrBlank(s)) {
            AppboyLogger.i(InAppMessageWebViewClient.TAG, "InAppMessageWebViewClient.shouldOverrideUrlLoading was given null or blank url. Returning true.");
            return true;
        }
        final Uri parse = Uri.parse(s);
        final Bundle bundleFromUrl = getBundleFromUrl(s);
        if (!parse.getScheme().equals("appboy")) {
            this.mInAppMessageWebViewClientListener.onOtherUrlAction(this.mInAppMessage, s, bundleFromUrl);
            return true;
        }
        final String authority = parse.getAuthority();
        if (authority.equals("close")) {
            this.mInAppMessageWebViewClientListener.onCloseAction(this.mInAppMessage, s, bundleFromUrl);
            return true;
        }
        if (authority.equals("feed")) {
            this.mInAppMessageWebViewClientListener.onNewsfeedAction(this.mInAppMessage, s, bundleFromUrl);
            return true;
        }
        if (authority.equals("customEvent")) {
            this.mInAppMessageWebViewClientListener.onCustomEventAction(this.mInAppMessage, s, bundleFromUrl);
        }
        return true;
    }
}
