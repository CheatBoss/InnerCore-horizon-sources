package com.google.android.gms.measurement.internal;

import android.net.*;
import com.google.android.gms.common.*;
import android.content.*;
import com.google.android.gms.internal.measurement.*;
import java.util.*;

public final class zzaf
{
    private static volatile zzbt zzadj;
    static zzk zzaiq;
    static List<zza<Integer>> zzair;
    static List<zza<Long>> zzais;
    static List<zza<Boolean>> zzait;
    static List<zza<String>> zzaiu;
    static List<zza<Double>> zzaiv;
    private static final zzsv zzaiw;
    private static Boolean zzaix;
    private static zza<Boolean> zzaiy;
    private static zza<Boolean> zzaiz;
    private static zza<Boolean> zzaja;
    public static zza<Boolean> zzajb;
    public static zza<Boolean> zzajc;
    public static zza<String> zzajd;
    public static zza<Long> zzaje;
    public static zza<Long> zzajf;
    public static zza<Long> zzajg;
    public static zza<String> zzajh;
    public static zza<String> zzaji;
    public static zza<Integer> zzajj;
    public static zza<Integer> zzajk;
    public static zza<Integer> zzajl;
    public static zza<Integer> zzajm;
    public static zza<Integer> zzajn;
    public static zza<Integer> zzajo;
    public static zza<Integer> zzajp;
    public static zza<Integer> zzajq;
    public static zza<Integer> zzajr;
    public static zza<Integer> zzajs;
    public static zza<String> zzajt;
    public static zza<Long> zzaju;
    public static zza<Long> zzajv;
    public static zza<Long> zzajw;
    public static zza<Long> zzajx;
    public static zza<Long> zzajy;
    public static zza<Long> zzajz;
    public static zza<Long> zzaka;
    public static zza<Long> zzakb;
    public static zza<Long> zzakc;
    public static zza<Long> zzakd;
    public static zza<Long> zzake;
    public static zza<Integer> zzakf;
    public static zza<Long> zzakg;
    public static zza<Integer> zzakh;
    public static zza<Integer> zzaki;
    public static zza<Long> zzakj;
    public static zza<Boolean> zzakk;
    public static zza<String> zzakl;
    public static zza<Long> zzakm;
    public static zza<Integer> zzakn;
    public static zza<Double> zzako;
    public static zza<Boolean> zzakp;
    public static zza<Boolean> zzakq;
    public static zza<Boolean> zzakr;
    public static zza<Boolean> zzaks;
    public static zza<Boolean> zzakt;
    public static zza<Boolean> zzaku;
    public static zza<Boolean> zzakv;
    public static zza<Boolean> zzakw;
    public static zza<Boolean> zzakx;
    public static zza<Boolean> zzaky;
    public static zza<Boolean> zzakz;
    public static zza<Boolean> zzala;
    public static zza<Boolean> zzalb;
    public static zza<Boolean> zzalc;
    public static zza<Boolean> zzald;
    public static zza<Boolean> zzale;
    public static zza<Boolean> zzalf;
    private static zza<Boolean> zzalg;
    public static zza<Boolean> zzalh;
    private static zza<Boolean> zzali;
    public static zza<Boolean> zzalj;
    public static zza<Boolean> zzalk;
    
