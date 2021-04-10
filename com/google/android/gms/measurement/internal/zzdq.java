package com.google.android.gms.measurement.internal;

final class zzdq implements Runnable
{
    private final /* synthetic */ zzdo zzarx;
    private final /* synthetic */ zzdn zzary;
    
    zzdq(final zzdo zzarx, final zzdn zzary) {
        this.zzarx = zzarx;
        this.zzary = zzary;
    }
    
    @Override
    public final void run() {
        this.zzarx.zza(this.zzary);
        this.zzarx.zzaro = null;
        this.zzarx.zzgg().zzb((zzdn)null);
    }
}
