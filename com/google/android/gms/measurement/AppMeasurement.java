package com.google.android.gms.measurement;

import com.google.android.gms.common.internal.*;
import android.content.*;
import android.os.*;
import java.util.*;
import com.google.android.gms.measurement.internal.*;

@Deprecated
public class AppMeasurement
{
    private final zzbt zzadj;
    
    public AppMeasurement(final zzbt zzadj) {
        Preconditions.checkNotNull(zzadj);
        this.zzadj = zzadj;
    }
    
    @Deprecated
    public static AppMeasurement getInstance(final Context context) {
        return zzbt.zza(context, null).zzki();
    }
    
    public void beginAdUnitExposure(final String s) {
        this.zzadj.zzgd().beginAdUnitExposure(s, this.zzadj.zzbx().elapsedRealtime());
    }
    
    public void clearConditionalUserProperty(final String s, final String s2, final Bundle bundle) {
        this.zzadj.zzge().clearConditionalUserProperty(s, s2, bundle);
    }
    
    protected void clearConditionalUserPropertyAs(final String s, final String s2, final String s3, final Bundle bundle) {
        this.zzadj.zzge().clearConditionalUserPropertyAs(s, s2, s3, bundle);
    }
    
    public void endAdUnitExposure(final String s) {
        this.zzadj.zzgd().endAdUnitExposure(s, this.zzadj.zzbx().elapsedRealtime());
    }
    
    public long generateEventId() {
        return this.zzadj.zzgm().zzmc();
    }
    
    public String getAppInstanceId() {
        return this.zzadj.zzge().zzfx();
    }
    
    public List<ConditionalUserProperty> getConditionalUserProperties(final String s, final String s2) {
        return this.zzadj.zzge().getConditionalUserProperties(s, s2);
    }
    
    protected List<ConditionalUserProperty> getConditionalUserPropertiesAs(final String s, final String s2, final String s3) {
        return this.zzadj.zzge().getConditionalUserPropertiesAs(s, s2, s3);
    }
    
    public String getCurrentScreenClass() {
        return this.zzadj.zzge().getCurrentScreenClass();
    }
    
    public String getCurrentScreenName() {
        return this.zzadj.zzge().getCurrentScreenName();
    }
    
    public String getGmpAppId() {
        return this.zzadj.zzge().getGmpAppId();
    }
    
    public int getMaxUserProperties(final String s) {
        this.zzadj.zzge();
        Preconditions.checkNotEmpty(s);
        return 25;
    }
    
    protected Map<String, Object> getUserProperties(final String s, final String s2, final boolean b) {
        return this.zzadj.zzge().getUserProperties(s, s2, b);
    }
    
    protected Map<String, Object> getUserPropertiesAs(final String s, final String s2, final String s3, final boolean b) {
        return this.zzadj.zzge().getUserPropertiesAs(s, s2, s3, b);
    }
    
    public void logEventInternal(final String s, final String s2, final Bundle bundle) {
        this.zzadj.zzge().logEvent(s, s2, bundle);
    }
    
    public void setConditionalUserProperty(final ConditionalUserProperty conditionalUserProperty) {
        this.zzadj.zzge().setConditionalUserProperty(conditionalUserProperty);
    }
    
    protected void setConditionalUserPropertyAs(final ConditionalUserProperty conditionalUserPropertyAs) {
        this.zzadj.zzge().setConditionalUserPropertyAs(conditionalUserPropertyAs);
    }
    
    public void setUserPropertyInternal(final String s, final String s2, final Object o) {
        Preconditions.checkNotEmpty(s);
        this.zzadj.zzge().zzb(s, s2, o, true);
    }
    
    public final void zzd(final boolean b) {
        this.zzadj.zzge().zzd(b);
    }
    
    public static class ConditionalUserProperty
    {
        public boolean mActive;
        public String mAppId;
        public long mCreationTimestamp;
        public String mExpiredEventName;
        public Bundle mExpiredEventParams;
        public String mName;
        public String mOrigin;
        public long mTimeToLive;
        public String mTimedOutEventName;
        public Bundle mTimedOutEventParams;
        public String mTriggerEventName;
        public long mTriggerTimeout;
        public String mTriggeredEventName;
        public Bundle mTriggeredEventParams;
        public long mTriggeredTimestamp;
        public Object mValue;
        
        public ConditionalUserProperty() {
        }
        
