package com.appboy;

import com.appboy.support.*;
import android.content.*;
import android.support.v4.app.*;
import android.util.*;
import com.appboy.configuration.*;
import android.app.*;
import com.appboy.push.*;
import android.os.*;

public final class AppboyGcmReceiver extends BroadcastReceiver
{
    @Deprecated
    public static final String CAMPAIGN_ID_KEY = "cid";
    private static final String GCM_DELETED_MESSAGES_KEY = "deleted_messages";
    private static final String GCM_ERROR_KEY = "error";
    private static final String GCM_MESSAGE_TYPE_KEY = "message_type";
    private static final String GCM_NUMBER_OF_MESSAGES_DELETED_KEY = "total_deleted";
    private static final String GCM_RECEIVE_INTENT_ACTION = "com.google.android.c2dm.intent.RECEIVE";
    private static final String GCM_REGISTRATION_ID_KEY = "registration_id";
    private static final String GCM_REGISTRATION_INTENT_ACTION = "com.google.android.c2dm.intent.REGISTRATION";
    private static final String GCM_UNREGISTERED_KEY = "unregistered";
    private static final String TAG;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyGcmReceiver.class);
    }
    
    boolean handleAppboyGcmMessage(final Context context, final Intent intent) {
        final NotificationManagerCompat from = NotificationManagerCompat.from(context);
        if ("deleted_messages".equals(intent.getStringExtra("message_type"))) {
            final int intExtra = intent.getIntExtra("total_deleted", -1);
            if (intExtra == -1) {
                final String tag = AppboyGcmReceiver.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to parse GCM message. Intent: ");
                sb.append(intent.toString());
                Log.e(tag, sb.toString());
                return false;
            }
            final String tag2 = AppboyGcmReceiver.TAG;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("GCM deleted ");
            sb2.append(intExtra);
            sb2.append(" messages. Fetch them from Appboy.");
            AppboyLogger.i(tag2, sb2.toString());
            return false;
        }
        else {
            final Bundle extras = intent.getExtras();
            final String tag3 = AppboyGcmReceiver.TAG;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Push message payload received: ");
            sb3.append(extras);
            AppboyLogger.i(tag3, sb3.toString());
            final Bundle appboyExtrasWithoutPreprocessing = AppboyNotificationUtils.getAppboyExtrasWithoutPreprocessing(extras);
            extras.putBundle("extra", appboyExtrasWithoutPreprocessing);
            if (!extras.containsKey("appboy_push_received_timestamp")) {
                extras.putLong("appboy_push_received_timestamp", System.currentTimeMillis());
            }
            AppboyNotificationUtils.handleContentCardsSerializedCardIfPresent(context, extras);
            if (!AppboyNotificationUtils.isNotificationMessage(intent)) {
                AppboyLogger.d(AppboyGcmReceiver.TAG, "Received data push");
                AppboyNotificationUtils.logPushDeliveryEvent(context, extras);
                AppboyNotificationUtils.sendPushMessageReceivedBroadcast(context, extras);
                AppboyNotificationUtils.requestGeofenceRefreshIfAppropriate(context, extras);
                return false;
            }
            AppboyLogger.d(AppboyGcmReceiver.TAG, "Received notification push");
            final int notificationId = AppboyNotificationUtils.getNotificationId(extras);
            extras.putInt("nid", notificationId);
            final AppboyConfigurationProvider appboyConfigurationProvider = new AppboyConfigurationProvider(context);
            final IAppboyNotificationFactory activeNotificationFactory = AppboyNotificationUtils.getActiveNotificationFactory();
            Label_0295: {
                if (extras.containsKey("ab_c")) {
                    if (extras.containsKey("appboy_story_newly_received")) {
                        break Label_0295;
                    }
                    AppboyLogger.d(AppboyGcmReceiver.TAG, "Received the initial push story notification.");
                    extras.putBoolean("appboy_story_newly_received", true);
                }
                AppboyNotificationUtils.logPushDeliveryEvent(context, extras);
            }
            final Notification notification = activeNotificationFactory.createNotification(appboyConfigurationProvider, context, extras, appboyExtrasWithoutPreprocessing);
            if (notification == null) {
                AppboyLogger.d(AppboyGcmReceiver.TAG, "Notification created by notification factory was null. Not displaying notification.");
                return false;
            }
            from.notify("appboy_notification", notificationId, notification);
            AppboyNotificationUtils.sendPushMessageReceivedBroadcast(context, extras);
            AppboyNotificationUtils.wakeScreenIfHasPermission(context, extras);
            if (extras != null && extras.containsKey("nd")) {
                AppboyNotificationUtils.setNotificationDurationAlarm(context, this.getClass(), notificationId, Integer.parseInt(extras.getString("nd")));
            }
            return true;
        }
    }
    
    void handleAppboyGcmReceiveIntent(final Context context, final Intent intent) {
        if (AppboyNotificationUtils.isAppboyPushMessage(intent)) {
            new HandleAppboyGcmMessageTask(context, intent);
        }
    }
    
    boolean handleRegistrationEventIfEnabled(final AppboyConfigurationProvider appboyConfigurationProvider, final Context context, final Intent intent) {
        if (appboyConfigurationProvider.isGcmMessagingRegistrationEnabled()) {
            this.handleRegistrationIntent(context, intent);
            return true;
        }
        return false;
    }
    
    boolean handleRegistrationIntent(final Context context, final Intent intent) {
        final String stringExtra = intent.getStringExtra("error");
        final String stringExtra2 = intent.getStringExtra("registration_id");
        String s3 = null;
        String string = null;
        Label_0219: {
            if (stringExtra != null) {
                String s;
                String s2;
                if ("SERVICE_NOT_AVAILABLE".equals(stringExtra)) {
                    s = AppboyGcmReceiver.TAG;
                    s2 = "Unable to connect to the GCM registration server. Try again later.";
                }
                else if ("ACCOUNT_MISSING".equals(stringExtra)) {
                    s = AppboyGcmReceiver.TAG;
                    s2 = "No Google account found on the phone. For pre-3.0 devices, a Google account is required on the device.";
                }
                else if ("AUTHENTICATION_FAILED".equals(stringExtra)) {
                    s = AppboyGcmReceiver.TAG;
                    s2 = "Unable to authenticate Google account. For Android versions <4.0.4, a valid Google Play account is required for Google Cloud Messaging to function. This phone will be unable to receive Google Cloud Messages until the user logs in with a valid Google Play account or upgrades the operating system on this device.";
                }
                else if ("INVALID_SENDER".equals(stringExtra)) {
                    s = AppboyGcmReceiver.TAG;
                    s2 = "One or multiple of the sender IDs provided are invalid.";
                }
                else if ("PHONE_REGISTRATION_ERROR".equals(stringExtra)) {
                    s = AppboyGcmReceiver.TAG;
                    s2 = "Device does not support GCM.";
                }
                else {
                    if (!"INVALID_PARAMETERS".equals(stringExtra)) {
                        s3 = AppboyGcmReceiver.TAG;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Received an unrecognised GCM registration error type. Ignoring. Error: ");
                        sb.append(stringExtra);
                        string = sb.toString();
                        break Label_0219;
                    }
                    s = AppboyGcmReceiver.TAG;
                    s2 = "The request sent by the device does not contain the expected parameters. This phone does not currently support GCM.";
                }
                Log.e(s, s2);
                return true;
            }
            if (stringExtra2 != null) {
                Appboy.getInstance(context).registerAppboyPushMessages(stringExtra2);
                return true;
            }
            if (!intent.hasExtra("unregistered")) {
                AppboyLogger.w(AppboyGcmReceiver.TAG, "The GCM registration message is missing error information, registration id, and unregistration confirmation. Ignoring.");
                return false;
            }
            s3 = AppboyGcmReceiver.TAG;
            string = "The device was un-registered from GCM.";
        }
        AppboyLogger.w(s3, string);
        return true;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        final String tag = AppboyGcmReceiver.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("Received broadcast message. Message: ");
        sb.append(intent.toString());
        AppboyLogger.i(tag, sb.toString());
        final String action = intent.getAction();
        if ("com.google.android.c2dm.intent.REGISTRATION".equals(action)) {
            this.handleRegistrationEventIfEnabled(new AppboyConfigurationProvider(context), context, intent);
            return;
        }
        if ("com.google.android.c2dm.intent.RECEIVE".equals(action)) {
            this.handleAppboyGcmReceiveIntent(context, intent);
            return;
        }
        if ("com.appboy.action.CANCEL_NOTIFICATION".equals(action)) {
            AppboyNotificationUtils.handleCancelNotificationAction(context, intent);
            return;
        }
        if ("com.appboy.action.APPBOY_ACTION_CLICKED".equals(action)) {
            AppboyNotificationActionUtils.handleNotificationActionClicked(context, intent);
            return;
        }
        if ("com.appboy.action.STORY_TRAVERSE".equals(action)) {
            this.handleAppboyGcmReceiveIntent(context, intent);
            return;
        }
        if ("com.appboy.action.APPBOY_STORY_CLICKED".equals(action)) {
            AppboyNotificationUtils.handlePushStoryPageClicked(context, intent);
            return;
        }
        if ("com.appboy.action.APPBOY_PUSH_CLICKED".equals(action)) {
            AppboyNotificationUtils.handleNotificationOpened(context, intent);
            return;
        }
        if ("com.appboy.action.APPBOY_PUSH_DELETED".equals(action)) {
            AppboyNotificationUtils.handleNotificationDeleted(context, intent);
            return;
        }
        AppboyLogger.w(AppboyGcmReceiver.TAG, "The GCM receiver received a message not sent from Appboy. Ignoring the message.");
    }
    
    public class HandleAppboyGcmMessageTask extends AsyncTask<Void, Void, Void>
    {
        private final Context mContext;
        private final Intent mIntent;
        
        public HandleAppboyGcmMessageTask(final Context mContext, final Intent mIntent) {
            this.mContext = mContext;
            this.mIntent = mIntent;
            this.execute((Object[])new Void[0]);
        }
        
        protected Void doInBackground(final Void... array) {
            try {
                AppboyGcmReceiver.this.handleAppboyGcmMessage(this.mContext, this.mIntent);
            }
            catch (Exception ex) {
                AppboyLogger.e(AppboyGcmReceiver.TAG, "Failed to create and display notification.", ex);
            }
            return null;
        }
    }
}
