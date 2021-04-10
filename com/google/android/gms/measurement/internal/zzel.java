package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import android.content.*;
import android.os.*;
import android.app.job.*;

public final class zzel<T extends Context>
{
    private final T zzaby;
    
    public zzel(final T zzaby) {
        Preconditions.checkNotNull(zzaby);
        this.zzaby = zzaby;
    }
    
    private final void zzb(final Runnable runnable) {
        final zzfa zzm = zzfa.zzm((Context)this.zzaby);
        zzm.zzgn().zzc(new zzeo(this, zzm, runnable));
    }
    
    private final zzap zzgo() {
        return zzbt.zza((Context)this.zzaby, null).zzgo();
    }
    
    public final IBinder onBind(final Intent intent) {
        if (intent == null) {
            this.zzgo().zzjd().zzbx("onBind called with null intent");
            return null;
        }
        final String action = intent.getAction();
        if ("com.google.android.gms.measurement.START".equals(action)) {
            return (IBinder)new zzbv(zzfa.zzm((Context)this.zzaby));
        }
        this.zzgo().zzjg().zzg("onBind received unknown action", action);
        return null;
    }
    
    public final void onCreate() {
        final zzbt zza = zzbt.zza((Context)this.zzaby, null);
        final zzap zzgo = zza.zzgo();
        zza.zzgr();
        zzgo.zzjl().zzbx("Local AppMeasurementService is starting up");
    }
    
    public final void onDestroy() {
        final zzbt zza = zzbt.zza((Context)this.zzaby, null);
        final zzap zzgo = zza.zzgo();
        zza.zzgr();
        zzgo.zzjl().zzbx("Local AppMeasurementService is shutting down");
    }
    
    public final void onRebind(final Intent intent) {
        if (intent == null) {
            this.zzgo().zzjd().zzbx("onRebind called with null intent");
            return;
        }
        this.zzgo().zzjl().zzg("onRebind called. action", intent.getAction());
    }
    
    public final int onStartCommand(final Intent intent, final int n, final int n2) {
        final zzbt zza = zzbt.zza((Context)this.zzaby, null);
        final zzap zzgo = zza.zzgo();
        if (intent == null) {
            zzgo.zzjg().zzbx("AppMeasurementService started with null intent");
            return 2;
        }
        final String action = intent.getAction();
        zza.zzgr();
        zzgo.zzjl().zze("Local AppMeasurementService called. startId, action", n2, action);
        if ("com.google.android.gms.measurement.UPLOAD".equals(action)) {
            this.zzb(new zzem(this, n2, zzgo, intent));
        }
        return 2;
    }
    
    public final boolean onStartJob(final JobParameters jobParameters) {
        final zzbt zza = zzbt.zza((Context)this.zzaby, null);
        final zzap zzgo = zza.zzgo();
        final String string = jobParameters.getExtras().getString("action");
        zza.zzgr();
        zzgo.zzjl().zzg("Local AppMeasurementJobService called. action", string);
        if ("com.google.android.gms.measurement.UPLOAD".equals(string)) {
            this.zzb(new zzen(this, zzgo, jobParameters));
        }
        return true;
    }
    
    public final boolean onUnbind(final Intent intent) {
        if (intent == null) {
            this.zzgo().zzjd().zzbx("onUnbind called with null intent");
            return true;
        }
        this.zzgo().zzjl().zzg("onUnbind called for intent. action", intent.getAction());
        return true;
    }
}
