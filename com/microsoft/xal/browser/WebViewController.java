package com.microsoft.xal.browser;

import android.app.*;
import com.microsoft.xal.logging.*;
import android.os.*;
import android.content.*;
import android.view.*;
import android.webkit.*;

public class WebViewController
{
    public static final String END_URL = "END_URL";
    public static final String RESPONSE_KEY = "RESPONSE";
    public static final int RESULT_FAILED = 8054;
    public static final String SHOW_TYPE = "SHOW_TYPE";
    public static final String START_URL = "START_URL";
    private Activity activity;
    private Intent inputIntent;
    private final XalLogger m_logger;
    private WebView m_webView;
    private int resultInt;
    private Intent resultIntent;
    
    public WebViewController() {
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
            b2 = false;
            if (split.length > 0) {
                b2 = true;
            }
        }
        if (b2) {
            final XalLogger logger = this.m_logger;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("deleteCookies() Deleted cookies for ");
            sb3.append(s);
            logger.Information(sb3.toString());
        }
        else {
            final XalLogger logger2 = this.m_logger;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("deleteCookies() Found no cookies for ");
            sb4.append(s);
            logger2.Information(sb4.toString());
        }
        if (Build$VERSION.SDK_INT >= 21) {
            instance.flush();
        }
    }
    
    public static void startForResult(final Activity activity, final Intent inputIntent) {
        final WebViewController webViewController = new WebViewController();
        webViewController.setActivity(activity);
        webViewController.setInputIntent(inputIntent);
        webViewController.onCreate(new Bundle());
    }
    
    void finish() {
    }
    
    public Activity getActivity() {
        return this.activity;
    }
    
    Intent getIntent() {
        return this.inputIntent;
    }
    
    public void onCreate(final Bundle bundle) {
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
        if (!string.isEmpty() && !string2.isEmpty()) {
            final com.microsoft.xal.browser.WebView.ShowUrlType showUrlType = (com.microsoft.xal.browser.WebView.ShowUrlType)extras.get("SHOW_TYPE");
            if (showUrlType != com.microsoft.xal.browser.WebView.ShowUrlType.CookieRemoval && showUrlType != com.microsoft.xal.browser.WebView.ShowUrlType.CookieRemovalSkipIfSharedCredentials) {
                this.setContentView((View)(this.m_webView = new WebView((Context)this.getActivity())));
                this.m_webView.getSettings().setJavaScriptEnabled(true);
                if (Build$VERSION.SDK_INT >= 21) {
                    this.m_webView.getSettings().setMixedContentMode(2);
                }
                this.m_webView.setWebChromeClient((WebChromeClient)new WebChromeClient() {
                    public void onProgressChanged(final WebView webView, final int n) {
                        WebViewController.this.setProgress(n * 100);
                    }
                });
                this.m_webView.setWebViewClient((WebViewClient)new WebViewClient() {
                    public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                        if (s.startsWith(string2, 0)) {
                            WebViewController.this.m_logger.Important("WebKitWebViewController found end URL. Ending UI flow.");
                            WebViewController.this.m_logger.Flush();
                            final Intent intent = new Intent();
                            intent.putExtra("RESPONSE", s);
                            WebViewController.this.setResult(-1, intent);
                            WebViewController.this.finish();
                            return true;
                        }
                        return false;
                    }
                });
                this.m_webView.loadUrl(string);
            }
            else {
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
            return;
        }
        this.m_logger.Error("onCreate() Received invalid start or end URL.");
        this.m_logger.Flush();
        this.setResult(8054);
        this.finish();
    }
    
    public void setActivity(final Activity activity) {
        this.activity = activity;
    }
    
    void setContentView(final View view) {
    }
    
    public void setInputIntent(final Intent inputIntent) {
        this.inputIntent = inputIntent;
    }
    
    void setProgress(final int n) {
    }
    
    void setResult(final int n) {
    }
    
    void setResult(final int n, final Intent intent) {
    }
}
