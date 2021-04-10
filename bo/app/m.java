package bo.app;

import com.appboy.support.*;
import android.content.*;
import com.appboy.configuration.*;
import org.json.*;
import java.util.*;

public class m
{
    private static final String a;
    private final SharedPreferences b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(m.class);
    }
    
    public m(final Context context) {
        this.b = context.getSharedPreferences("com.appboy.override.configuration.cache", 0);
    }
    
    private static void a(final SharedPreferences$Editor sharedPreferences$Editor, final String s, final Boolean b) {
        if (b == null) {
            return;
        }
        sharedPreferences$Editor.putBoolean(s, (boolean)b);
    }
    
    private static void a(final SharedPreferences$Editor sharedPreferences$Editor, final String s, final Integer n) {
        if (n == null) {
            return;
        }
        sharedPreferences$Editor.putInt(s, (int)n);
    }
    
    private static void a(final SharedPreferences$Editor sharedPreferences$Editor, final String s, final String s2) {
        if (s2 == null) {
            return;
        }
        sharedPreferences$Editor.putString(s, s2);
    }
    
    public int a(final String s, final int n) {
        return this.b.getInt(s, n);
    }
    
    public String a(final String s, final String s2) {
        return this.b.getString(s, s2);
    }
    
    public void a() {
        AppboyLogger.d(m.a, "Clearing Braze Override configuration cache");
        final SharedPreferences$Editor edit = this.b.edit();
        edit.clear();
        edit.apply();
    }
    
    void a(final SharedPreferences$Editor sharedPreferences$Editor, final String s, final Enum enum1) {
        if (enum1 == null) {
            return;
        }
        a(sharedPreferences$Editor, s, enum1.toString());
    }
    
    public void a(final AppboyConfig appboyConfig) {
        final String a = m.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Setting Braze Override configuration with config: ");
        sb.append(appboyConfig);
        AppboyLogger.d(a, sb.toString());
        final SharedPreferences$Editor edit = this.b.edit();
        a(edit, "com_appboy_api_key", appboyConfig.getApiKey());
        a(edit, "com_appboy_server_target", appboyConfig.getServerTarget());
        this.a(edit, "com_appboy_sdk_flavor", appboyConfig.getSdkFlavor());
        a(edit, "com_appboy_enable_fresco_library_use", appboyConfig.getIsFrescoLibraryEnabled());
        a(edit, "com_appboy_newsfeed_unread_visual_indicator_on", appboyConfig.getIsNewsFeedVisualIndicatorOn());
        a(edit, "com_appboy_push_gcm_sender_id", appboyConfig.getGcmSenderId());
        a(edit, "com_appboy_custom_endpoint", appboyConfig.getCustomEndpoint());
        a(edit, "com_appboy_push_small_notification_icon", appboyConfig.getSmallNotificationIcon());
        a(edit, "com_appboy_push_large_notification_icon", appboyConfig.getLargeNotificationIcon());
        a(edit, "com_appboy_session_timeout", appboyConfig.getSessionTimeout());
        a(edit, "com_appboy_location_update_time_interval", appboyConfig.getLocationUpdateTimeIntervalSeconds());
        a(edit, "com_appboy_default_notification_accent_color", appboyConfig.getDefaultNotificationAccentColor());
        a(edit, "com_appboy_trigger_action_minimum_time_interval_seconds", appboyConfig.getTriggerActionMinimumTimeIntervalSeconds());
        a(edit, "com_appboy_push_gcm_messaging_registration_enabled", appboyConfig.getGcmMessagingRegistrationEnabled());
        a(edit, "com_appboy_push_adm_messaging_registration_enabled", appboyConfig.getAdmMessagingRegistrationEnabled());
        a(edit, "com_appboy_handle_push_deep_links_automatically", appboyConfig.getHandlePushDeepLinksAutomatically());
        a(edit, "com_appboy_notifications_enabled_tracking_on", appboyConfig.getNotificationsEnabledTrackingOn());
        a(edit, "com_appboy_disable_location_collection", appboyConfig.getDisableLocationCollection());
        a(edit, "com_appboy_enable_background_location_collection", appboyConfig.getEnableBackgroundLocationCollection());
        a(edit, "com_appboy_location_update_distance", appboyConfig.getLocationUpdateDistance());
        a(edit, "com_appboy_data_flush_interval_bad_network", appboyConfig.getBadNetworkDataFlushInterval());
        a(edit, "com_appboy_data_flush_interval_good_network", appboyConfig.getGoodNetworkDataFlushInterval());
        a(edit, "com_appboy_data_flush_interval_great_network", appboyConfig.getGreatNetworkDataFlushInterval());
        a(edit, "com_appboy_default_notification_channel_name", appboyConfig.getDefaultNotificationChannelName());
        a(edit, "com_appboy_default_notification_channel_description", appboyConfig.getDefaultNotificationChannelDescription());
        a(edit, "com_appboy_push_deep_link_back_stack_activity_enabled", appboyConfig.getPushDeepLinkBackStackActivityEnabled());
        a(edit, "com_appboy_push_deep_link_back_stack_activity_class_name", appboyConfig.getPushDeepLinkBackStackActivityClassName());
        a(edit, "com_appboy_session_start_based_timeout_enabled", appboyConfig.getIsSessionStartBasedTimeoutEnabled());
        a(edit, "com_appboy_firebase_cloud_messaging_registration_enabled", appboyConfig.getIsFirebaseCloudMessagingRegistrationEnabled());
        a(edit, "com_appboy_firebase_cloud_messaging_sender_id", appboyConfig.getFirebaseCloudMessagingSenderIdKey());
        a(edit, "com_appboy_content_cards_unread_visual_indicator_enabled", appboyConfig.getContentCardsUnreadVisualIndicatorEnabled());
        if (appboyConfig.getLocaleToApiMapping() != null) {
            a(edit, "com_appboy_locale_api_key_map", new JSONArray((Collection)appboyConfig.getLocaleToApiMapping()).toString());
        }
        edit.apply();
    }
    
    public boolean a(final String s) {
        return this.b.contains(s);
    }
    
    public boolean a(final String s, final boolean b) {
        return this.b.getBoolean(s, b);
    }
}
