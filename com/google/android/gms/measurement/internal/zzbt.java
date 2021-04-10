package com.google.android.gms.measurement.internal;

import com.google.android.gms.measurement.*;
import android.content.*;
import com.google.android.gms.common.internal.*;
import com.google.android.gms.internal.measurement.*;
import com.google.android.gms.common.util.*;
import android.app.*;
import android.text.*;
import com.google.android.gms.common.api.internal.*;
import com.google.android.gms.common.wrappers.*;
import java.util.concurrent.atomic.*;

public class zzbt implements zzcq
{
    private static volatile zzbt zzapl;
    private final boolean zzadv;
    private final String zzadx;
    private final long zzagx;
    private final zzk zzaiq;
    private final String zzapm;
    private final String zzapn;
    private final zzn zzapo;
    private final zzba zzapp;
    private final zzap zzapq;
    private final zzbo zzapr;
    private final zzeq zzaps;
    private final AppMeasurement zzapt;
    private final zzfk zzapu;
    private final zzan zzapv;
    private final zzdo zzapw;
    private final zzcs zzapx;
    private final zza zzapy;
    private zzal zzapz;
    private zzdr zzaqa;
    private zzx zzaqb;
    private zzaj zzaqc;
    private zzbg zzaqd;
    private Boolean zzaqe;
    private long zzaqf;
    private volatile Boolean zzaqg;
    private int zzaqh;
    private int zzaqi;
    private final Context zzri;
    private final Clock zzrz;
    private boolean zzvz;
    