        public ConditionalUserProperty(final ConditionalUserProperty conditionalUserProperty) {
            Preconditions.checkNotNull(conditionalUserProperty);
            this.mAppId = conditionalUserProperty.mAppId;
            this.mOrigin = conditionalUserProperty.mOrigin;
            this.mCreationTimestamp = conditionalUserProperty.mCreationTimestamp;
            this.mName = conditionalUserProperty.mName;
            final Object mValue = conditionalUserProperty.mValue;
            if (mValue != null && (this.mValue = zzfk.zzf(mValue)) == null) {
                this.mValue = conditionalUserProperty.mValue;
            }
            this.mActive = conditionalUserProperty.mActive;
            this.mTriggerEventName = conditionalUserProperty.mTriggerEventName;
            this.mTriggerTimeout = conditionalUserProperty.mTriggerTimeout;
            this.mTimedOutEventName = conditionalUserProperty.mTimedOutEventName;
            if (conditionalUserProperty.mTimedOutEventParams != null) {
                this.mTimedOutEventParams = new Bundle(conditionalUserProperty.mTimedOutEventParams);
            }
            this.mTriggeredEventName = conditionalUserProperty.mTriggeredEventName;
            if (conditionalUserProperty.mTriggeredEventParams != null) {
                this.mTriggeredEventParams = new Bundle(conditionalUserProperty.mTriggeredEventParams);
            }
            this.mTriggeredTimestamp = conditionalUserProperty.mTriggeredTimestamp;
            this.mTimeToLive = conditionalUserProperty.mTimeToLive;
            this.mExpiredEventName = conditionalUserProperty.mExpiredEventName;
            if (conditionalUserProperty.mExpiredEventParams != null) {
                this.mExpiredEventParams = new Bundle(conditionalUserProperty.mExpiredEventParams);
            }
        }
    }
    
    public static final class Event
    {
        public static final String[] zzadk;
        public static final String[] zzadl;
        
        static {
            zzadk = new String[] { "app_clear_data", "app_exception", "app_remove", "app_upgrade", "app_install", "app_update", "firebase_campaign", "error", "first_open", "first_visit", "in_app_purchase", "notification_dismiss", "notification_foreground", "notification_open", "notification_receive", "os_update", "session_start", "user_engagement", "ad_exposure", "adunit_exposure", "ad_query", "ad_activeview", "ad_impression", "ad_click", "ad_reward", "screen_view", "ga_extra_parameter" };
            zzadl = new String[] { "_cd", "_ae", "_ui", "_ug", "_in", "_au", "_cmp", "_err", "_f", "_v", "_iap", "_nd", "_nf", "_no", "_nr", "_ou", "_s", "_e", "_xa", "_xu", "_aq", "_aa", "_ai", "_ac", "_ar", "_vs", "_ep" };
        }
        
        public static String zzal(final String s) {
            return zzfk.zza(s, Event.zzadk, Event.zzadl);
        }
    }
    
    public interface EventInterceptor
    {
        void interceptEvent(final String p0, final String p1, final Bundle p2, final long p3);
    }
    
    public interface OnEventListener
    {
        void onEvent(final String p0, final String p1, final Bundle p2, final long p3);
    }
    
    public static final class Param
    {
        public static final String[] zzadm;
        public static final String[] zzadn;
        
        static {
            zzadm = new String[] { "firebase_conversion", "engagement_time_msec", "exposure_time", "ad_event_id", "ad_unit_id", "firebase_error", "firebase_error_value", "firebase_error_length", "firebase_event_origin", "firebase_screen", "firebase_screen_class", "firebase_screen_id", "firebase_previous_screen", "firebase_previous_class", "firebase_previous_id", "message_device_time", "message_id", "message_name", "message_time", "previous_app_version", "previous_os_version", "topic", "update_with_analytics", "previous_first_open_count", "system_app", "system_app_update", "previous_install_count", "ga_event_id", "ga_extra_params_ct", "ga_group_name", "ga_list_length", "ga_index", "ga_event_name", "campaign_info_source", "deferred_analytics_collection", "session_number", "session_id" };
            zzadn = new String[] { "_c", "_et", "_xt", "_aeid", "_ai", "_err", "_ev", "_el", "_o", "_sn", "_sc", "_si", "_pn", "_pc", "_pi", "_ndt", "_nmid", "_nmn", "_nmt", "_pv", "_po", "_nt", "_uwa", "_pfo", "_sys", "_sysu", "_pin", "_eid", "_epc", "_gn", "_ll", "_i", "_en", "_cis", "_dac", "_sno", "_sid" };
        }
        
        public static String zzal(final String s) {
            return zzfk.zza(s, Param.zzadm, Param.zzadn);
        }
    }
    
    public static final class UserProperty
    {
        public static final String[] zzado;
        public static final String[] zzadp;
        
        static {
            zzado = new String[] { "firebase_last_notification", "first_open_time", "first_visit_time", "last_deep_link_referrer", "user_id", "first_open_after_install", "lifetime_user_engagement", "google_allow_ad_personalization_signals", "session_number", "session_id" };
            zzadp = new String[] { "_ln", "_fot", "_fvt", "_ldl", "_id", "_fi", "_lte", "_ap", "_sno", "_sid" };
        }
        
        public static String zzal(final String s) {
            return zzfk.zza(s, UserProperty.zzado, UserProperty.zzadp);
        }
    }
}
