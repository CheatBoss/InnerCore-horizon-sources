package com.google.android.gms.measurement.internal;

import java.util.*;
import android.app.*;
import android.support.v4.util.*;
import android.os.*;
import com.google.android.gms.common.internal.*;
import android.content.*;
import com.google.android.gms.common.util.*;

public final class zzdo extends zzf
{
    protected zzdn zzaro;
    private volatile zzdn zzarp;
    private zzdn zzarq;
    private final Map<Activity, zzdn> zzarr;
    private zzdn zzars;
    private String zzart;
    
    public zzdo(final zzbt zzbt) {
        super(zzbt);
        this.zzarr = new ArrayMap<Activity, zzdn>();
    }
    
    private final void zza(final Activity activity, final zzdn zzdn, final boolean b) {
        zzdn zzdn2;
        if (this.zzarp == null) {
            zzdn2 = this.zzarq;
        }
        else {
            zzdn2 = this.zzarp;
        }
        zzdn zzarp = zzdn;
        if (zzdn.zzarl == null) {
            zzarp = new zzdn(zzdn.zzuw, zzcn(activity.getClass().getCanonicalName()), zzdn.zzarm);
        }
        this.zzarq = this.zzarp;
        this.zzarp = zzarp;
        this.zzgn().zzc(new zzdp(this, b, zzdn2, zzarp));
    }
    
    private final void zza(final zzdn zzdn) {
        this.zzgd().zzq(this.zzbx().elapsedRealtime());
        if (this.zzgj().zzn(zzdn.zzarn)) {
            zzdn.zzarn = false;
        }
    }
    
    public static void zza(final zzdn zzdn, final Bundle bundle, final boolean b) {
        if (bundle != null && zzdn != null && (!bundle.containsKey("_sc") || b)) {
            if (zzdn.zzuw != null) {
                bundle.putString("_sn", zzdn.zzuw);
            }
            else {
                bundle.remove("_sn");
            }
            bundle.putString("_sc", zzdn.zzarl);
            bundle.putLong("_si", zzdn.zzarm);
            return;
        }
        if (bundle != null && zzdn == null && b) {
            bundle.remove("_sn");
            bundle.remove("_sc");
            bundle.remove("_si");
        }
    }
    
    private static String zzcn(String s) {
        final String[] split = s.split("\\.");
        if (split.length > 0) {
            s = split[split.length - 1];
        }
        else {
            s = "";
        }
        String substring = s;
        if (s.length() > 100) {
            substring = s.substring(0, 100);
        }
        return substring;
    }
    
    private final zzdn zze(final Activity activity) {
        Preconditions.checkNotNull(activity);
        zzdn zzdn;
        if ((zzdn = this.zzarr.get(activity)) == null) {
            zzdn = new zzdn(null, zzcn(activity.getClass().getCanonicalName()), this.zzgm().zzmc());
            this.zzarr.put(activity, zzdn);
        }
        return zzdn;
    }
    
    public final void onActivityCreated(final Activity activity, Bundle bundle) {
        if (bundle == null) {
            return;
        }
        bundle = bundle.getBundle("com.google.app_measurement.screen_service");
        if (bundle == null) {
            return;
        }
        this.zzarr.put(activity, new zzdn(bundle.getString("name"), bundle.getString("referrer_name"), bundle.getLong("id")));
    }
    
    public final void onActivityDestroyed(final Activity activity) {
        this.zzarr.remove(activity);
    }
    
    public final void onActivityPaused(final Activity activity) {
        final zzdn zze = this.zze(activity);
        this.zzarq = this.zzarp;
        this.zzarp = null;
        this.zzgn().zzc(new zzdq(this, zze));
    }
    
    public final void onActivityResumed(final Activity activity) {
        this.zza(activity, this.zze(activity), false);
        final zza zzgd = this.zzgd();
        zzgd.zzgn().zzc(new zzd(zzgd, zzgd.zzbx().elapsedRealtime()));
    }
    
    public final void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
        if (bundle == null) {
            return;
        }
        final zzdn zzdn = this.zzarr.get(activity);
        if (zzdn == null) {
            return;
        }
        final Bundle bundle2 = new Bundle();
        bundle2.putLong("id", zzdn.zzarm);
        bundle2.putString("name", zzdn.zzuw);
        bundle2.putString("referrer_name", zzdn.zzarl);
        bundle.putBundle("com.google.app_measurement.screen_service", bundle2);
    }
    
    public final void zza(final String zzart, final zzdn zzars) {
        this.zzaf();
        synchronized (this) {
            if (this.zzart == null || this.zzart.equals(zzart) || zzars != null) {
                this.zzart = zzart;
                this.zzars = zzars;
            }
        }
    }
    
    @Override
    protected final boolean zzgt() {
        return false;
    }
    
    public final zzdn zzla() {
        this.zzcl();
        this.zzaf();
        return this.zzaro;
    }
    
    public final zzdn zzlb() {
        this.zzgb();
        return this.zzarp;
    }
}
