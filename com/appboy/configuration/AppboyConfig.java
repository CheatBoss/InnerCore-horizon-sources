package com.appboy.configuration;

import java.util.*;
import com.appboy.enums.*;
import com.appboy.support.*;

public class AppboyConfig
{
    private static final String a;
    private final Boolean A;
    private final Boolean B;
    private final Boolean C;
    private final Boolean D;
    private final Boolean E;
    private final Boolean F;
    private final List<String> G;
    private final String b;
    private final String c;
    private final String d;
    private final String e;
    private final String f;
    private final String g;
    private final String h;
    private final String i;
    private final String j;
    private final String k;
    private final SdkFlavor l;
    private final Integer m;
    private final Integer n;
    private final Integer o;
    private final Integer p;
    private final Integer q;
    private final Integer r;
    private final Integer s;
    private final Integer t;
    private final Boolean u;
    private final Boolean v;
    private final Boolean w;
    private final Boolean x;
    private final Boolean y;
    private final Boolean z;
    
    static {
        a = AppboyLogger.getAppboyLogTag(AppboyConfig.class);
    }
    
    private AppboyConfig(final Builder builder) {
        this.b = builder.a;
        this.u = builder.t;
        this.v = builder.u;
        this.c = builder.b;
        this.e = builder.d;
        this.f = builder.e;
        this.g = builder.f;
        this.m = builder.l;
        this.G = builder.F;
        this.y = builder.x;
        this.z = builder.y;
        this.n = builder.m;
        this.q = builder.p;
        this.o = builder.n;
        this.p = builder.o;
        this.w = builder.v;
        this.x = builder.w;
        this.B = builder.A;
        this.A = builder.z;
        this.r = builder.q;
        this.s = builder.r;
        this.t = builder.s;
        this.d = builder.c;
        this.l = builder.k;
        this.h = builder.g;
        this.i = builder.h;
        this.C = builder.B;
        this.j = builder.i;
        this.D = builder.C;
        this.k = builder.j;
        this.E = builder.D;
        this.F = builder.E;
    }
    
    public Boolean getAdmMessagingRegistrationEnabled() {
        return this.v;
    }
    
    public String getApiKey() {
        return this.b;
    }
    
    public Integer getBadNetworkDataFlushInterval() {
        return this.r;
    }
    
    public Boolean getContentCardsUnreadVisualIndicatorEnabled() {
        return this.F;
    }
    
    public String getCustomEndpoint() {
        return this.g;
    }
    
    public Integer getDefaultNotificationAccentColor() {
        return this.o;
    }
    
    public String getDefaultNotificationChannelDescription() {
        return this.i;
    }
    
    public String getDefaultNotificationChannelName() {
        return this.h;
    }
    
    public Boolean getDisableLocationCollection() {
        return this.y;
    }
    
    public Boolean getEnableBackgroundLocationCollection() {
        return this.z;
    }
    
    public String getFirebaseCloudMessagingSenderIdKey() {
        return this.k;
    }
    
    public Boolean getGcmMessagingRegistrationEnabled() {
        return this.u;
    }
    
    public String getGcmSenderId() {
        return this.c;
    }
    
    public Integer getGoodNetworkDataFlushInterval() {
        return this.s;
    }
    
    public Integer getGreatNetworkDataFlushInterval() {
        return this.t;
    }
    
    public Boolean getHandlePushDeepLinksAutomatically() {
        return this.w;
    }
    
    public Boolean getIsFirebaseCloudMessagingRegistrationEnabled() {
        return this.E;
    }
    
    public Boolean getIsFrescoLibraryEnabled() {
        return this.B;
    }
    
    public Boolean getIsNewsFeedVisualIndicatorOn() {
        return this.A;
    }
    
    public Boolean getIsSessionStartBasedTimeoutEnabled() {
        return this.D;
    }
    
    public String getLargeNotificationIcon() {
        return this.f;
    }
    
    public List<String> getLocaleToApiMapping() {
        return this.G;
    }
    
    public Integer getLocationUpdateDistance() {
        return this.q;
    }
    
    public Integer getLocationUpdateTimeIntervalSeconds() {
        return this.n;
    }
    
    @Deprecated
    public Boolean getNotificationsEnabledTrackingOn() {
        return this.x;
    }
    
    public String getPushDeepLinkBackStackActivityClassName() {
        return this.j;
    }
    
