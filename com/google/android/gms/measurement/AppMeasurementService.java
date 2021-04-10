package com.google.android.gms.measurement;

import android.app.*;
import com.google.android.gms.measurement.internal.*;
import android.content.*;
import android.os.*;
import android.app.job.*;
import android.support.v4.content.*;

public final class AppMeasurementService extends Service implements zzep
{
    private zzel<AppMeasurementService> zzadr;
    
    private final zzel<AppMeasurementService> zzfu() {
        if (this.zzadr == null) {
            this.zzadr = new zzel<AppMeasurementService>(this);
        }
        return this.zzadr;
    }
    
    public final boolean callServiceStopSelfResult(final int n) {
        return this.stopSelfResult(n);
    }
    
    public final IBinder onBind(final Intent intent) {
        return this.zzfu().onBind(intent);
    }
    
    public final void onCreate() {
        super.onCreate();
        this.zzfu().onCreate();
    }
    
    public final void onDestroy() {
        this.zzfu().onDestroy();
        super.onDestroy();
    }
    
    public final void onRebind(final Intent intent) {
        this.zzfu().onRebind(intent);
    }
    
    public final int onStartCommand(final Intent intent, final int n, final int n2) {
        return this.zzfu().onStartCommand(intent, n, n2);
    }
    
    public final boolean onUnbind(final Intent intent) {
        return this.zzfu().onUnbind(intent);
    }
    
    public final void zza(final JobParameters jobParameters, final boolean b) {
        throw new UnsupportedOperationException();
    }
    
    public final void zzb(final Intent intent) {
        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }
}
