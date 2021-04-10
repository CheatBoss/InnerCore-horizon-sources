package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;

final class zzbq implements UncaughtExceptionHandler
{
    private final String zzapf;
    private final /* synthetic */ zzbo zzapg;
    
    public zzbq(final zzbo zzapg, final String zzapf) {
        this.zzapg = zzapg;
        Preconditions.checkNotNull(zzapf);
        this.zzapf = zzapf;
    }
    
    @Override
    public final void uncaughtException(final Thread thread, final Throwable t) {
        synchronized (this) {
            this.zzapg.zzgo().zzjd().zzg(this.zzapf, t);
        }
    }
}