    public Boolean getPushDeepLinkBackStackActivityEnabled() {
        return this.C;
    }
    
    public SdkFlavor getSdkFlavor() {
        return this.l;
    }
    
    public String getServerTarget() {
        return this.d;
    }
    
    public Integer getSessionTimeout() {
        return this.m;
    }
    
    public String getSmallNotificationIcon() {
        return this.e;
    }
    
    public Integer getTriggerActionMinimumTimeIntervalSeconds() {
        return this.p;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AppboyConfig{ApiKey = '");
        sb.append(this.b);
        sb.append('\'');
        sb.append("\nGcmSenderId = '");
        sb.append(this.c);
        sb.append('\'');
        sb.append("\nServerTarget = '");
        sb.append(this.d);
        sb.append('\'');
        sb.append("\nSdkFlavor = '");
        sb.append(this.l);
        sb.append('\'');
        sb.append("\nSmallNotificationIcon = '");
        sb.append(this.e);
        sb.append('\'');
        sb.append("\nLargeNotificationIcon = '");
        sb.append(this.f);
        sb.append('\'');
        sb.append("\nSessionTimeout = ");
        sb.append(this.m);
        sb.append("\nLocationUpdateTimeIntervalSeconds = ");
        sb.append(this.n);
        sb.append("\nDefaultNotificationAccentColor = ");
        sb.append(this.o);
        sb.append("\nTriggerActionMinimumTimeIntervalSeconds = ");
        sb.append(this.p);
        sb.append("\nLocationUpdateDistance = ");
        sb.append(this.q);
        sb.append("\nBadNetworkInterval = ");
        sb.append(this.r);
        sb.append("\nGoodNetworkInterval = ");
        sb.append(this.s);
        sb.append("\nGreatNetworkInterval = ");
        sb.append(this.t);
        sb.append("\nGcmMessagingRegistrationEnabled = ");
        sb.append(this.u);
        sb.append("\nAdmMessagingRegistrationEnabled = ");
        sb.append(this.v);
        sb.append("\nHandlePushDeepLinksAutomatically = ");
        sb.append(this.w);
        sb.append("\nNotificationsEnabledTrackingOn = ");
        sb.append(this.x);
        sb.append("\nDisableLocationCollection = ");
        sb.append(this.y);
        sb.append("\nEnableBackgroundLocationCollection = ");
        sb.append(this.z);
        sb.append("\nIsNewsFeedVisualIndicatorOn = ");
        sb.append(this.A);
        sb.append("\nIsFrescoLibraryEnabled = ");
        sb.append(this.B);
        sb.append("\nLocaleToApiMapping = ");
        sb.append(this.G);
        sb.append("\nSessionStartBasedTimeoutEnabled = ");
        sb.append(this.D);
        sb.append("\nIsFirebaseCloudMessagingRegistrationEnabled = ");
        sb.append(this.E);
        sb.append("\nFirebaseCloudMessagingSenderIdKey = '");
        sb.append(this.k);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
    
    public static class Builder
    {
        private Boolean A;
        private Boolean B;
        private Boolean C;
        private Boolean D;
        private Boolean E;
        private List<String> F;
        private String a;
        private String b;
        private String c;
        private String d;
        private String e;
        private String f;
        private String g;
        private String h;
        private String i;
        private String j;
        private SdkFlavor k;
        private Integer l;
        private Integer m;
        private Integer n;
        private Integer o;
        private Integer p;
        private Integer q;
        private Integer r;
        private Integer s;
        private Boolean t;
        private Boolean u;
        private Boolean v;
        private Boolean w;
        private Boolean x;
        private Boolean y;
        private Boolean z;
        
        public AppboyConfig build() {
            return new AppboyConfig(this, null);
        }
        
        public Builder setAdmMessagingRegistrationEnabled(final boolean b) {
            this.u = b;
            return this;
        }
        
        public Builder setApiKey(final String a) {
            if (!StringUtils.isNullOrBlank(a)) {
                this.a = a;
                return this;
            }
            AppboyLogger.e(AppboyConfig.a, "Cannot set Braze API key to null or blank string. API key field not set");
            return this;
        }
        
        public Builder setBadNetworkDataFlushInterval(final int n) {
            this.q = n;
            return this;
        }
        
        public Builder setContentCardsUnreadVisualIndicatorEnabled(final boolean b) {
            this.E = b;
            return this;
        }
        
        public Builder setCustomEndpoint(final String f) {
            this.f = f;
            return this;
        }
        
        public Builder setDefaultNotificationAccentColor(final int n) {
            this.n = n;
            return this;
        }
        
        public Builder setDefaultNotificationChannelDescription(final String h) {
            if (!StringUtils.isNullOrBlank(h)) {
                this.h = h;
                return this;
            }
            AppboyLogger.e(AppboyConfig.a, "Cannot set Braze default NotificationChannel description to null or blank string. NotificationChannel description field not set.");
            return this;
        }
        
        public Builder setDefaultNotificationChannelName(final String g) {
            if (!StringUtils.isNullOrBlank(g)) {
                this.g = g;
                return this;
            }
            AppboyLogger.e(AppboyConfig.a, "Cannot set Braze default NotificationChannel name to null or blank string. NotificationChannel name field not set.");
            return this;
        }
        
        public Builder setDisableLocationCollection(final boolean b) {
            this.x = b;
            return this;
        }
        
        public Builder setEnableBackgroundLocationCollection(final boolean b) {
            this.y = b;
            return this;
        }
        
        public Builder setFirebaseCloudMessagingSenderIdKey(final String j) {
            if (!StringUtils.isNullOrEmpty(j)) {
                this.j = j;
                return this;
            }
            AppboyLogger.e(AppboyConfig.a, "Cannot set Firebase Cloud Messaging Sender Id to null or empty string. Firebase Cloud Messaging Sender Id field not set");
            return this;
        }
        
        public Builder setFrescoLibraryEnabled(final boolean b) {
            this.A = b;
            return this;
        }
        
        public Builder setGcmMessagingRegistrationEnabled(final boolean b) {
            this.t = b;
            return this;
        }
        
        public Builder setGcmSenderId(final String b) {
            if (!StringUtils.isNullOrEmpty(b)) {
                this.b = b;
                return this;
            }
            AppboyLogger.e(AppboyConfig.a, "Cannot set GCM Sender Id to null or empty string. GCM Sender Id field not set");
            return this;
        }
        
        public Builder setGoodNetworkDataFlushInterval(final int n) {
            this.r = n;
            return this;
        }
        
        public Builder setGreatNetworkDataFlushInterval(final int n) {
            this.s = n;
            return this;
        }
        
        public Builder setHandlePushDeepLinksAutomatically(final boolean b) {
            this.v = b;
            return this;
        }
        
        public Builder setIsFirebaseCloudMessagingRegistrationEnabled(final boolean b) {
            this.D = b;
            return this;
        }
        
        public Builder setIsSessionStartBasedTimeoutEnabled(final boolean b) {
            this.C = b;
            return this;
        }
        
        public Builder setLargeNotificationIcon(final String e) {
            this.e = e;
            return this;
        }
        
        public Builder setLocaleToApiMapping(final List<String> f) {
            if (!f.isEmpty()) {
                this.F = f;
                return this;
            }
            AppboyLogger.e(AppboyConfig.a, "Cannot set locale to API key mapping to empty list. Locale mapping not set.");
            return this;
        }
        
        public Builder setLocationUpdateDistance(final int n) {
            this.p = n;
            return this;
        }
        
        public Builder setLocationUpdateTimeIntervalSeconds(final int n) {
            this.m = n;
            return this;
        }
        
        public Builder setNewsfeedVisualIndicatorOn(final boolean b) {
            this.z = b;
            return this;
        }
        
        @Deprecated
        public Builder setNotificationsEnabledTrackingOn(final boolean b) {
            this.w = b;
            return this;
        }
        
        public Builder setPushDeepLinkBackStackActivityClass(final Class clazz) {
            if (clazz != null) {
                this.i = clazz.getName();
            }
            return this;
        }
        
        public Builder setPushDeepLinkBackStackActivityEnabled(final boolean b) {
            this.B = b;
            return this;
        }
        
        public Builder setSdkFlavor(final SdkFlavor k) {
            this.k = k;
            return this;
        }
        
        public Builder setServerTarget(final String c) {
            this.c = c;
            return this;
        }
        
        public Builder setSessionTimeout(final int n) {
            this.l = n;
            return this;
        }
        
        public Builder setSmallNotificationIcon(final String d) {
            this.d = d;
            return this;
        }
        
        public Builder setTriggerActionMinimumTimeIntervalSeconds(final int n) {
            this.o = n;
            return this;
        }
    }
}
