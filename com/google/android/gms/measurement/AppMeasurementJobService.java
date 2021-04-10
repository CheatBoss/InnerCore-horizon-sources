package com.google.android.gms.measurement;

import com.google.android.gms.measurement.internal.*;
import android.content.*;
import android.app.job.*;

public final class AppMeasurementJobService extends JobService implements zzep
{
    private zzel<AppMeasurementJobService> zzadr;
    
    private final zzel<AppMeasurementJobService> zzfu() {
        if (this.zzadr == null) {
            this.zzadr = new zzel<AppMeasurementJobService>(this);
        }
        return this.zzadr;
    }
    
    public final boolean callServiceStopSelfResult(final int n) {
        throw new UnsupportedOperationException();
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
    
    public final boolean onStartJob(final JobParameters jobParameters) {
        return this.zzfu().onStartJob(jobParameters);
    }
    
    public final boolean onStopJob(final JobParameters jobParameters) {
        return false;
    }
    
    public final boolean onUnbind(final Intent intent) {
        return this.zzfu().onUnbind(intent);
    }
    
    public final void zza(final JobParameters jobParameters, final boolean b) {
        this.jobFinished(jobParameters, false);
    }
    
    public final void zzb(final Intent intent) {
    }
}
