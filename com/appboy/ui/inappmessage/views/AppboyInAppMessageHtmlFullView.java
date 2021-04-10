package com.appboy.ui.inappmessage.views;

import com.appboy.support.*;
import android.content.*;
import android.util.*;
import com.appboy.ui.*;
import android.graphics.*;
import com.appboy.ui.inappmessage.jsinterface.*;
import android.webkit.*;

public class AppboyInAppMessageHtmlFullView extends AppboyInAppMessageHtmlBaseView
{
    public static final String APPBOY_BRIDGE_PREFIX = "appboyInternalBridge";
    private static final String TAG;
    private WebView mMessageWebView;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyInAppMessageHtmlFullView.class);
    }
    
    public AppboyInAppMessageHtmlFullView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    @Override
    public WebView getMessageWebView() {
        if (this.mMessageWebView == null) {
            final AppboyInAppMessageWebView mMessageWebView = (AppboyInAppMessageWebView)this.findViewById(R$id.com_appboy_inappmessage_html_full_webview);
            if ((this.mMessageWebView = mMessageWebView) != null) {
                final WebSettings settings = mMessageWebView.getSettings();
                settings.setJavaScriptEnabled(true);
                settings.setUseWideViewPort(true);
                settings.setLoadWithOverviewMode(true);
                settings.setDisplayZoomControls(false);
                this.mMessageWebView.setLayerType(2, (Paint)null);
                this.mMessageWebView.setBackgroundColor(0);
                this.mMessageWebView.setWebChromeClient((WebChromeClient)new WebChromeClient() {
                    public boolean onConsoleMessage(final ConsoleMessage consoleMessage) {
                        final String access$000 = AppboyInAppMessageHtmlFullView.TAG;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Html In-app log. Line: ");
                        sb.append(consoleMessage.lineNumber());
                        sb.append(". SourceId: ");
                        sb.append(consoleMessage.sourceId());
                        sb.append(". Log Level: ");
                        sb.append(consoleMessage.messageLevel());
                        sb.append(". Message: ");
                        sb.append(consoleMessage.message());
                        AppboyLogger.d(access$000, sb.toString());
                        return true;
                    }
                });
                this.mMessageWebView.addJavascriptInterface((Object)new AppboyInAppMessageHtmlJavascriptInterface(this.getContext()), "appboyInternalBridge");
            }
        }
        return this.mMessageWebView;
    }
}
