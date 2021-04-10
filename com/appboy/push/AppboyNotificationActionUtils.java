package com.appboy.push;

import android.support.v4.app.*;
import android.os.*;
import android.content.*;
import android.app.*;
import com.appboy.support.*;
import com.appboy.configuration.*;
import com.appboy.*;

public class AppboyNotificationActionUtils
{
    private static final String TAG;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyNotificationActionUtils.class);
    }
    
    private static void addNotificationAction(final Context context, final NotificationCompat.Builder builder, final Bundle bundle, final int n) {
        final Bundle bundle2 = new Bundle(bundle);
        final String actionFieldAtIndex = getActionFieldAtIndex(n, bundle, "ab_a*_a");
        bundle2.putInt("appboy_action_index", n);
        bundle2.putString("appboy_action_type", actionFieldAtIndex);
        bundle2.putString("appboy_action_id", getActionFieldAtIndex(n, bundle, "ab_a*_id"));
        bundle2.putString("appboy_action_uri", getActionFieldAtIndex(n, bundle, "ab_a*_uri"));
        bundle2.putString("appboy_action_use_webview", getActionFieldAtIndex(n, bundle, "ab_a*_use_webview"));
        final Intent setClass = new Intent("com.appboy.action.APPBOY_ACTION_CLICKED").setClass(context, (Class)AppboyNotificationRoutingActivity.class);
        setClass.putExtras(bundle2);
        final NotificationCompat.Action.Builder builder2 = new NotificationCompat.Action.Builder(0, getActionFieldAtIndex(n, bundle, "ab_a*_t"), PendingIntent.getActivity(context, IntentUtils.getRequestCode(), setClass, 134217728));
        builder2.addExtras(new Bundle(bundle2));
        builder.addAction(builder2.build());
    }
    
    public static void addNotificationActions(final Context context, final NotificationCompat.Builder builder, final Bundle bundle) {
        while (true) {
            while (true) {
                if (bundle != null) {
                    final int n = 0;
                    break Label_0014;
                }
                try {
                    AppboyLogger.w(AppboyNotificationActionUtils.TAG, "Notification extras were null. Doing nothing.");
                    return;
                    // iftrue(Label_0051:, StringUtils.isNullOrBlank(getActionFieldAtIndex(n, bundle, "ab_a*_a")))
                    final int n;
                    addNotificationAction(context, builder, bundle, n);
                    ++n;
                    continue;
                    final Exception ex;
                    AppboyLogger.e(AppboyNotificationActionUtils.TAG, "Caught exception while adding notification action buttons.", ex);
                    Label_0051: {
                        return;
                    }
                }
                catch (Exception ex2) {}
                break;
            }
            final Exception ex2;
            final Exception ex = ex2;
            continue;
        }
    }
    
    public static String getActionFieldAtIndex(final int n, final Bundle bundle, final String s) {
        return getActionFieldAtIndex(n, bundle, s, "");
    }
    
    public static String getActionFieldAtIndex(final int n, final Bundle bundle, final String s, final String s2) {
        final String string = bundle.getString(s.replace("*", String.valueOf(n)));
        if (string == null) {
            return s2;
        }
        return string;
    }
    
    public static void handleNotificationActionClicked(final Context context, final Intent intent) {
        try {
            final String stringExtra = intent.getStringExtra("appboy_action_type");
            if (StringUtils.isNullOrBlank(stringExtra)) {
                AppboyLogger.w(AppboyNotificationActionUtils.TAG, "Notification action button type was blank or null. Doing nothing.");
                return;
            }
            final int intExtra = intent.getIntExtra("nid", -1);
            logNotificationActionClicked(context, intent);
            if (!stringExtra.equals("ab_uri") && !stringExtra.equals("ab_open")) {
                if (stringExtra.equals("ab_none")) {
                    AppboyNotificationUtils.cancelNotification(context, intExtra);
                    return;
                }
                AppboyLogger.w(AppboyNotificationActionUtils.TAG, "Unknown notification action button clicked. Doing nothing.");
            }
            else {
                AppboyNotificationUtils.cancelNotification(context, intExtra);
                context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
                if (stringExtra.equals("ab_uri") && intent.getExtras().containsKey("appboy_action_uri")) {
                    intent.putExtra("uri", intent.getStringExtra("appboy_action_uri"));
                    if (intent.getExtras().containsKey("appboy_action_use_webview")) {
                        intent.putExtra("ab_use_webview", intent.getStringExtra("appboy_action_use_webview"));
                    }
                }
                else {
                    intent.removeExtra("uri");
                }
                AppboyNotificationUtils.sendNotificationOpenedBroadcast(context, intent);
                if (new AppboyConfigurationProvider(context).getHandlePushDeepLinksAutomatically()) {
                    AppboyNotificationUtils.routeUserWithNotificationOpenedIntent(context, intent);
                }
            }
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyNotificationActionUtils.TAG, "Caught exception while handling notification action button click.", ex);
        }
    }
    
    private static void logNotificationActionClicked(final Context context, final Intent intent) {
        Appboy.getInstance(context).logPushNotificationActionClicked(intent.getStringExtra("cid"), intent.getStringExtra("appboy_action_id"));
    }
}