    static {
        zzaf.zzair = new ArrayList<zza<Integer>>();
        zzaf.zzais = new ArrayList<zza<Long>>();
        zzaf.zzait = new ArrayList<zza<Boolean>>();
        zzaf.zzaiu = new ArrayList<zza<String>>();
        zzaf.zzaiv = new ArrayList<zza<Double>>();
        final String value = String.valueOf(Uri.encode("com.google.android.gms.measurement"));
        String concat;
        if (value.length() != 0) {
            concat = "content://com.google.android.gms.phenotype/".concat(value);
        }
        else {
            concat = new String("content://com.google.android.gms.phenotype/");
        }
        zzaiw = new zzsv(Uri.parse(concat));
        zzaf.zzaiy = zza.zzb("measurement.log_third_party_store_events_enabled", false, false);
        zzaf.zzaiz = zza.zzb("measurement.log_installs_enabled", false, false);
        zzaf.zzaja = zza.zzb("measurement.log_upgrades_enabled", false, false);
        zzaf.zzajb = zza.zzb("measurement.log_androidId_enabled", false, false);
        zzaf.zzajc = zza.zzb("measurement.upload_dsid_enabled", false, false);
        zzaf.zzajd = zza.zzd("measurement.log_tag", "FA", "FA-SVC");
        zzaf.zzaje = zza.zzb("measurement.ad_id_cache_time", 10000L, 10000L);
        zzaf.zzajf = zza.zzb("measurement.monitoring.sample_period_millis", 86400000L, 86400000L);
        zzaf.zzajg = zza.zzb("measurement.config.cache_time", 86400000L, 3600000L);
        zzaf.zzajh = zza.zzd("measurement.config.url_scheme", "https", "https");
        zzaf.zzaji = zza.zzd("measurement.config.url_authority", "app-measurement.com", "app-measurement.com");
        zzaf.zzajj = zza.zzc("measurement.upload.max_bundles", 100, 100);
        zzaf.zzajk = zza.zzc("measurement.upload.max_batch_size", 65536, 65536);
        zzaf.zzajl = zza.zzc("measurement.upload.max_bundle_size", 65536, 65536);
        zzaf.zzajm = zza.zzc("measurement.upload.max_events_per_bundle", 1000, 1000);
        zzaf.zzajn = zza.zzc("measurement.upload.max_events_per_day", 100000, 100000);
        zzaf.zzajo = zza.zzc("measurement.upload.max_error_events_per_day", 1000, 1000);
        zzaf.zzajp = zza.zzc("measurement.upload.max_public_events_per_day", 50000, 50000);
        zzaf.zzajq = zza.zzc("measurement.upload.max_conversions_per_day", 500, 500);
        zzaf.zzajr = zza.zzc("measurement.upload.max_realtime_events_per_day", 10, 10);
        zzaf.zzajs = zza.zzc("measurement.store.max_stored_events_per_app", 100000, 100000);
        zzaf.zzajt = zza.zzd("measurement.upload.url", "https://app-measurement.com/a", "https://app-measurement.com/a");
        zzaf.zzaju = zza.zzb("measurement.upload.backoff_period", 43200000L, 43200000L);
        zzaf.zzajv = zza.zzb("measurement.upload.window_interval", 3600000L, 3600000L);
        zzaf.zzajw = zza.zzb("measurement.upload.interval", 3600000L, 3600000L);
        zzaf.zzajx = zza.zzb("measurement.upload.realtime_upload_interval", 10000L, 10000L);
        zzaf.zzajy = zza.zzb("measurement.upload.debug_upload_interval", 1000L, 1000L);
        zzaf.zzajz = zza.zzb("measurement.upload.minimum_delay", 500L, 500L);
        zzaf.zzaka = zza.zzb("measurement.alarm_manager.minimum_interval", 60000L, 60000L);
        zzaf.zzakb = zza.zzb("measurement.upload.stale_data_deletion_interval", 86400000L, 86400000L);
        zzaf.zzakc = zza.zzb("measurement.upload.refresh_blacklisted_config_interval", 604800000L, 604800000L);
        zzaf.zzakd = zza.zzb("measurement.upload.initial_upload_delay_time", 15000L, 15000L);
        zzaf.zzake = zza.zzb("measurement.upload.retry_time", 1800000L, 1800000L);
        zzaf.zzakf = zza.zzc("measurement.upload.retry_count", 6, 6);
        zzaf.zzakg = zza.zzb("measurement.upload.max_queue_time", 2419200000L, 2419200000L);
        zzaf.zzakh = zza.zzc("measurement.lifetimevalue.max_currency_tracked", 4, 4);
        zzaf.zzaki = zza.zzc("measurement.audience.filter_result_max_count", 200, 200);
        zzaf.zzakj = zza.zzb("measurement.service_client.idle_disconnect_millis", 5000L, 5000L);
        zzaf.zzakk = zza.zzb("measurement.test.boolean_flag", false, false);
        zzaf.zzakl = zza.zzd("measurement.test.string_flag", "---", "---");
        zzaf.zzakm = zza.zzb("measurement.test.long_flag", -1L, -1L);
        zzaf.zzakn = zza.zzc("measurement.test.int_flag", -2, -2);
        zzaf.zzako = zza.zza("measurement.test.double_flag", -3.0, -3.0);
        zzaf.zzakp = zza.zzb("measurement.lifetimevalue.user_engagement_tracking_enabled", false, false);
        zzaf.zzakq = zza.zzb("measurement.audience.complex_param_evaluation", false, false);
        zzaf.zzakr = zza.zzb("measurement.validation.internal_limits_internal_event_params", false, false);
        zzaf.zzaks = zza.zzb("measurement.quality.unsuccessful_update_retry_counter", false, false);
        zzaf.zzakt = zza.zzb("measurement.iid.disable_on_collection_disabled", true, true);
        zzaf.zzaku = zza.zzb("measurement.app_launch.call_only_when_enabled", true, true);
        zzaf.zzakv = zza.zzb("measurement.run_on_worker_inline", true, false);
        zzaf.zzakw = zza.zzb("measurement.audience.dynamic_filters", false, false);
        zzaf.zzakx = zza.zzb("measurement.reset_analytics.persist_time", false, false);
        zzaf.zzaky = zza.zzb("measurement.validation.value_and_currency_params", false, false);
        zzaf.zzakz = zza.zzb("measurement.sampling.time_zone_offset_enabled", false, false);
        zzaf.zzala = zza.zzb("measurement.referrer.enable_logging_install_referrer_cmp_from_apk", false, false);
        zzaf.zzalb = zza.zzb("measurement.disconnect_from_remote_service", false, false);
        zzaf.zzalc = zza.zzb("measurement.clear_local_database", false, false);
        zzaf.zzald = zza.zzb("measurement.fetch_config_with_admob_app_id", false, false);
        zzaf.zzale = zza.zzb("measurement.sessions.session_id_enabled", false, false);
        zzaf.zzalf = zza.zzb("measurement.sessions.immediate_start_enabled", false, false);
        zzaf.zzalg = zza.zzb("measurement.sessions.background_sessions_enabled", false, false);
        zzaf.zzalh = zza.zzb("measurement.collection.firebase_global_collection_flag_enabled", true, true);
        zzaf.zzali = zza.zzb("measurement.collection.efficient_engagement_reporting_enabled", false, false);
        zzaf.zzalj = zza.zzb("measurement.personalized_ads_feature_enabled", false, false);
        zzaf.zzalk = zza.zzb("measurement.remove_app_instance_id_cache_enabled", true, true);
    }
    
