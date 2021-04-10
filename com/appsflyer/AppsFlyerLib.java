package com.appsflyer;

import java.net.*;
import android.app.*;
import java.util.*;
import android.content.*;

public abstract class AppsFlyerLib
{
    public static AppsFlyerLib getInstance() {
        return AppsFlyerLibCore.getInstance();
    }
    
    public abstract void enableFacebookDeferredApplinks(final boolean p0);
    
    public abstract AppsFlyerLib enableLocationCollection(final boolean p0);
    
    public abstract String getAppsFlyerUID(final Context p0);
    
    public abstract String getAttributionId(final Context p0);
    
    public abstract String getHostName();
    
    public abstract String getHostPrefix();
    
    public abstract String getOutOfStore(final Context p0);
    
    public abstract String getSdkVersion();
    
    @Deprecated
    public abstract AppsFlyerLib init(final String p0, final AppsFlyerConversionListener p1);
    
    public abstract AppsFlyerLib init(final String p0, final AppsFlyerConversionListener p1, final Context p2);
    
    public abstract boolean isPreInstalledApp(final Context p0);
    
    public abstract boolean isTrackingStopped();
    
    public abstract void onPause(final Context p0);
    
    public abstract void performOnAppAttribution(final Context p0, final URI p1);
    
    public abstract void registerConversionListener(final Context p0, final AppsFlyerConversionListener p1);
    
    public abstract void registerValidatorListener(final Context p0, final AppsFlyerInAppPurchaseValidatorListener p1);
    
    public abstract void reportTrackSession(final Context p0);
    
    public abstract void sendAdRevenue(final Context p0, final Map<String, Object> p1);
    
    @Deprecated
    public abstract void sendDeepLinkData(final Activity p0);
    
    public abstract void sendPushNotificationData(final Activity p0);
    
    public abstract void setAdditionalData(final HashMap<String, Object> p0);
    
    public abstract void setAndroidIdData(final String p0);
    
    public abstract void setAppId(final String p0);
    
    public abstract void setAppInviteOneLink(final String p0);
    
    public abstract void setCollectAndroidID(final boolean p0);
    
    public abstract void setCollectIMEI(final boolean p0);
    
    public abstract void setCollectOaid(final boolean p0);
    
    public abstract void setConsumeAFDeepLinks(final boolean p0);
    
    public abstract void setCurrencyCode(final String p0);
    
    public abstract void setCustomerIdAndTrack(final String p0, final Context p1);
    
    public abstract void setCustomerUserId(final String p0);
    
    public abstract void setDebugLog(final boolean p0);
    
    public abstract void setDeviceTrackingDisabled(final boolean p0);
    
    public abstract void setExtension(final String p0);
    
    public abstract void setHost(final String p0, final String p1);
    
    @Deprecated
    public abstract void setHostName(final String p0);
    
    public abstract void setImeiData(final String p0);
    
    public abstract void setIsUpdate(final boolean p0);
    
    public abstract void setLogLevel(final AFLogger.LogLevel p0);
    
    public abstract void setMinTimeBetweenSessions(final int p0);
    
    public abstract void setOaidData(final String p0);
    
    public abstract void setOneLinkCustomDomain(final String... p0);
    
    public abstract void setOutOfStore(final String p0);
    
    public abstract void setPhoneNumber(final String p0);
    
    public abstract void setPluginDeepLinkData(final Intent p0);
    
    public abstract void setPreinstallAttribution(final String p0, final String p1, final String p2);
    
    public abstract void setResolveDeepLinkURLs(final String... p0);
    
    public abstract void setSharingFilter(final String... p0);
    
    public abstract void setSharingFilterForAllPartners();
    
    public abstract void setUserEmails(final AppsFlyerProperties.EmailsCryptType p0, final String... p1);
    
    public abstract void setUserEmails(final String... p0);
    
    public abstract void startTracking(final Context p0);
    
    public abstract void startTracking(final Context p0, final String p1);
    
    public abstract void startTracking(final Context p0, final String p1, final AppsFlyerTrackingRequestListener p2);
    
    public abstract void stopTracking(final boolean p0, final Context p1);
    
    @Deprecated
    public abstract void trackAppLaunch(final Context p0, final String p1);
    
    public abstract void trackEvent(final Context p0, final String p1, final Map<String, Object> p2);
    
    public abstract void trackEvent(final Context p0, final String p1, final Map<String, Object> p2, final AppsFlyerTrackingRequestListener p3);
    
    public abstract void trackLocation(final Context p0, final double p1, final double p2);
    
    public abstract void unregisterConversionListener();
    
    public abstract void updateServerUninstallToken(final Context p0, final String p1);
    
    public abstract void validateAndTrackInAppPurchase(final Context p0, final String p1, final String p2, final String p3, final String p4, final String p5, final Map<String, String> p6);
    
    public abstract void waitForCustomerUserId(final boolean p0);
}
