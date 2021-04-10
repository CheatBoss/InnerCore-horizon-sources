package com.appboy.push;

import android.content.*;
import android.content.res.*;
import com.appboy.*;
import com.appboy.configuration.*;
import org.json.*;
import java.util.*;
import android.util.*;
import com.appboy.ui.*;
import com.appboy.enums.*;
import com.appboy.ui.actions.*;
import com.appboy.ui.support.*;
import android.support.v4.app.*;
import android.net.*;
import android.graphics.*;
import android.app.*;
import com.appboy.support.*;
import android.os.*;

public class AppboyNotificationUtils
{
    public static final String APPBOY_NOTIFICATION_DELETED_SUFFIX = ".intent.APPBOY_PUSH_DELETED";
    public static final String APPBOY_NOTIFICATION_OPENED_SUFFIX = ".intent.APPBOY_NOTIFICATION_OPENED";
    public static final String APPBOY_NOTIFICATION_RECEIVED_SUFFIX = ".intent.APPBOY_PUSH_RECEIVED";
    private static final String SOURCE_KEY = "source";
    private static final String TAG;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyNotificationUtils.class);
    }
    
    @Deprecated
    public static String bundleOptString(final Bundle bundle, final String s, final String s2) {
        return bundle.getString(s, s2);
    }
    
    public static void cancelNotification(final Context context, final int n) {
        try {
            final String tag = AppboyNotificationUtils.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Cancelling notification action with id: ");
            sb.append(n);
            AppboyLogger.d(tag, sb.toString());
            final Intent setClass = new Intent("com.appboy.action.CANCEL_NOTIFICATION").setClass(context, (Class)getNotificationReceiverClass());
            setClass.putExtra("nid", n);
            IntentUtils.addComponentAndSendBroadcast(context, setClass);
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyNotificationUtils.TAG, "Exception occurred attempting to cancel notification.", ex);
        }
    }
    
    public static IAppboyNotificationFactory getActiveNotificationFactory() {
        IAppboyNotificationFactory appboyNotificationFactory;
        if ((appboyNotificationFactory = Appboy.getCustomAppboyNotificationFactory()) == null) {
            appboyNotificationFactory = AppboyNotificationFactory.getInstance();
        }
        return appboyNotificationFactory;
    }
    
    public static Bundle getAppboyExtrasWithoutPreprocessing(final Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        if (bundle.containsKey("appboy_story_newly_received") && !bundle.getBoolean("appboy_story_newly_received")) {
            return bundle.getBundle("extra");
        }
        if (!Constants.IS_AMAZON) {
            return parseJSONStringDictionaryIntoBundle(bundle.getString("extra", "{}"));
        }
        return new Bundle(bundle);
    }
    
    private static int getNotificationChannelImportance(final NotificationChannel notificationChannel) {
        return notificationChannel.getImportance();
    }
    
    public static int getNotificationId(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey("n")) {
                try {
                    final int int1 = Integer.parseInt(bundle.getString("n"));
                    final String tag = AppboyNotificationUtils.TAG;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Using notification id provided in the message's extras bundle: ");
                    sb.append(int1);
                    AppboyLogger.d(tag, sb.toString());
                    return int1;
                }
                catch (NumberFormatException ex) {
                    AppboyLogger.e(AppboyNotificationUtils.TAG, "Unable to parse notification id provided in the message's extras bundle. Using default notification id instead: -1", ex);
                    return -1;
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(bundle.getString("t", ""));
            sb2.append(bundle.getString("a", ""));
            final int hashCode = sb2.toString().hashCode();
            final String tag2 = AppboyNotificationUtils.TAG;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Message without notification id provided in the extras bundle received. Using a hash of the message: ");
            sb3.append(hashCode);
            AppboyLogger.d(tag2, sb3.toString());
            return hashCode;
        }
        AppboyLogger.d(AppboyNotificationUtils.TAG, "Message without extras bundle received. Using default notification id: ");
        return -1;
    }
    
    public static int getNotificationPriority(final Bundle bundle) {
        if (bundle != null && bundle.containsKey("p")) {
            try {
                final int int1 = Integer.parseInt(bundle.getString("p"));
                if (isValidNotificationPriority(int1)) {
                    return int1;
                }
                final String tag = AppboyNotificationUtils.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("Received invalid notification priority ");
                sb.append(int1);
                AppboyLogger.e(tag, sb.toString());
            }
            catch (NumberFormatException ex) {
                AppboyLogger.e(AppboyNotificationUtils.TAG, "Unable to parse custom priority. Returning default priority of 0", ex);
            }
        }
        return 0;
    }
    
    public static Class<?> getNotificationReceiverClass() {
        if (Constants.IS_AMAZON) {
            return AppboyAdmReceiver.class;
        }
        return AppboyGcmReceiver.class;
    }
    
    static String getOptionalStringResource(final Resources resources, final int n, final String s) {
        try {
            return resources.getString(n);
        }
        catch (Resources$NotFoundException ex) {
            return s;
        }
    }
    
    private static PendingIntent getPushActionPendingIntent(final Context context, final String s, final Bundle bundle) {
        final Intent setClass = new Intent(s).setClass(context, (Class)getNotificationReceiverClass());
        if (bundle != null) {
            setClass.putExtras(bundle);
        }
        return PendingIntent.getBroadcast(context, IntentUtils.getRequestCode(), setClass, 1073741824);
    }
    
    static NotificationChannel getValidNotificationChannel(final NotificationManager notificationManager, final Bundle bundle) {
        String s;
        String s2;
        if (bundle == null) {
            s = AppboyNotificationUtils.TAG;
            s2 = "Notification extras bundle was null. Could not find a valid notification channel";
        }
        else {
            final String string = bundle.getString("ab_nc", (String)null);
            if (!StringUtils.isNullOrBlank(string)) {
                final NotificationChannel notificationChannel = notificationManager.getNotificationChannel(string);
                if (notificationChannel != null) {
                    final String tag = AppboyNotificationUtils.TAG;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Found notification channel in extras with id: ");
                    sb.append(string);
                    AppboyLogger.d(tag, sb.toString());
                    return notificationChannel;
                }
                final String tag2 = AppboyNotificationUtils.TAG;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Notification channel from extras is invalid, no channel found with id: ");
                sb2.append(string);
                AppboyLogger.d(tag2, sb2.toString());
            }
            final NotificationChannel notificationChannel2 = notificationManager.getNotificationChannel("com_appboy_default_notification_channel");
            if (notificationChannel2 != null) {
                return notificationChannel2;
            }
            s = AppboyNotificationUtils.TAG;
            s2 = "Appboy default notification channel does not exist on device.";
        }
        AppboyLogger.d(s, s2);
        return null;
    }
    
    public static void handleCancelNotificationAction(final Context context, final Intent intent) {
        try {
            if (intent.hasExtra("nid")) {
                final int intExtra = intent.getIntExtra("nid", -1);
                final String tag = AppboyNotificationUtils.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("Cancelling notification action with id: ");
                sb.append(intExtra);
                AppboyLogger.d(tag, sb.toString());
                ((NotificationManager)context.getSystemService("notification")).cancel("appboy_notification", intExtra);
            }
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyNotificationUtils.TAG, "Exception occurred handling cancel notification intent.", ex);
        }
    }
    
    public static void handleContentCardsSerializedCardIfPresent(final Context context, final Bundle bundle) {
        if (!Constants.IS_AMAZON && bundle.containsKey("ab_cd")) {
            final String string = bundle.getString("ab_cd", (String)null);
            final String string2 = bundle.getString("ab_cd_uid", (String)null);
            final String tag = AppboyNotificationUtils.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Push contains associated Content Cards card. User id: ");
            sb.append(string2);
            sb.append(" Card data: ");
            sb.append(string);
            AppboyLogger.d(tag, sb.toString());
            AppboyInternal.addSerializedContentCardToStorage(context, string, string2);
        }
    }
    
    public static void handleNotificationDeleted(final Context context, final Intent intent) {
        try {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Sending notification deleted broadcast");
            sendPushActionIntent(context, ".intent.APPBOY_PUSH_DELETED", intent.getExtras());
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyNotificationUtils.TAG, "Exception occurred attempting to handle notification delete intent.", ex);
        }
    }
    
    public static void handleNotificationOpened(final Context context, final Intent intent) {
        try {
            logNotificationOpened(context, intent);
            sendNotificationOpenedBroadcast(context, intent);
            if (new AppboyConfigurationProvider(context).getHandlePushDeepLinksAutomatically()) {
                routeUserWithNotificationOpenedIntent(context, intent);
            }
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyNotificationUtils.TAG, "Exception occurred attempting to handle notification opened intent.", ex);
        }
    }
    
    public static void handlePushStoryPageClicked(final Context context, final Intent intent) {
        try {
            Appboy.getInstance(context).logPushStoryPageClicked(intent.getStringExtra("appboy_campaign_id"), intent.getStringExtra("appboy_story_page_id"));
            if (!StringUtils.isNullOrBlank(intent.getStringExtra("appboy_action_uri"))) {
                intent.putExtra("uri", intent.getStringExtra("appboy_action_uri"));
                final String stringExtra = intent.getStringExtra("appboy_action_use_webview");
                if (!StringUtils.isNullOrBlank(stringExtra)) {
                    intent.putExtra("ab_use_webview", stringExtra);
                }
            }
            else {
                intent.removeExtra("uri");
            }
            context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
            sendNotificationOpenedBroadcast(context, intent);
            if (new AppboyConfigurationProvider(context).getHandlePushDeepLinksAutomatically()) {
                routeUserWithNotificationOpenedIntent(context, intent);
            }
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyNotificationUtils.TAG, "Caught exception while handling story click.", ex);
        }
    }
    
    public static boolean isAppboyPushMessage(final Intent intent) {
        final Bundle extras = intent.getExtras();
        return extras != null && "true".equals(extras.getString("_ab"));
    }
    
    public static boolean isNotificationMessage(final Intent intent) {
        final Bundle extras = intent.getExtras();
        return extras != null && extras.containsKey("t") && extras.containsKey("a");
    }
    
    @Deprecated
    public static boolean isUninstallTrackingPush(final Bundle bundle) {
        AppboyLogger.w(AppboyNotificationUtils.TAG, "Uninstall tracking no longer sends a silent push notification to devices. This method should not be used. Returning false.");
        return false;
    }
    
    public static boolean isValidNotificationPriority(final int n) {
        return n >= -2 && n <= 2;
    }
    
    public static boolean isValidNotificationVisibility(final int n) {
        boolean b = true;
        if (n != -1) {
            b = b;
            if (n != 0) {
                if (n == 1) {
                    return true;
                }
                b = false;
            }
        }
        return b;
    }
    
    public static void logBaiduNotificationClick(final Context context, final String s) {
        if (s == null) {
            AppboyLogger.w(AppboyNotificationUtils.TAG, "customContentString was null. Doing nothing.");
            return;
        }
        try {
            final JSONObject jsonObject = new JSONObject(s);
            final String optString = jsonObject.optString("source", (String)null);
            final String optString2 = jsonObject.optString("cid", (String)null);
            if (optString != null && "Appboy".equals(optString) && optString2 != null) {
                Appboy.getInstance(context).logPushNotificationOpened(optString2);
            }
        }
        catch (Exception ex) {
            final String tag = AppboyNotificationUtils.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Caught an exception processing customContentString: ");
            sb.append(s);
            AppboyLogger.e(tag, sb.toString(), ex);
        }
    }
    
    private static void logNotificationOpened(final Context context, final Intent intent) {
        Appboy.getInstance(context).logPushNotificationOpened(intent);
    }
    
    public static void logPushDeliveryEvent(final Context context, final Bundle bundle) {
        if (bundle == null) {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Could not log push delivery event due to null push extras bundle.");
            return;
        }
        final String string = bundle.getString("cid");
        if (!StringUtils.isNullOrBlank(string)) {
            Appboy.getInstance(context).logPushDeliveryEvent(string);
            return;
        }
        final String tag = AppboyNotificationUtils.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("Could not log push delivery event due to null or blank campaign id in push extras bundle: ");
        sb.append(bundle);
        AppboyLogger.d(tag, sb.toString());
    }
    
    public static Bundle parseJSONStringDictionaryIntoBundle(final String s) {
        try {
            final Bundle bundle = new Bundle();
            final JSONObject jsonObject = new JSONObject(s);
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                final String s2 = keys.next();
                bundle.putString(s2, jsonObject.getString(s2));
            }
            return bundle;
        }
        catch (JSONException ex) {
            AppboyLogger.e(AppboyNotificationUtils.TAG, "Unable parse JSON into a bundle.", (Throwable)ex);
            return null;
        }
    }
    
    public static void prefetchBitmapsIfNewlyReceivedStoryPush(final Context context, final Bundle bundle) {
        if (!bundle.containsKey("ab_c")) {
            return;
        }
        if (bundle.getBoolean("appboy_story_newly_received", false)) {
            String s = AppboyNotificationActionUtils.getActionFieldAtIndex(0, bundle, "ab_c*_i");
            for (int n = 0; !StringUtils.isNullOrBlank(s); s = AppboyNotificationActionUtils.getActionFieldAtIndex(n, bundle, "ab_c*_i")) {
                final String tag = AppboyNotificationUtils.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("Pre-fetching bitmap at URL: ");
                sb.append(s);
                AppboyLogger.v(tag, sb.toString());
                Appboy.getInstance(context).getAppboyImageLoader().getBitmapFromUrl(context, s, AppboyViewBounds.NOTIFICATION_ONE_IMAGE_STORY);
                ++n;
            }
            bundle.putBoolean("appboy_story_newly_received", false);
        }
    }
    
    public static boolean requestGeofenceRefreshIfAppropriate(final Context context, final Bundle bundle) {
        String s;
        String s2;
        if (bundle.containsKey("ab_sync_geos")) {
            if (Boolean.parseBoolean(bundle.getString("ab_sync_geos"))) {
                AppboyInternal.requestGeofenceRefresh(context, true);
                return true;
            }
            s = AppboyNotificationUtils.TAG;
            s2 = "Geofence sync key was false. Not syncing geofences.";
        }
        else {
            s = AppboyNotificationUtils.TAG;
            s2 = "Geofence sync key not included in push payload. Not syncing geofences.";
        }
        AppboyLogger.d(s, s2);
        return false;
    }
    
    public static void routeUserWithNotificationOpenedIntent(final Context context, final Intent intent) {
        Bundle bundleExtra;
        if ((bundleExtra = intent.getBundleExtra("extra")) == null) {
            bundleExtra = new Bundle();
        }
        bundleExtra.putString("cid", intent.getStringExtra("cid"));
        bundleExtra.putString("source", "Appboy");
        final String stringExtra = intent.getStringExtra("uri");
        if (!StringUtils.isNullOrBlank(stringExtra)) {
            final String tag = AppboyNotificationUtils.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Found a deep link ");
            sb.append(stringExtra);
            Log.d(tag, sb.toString());
            final boolean equalsIgnoreCase = "true".equalsIgnoreCase(intent.getStringExtra("ab_use_webview"));
            final String tag2 = AppboyNotificationUtils.TAG;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Use webview set to: ");
            sb2.append(equalsIgnoreCase);
            Log.d(tag2, sb2.toString());
            bundleExtra.putString("uri", stringExtra);
            bundleExtra.putBoolean("ab_use_webview", equalsIgnoreCase);
            AppboyNavigator.getAppboyNavigator().gotoUri(context, ActionFactory.createUriActionFromUrlString(stringExtra, bundleExtra, equalsIgnoreCase, Channel.PUSH));
            return;
        }
        Log.d(AppboyNotificationUtils.TAG, "Push notification had no deep link. Opening main activity.");
        context.startActivity(UriUtils.getMainActivityIntent(context, bundleExtra));
    }
    
    static void sendNotificationOpenedBroadcast(final Context context, final Intent intent) {
        AppboyLogger.d(AppboyNotificationUtils.TAG, "Sending notification opened broadcast");
        sendPushActionIntent(context, ".intent.APPBOY_NOTIFICATION_OPENED", intent.getExtras());
    }
    
    private static void sendPushActionIntent(final Context context, final String s, final Bundle bundle) {
        final StringBuilder sb = new StringBuilder();
        sb.append(context.getPackageName());
        sb.append(s);
        final Intent intent = new Intent(sb.toString());
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        IntentUtils.addComponentAndSendBroadcast(context, intent);
    }
    
    public static void sendPushMessageReceivedBroadcast(final Context context, final Bundle bundle) {
        AppboyLogger.d(AppboyNotificationUtils.TAG, "Sending push message received broadcast");
        sendPushActionIntent(context, ".intent.APPBOY_PUSH_RECEIVED", bundle);
    }
    
    public static void setAccentColorIfPresentAndSupported(final AppboyConfigurationProvider appboyConfigurationProvider, final NotificationCompat.Builder builder, final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 21) {
            if (bundle != null && bundle.containsKey("ac")) {
                AppboyLogger.d(AppboyNotificationUtils.TAG, "Using accent color for notification from extras bundle");
                builder.setColor((int)Long.parseLong(bundle.getString("ac")));
                return;
            }
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Using default accent color for notification");
            builder.setColor(appboyConfigurationProvider.getDefaultNotificationAccentColor());
        }
    }
    
    public static void setCategoryIfPresentAndSupported(final NotificationCompat.Builder builder, final Bundle bundle) {
        if (Build$VERSION.SDK_INT < 21) {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Notification category not supported on this android version. Not setting category for notification.");
            return;
        }
        if (bundle != null && bundle.containsKey("ab_ct")) {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Setting category for notification");
            builder.setCategory(bundle.getString("ab_ct"));
            return;
        }
        AppboyLogger.d(AppboyNotificationUtils.TAG, "Category not present in notification extras. Not setting category for notification.");
    }
    
    public static void setContentIfPresent(final NotificationCompat.Builder builder, final Bundle bundle) {
        if (bundle != null) {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Setting content for notification");
            builder.setContentText(bundle.getString("a"));
        }
    }
    
    public static void setContentIntentIfPresent(final Context context, final NotificationCompat.Builder builder, final Bundle bundle) {
        try {
            builder.setContentIntent(getPushActionPendingIntent(context, "com.appboy.action.APPBOY_PUSH_CLICKED", bundle));
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyNotificationUtils.TAG, "Error setting content intent.", ex);
        }
    }
    
    public static void setDeleteIntent(final Context context, final NotificationCompat.Builder builder, final Bundle bundle) {
        AppboyLogger.d(AppboyNotificationUtils.TAG, "Setting delete intent.");
        try {
            builder.setDeleteIntent(getPushActionPendingIntent(context, "com.appboy.action.APPBOY_PUSH_DELETED", bundle));
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyNotificationUtils.TAG, "Error setting delete intent.", ex);
        }
    }
    
    public static boolean setLargeIconIfPresentAndSupported(final Context context, final AppboyConfigurationProvider appboyConfigurationProvider, final NotificationCompat.Builder builder, final Bundle bundle) {
        String s = null;
        String s2 = null;
        Label_0018: {
            if (bundle.containsKey("ab_c")) {
                s = AppboyNotificationUtils.TAG;
                s2 = "Large icon not supported in story push.";
            }
            else {
            Label_0124_Outer:
                while (true) {
                    if (bundle != null) {
                        while (true) {
                            try {
                                if (bundle.containsKey("ab_li")) {
                                    AppboyLogger.d(AppboyNotificationUtils.TAG, "Setting large icon for notification");
                                    builder.setLargeIcon(AppboyImageUtils.getBitmap(context, Uri.parse(bundle.getString("ab_li")), AppboyViewBounds.NOTIFICATION_LARGE_ICON));
                                    return true;
                                }
                                AppboyLogger.d(AppboyNotificationUtils.TAG, "Large icon bitmap url not present in extras. Attempting to use resource id instead.");
                                final int largeNotificationIconResourceId = appboyConfigurationProvider.getLargeNotificationIconResourceId();
                                if (largeNotificationIconResourceId != 0) {
                                    builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeNotificationIconResourceId));
                                    return true;
                                }
                                AppboyLogger.d(AppboyNotificationUtils.TAG, "Large icon resource id not present for notification");
                                while (true) {
                                    s = AppboyNotificationUtils.TAG;
                                    s2 = "Large icon not set for notification";
                                    break Label_0018;
                                    final Exception ex;
                                    AppboyLogger.e(AppboyNotificationUtils.TAG, "Error setting large notification icon", ex);
                                    continue Label_0124_Outer;
                                }
                            }
                            catch (Exception ex2) {}
                            final Exception ex2;
                            final Exception ex = ex2;
                            continue;
                        }
                    }
                    continue;
                }
            }
        }
        AppboyLogger.d(s, s2);
        return false;
    }
    
    public static void setNotificationBadgeNumberIfPresent(final NotificationCompat.Builder builder, final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 26) {
            final String string = bundle.getString("ab_bc", (String)null);
            if (!StringUtils.isNullOrBlank(string)) {
                try {
                    builder.setNumber(Integer.parseInt(string));
                }
                catch (NumberFormatException ex) {
                    AppboyLogger.e(AppboyNotificationUtils.TAG, "Caught exception while setting number on notification.", ex);
                }
            }
        }
    }
    
    public static void setNotificationChannelIfSupported(final Context context, final AppboyConfigurationProvider appboyConfigurationProvider, final NotificationCompat.Builder builder, final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 26) {
            final NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
            final NotificationChannel validNotificationChannel = getValidNotificationChannel(notificationManager, bundle);
            if (validNotificationChannel != null) {
                final String tag = AppboyNotificationUtils.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("Using notification channel with id: ");
                sb.append(validNotificationChannel.getId());
                AppboyLogger.d(tag, sb.toString());
                builder.setChannelId(validNotificationChannel.getId());
                return;
            }
            if (validNotificationChannel == null || validNotificationChannel.getId().equals("com_appboy_default_notification_channel")) {
                final NotificationChannel notificationChannel = new NotificationChannel("com_appboy_default_notification_channel", (CharSequence)appboyConfigurationProvider.getDefaultNotificationChannelName(), 3);
                notificationChannel.setDescription(appboyConfigurationProvider.getDefaultNotificationChannelDescription());
                notificationManager.createNotificationChannel(notificationChannel);
                builder.setChannelId("com_appboy_default_notification_channel");
                AppboyLogger.d(AppboyNotificationUtils.TAG, "Using default notification channel with id: com_appboy_default_notification_channel");
            }
        }
    }
    
    public static void setNotificationDurationAlarm(final Context context, final Class<?> clazz, final int n, final int n2) {
        final Intent intent = new Intent(context, (Class)clazz);
        intent.setAction("com.appboy.action.CANCEL_NOTIFICATION");
        intent.putExtra("nid", n);
        final PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, intent, 134217728);
        final AlarmManager alarmManager = (AlarmManager)context.getSystemService("alarm");
        if (n2 >= 1000) {
            final String tag = AppboyNotificationUtils.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Setting Notification duration alarm for ");
            sb.append(n2);
            sb.append(" ms");
            AppboyLogger.d(tag, sb.toString());
            alarmManager.set(3, SystemClock.elapsedRealtime() + n2, broadcast);
        }
    }
    
    public static void setPriorityIfPresentAndSupported(final NotificationCompat.Builder builder, final Bundle bundle) {
        if (bundle != null) {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Setting priority for notification");
            builder.setPriority(getNotificationPriority(bundle));
        }
    }
    
    public static void setPublicVersionIfPresentAndSupported(final Context context, final AppboyConfigurationProvider appboyConfigurationProvider, final NotificationCompat.Builder builder, Bundle jsonStringDictionaryIntoBundle) {
        if (Build$VERSION.SDK_INT >= 21 && jsonStringDictionaryIntoBundle != null && jsonStringDictionaryIntoBundle.containsKey("ab_pn")) {
            jsonStringDictionaryIntoBundle = parseJSONStringDictionaryIntoBundle(jsonStringDictionaryIntoBundle.getString("ab_pn"));
            final NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context);
            setContentIfPresent(builder2, jsonStringDictionaryIntoBundle);
            setTitleIfPresent(builder2, jsonStringDictionaryIntoBundle);
            setSummaryTextIfPresentAndSupported(builder2, jsonStringDictionaryIntoBundle);
            setSmallIcon(appboyConfigurationProvider, builder2);
            setAccentColorIfPresentAndSupported(appboyConfigurationProvider, builder2, jsonStringDictionaryIntoBundle);
            builder.setPublicVersion(builder2.build());
        }
    }
    
    public static void setSetShowWhen(final NotificationCompat.Builder builder, final Bundle bundle) {
        if (bundle.containsKey("ab_c")) {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Set show when not supported in story push.");
            builder.setShowWhen(false);
        }
    }
    
    public static int setSmallIcon(final AppboyConfigurationProvider appboyConfigurationProvider, final NotificationCompat.Builder builder) {
        int smallIcon = appboyConfigurationProvider.getSmallNotificationIconResourceId();
        if (smallIcon == 0) {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Small notification icon resource was not found. Will use the app icon when displaying notifications.");
            smallIcon = appboyConfigurationProvider.getApplicationIconResourceId();
        }
        else {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Setting small icon for notification via resource id");
        }
        builder.setSmallIcon(smallIcon);
        return smallIcon;
    }
    
    public static void setSoundIfPresentAndSupported(final NotificationCompat.Builder builder, final Bundle bundle) {
        if (bundle != null && bundle.containsKey("sd")) {
            final String string = bundle.getString("sd");
            if (string != null) {
                if (string.equals("d")) {
                    AppboyLogger.d(AppboyNotificationUtils.TAG, "Setting default sound for notification.");
                    builder.setDefaults(1);
                    return;
                }
                AppboyLogger.d(AppboyNotificationUtils.TAG, "Setting sound for notification via uri.");
                builder.setSound(Uri.parse(string));
            }
        }
        else {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Sound key not present in notification extras. Not setting sound for notification.");
        }
    }
    
    public static void setStyleIfSupported(final Context context, final NotificationCompat.Builder builder, final Bundle bundle, final Bundle bundle2) {
        if (bundle != null) {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Setting style for notification");
            builder.setStyle(AppboyNotificationStyleFactory.getBigNotificationStyle(context, bundle, bundle2, builder));
        }
    }
    
    public static void setSummaryTextIfPresentAndSupported(final NotificationCompat.Builder builder, final Bundle bundle) {
        if (bundle != null && bundle.containsKey("s")) {
            final String string = bundle.getString("s");
            if (string != null) {
                AppboyLogger.d(AppboyNotificationUtils.TAG, "Setting summary text for notification");
                builder.setSubText(string);
            }
        }
        else {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Summary text not present in notification extras. Not setting summary text for notification.");
        }
    }
    
    public static void setTickerIfPresent(final NotificationCompat.Builder builder, final Bundle bundle) {
        if (bundle != null) {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Setting ticker for notification");
            builder.setTicker(bundle.getString("t"));
        }
    }
    
    public static void setTitleIfPresent(final NotificationCompat.Builder builder, final Bundle bundle) {
        if (bundle != null) {
            AppboyLogger.d(AppboyNotificationUtils.TAG, "Setting title for notification");
            builder.setContentTitle(bundle.getString("t"));
        }
    }
    
    public static void setVisibilityIfPresentAndSupported(final NotificationCompat.Builder builder, final Bundle bundle) {
        if (Build$VERSION.SDK_INT >= 21) {
            if (bundle == null || !bundle.containsKey("ab_vs")) {
                return;
            }
            try {
                final int int1 = Integer.parseInt(bundle.getString("ab_vs"));
                if (isValidNotificationVisibility(int1)) {
                    AppboyLogger.d(AppboyNotificationUtils.TAG, "Setting visibility for notification");
                    builder.setVisibility(int1);
                    return;
                }
                final String tag = AppboyNotificationUtils.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("Received invalid notification visibility ");
                sb.append(int1);
                AppboyLogger.e(tag, sb.toString());
                return;
            }
            catch (Exception ex) {
                AppboyLogger.e(AppboyNotificationUtils.TAG, "Failed to parse visibility from notificationExtras", ex);
                return;
            }
        }
        AppboyLogger.d(AppboyNotificationUtils.TAG, "Notification visibility not supported on this android version. Not setting visibility for notification.");
    }
    
    public static boolean wakeScreenIfHasPermission(final Context context, final Bundle bundle) {
        if (!PermissionUtils.hasPermission(context, "android.permission.WAKE_LOCK")) {
            return false;
        }
        Label_0110: {
            if (Build$VERSION.SDK_INT >= 26) {
                final NotificationChannel validNotificationChannel = getValidNotificationChannel((NotificationManager)context.getSystemService("notification"), bundle);
                String s;
                String string;
                if (validNotificationChannel == null) {
                    s = AppboyNotificationUtils.TAG;
                    string = "Not waking screen on Android O+ device, could not find notification channel.";
                }
                else {
                    final int notificationChannelImportance = getNotificationChannelImportance(validNotificationChannel);
                    if (notificationChannelImportance != 1) {
                        break Label_0110;
                    }
                    s = AppboyNotificationUtils.TAG;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Not acquiring wake-lock for Android O+ notification with importance: ");
                    sb.append(notificationChannelImportance);
                    string = sb.toString();
                }
                AppboyLogger.d(s, string);
                return false;
            }
            if (getNotificationPriority(bundle) == -2) {
                return false;
            }
        }
        final PowerManager$WakeLock wakeLock = ((PowerManager)context.getSystemService("power")).newWakeLock(268435482, AppboyNotificationUtils.TAG);
        wakeLock.acquire();
        wakeLock.release();
        return true;
    }
}
