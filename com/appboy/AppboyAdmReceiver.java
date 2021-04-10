package com.appboy;

import com.appboy.support.*;
import android.content.*;
import com.appboy.configuration.*;
import android.app.*;
import com.appboy.push.*;
import android.os.*;

public final class AppboyAdmReceiver extends BroadcastReceiver
{
    private static final String ADM_DELETED_MESSAGES_KEY = "deleted_messages";
    private static final String ADM_ERROR_KEY = "error";
    private static final String ADM_MESSAGE_TYPE_KEY = "message_type";
    private static final String ADM_NUMBER_OF_MESSAGES_DELETED_KEY = "total_deleted";
    private static final String ADM_RECEIVE_INTENT_ACTION = "com.amazon.device.messaging.intent.RECEIVE";
    private static final String ADM_REGISTRATION_ID_KEY = "registration_id";
    private static final String ADM_REGISTRATION_INTENT_ACTION = "com.amazon.device.messaging.intent.REGISTRATION";
    private static final String ADM_UNREGISTERED_KEY = "unregistered";
    public static final String CAMPAIGN_ID_KEY = "cid";
    private static final String TAG;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyAdmReceiver.class);
    }
    
    boolean handleAppboyAdmMessage(final Context context, final Intent intent) {
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
        if ("deleted_messages".equals(intent.getStringExtra("message_type"))) {
            final int intExtra = intent.getIntExtra("total_deleted", -1);
            if (intExtra == -1) {
                final String tag = AppboyAdmReceiver.TAG;
                final StringBuilder sb = new StringBuilder();
                sb.append("Unable to parse ADM message. Intent: ");
                sb.append(intent.toString());
                AppboyLogger.e(tag, sb.toString());
                return false;
            }
            final String tag2 = AppboyAdmReceiver.TAG;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("ADM deleted ");
            sb2.append(intExtra);
            sb2.append(" messages. Fetch them from Appboy.");
            AppboyLogger.i(tag2, sb2.toString());
            return false;
        }
        else {
            final Bundle extras = intent.getExtras();
            final String tag3 = AppboyAdmReceiver.TAG;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Push message payload received: ");
            sb3.append(extras);
            AppboyLogger.d(tag3, sb3.toString());
            if (!extras.containsKey("appboy_push_received_timestamp")) {
                extras.putLong("appboy_push_received_timestamp", System.currentTimeMillis());
            }
            AppboyNotificationUtils.logPushDeliveryEvent(context, extras);
            final Bundle appboyExtrasWithoutPreprocessing = AppboyNotificationUtils.getAppboyExtrasWithoutPreprocessing(extras);
            extras.putBundle("extra", appboyExtrasWithoutPreprocessing);
            if (!AppboyNotificationUtils.isNotificationMessage(intent)) {
                AppboyNotificationUtils.sendPushMessageReceivedBroadcast(context, extras);
                AppboyNotificationUtils.requestGeofenceRefreshIfAppropriate(context, extras);
                return false;
            }
            final int notificationId = AppboyNotificationUtils.getNotificationId(extras);
            extras.putInt("nid", notificationId);
            final Notification notification = AppboyNotificationUtils.getActiveNotificationFactory().createNotification(new AppboyConfigurationProvider(context), context, extras, appboyExtrasWithoutPreprocessing);
            if (notification == null) {
                AppboyLogger.d(AppboyAdmReceiver.TAG, "Notification created by notification factory was null. Not displaying notification.");
                return false;
            }
            notificationManager.notify("appboy_notification", notificationId, notification);
            AppboyNotificationUtils.sendPushMessageReceivedBroadcast(context, extras);
            AppboyNotificationUtils.wakeScreenIfHasPermission(context, extras);
            if (extras.containsKey("nd")) {
                AppboyNotificationUtils.setNotificationDurationAlarm(context, this.getClass(), notificationId, Integer.parseInt(extras.getString("nd")));
            }
            return true;
        }
    }
    
    void handleAppboyAdmReceiveIntent(final Context context, final Intent intent) {
        if (AppboyNotificationUtils.isAppboyPushMessage(intent)) {
            new HandleAppboyAdmMessageTask(context, intent);
        }
    }
    
    boolean handleRegistrationEventIfEnabled(final AppboyConfigurationProvider appboyConfigurationProvider, final Context context, final Intent intent) {
        final String tag = AppboyAdmReceiver.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("Received ADM registration. Message: ");
        sb.append(intent.toString());
        AppboyLogger.i(tag, sb.toString());
        if (appboyConfigurationProvider.isAdmMessagingRegistrationEnabled()) {
            AppboyLogger.d(AppboyAdmReceiver.TAG, "ADM enabled in appboy.xml. Continuing to process ADM registration intent.");
            this.handleRegistrationIntent(context, intent);
            return true;
        }
        AppboyLogger.w(AppboyAdmReceiver.TAG, "ADM not enabled in appboy.xml. Ignoring ADM registration intent. Note: you must set com_appboy_push_adm_messaging_registration_enabled to true in your appboy.xml to enable ADM.");
        return false;
    }
    
    boolean handleRegistrationIntent(final Context context, final Intent intent) {
        final String stringExtra = intent.getStringExtra("error");
        final String stringExtra2 = intent.getStringExtra("registration_id");
        final String stringExtra3 = intent.getStringExtra("unregistered");
        if (stringExtra != null) {
            final String tag = AppboyAdmReceiver.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Error during ADM registration: ");
            sb.append(stringExtra);
            AppboyLogger.e(tag, sb.toString());
        }
        else if (stringExtra2 != null) {
            final String tag2 = AppboyAdmReceiver.TAG;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Registering for ADM messages with registrationId: ");
            sb2.append(stringExtra2);
            AppboyLogger.i(tag2, sb2.toString());
            Appboy.getInstance(context).registerAppboyPushMessages(stringExtra2);
        }
        else {
            if (stringExtra3 == null) {
                AppboyLogger.w(AppboyAdmReceiver.TAG, "The ADM registration intent is missing error information, registration id, and unregistration confirmation. Ignoring.");
                return false;
            }
            final String tag3 = AppboyAdmReceiver.TAG;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("The device was un-registered from ADM: ");
            sb3.append(stringExtra3);
            AppboyLogger.w(tag3, sb3.toString());
        }
        return true;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        final String tag = AppboyAdmReceiver.TAG;
        final StringBuilder sb = new StringBuilder();
        sb.append("Received broadcast message. Message: ");
        sb.append(intent.toString());
        AppboyLogger.i(tag, sb.toString());
        final String action = intent.getAction();
        if ("com.amazon.device.messaging.intent.REGISTRATION".equals(action)) {
            this.handleRegistrationEventIfEnabled(new AppboyConfigurationProvider(context), context, intent);
            return;
        }
        if ("com.amazon.device.messaging.intent.RECEIVE".equals(action)) {
            this.handleAppboyAdmReceiveIntent(context, intent);
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
        if ("com.appboy.action.APPBOY_PUSH_CLICKED".equals(action)) {
            AppboyNotificationUtils.handleNotificationOpened(context, intent);
            return;
        }
        if ("com.appboy.action.APPBOY_PUSH_DELETED".equals(action)) {
            AppboyNotificationUtils.handleNotificationDeleted(context, intent);
            return;
        }
        AppboyLogger.w(AppboyAdmReceiver.TAG, "The ADM receiver received a message not sent from Appboy. Ignoring the message.");
    }
    
    public class HandleAppboyAdmMessageTask extends AsyncTask<Void, Void, Void>
    {
        private final Context mContext;
        private final Intent mIntent;
        
        public HandleAppboyAdmMessageTask(final Context mContext, final Intent mIntent) {
            this.mContext = mContext;
            this.mIntent = mIntent;
            this.execute((Object[])new Void[0]);
        }
        
        protected Void doInBackground(final Void... array) {
            try {
                AppboyAdmReceiver.this.handleAppboyAdmMessage(this.mContext, this.mIntent);
            }
            catch (Exception ex) {
                AppboyLogger.e(AppboyAdmReceiver.TAG, "Failed to create and display notification.", ex);
            }
            return null;
        }
    }
}
