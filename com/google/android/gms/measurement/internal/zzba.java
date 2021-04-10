package com.google.android.gms.measurement.internal;

import android.util.*;
import android.content.*;
import com.google.android.gms.ads.identifier.*;
import java.util.*;
import java.math.*;
import java.security.*;
import android.os.*;
import android.text.*;

final class zzba extends zzcp
{
    static final Pair<String, Long> zzanc;
    private SharedPreferences zzabr;
    public zzbe zzand;
    public final zzbd zzane;
    public final zzbd zzanf;
    public final zzbd zzang;
    public final zzbd zzanh;
    public final zzbd zzani;
    public final zzbd zzanj;
    public final zzbd zzank;
    public final zzbf zzanl;
    private String zzanm;
    private boolean zzann;
    private long zzano;
    public final zzbd zzanp;
    public final zzbd zzanq;
    public final zzbc zzanr;
    public final zzbf zzans;
    public final zzbd zzant;
    public final zzbd zzanu;
    public boolean zzanv;
    
    static {
        zzanc = new Pair((Object)"", (Object)0L);
    }
    
    zzba(final zzbt zzbt) {
        super(zzbt);
        this.zzane = new zzbd(this, "last_upload", 0L);
        this.zzanf = new zzbd(this, "last_upload_attempt", 0L);
        this.zzang = new zzbd(this, "backoff", 0L);
        this.zzanh = new zzbd(this, "last_delete_stale", 0L);
        this.zzanp = new zzbd(this, "time_before_start", 10000L);
        this.zzanq = new zzbd(this, "session_timeout", 1800000L);
        this.zzanr = new zzbc(this, "start_new_session", true);
        this.zzans = new zzbf(this, "allow_ad_personalization", null);
        this.zzant = new zzbd(this, "last_pause_time", 0L);
        this.zzanu = new zzbd(this, "time_active", 0L);
        this.zzani = new zzbd(this, "midnight_offset", 0L);
        this.zzanj = new zzbd(this, "first_open_time", 0L);
        this.zzank = new zzbd(this, "app_install_time", 0L);
        this.zzanl = new zzbf(this, "app_instance_id", null);
    }
    
    private final SharedPreferences zzjr() {
        this.zzaf();
        this.zzcl();
        return this.zzabr;
    }
    
    final void setMeasurementEnabled(final boolean b) {
        this.zzaf();
        this.zzgo().zzjl().zzg("Setting measurementEnabled", b);
        final SharedPreferences$Editor edit = this.zzjr().edit();
        edit.putBoolean("measurement_enabled", b);
        edit.apply();
    }
    
    final Pair<String, Boolean> zzby(final String s) {
        this.zzaf();
        final long elapsedRealtime = this.zzbx().elapsedRealtime();
        if (this.zzanm != null && elapsedRealtime < this.zzano) {
            return (Pair<String, Boolean>)new Pair((Object)this.zzanm, (Object)this.zzann);
        }
        this.zzano = elapsedRealtime + this.zzgq().zza(s, zzaf.zzaje);
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(true);
        try {
            final AdvertisingIdClient.Info advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.getContext());
            if (advertisingIdInfo != null) {
                this.zzanm = advertisingIdInfo.getId();
                this.zzann = advertisingIdInfo.isLimitAdTrackingEnabled();
            }
            if (this.zzanm == null) {
                this.zzanm = "";
            }
        }
        catch (Exception ex) {
            this.zzgo().zzjk().zzg("Unable to get advertising id", ex);
            this.zzanm = "";
        }
        AdvertisingIdClient.setShouldSkipGmsCoreVersionCheck(false);
        return (Pair<String, Boolean>)new Pair((Object)this.zzanm, (Object)this.zzann);
    }
    
    final String zzbz(String s) {
        this.zzaf();
        s = (String)this.zzby(s).first;
        final MessageDigest messageDigest = zzfk.getMessageDigest();
        if (messageDigest == null) {
            return null;
        }
        return String.format(Locale.US, "%032X", new BigInteger(1, messageDigest.digest(s.getBytes())));
    }
    
    final void zzca(final String s) {
        this.zzaf();
        final SharedPreferences$Editor edit = this.zzjr().edit();
        edit.putString("gmp_app_id", s);
        edit.apply();
    }
    
    final void zzcb(final String s) {
        this.zzaf();
        final SharedPreferences$Editor edit = this.zzjr().edit();
        edit.putString("admob_app_id", s);
        edit.apply();
    }
    
    final void zzg(final boolean b) {
        this.zzaf();
        this.zzgo().zzjl().zzg("Setting useService", b);
        final SharedPreferences$Editor edit = this.zzjr().edit();
        edit.putBoolean("use_service", b);
        edit.apply();
    }
    
    @Override
    protected final boolean zzgt() {
        return true;
    }
    
    @Override
    protected final void zzgu() {
        final SharedPreferences sharedPreferences = this.getContext().getSharedPreferences("com.google.android.gms.measurement.prefs", 0);
        this.zzabr = sharedPreferences;
        if (!(this.zzanv = sharedPreferences.getBoolean("has_been_opened", false))) {
            final SharedPreferences$Editor edit = this.zzabr.edit();
            edit.putBoolean("has_been_opened", true);
            edit.apply();
        }
        this.zzand = new zzbe(this, "health_monitor", Math.max(0L, zzaf.zzajf.get()), null);
    }
    
    final boolean zzh(final boolean b) {
        this.zzaf();
        return this.zzjr().getBoolean("measurement_enabled", b);
    }
    
    final void zzi(final boolean b) {
        this.zzaf();
        this.zzgo().zzjl().zzg("Updating deferred analytics collection", b);
        final SharedPreferences$Editor edit = this.zzjr().edit();
        edit.putBoolean("deferred_analytics_collection", b);
        edit.apply();
    }
    
    final String zzjs() {
        this.zzaf();
        return this.zzjr().getString("gmp_app_id", (String)null);
    }
    
    final String zzjt() {
        this.zzaf();
        return this.zzjr().getString("admob_app_id", (String)null);
    }
    
    final Boolean zzju() {
        this.zzaf();
        if (!this.zzjr().contains("use_service")) {
            return null;
        }
        return this.zzjr().getBoolean("use_service", false);
    }
    
    final void zzjv() {
        this.zzaf();
        this.zzgo().zzjl().zzbx("Clearing collection preferences.");
        final boolean contains = this.zzjr().contains("measurement_enabled");
        boolean zzh = true;
        if (contains) {
            zzh = this.zzh(true);
        }
        final SharedPreferences$Editor edit = this.zzjr().edit();
        edit.clear();
        edit.apply();
        if (contains) {
            this.setMeasurementEnabled(zzh);
        }
    }
    
    protected final String zzjw() {
        this.zzaf();
        final String string = this.zzjr().getString("previous_os_version", (String)null);
        this.zzgk().zzcl();
        final String release = Build$VERSION.RELEASE;
        if (!TextUtils.isEmpty((CharSequence)release) && !release.equals(string)) {
            final SharedPreferences$Editor edit = this.zzjr().edit();
            edit.putString("previous_os_version", release);
            edit.apply();
        }
        return string;
    }
    
    final boolean zzjx() {
        this.zzaf();
        return this.zzjr().getBoolean("deferred_analytics_collection", false);
    }
    
    final boolean zzjy() {
        return this.zzabr.contains("deferred_analytics_collection");
    }
}
