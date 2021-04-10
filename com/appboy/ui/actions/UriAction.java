package com.appboy.ui.actions;

import com.appboy.enums.*;
import android.os.*;
import android.net.*;
import android.content.pm.*;
import android.util.*;
import java.util.*;
import android.support.v4.app.*;
import com.appboy.configuration.*;
import com.appboy.ui.support.*;
import com.appboy.ui.*;
import android.content.*;
import com.appboy.support.*;

public class UriAction implements IAction
{
    private static final String TAG;
    private final Channel mChannel;
    private final Bundle mExtras;
    private Uri mUri;
    private boolean mUseWebView;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(UriAction.class);
    }
    
    UriAction(final Uri mUri, final Bundle mExtras, final boolean mUseWebView, final Channel mChannel) {
        this.mUri = mUri;
        this.mExtras = mExtras;
        this.mUseWebView = mUseWebView;
        this.mChannel = mChannel;
    }
    
    private static Intent getActionViewIntent(final Context context, final Uri data, final Bundle bundle) {
        final Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(data);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        final List queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
        if (queryIntentActivities.size() > 1) {
            for (final ResolveInfo resolveInfo : queryIntentActivities) {
                if (resolveInfo.activityInfo.packageName.equals(context.getPackageName())) {
                    final String tag = UriAction.TAG;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Setting deep link activity to ");
                    sb.append(resolveInfo.activityInfo.packageName);
                    sb.append(".");
                    Log.d(tag, sb.toString());
                    intent.setPackage(resolveInfo.activityInfo.packageName);
                    break;
                }
            }
        }
        return intent;
    }
    
    private static TaskStackBuilder getConfiguredTaskBackStackBuilder(final Context context, final Bundle bundle) {
        final AppboyConfigurationProvider appboyConfigurationProvider = new AppboyConfigurationProvider(context);
        final TaskStackBuilder create = TaskStackBuilder.create(context);
        if (!appboyConfigurationProvider.getIsPushDeepLinkBackStackActivityEnabled()) {
            AppboyLogger.i(UriAction.TAG, "Not adding back stack activity while opening uri from push due to disabled configuration setting.");
            return create;
        }
        final String pushDeepLinkBackStackActivityClassName = appboyConfigurationProvider.getPushDeepLinkBackStackActivityClassName();
        if (StringUtils.isNullOrBlank(pushDeepLinkBackStackActivityClassName)) {
            AppboyLogger.i(UriAction.TAG, "Adding main activity intent to back stack while opening uri from push");
            create.addNextIntent(UriUtils.getMainActivityIntent(context, bundle));
            return create;
        }
        if (UriUtils.isActivityRegisteredInManifest(context, pushDeepLinkBackStackActivityClassName)) {
            final String tag = UriAction.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Adding custom back stack activity while opening uri from push: ");
            sb.append(pushDeepLinkBackStackActivityClassName);
            AppboyLogger.i(tag, sb.toString());
            create.addNextIntent(new Intent().setClassName(context, pushDeepLinkBackStackActivityClassName));
            return create;
        }
        final String tag2 = UriAction.TAG;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Not adding unregistered activity to the back stack while opening uri from push: ");
        sb2.append(pushDeepLinkBackStackActivityClassName);
        AppboyLogger.i(tag2, sb2.toString());
        return create;
    }
    
    private static Intent getWebViewActivityIntent(final Context context, final Uri uri, final Bundle bundle) {
        final Intent intent = new Intent(context, (Class)AppboyWebViewActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.putExtra("url", uri.toString());
        return intent;
    }
    
    private static void openUriWithActionView(final Context context, final Uri uri, final Bundle bundle) {
        final Intent actionViewIntent = getActionViewIntent(context, uri, bundle);
        actionViewIntent.setFlags(872415232);
        if (actionViewIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(actionViewIntent);
            return;
        }
        final String tag = UriAction.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("Could not find appropriate activity to open for deep link ");
        sb.append(uri);
        sb.append(".");
        Log.w(tag, sb.toString());
    }
    
    private static void openUriWithActionViewFromPush(final Context context, final Uri uri, final Bundle bundle) {
        final TaskStackBuilder configuredTaskBackStackBuilder = getConfiguredTaskBackStackBuilder(context, bundle);
        configuredTaskBackStackBuilder.addNextIntent(getActionViewIntent(context, uri, bundle));
        try {
            configuredTaskBackStackBuilder.startActivities(bundle);
        }
        catch (ActivityNotFoundException ex) {
            final String tag = UriAction.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not find appropriate activity to open for deep link ");
            sb.append(uri);
            Log.w(tag, sb.toString(), (Throwable)ex);
        }
    }
    
    static void openUriWithWebViewActivity(final Context context, final Uri uri, final Bundle bundle) {
        final Intent webViewActivityIntent = getWebViewActivityIntent(context, uri, bundle);
        webViewActivityIntent.setFlags(872415232);
        try {
            context.startActivity(webViewActivityIntent);
        }
        catch (Exception ex) {
            AppboyLogger.e(UriAction.TAG, "Appboy AppboyWebViewActivity not opened successfully.", ex);
        }
    }
    
    private static void openUriWithWebViewActivityFromPush(final Context context, final Uri uri, final Bundle bundle) {
        final TaskStackBuilder configuredTaskBackStackBuilder = getConfiguredTaskBackStackBuilder(context, bundle);
        configuredTaskBackStackBuilder.addNextIntent(getWebViewActivityIntent(context, uri, bundle));
        try {
            configuredTaskBackStackBuilder.startActivities(bundle);
        }
        catch (Exception ex) {
            AppboyLogger.e(UriAction.TAG, "Appboy AppboyWebViewActivity not opened successfully.", ex);
        }
    }
    
    @Override
    public void execute(final Context context) {
        if (AppboyFileUtils.isLocalUri(this.mUri)) {
            final String tag = UriAction.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Not executing local Uri: ");
            sb.append(this.mUri);
            AppboyLogger.d(tag, sb.toString());
            return;
        }
        final String tag2 = UriAction.TAG;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Executing Uri action from channel ");
        sb2.append(this.mChannel);
        sb2.append(": ");
        sb2.append(this.mUri);
        sb2.append(". UseWebView: ");
        sb2.append(this.mUseWebView);
        sb2.append(". Extras: ");
        sb2.append(this.mExtras);
        AppboyLogger.d(tag2, sb2.toString());
        if (this.mUseWebView && AppboyFileUtils.REMOTE_SCHEMES.contains(this.mUri.getScheme())) {
            if (this.mChannel.equals(Channel.PUSH)) {
                openUriWithWebViewActivityFromPush(context, this.mUri, this.mExtras);
                return;
            }
            openUriWithWebViewActivity(context, this.mUri, this.mExtras);
        }
        else {
            if (this.mChannel.equals(Channel.PUSH)) {
                openUriWithActionViewFromPush(context, this.mUri, this.mExtras);
                return;
            }
            openUriWithActionView(context, this.mUri, this.mExtras);
        }
    }
    
    @Override
    public Channel getChannel() {
        return this.mChannel;
    }
    
    public Bundle getExtras() {
        return this.mExtras;
    }
    
    public Uri getUri() {
        return this.mUri;
    }
    
    public boolean getUseWebView() {
        return this.mUseWebView;
    }
    
    public void setUri(final Uri mUri) {
        this.mUri = mUri;
    }
    
    public void setUseWebView(final boolean mUseWebView) {
        this.mUseWebView = mUseWebView;
    }
}
