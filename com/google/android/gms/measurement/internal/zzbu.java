package com.google.android.gms.measurement.internal;

final class zzbu implements Runnable
{
    private final /* synthetic */ zzcr zzaqj;
    private final /* synthetic */ zzbt zzaqk;
    
    zzbu(final zzbt zzaqk, final zzcr zzaqj) {
        this.zzaqk = zzaqk;
        this.zzaqj = zzaqj;
    }
    
    @Override
    public final void run() {
        this.zzaqk.zza(this.zzaqj);
        this.zzaqk.start();
    }
}
