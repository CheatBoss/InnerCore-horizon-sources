package com.appboy.configuration;

import android.content.*;
import bo.app.*;
import java.util.*;
import org.json.*;
import com.appboy.support.*;
import android.content.pm.*;
import com.appboy.enums.*;

public class AppboyConfigurationProvider extends CachedConfigurationProvider
{
    private static final String a;
    private final Context b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(AppboyConfigurationProvider.class);
    }
    
    public AppboyConfigurationProvider(final Context b) {
        super(b);
        this.b = b;
    }
    
    private int a(final a a) {
        String s;
        if (a.equals(AppboyConfigurationProvider.a.b)) {
            s = "com_appboy_push_large_notification_icon";
        }
        else {
            s = "com_appboy_push_small_notification_icon";
        }
        if (this.mConfigurationCache.containsKey(s)) {
            return (int)this.mConfigurationCache.get(s);
        }
        if (this.mRuntimeAppConfigurationProvider.a(s)) {
            final String a2 = this.mRuntimeAppConfigurationProvider.a(s, "");
            final int identifier = this.b.getResources().getIdentifier(a2, "drawable", PackageUtils.getResourcePackageName(this.b));
            this.mConfigurationCache.put(s, identifier);
            final String a3 = AppboyConfigurationProvider.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Using runtime override value for key: ");
            sb.append(s);
            sb.append(" and value: ");
            sb.append(a2);
            AppboyLogger.d(a3, sb.toString());
            return identifier;
        }
        final int identifier2 = this.b.getResources().getIdentifier(s, "drawable", PackageUtils.getResourcePackageName(this.b));
        this.mConfigurationCache.put(s, identifier2);
        return identifier2;
    }
    
    private String a() {
        return this.getStringValue("com_appboy_server_target", "PROD");
    }
    
    public cf getAppboyApiKey() {
        cf cf2;
        final cf cf = cf2 = this.mConfigurationCache.get("com_appboy_api_key");
        if (cf == null) {
            String s = this.mRuntimeAppConfigurationProvider.a("com_appboy_api_key", null);
            Label_0085: {
                String s2;
                String s3;
                if (s != null) {
                    s2 = AppboyConfigurationProvider.a;
                    s3 = "Found an override api key. Using it to configure Appboy.";
                }
                else {
                    s = this.getAppboyApiKeyStringFromLocaleMapping(Locale.getDefault());
                    if (s == null) {
                        s = this.readStringResourceValue("com_appboy_api_key", null);
                        break Label_0085;
                    }
                    s2 = AppboyConfigurationProvider.a;
                    s3 = "Found a locale that matches the current locale in appboy.xml. Using the associated api key";
                }
                AppboyLogger.i(s2, s3);
            }
            cf2 = cf;
            if (s != null) {
                cf2 = new cf(s);
                this.mConfigurationCache.put("com_appboy_api_key", cf2);
            }
        }
        if (cf2 != null) {
            return cf2;
        }
        AppboyLogger.e(AppboyConfigurationProvider.a, "****************************************************");
        AppboyLogger.e(AppboyConfigurationProvider.a, "**                                                **");
        AppboyLogger.e(AppboyConfigurationProvider.a, "**                 !! WARNING !!                  **");
        AppboyLogger.e(AppboyConfigurationProvider.a, "**                                                **");
        AppboyLogger.e(AppboyConfigurationProvider.a, "**     No API key set in res/values/appboy.xml    **");
        AppboyLogger.e(AppboyConfigurationProvider.a, "** No cached API Key found from Appboy.configure  **");
        AppboyLogger.e(AppboyConfigurationProvider.a, "**          Braze functionality disabled          **");
        AppboyLogger.e(AppboyConfigurationProvider.a, "**                                                **");
        AppboyLogger.e(AppboyConfigurationProvider.a, "****************************************************");
        throw new RuntimeException("Unable to read the Braze API key from the res/values/appboy.xml file. See log for more details.");
    }
    
    public String getAppboyApiKeyStringFromLocaleMapping(final Locale locale) {
        String s;
        String s2;
        if (locale == null) {
            s = AppboyConfigurationProvider.a;
            s2 = "Passed in a null locale to match.";
        }
        else {
            String[] stringArrayResourceValue = null;
            Label_0139: {
                if (this.mRuntimeAppConfigurationProvider.a("com_appboy_locale_api_key_map")) {
                    final String a = this.mRuntimeAppConfigurationProvider.a("com_appboy_locale_api_key_map", null);
                    Throwable t = null;
                    try {
                        final JSONArray jsonArray = new JSONArray(a);
                        final String[] array = new String[jsonArray.length()];
                        int n = 0;
                        while (true) {
                            stringArrayResourceValue = array;
                            try {
                                if (n < jsonArray.length()) {
                                    array[n] = jsonArray.getString(n);
                                    ++n;
                                    continue;
                                }
                                break Label_0139;
                            }
                            catch (JSONException ex) {
                                stringArrayResourceValue = array;
                                t = (Throwable)ex;
                            }
                        }
                    }
                    catch (JSONException t) {
                        stringArrayResourceValue = null;
                    }
                    AppboyLogger.e(AppboyConfigurationProvider.a, "Caught exception creating locale to api key mapping from override cache", t);
                }
                else {
                    stringArrayResourceValue = this.readStringArrayResourceValue("com_appboy_locale_api_key_map", null);
                }
            }
            if (stringArrayResourceValue != null) {
                for (int length = stringArrayResourceValue.length, i = 0; i < length; ++i) {
                    final String s3 = stringArrayResourceValue[i];
                    if (StringUtils.countOccurrences(s3, ",") == 1) {
                        final String[] split = s3.split(",");
                        if (split.length == 2) {
                            final String lowerCase = split[0].trim().toLowerCase(Locale.US);
                            final boolean equals = lowerCase.equals(locale.toString().toLowerCase(Locale.US));
                            if (lowerCase.equals(locale.getCountry().toLowerCase(Locale.US)) || equals) {
                                return split[1].trim();
                            }
                        }
                    }
                }
                return null;
            }
            s = AppboyConfigurationProvider.a;
            s2 = "Locale to api key mappings not present in XML.";
        }
        AppboyLogger.d(s, s2);
        return null;
    }
    
    public int getApplicationIconResourceId() {
        if (this.mConfigurationCache.containsKey("application_icon")) {
            return this.mConfigurationCache.get("application_icon");
        }
        final String packageName = this.b.getPackageName();
        int n;
        try {
            n = this.b.getPackageManager().getApplicationInfo(packageName, 0).icon;
        }
        catch (PackageManager$NameNotFoundException ex) {
            final String a = AppboyConfigurationProvider.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot find package named ");
            sb.append(packageName);
            AppboyLogger.e(a, sb.toString());
            try {
                n = this.b.getPackageManager().getApplicationInfo(this.b.getPackageName(), 0).icon;
            }
            catch (PackageManager$NameNotFoundException ex2) {
                final String a2 = AppboyConfigurationProvider.a;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Cannot find package named ");
                sb2.append(packageName);
                AppboyLogger.e(a2, sb2.toString());
                n = 0;
            }
        }
        this.mConfigurationCache.put("application_icon", n);
        return n;
    }
    
    public String getBaseUrlForRequests() {
        if ("STAGING".equals(this.a().toUpperCase(Locale.US))) {
            return "https://sondheim.appboy.com/api/v3/";
        }
        return "https://dev.appboy.com/api/v3/";
    }
    
    public String getCustomEndpoint() {
        return this.getStringValue("com_appboy_custom_endpoint", null);
    }
    
    public int getDefaultNotificationAccentColor() {
        return this.getIntValue("com_appboy_default_notification_accent_color", 0);
    }
    
    public String getDefaultNotificationChannelDescription() {
        return this.getStringValue("com_appboy_default_notification_channel_description", "");
    }
    
    public String getDefaultNotificationChannelName() {
        return this.getStringValue("com_appboy_default_notification_channel_name", "General");
    }
    
    public String getFirebaseCloudMessagingSenderIdKey() {
        return this.getStringValue("com_appboy_firebase_cloud_messaging_sender_id", null);
    }
    
    public String getGcmSenderId() {
        return this.getStringValue("com_appboy_push_gcm_sender_id", null);
    }
    
    public boolean getHandlePushDeepLinksAutomatically() {
        return this.getBooleanValue("com_appboy_handle_push_deep_links_automatically", false);
    }
    
    public boolean getIsFrescoLibraryUseEnabled() {
        return this.getBooleanValue("com_appboy_enable_fresco_library_use", false);
    }
    
    public boolean getIsNewsfeedVisualIndicatorOn() {
        return this.getBooleanValue("com_appboy_newsfeed_unread_visual_indicator_on", true);
    }
    
    @Deprecated
    public boolean getIsNotificationsEnabledTrackingOn() {
        return this.getBooleanValue("com_appboy_notifications_enabled_tracking_on", false);
    }
    
    public boolean getIsPushDeepLinkBackStackActivityEnabled() {
        return this.getBooleanValue("com_appboy_push_deep_link_back_stack_activity_enabled", true);
    }
    
    public boolean getIsSessionStartBasedTimeoutEnabled() {
        return this.getBooleanValue("com_appboy_session_start_based_timeout_enabled", false);
    }
    
    public int getLargeNotificationIconResourceId() {
        return this.a(AppboyConfigurationProvider.a.b);
    }
    
    public float getLocationUpdateDistanceInMeters() {
        return (float)this.getIntValue("com_appboy_location_update_distance", -1);
    }
    
    public long getLocationUpdateTimeIntervalInMillis() {
        return this.getIntValue("com_appboy_location_update_time_interval", -1) * 1000L;
    }
    
    public String getPushDeepLinkBackStackActivityClassName() {
        return this.getStringValue("com_appboy_push_deep_link_back_stack_activity_class_name", "");
    }
    
    public SdkFlavor getSdkFlavor() {
        final String stringValue = this.getStringValue("com_appboy_sdk_flavor", null);
        if (StringUtils.isNullOrBlank(stringValue)) {
            return null;
        }
        try {
            return SdkFlavor.valueOf(stringValue.toUpperCase(Locale.US));
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyConfigurationProvider.a, "Exception while parsing stored SDK flavor. Returning null.", ex);
            return null;
        }
    }
    
    public int getSessionTimeoutSeconds() {
        return this.getIntValue("com_appboy_session_timeout", 10);
    }
    
    public int getSmallNotificationIconResourceId() {
        return this.a(AppboyConfigurationProvider.a.a);
    }
    
    public long getTriggerActionMinimumTimeIntervalInSeconds() {
        return this.getIntValue("com_appboy_trigger_action_minimum_time_interval_seconds", 30);
    }
    
    public int getVersionCode() {
        if (this.mConfigurationCache.containsKey("version_code")) {
            return this.mConfigurationCache.get("version_code");
        }
        int versionCode;
        try {
            versionCode = this.b.getPackageManager().getPackageInfo(PackageUtils.getResourcePackageName(this.b), 0).versionCode;
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyConfigurationProvider.a, "Unable to read the version code.", ex);
            versionCode = -1;
        }
        this.mConfigurationCache.put("version_code", versionCode);
        return versionCode;
    }
    
    public boolean isAdmMessagingRegistrationEnabled() {
        return this.getBooleanValue("com_appboy_push_adm_messaging_registration_enabled", false);
    }
    
    public boolean isBackgroundLocationCollectionEnabled() {
        return this.getBooleanValue("com_appboy_enable_background_location_collection", false);
    }
    
    public boolean isContentCardsUnreadVisualIndicatorEnabled() {
        return this.getBooleanValue("com_appboy_content_cards_unread_visual_indicator_enabled", true);
    }
    
    public boolean isFirebaseCloudMessagingRegistrationEnabled() {
        return this.getBooleanValue("com_appboy_firebase_cloud_messaging_registration_enabled", false);
    }
    
    public boolean isGcmMessagingRegistrationEnabled() {
        return this.getBooleanValue("com_appboy_push_gcm_messaging_registration_enabled", false);
    }
    
    public boolean isLocationCollectionEnabled() {
        return this.getBooleanValue("com_appboy_disable_location_collection", false) ^ true;
    }
    
    enum a
    {
        a, 
        b;
    }
}