    static void zza(final zzbt zzadj) {
        zzaf.zzadj = zzadj;
    }
    
    static void zza(final zzk zzaiq) {
        zzaf.zzaiq = zzaiq;
        zzq();
    }
    
    static void zza(final Exception ex) {
        if (zzaf.zzadj == null) {
            return;
        }
        final Context context = zzaf.zzadj.getContext();
        if (zzaf.zzaix == null) {
            zzaf.zzaix = (GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context, 12451000) == 0);
        }
        if (zzaf.zzaix) {
            zzaf.zzadj.zzgo().zzjd().zzg("Got Exception on PhenotypeFlag.get on Play device", ex);
        }
    }
    
    public static final class zza<V>
    {
        private final V zzaan;
        private zzsl<V> zzall;
        private final V zzalm;
        private volatile V zzaln;
        private final String zzoj;
        
        private zza(final String zzoj, final V zzaan, final V zzalm) {
            this.zzoj = zzoj;
            this.zzaan = zzaan;
            this.zzalm = zzalm;
        }
        
        static zza<Double> zza(final String s, final double n, final double n2) {
            final Double value = -3.0;
            final zza zza = new zza<Double>(s, value, value);
            zzaf.zzaiv.add((zza<Double>)zza);
            return (zza<Double>)zza;
        }
        
        static zza<Long> zzb(final String s, final long n, final long n2) {
            final zza<Long> zza = new zza<Long>(s, n, n2);
            zzaf.zzais.add(zza);
            return zza;
        }
        
        static zza<Boolean> zzb(final String s, final boolean b, final boolean b2) {
            final zza<Boolean> zza = new zza<Boolean>(s, b, b2);
            zzaf.zzait.add(zza);
            return zza;
        }
        
        static zza<Integer> zzc(final String s, final int n, final int n2) {
            final zza<Integer> zza = new zza<Integer>(s, n, n2);
            zzaf.zzair.add(zza);
            return zza;
        }
        
        static zza<String> zzd(final String s, final String s2, final String s3) {
            final zza<String> zza = new zza<String>(s, s2, s3);
            zzaf.zzaiu.add(zza);
            return zza;
        }
        
        private static void zzix() {
            synchronized (zza.class) {
                if (!zzk.isMainThread()) {
                    final zzk zzaiq = zzaf.zzaiq;
                    try {
                        for (final zza<Boolean> zza : zzaf.zzait) {
                            zza.zzaln = zza.zzall.get();
                        }
                        for (final zza<String> zza2 : zzaf.zzaiu) {
                            zza2.zzaln = zza2.zzall.get();
                        }
                        for (final zza<Long> zza3 : zzaf.zzais) {
                            zza3.zzaln = zza3.zzall.get();
                        }
                        for (final zza<Integer> zza4 : zzaf.zzair) {
                            zza4.zzaln = zza4.zzall.get();
                        }
                        for (final zza<Double> zza5 : zzaf.zzaiv) {
                            zza5.zzaln = zza5.zzall.get();
                        }
                    }
                    catch (SecurityException ex) {
                        zzaf.zza(ex);
                    }
                    return;
                }
                throw new IllegalStateException("Tried to refresh flag cache on main thread or on package side.");
            }
        }
        
        private static void zzq() {
            synchronized (zza.class) {
                for (final zza<Boolean> zza : zzaf.zzait) {
                    final zzsv zziw = zzaf.zzaiw;
                    final String zzoj = zza.zzoj;
                    final zzk zzaiq = zzaf.zzaiq;
                    zza.zzall = zziw.zzf(zzoj, zza.zzaan);
                }
                for (final zza<String> zza2 : zzaf.zzaiu) {
                    final zzsv zziw2 = zzaf.zzaiw;
                    final String zzoj2 = zza2.zzoj;
                    final zzk zzaiq2 = zzaf.zzaiq;
                    zza2.zzall = zziw2.zzx(zzoj2, zza2.zzaan);
                }
                for (final zza<Long> zza3 : zzaf.zzais) {
                    final zzsv zziw3 = zzaf.zzaiw;
                    final String zzoj3 = zza3.zzoj;
                    final zzk zzaiq3 = zzaf.zzaiq;
                    zza3.zzall = zziw3.zze(zzoj3, zza3.zzaan);
                }
                for (final zza<Integer> zza4 : zzaf.zzair) {
                    final zzsv zziw4 = zzaf.zzaiw;
                    final String zzoj4 = zza4.zzoj;
                    final zzk zzaiq4 = zzaf.zzaiq;
                    zza4.zzall = zziw4.zzd(zzoj4, zza4.zzaan);
                }
                for (final zza<Double> zza5 : zzaf.zzaiv) {
                    final zzsv zziw5 = zzaf.zzaiw;
                    final String zzoj5 = zza5.zzoj;
                    final zzk zzaiq5 = zzaf.zzaiq;
                    zza5.zzall = zziw5.zzb(zzoj5, zza5.zzaan);
                }
            }
        }
        
        public final V get() {
            if (zzaf.zzaiq == null) {
                return this.zzaan;
            }
            final zzk zzaiq = zzaf.zzaiq;
            if (zzk.isMainThread()) {
                if (this.zzaln == null) {
                    return this.zzaan;
                }
                return this.zzaln;
            }
            else {
                zzix();
                try {
                    return this.zzall.get();
                }
                catch (SecurityException ex) {
                    zzaf.zza(ex);
                    return this.zzall.getDefaultValue();
                }
            }
        }
        
        public final V get(final V v) {
            if (v != null) {
                return v;
            }
            if (zzaf.zzaiq == null) {
                return this.zzaan;
            }
            final zzk zzaiq = zzaf.zzaiq;
            if (zzk.isMainThread()) {
                if (this.zzaln == null) {
                    return this.zzaan;
                }
                return this.zzaln;
            }
            else {
                zzix();
                try {
                    return this.zzall.get();
                }
                catch (SecurityException ex) {
                    zzaf.zza(ex);
                    return this.zzall.getDefaultValue();
                }
            }
        }
        
        public final String getKey() {
            return this.zzoj;
        }
    }
}