    private zzbt(final zzcr zzcr) {
        this.zzvz = false;
        Preconditions.checkNotNull(zzcr);
        zzaf.zza(this.zzaiq = new zzk(zzcr.zzri));
        this.zzri = zzcr.zzri;
        this.zzadx = zzcr.zzadx;
        this.zzapm = zzcr.zzapm;
        this.zzapn = zzcr.zzapn;
        this.zzadv = zzcr.zzadv;
        this.zzaqg = zzcr.zzaqg;
        zzsl.init(this.zzri);
        final Clock instance = DefaultClock.getInstance();
        this.zzrz = instance;
        this.zzagx = instance.currentTimeMillis();
        this.zzapo = new zzn(this);
        final zzba zzapp = new zzba(this);
        zzapp.zzq();
        this.zzapp = zzapp;
        final zzap zzapq = new zzap(this);
        zzapq.zzq();
        this.zzapq = zzapq;
        final zzfk zzapu = new zzfk(this);
        zzapu.zzq();
        this.zzapu = zzapu;
        final zzan zzapv = new zzan(this);
        zzapv.zzq();
        this.zzapv = zzapv;
        this.zzapy = new zza(this);
        final zzdo zzapw = new zzdo(this);
        zzapw.zzq();
        this.zzapw = zzapw;
        final zzcs zzapx = new zzcs(this);
        zzapx.zzq();
        this.zzapx = zzapx;
        this.zzapt = new AppMeasurement(this);
        final zzeq zzaps = new zzeq(this);
        zzaps.zzq();
        this.zzaps = zzaps;
        final zzbo zzapr = new zzbo(this);
        zzapr.zzq();
        this.zzapr = zzapr;
        Label_0397: {
            zzar zzar;
            String s;
            if (this.zzri.getApplicationContext() instanceof Application) {
                final zzcs zzge = this.zzge();
                if (!(zzge.getContext().getApplicationContext() instanceof Application)) {
                    break Label_0397;
                }
                final Application application = (Application)zzge.getContext().getApplicationContext();
                if (zzge.zzaqv == null) {
                    zzge.zzaqv = new zzdm(zzge, null);
                }
                application.unregisterActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)zzge.zzaqv);
                application.registerActivityLifecycleCallbacks((Application$ActivityLifecycleCallbacks)zzge.zzaqv);
                zzar = zzge.zzgo().zzjl();
                s = "Registered activity lifecycle callback";
            }
            else {
                zzar = this.zzgo().zzjg();
                s = "Application context is not an Application";
            }
            zzar.zzbx(s);
        }
        this.zzapr.zzc(new zzbu(this, zzcr));
    }
    
    public static zzbt zza(final Context context, final zzak zzak) {
        zzak zzak2 = zzak;
        Label_0052: {
            if (zzak != null) {
                if (zzak.origin != null) {
                    zzak2 = zzak;
                    if (zzak.zzadx != null) {
                        break Label_0052;
                    }
                }
                zzak2 = new zzak(zzak.zzadt, zzak.zzadu, zzak.zzadv, zzak.zzadw, null, null, zzak.zzady);
            }
        }
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (zzbt.zzapl == null) {
            synchronized (zzbt.class) {
                if (zzbt.zzapl == null) {
                    zzbt.zzapl = new zzbt(new zzcr(context, zzak2));
                }
                return zzbt.zzapl;
            }
        }
        if (zzak2 != null && zzak2.zzady != null && zzak2.zzady.containsKey("dataCollectionDefaultEnabled")) {
            zzbt.zzapl.zzd(zzak2.zzady.getBoolean("dataCollectionDefaultEnabled"));
        }
        return zzbt.zzapl;
    }
    
    private static void zza(final zzco zzco) {
        if (zzco != null) {
            return;
        }
        throw new IllegalStateException("Component not created");
    }
    
    private static void zza(final zzcp zzcp) {
        if (zzcp == null) {
            throw new IllegalStateException("Component not created");
        }
        if (zzcp.isInitialized()) {
            return;
        }
        final String value = String.valueOf(zzcp.getClass());
        final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 27);
        sb.append("Component not initialized: ");
        sb.append(value);
        throw new IllegalStateException(sb.toString());
    }
    
    private final void zza(final zzcr zzcr) {
        this.zzgn().zzaf();
        zzn.zzht();
        final zzx zzaqb = new zzx(this);
        zzaqb.zzq();
        this.zzaqb = zzaqb;
        final zzaj zzaqc = new zzaj(this);
        zzaqc.zzq();
        this.zzaqc = zzaqc;
        final zzal zzapz = new zzal(this);
        zzapz.zzq();
        this.zzapz = zzapz;
        final zzdr zzaqa = new zzdr(this);
        zzaqa.zzq();
        this.zzaqa = zzaqa;
        this.zzapu.zzgs();
        this.zzapp.zzgs();
        this.zzaqd = new zzbg(this);
        this.zzaqc.zzgs();
        this.zzgo().zzjj().zzg("App measurement is starting up, version", this.zzapo.zzhc());
        this.zzgo().zzjj().zzbx("To enable debug logging run: adb shell setprop log.tag.FA VERBOSE");
        final String zzal = zzaqc.zzal();
        if (TextUtils.isEmpty((CharSequence)this.zzadx)) {
            zzar zzar;
            String concat;
            if (this.zzgm().zzcw(zzal)) {
                zzar = this.zzgo().zzjj();
                concat = "Faster debug mode event logging enabled. To disable, run:\n  adb shell setprop debug.firebase.analytics.app .none.";
            }
            else {
                zzar = this.zzgo().zzjj();
                final String value = String.valueOf(zzal);
                if (value.length() != 0) {
                    concat = "To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app ".concat(value);
                }
                else {
                    concat = new String("To enable faster debug mode event logging run:\n  adb shell setprop debug.firebase.analytics.app ");
                }
            }
            zzar.zzbx(concat);
        }
        this.zzgo().zzjk().zzbx("Debug-level message logging enabled");
        if (this.zzaqh != this.zzaqi) {
            this.zzgo().zzjd().zze("Not all components initialized", this.zzaqh, this.zzaqi);
        }
        this.zzvz = true;
    }
    
    private static void zza(final zzf zzf) {
        if (zzf == null) {
            throw new IllegalStateException("Component not created");
        }
        if (zzf.isInitialized()) {
            return;
        }
        final String value = String.valueOf(zzf.getClass());
        final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 27);
        sb.append("Component not initialized: ");
        sb.append(value);
        throw new IllegalStateException(sb.toString());
    }
    
    private final void zzcl() {
        if (this.zzvz) {
            return;
        }
        throw new IllegalStateException("AppMeasurement is not initialized");
    }
    
    @Override
    public final Context getContext() {
        return this.zzri;
    }
    
    public final boolean isEnabled() {
        this.zzgn().zzaf();
        this.zzcl();
        if (this.zzapo.zzhu()) {
            return false;
        }
        Boolean b = this.zzapo.zzhv();
        if (b == null) {
            boolean booleanValue;
            final boolean b2 = booleanValue = (((GoogleServices.isMeasurementExplicitlyDisabled() ? 1 : 0) ^ 0x1) != 0x0);
            if (!b2) {
                return this.zzgp().zzh(booleanValue);
            }
            booleanValue = b2;
            if (this.zzaqg == null) {
                return this.zzgp().zzh(booleanValue);
            }
            booleanValue = b2;
            if (!zzaf.zzalh.get()) {
                return this.zzgp().zzh(booleanValue);
            }
            b = this.zzaqg;
        }
        boolean booleanValue = b;
        return this.zzgp().zzh(booleanValue);
    }
    
    protected final void start() {
        this.zzgn().zzaf();
        if (this.zzgp().zzane.get() == 0L) {
            this.zzgp().zzane.set(this.zzrz.currentTimeMillis());
        }
        if (this.zzgp().zzanj.get() == 0L) {
            this.zzgo().zzjl().zzg("Persisting first open", this.zzagx);
            this.zzgp().zzanj.set(this.zzagx);
        }
        if (!this.zzkr()) {
            if (this.isEnabled()) {
                if (!this.zzgm().zzx("android.permission.INTERNET")) {
                    this.zzgo().zzjd().zzbx("App is missing INTERNET permission");
                }
                if (!this.zzgm().zzx("android.permission.ACCESS_NETWORK_STATE")) {
                    this.zzgo().zzjd().zzbx("App is missing ACCESS_NETWORK_STATE permission");
                }
                if (!Wrappers.packageManager(this.zzri).isCallerInstantApp() && !this.zzapo.zzib()) {
                    if (!zzbj.zza(this.zzri)) {
                        this.zzgo().zzjd().zzbx("AppMeasurementReceiver not registered/enabled");
                    }
                    if (!zzfk.zza(this.zzri, false)) {
                        this.zzgo().zzjd().zzbx("AppMeasurementService not registered/enabled");
                    }
                }
                this.zzgo().zzjd().zzbx("Uploading is not possible. App measurement disabled");
            }
        }
        else {
            if (!TextUtils.isEmpty((CharSequence)this.zzgf().getGmpAppId()) || !TextUtils.isEmpty((CharSequence)this.zzgf().zzgw())) {
                this.zzgm();
                if (zzfk.zza(this.zzgf().getGmpAppId(), this.zzgp().zzjs(), this.zzgf().zzgw(), this.zzgp().zzjt())) {
                    this.zzgo().zzjj().zzbx("Rechecking which service to use due to a GMP App Id change");
                    this.zzgp().zzjv();
                    if (this.zzapo.zza(zzaf.zzalc)) {
                        this.zzgi().resetAnalyticsData();
                    }
                    this.zzaqa.disconnect();
                    this.zzaqa.zzdj();
                    this.zzgp().zzanj.set(this.zzagx);
                    this.zzgp().zzanl.zzcc(null);
                }
                this.zzgp().zzca(this.zzgf().getGmpAppId());
                this.zzgp().zzcb(this.zzgf().zzgw());
                if (this.zzapo.zzbj(this.zzgf().zzal())) {
                    this.zzaps.zzam(this.zzagx);
                }
            }
            this.zzge().zzcm(this.zzgp().zzanl.zzjz());
            if (!TextUtils.isEmpty((CharSequence)this.zzgf().getGmpAppId()) || !TextUtils.isEmpty((CharSequence)this.zzgf().zzgw())) {
                final boolean enabled = this.isEnabled();
                if (!this.zzgp().zzjy() && !this.zzapo.zzhu()) {
                    this.zzgp().zzi(enabled ^ true);
                }
                if (this.zzapo.zze(this.zzgf().zzal(), zzaf.zzalj)) {
                    this.zzj(false);
                }
                if (!this.zzapo.zzbd(this.zzgf().zzal()) || enabled) {
                    this.zzge().zzkz();
                }
                this.zzgg().zza(new AtomicReference<String>());
            }
        }
    }
    
    final void zzb(final zzcp zzcp) {
        ++this.zzaqh;
    }
    
    final void zzb(final zzf zzf) {
        ++this.zzaqh;
    }
    
    @Override
    public final Clock zzbx() {
        return this.zzrz;
    }
    
    final void zzd(final boolean b) {
        this.zzaqg = b;
    }
    
    final void zzga() {
        throw new IllegalStateException("Unexpected call on client side");
    }
    
    final void zzgb() {
    }
    
    public final zza zzgd() {
        final zza zzapy = this.zzapy;
        if (zzapy != null) {
            return zzapy;
        }
        throw new IllegalStateException("Component not created");
    }
    
    public final zzcs zzge() {
        zza(this.zzapx);
        return this.zzapx;
    }
    
    public final zzaj zzgf() {
        zza(this.zzaqc);
        return this.zzaqc;
    }
    
    public final zzdr zzgg() {
        zza(this.zzaqa);
        return this.zzaqa;
    }
    
    public final zzdo zzgh() {
        zza(this.zzapw);
        return this.zzapw;
    }
    
    public final zzal zzgi() {
        zza(this.zzapz);
        return this.zzapz;
    }
    
    public final zzeq zzgj() {
        zza(this.zzaps);
        return this.zzaps;
    }
    
    public final zzx zzgk() {
        zza(this.zzaqb);
        return this.zzaqb;
    }
    
    public final zzan zzgl() {
        zza((zzco)this.zzapv);
        return this.zzapv;
    }
    
    public final zzfk zzgm() {
        zza((zzco)this.zzapu);
        return this.zzapu;
    }
    
    @Override
    public final zzbo zzgn() {
        zza(this.zzapr);
        return this.zzapr;
    }
    
    @Override
    public final zzap zzgo() {
        zza(this.zzapq);
        return this.zzapq;
    }
    
    public final zzba zzgp() {
        zza((zzco)this.zzapp);
        return this.zzapp;
    }
    
    public final zzn zzgq() {
        return this.zzapo;
    }
    
    @Override
    public final zzk zzgr() {
        return this.zzaiq;
    }
    
    final void zzj(final boolean b) {
        this.zzgn().zzaf();
        final String zzjz = this.zzgp().zzans.zzjz();
        boolean b2 = false;
        Label_0096: {
            if (!b && zzjz != null) {
                if (!"unset".equals(zzjz)) {
                    this.zzge().zza("app", "_ap", zzjz, this.zzrz.currentTimeMillis());
                    b2 = false;
                    break Label_0096;
                }
                this.zzge().zza("app", "_ap", null, this.zzrz.currentTimeMillis());
            }
            b2 = true;
        }
        if (b2) {
            final Boolean zzau = this.zzapo.zzau("google_analytics_default_allow_ad_personalization_signals");
            if (zzau != null) {
                final zzcs zzge = this.zzge();
                long n;
                if (zzau) {
                    n = 1L;
                }
                else {
                    n = 0L;
                }
                zzge.zza("auto", "_ap", (Object)n, this.zzrz.currentTimeMillis());
                return;
            }
            this.zzge().zza("auto", "_ap", null, this.zzrz.currentTimeMillis());
        }
    }
    
    public final zzap zzkf() {
        final zzap zzapq = this.zzapq;
        if (zzapq != null && zzapq.isInitialized()) {
            return this.zzapq;
        }
        return null;
    }
    
    public final zzbg zzkg() {
        return this.zzaqd;
    }
    
    final zzbo zzkh() {
        return this.zzapr;
    }
    
    public final AppMeasurement zzki() {
        return this.zzapt;
    }
    
    public final boolean zzkj() {
        return TextUtils.isEmpty((CharSequence)this.zzadx);
    }
    
    public final String zzkk() {
        return this.zzadx;
    }
    
    public final String zzkl() {
        return this.zzapm;
    }
    
    public final String zzkm() {
        return this.zzapn;
    }
    
    public final boolean zzkn() {
        return this.zzadv;
    }
    
    public final boolean zzko() {
        return this.zzaqg != null && this.zzaqg;
    }
    
    final long zzkp() {
        final Long value = this.zzgp().zzanj.get();
        if (value == 0L) {
            return this.zzagx;
        }
        return Math.min(this.zzagx, value);
    }
    
    final void zzkq() {
        ++this.zzaqi;
    }
    
    protected final boolean zzkr() {
        this.zzcl();
        this.zzgn().zzaf();
        final Boolean zzaqe = this.zzaqe;
        if (zzaqe == null || this.zzaqf == 0L || (zzaqe != null && !zzaqe && Math.abs(this.zzrz.elapsedRealtime() - this.zzaqf) > 1000L)) {
            this.zzaqf = this.zzrz.elapsedRealtime();
            final boolean zzx = this.zzgm().zzx("android.permission.INTERNET");
            final boolean b = true;
            final Boolean value = zzx && this.zzgm().zzx("android.permission.ACCESS_NETWORK_STATE") && (Wrappers.packageManager(this.zzri).isCallerInstantApp() || this.zzapo.zzib() || (zzbj.zza(this.zzri) && zzfk.zza(this.zzri, false)));
            this.zzaqe = value;
            if (value) {
                boolean b2 = b;
                if (!this.zzgm().zzt(this.zzgf().getGmpAppId(), this.zzgf().zzgw())) {
                    b2 = (!TextUtils.isEmpty((CharSequence)this.zzgf().zzgw()) && b);
                }
                this.zzaqe = b2;
            }
        }
        return this.zzaqe;
    }
}
