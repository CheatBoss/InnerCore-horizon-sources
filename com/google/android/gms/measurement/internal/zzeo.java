package com.google.android.gms.measurement.internal;

final class zzeo implements Runnable
{
    private final /* synthetic */ Runnable zzacf;
    private final /* synthetic */ zzfa zzasv;
    
    zzeo(final zzel zzel, final zzfa zzasv, final Runnable zzacf) {
        this.zzasv = zzasv;
        this.zzacf = zzacf;
    }
    
    @Override
    public final void run() {
        this.zzasv.zzly();
        this.zzasv.zzg(this.zzacf);
        this.zzasv.zzlt();
    }
}
