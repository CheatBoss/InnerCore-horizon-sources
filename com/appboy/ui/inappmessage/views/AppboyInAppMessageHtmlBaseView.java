package com.appboy.ui.inappmessage.views;

import android.widget.*;
import android.content.*;
import android.util.*;
import android.view.*;
import com.appboy.ui.inappmessage.*;
import android.webkit.*;

public abstract class AppboyInAppMessageHtmlBaseView extends RelativeLayout implements IInAppMessageView
{
    private static final String FILE_URI_SCHEME_PREFIX = "file://";
    private static final String HTML_ENCODING = "utf-8";
    private static final String HTML_MIME_TYPE = "text/html";
    
    public AppboyInAppMessageHtmlBaseView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public View getMessageClickableView() {
        return (View)this;
    }
    
    public abstract WebView getMessageWebView();
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        if (n == 4) {
            InAppMessageViewUtils.closeInAppMessageOnKeycodeBack();
            return true;
        }
        return super.onKeyDown(n, keyEvent);
    }
    
    public void setInAppMessageWebViewClient(final InAppMessageWebViewClient webViewClient) {
        this.getMessageWebView().setWebViewClient((WebViewClient)webViewClient);
    }
    
    public void setWebViewContent(final String s, final String s2) {
        final WebView messageWebView = this.getMessageWebView();
        final StringBuilder sb = new StringBuilder();
        sb.append("file://");
        sb.append(s2);
        sb.append("/");
        messageWebView.loadDataWithBaseURL(sb.toString(), s, "text/html", "utf-8", (String)null);
    }
}
