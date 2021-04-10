package com.appboy.ui;

import com.appboy.ui.activities.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.net.*;
import android.graphics.*;
import com.appboy.support.*;
import com.appboy.enums.*;
import com.appboy.ui.actions.*;
import android.webkit.*;

public class AppboyWebViewActivity extends AppboyBaseActivity
{
    private static final String TAG;
    @Deprecated
    public static final String URL_EXTRA = "url";
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyWebViewActivity.class);
    }
    
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.requestWindowFeature(2);
        this.requestWindowFeature(5);
        this.getWindow().setFlags(16777216, 16777216);
        this.setContentView(R$layout.com_appboy_webview_activity);
        this.setProgressBarVisibility(true);
        final WebView webView = (WebView)this.findViewById(R$id.com_appboy_webview_activity_webview);
        final WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setPluginState(WebSettings$PluginState.OFF);
        settings.setDisplayZoomControls(false);
        settings.setBuiltInZoomControls(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setDomStorageEnabled(true);
        webView.setLayoutParams((ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        webView.setWebChromeClient((WebChromeClient)new WebChromeClient() {
            public void onProgressChanged(final WebView webView, final int n) {
                AppboyWebViewActivity appboyWebViewActivity;
                boolean progressBarVisibility;
                if (n < 100) {
                    appboyWebViewActivity = AppboyWebViewActivity.this;
                    progressBarVisibility = true;
                }
                else {
                    appboyWebViewActivity = AppboyWebViewActivity.this;
                    progressBarVisibility = false;
                }
                appboyWebViewActivity.setProgressBarVisibility(progressBarVisibility);
            }
        });
        webView.setDownloadListener((DownloadListener)new DownloadListener() {
            public void onDownloadStart(final String s, final String s2, final String s3, final String s4, final long n) {
                final Intent intent = new Intent("android.intent.action.VIEW");
                intent.setData(Uri.parse(s));
                AppboyWebViewActivity.this.startActivity(intent);
            }
        });
        webView.getSettings().setCacheMode(2);
        webView.setLayerType(2, (Paint)null);
        webView.setWebViewClient((WebViewClient)new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView webView, final String s) {
                try {
                    if (!AppboyFileUtils.REMOTE_SCHEMES.contains(Uri.parse(s).getScheme())) {
                        ActionFactory.createUriActionFromUrlString(s, AppboyWebViewActivity.this.getIntent().getExtras(), false, Channel.UNKNOWN).execute(webView.getContext());
                        AppboyWebViewActivity.this.finish();
                        return true;
                    }
                }
                catch (Exception ex) {
                    final String access$000 = AppboyWebViewActivity.TAG;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unexpected exception while processing url ");
                    sb.append(s);
                    sb.append(". Passing url back to WebView.");
                    AppboyLogger.i(access$000, sb.toString(), ex);
                }
                return super.shouldOverrideUrlLoading(webView, s);
            }
        });
        final Bundle extras = this.getIntent().getExtras();
        if (extras != null && extras.containsKey("url")) {
            webView.loadUrl(extras.getString("url"));
        }
    }
}
