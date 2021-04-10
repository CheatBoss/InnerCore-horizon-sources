package com.microsoft.xal.browser;

import android.app.*;
import com.microsoft.xal.logging.*;
import android.os.*;
import android.view.*;
import android.webkit.*;
import android.content.*;

public class WebKitWebViewController extends Activity
{
    public static final String END_URL = "END_URL";
    public static final String RESPONSE_KEY = "RESPONSE";
    public static final int RESULT_FAILED = 8054;
    public static final String SHOW_TYPE = "SHOW_TYPE";
    public static final String START_URL = "START_URL";
    private final XalLogger m_logger;
    private WebView m_webView;
    
    public WebKitWebViewController() {
        this.m_logger = new XalLogger("WebKitWebViewController");
    }
    
    private void deleteCookies(final String s, final boolean b) {
        final CookieManager instance = CookieManager.getInstance();
        final StringBuilder sb = new StringBuilder();
        String s2;
        if (b) {
            s2 = "https://";
        }
        else {
            s2 = "http://";
        }
        sb.append(s2);
        sb.append(s);
        final String string = sb.toString();
        final String cookie = instance.getCookie(string);
        boolean b2 = false;
        if (cookie != null) {
            final String[] split = cookie.split(";");
            for (int length = split.length, i = 0; i < length; ++i) {
                final String s3 = split[i].split("=")[0];
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s3.trim());
                sb2.append("=;Domain=");
                sb2.append(s);
                sb2.append(";Path=/");
                instance.setCookie(string, sb2.toString());
            }
            b2 = b2;
            if (split.length > 0) {
                b2 = true;
            }
        }
        XalLogger xalLogger;
        StringBuilder sb3;
        String s4;
        if (b2) {
            xalLogger = this.m_logger;
            sb3 = new StringBuilder();
            s4 = "deleteCookies() Deleted cookies for ";
        }
        else {
            xalLogger = this.m_logger;
            sb3 = new StringBuilder();
            s4 = "deleteCookies() Found no cookies for ";
        }
        sb3.append(s4);
        sb3.append(s);
        xalLogger.Information(sb3.toString());
        if (Build$VERSION.SDK_INT >= 21) {
            instance.flush();
        }
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final Bundle extras = this.getIntent().getExtras();
        if (extras == null) {
            this.m_logger.Error("onCreate() Called with no extras.");
            this.m_logger.Flush();
            this.setResult(8054);
            this.finish();
            return;
        }
        final String string = extras.getString("START_URL", "");
        final String string2 = extras.getString("END_URL", "");
        if (string.isEmpty() || string2.isEmpty()) {
            this.m_logger.Error("onCreate() Received invalid start or end URL.");
            this.m_logger.Flush();
            this.setResult(8054);
            this.finish();
            return;
        }
        final com.microsoft.xal.browser.WebView.ShowUrlType showUrlType = (com.microsoft.xal.browser.WebView.ShowUrlType)extras.get("SHOW_TYPE");
        if (showUrlType != com.microsoft.xal.browser.WebView.ShowUrlType.CookieRemoval && showUrlType != com.microsoft.xal.browser.WebView.ShowUrlType.CookieRemovalSkipIfSharedCredentials) {
            this.setContentView((View)(this.m_webView = new WebView((Context)this)));
            this.m_webView.getSettings().setJavaScriptEnabled(true);
            if (Build$VERSION.SDK_INT >= 21) {
                this.m_webView.getSettings().setMixedContentMode(2);
            }
            this.m_webView.setWebChromeClient((WebChromeClient)new WebChromeClient() {
                public void onProgressChanged(final WebView webView, final int n) {
                    WebKitWebViewController.this.setProgress(n * 100);
                }
            });
            this.m_webView.setWebViewClient((WebViewClient)new WebViewClient() {
                public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                    if (s.startsWith(string2, 0)) {
                        WebKitWebViewController.this.m_logger.Important("WebKitWebViewController found end URL. Ending UI flow.");
                        WebKitWebViewController.this.m_logger.Flush();
                        final Intent intent = new Intent();
                        intent.putExtra("RESPONSE", s);
                        WebKitWebViewController.this.setResult(-1, intent);
                        WebKitWebViewController.this.finish();
                        return true;
                    }
                    return false;
                }
            });
            this.m_webView.loadUrl(string);
            return;
        }
        this.m_logger.Important("onCreate() WebView invoked for cookie removal. Deleting cookies and finishing.");
        this.deleteCookies("login.live.com", true);
        this.deleteCookies("account.live.com", true);
        this.deleteCookies("live.com", true);
        this.deleteCookies("xboxlive.com", true);
        this.deleteCookies("sisu.xboxlive.com", true);
        this.m_logger.Flush();
        final Intent intent = new Intent();
        intent.putExtra("RESPONSE", string2);
        this.setResult(-1, intent);
        this.finish();
    }
}
