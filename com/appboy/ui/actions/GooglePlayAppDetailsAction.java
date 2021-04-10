package com.appboy.ui.actions;

import com.appboy.enums.*;
import com.appboy.support.*;
import android.content.pm.*;
import android.net.*;
import android.os.*;
import android.content.*;

public final class GooglePlayAppDetailsAction implements IAction
{
    private static final String AMAZON_STORE_APP_BASE = "amzn://apps/android?asin=";
    private static final String AMAZON_STORE_WEB_BASE = "http://www.amazon.com/gp/mas/dl/android?asin=";
    private static final String PLAY_STORE_APP_BASE = "market://details?id=";
    private static final String PLAY_STORE_WEB_BASE = "https://play.google.com/store/apps/details?id=";
    private static final String TAG;
    private final AppStore mAppStore;
    private final Channel mChannel;
    private final String mKindleId;
    private final String mPackageName;
    private boolean mUseWebView;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(GooglePlayAppDetailsAction.class);
    }
    
    public GooglePlayAppDetailsAction(final String mPackageName, final boolean mUseWebView, final AppStore mAppStore, final String mKindleId, final Channel mChannel) {
        this.mPackageName = mPackageName;
        this.mUseWebView = mUseWebView;
        this.mAppStore = mAppStore;
        this.mKindleId = mKindleId;
        this.mChannel = mChannel;
    }
    
    @Override
    public void execute(final Context context) {
        Label_0052: {
            if (this.mAppStore != AppStore.KINDLE_STORE) {
                try {
                    context.getPackageManager().getPackageInfo("com.google.android.gsf", 0);
                    break Label_0052;
                }
                catch (Exception ex) {
                    AppboyLogger.e(GooglePlayAppDetailsAction.TAG, "Unexpected exception while checking for com.google.android.gsf.");
                }
                catch (PackageManager$NameNotFoundException ex2) {
                    AppboyLogger.i(GooglePlayAppDetailsAction.TAG, "Google Play Store not found, launching Play Store with WebView");
                }
                this.mUseWebView = true;
            }
        }
        if (this.mUseWebView) {
            StringBuilder sb;
            String s;
            if (this.mAppStore == AppStore.KINDLE_STORE) {
                sb = new StringBuilder();
                sb.append("http://www.amazon.com/gp/mas/dl/android?asin=");
                s = this.mKindleId;
            }
            else {
                sb = new StringBuilder();
                sb.append("https://play.google.com/store/apps/details?id=");
                s = this.mPackageName;
            }
            sb.append(s);
            UriAction.openUriWithWebViewActivity(context, Uri.parse(sb.toString()), null);
            return;
        }
        StringBuilder sb2;
        String s2;
        if (this.mAppStore == AppStore.KINDLE_STORE) {
            sb2 = new StringBuilder();
            sb2.append("amzn://apps/android?asin=");
            s2 = this.mKindleId;
        }
        else {
            sb2 = new StringBuilder();
            sb2.append("market://details?id=");
            s2 = this.mPackageName;
        }
        sb2.append(s2);
        context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(sb2.toString())));
    }
    
    @Override
    public Channel getChannel() {
        return this.mChannel;
    }
    
    public boolean getUseWebView() {
        return this.mUseWebView;
    }
}
