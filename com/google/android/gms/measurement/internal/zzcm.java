package com.google.android.gms.measurement.internal;

final class zzcm implements Runnable
{
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzbv zzaqo;
    
    zzcm(final zzbv zzaqo, final zzh zzaqn) {
        this.zzaqo = zzaqo;
        this.zzaqn = zzaqn;
    }
    
    @Override
    public final void run() {
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zzf(this.zzaqn);
    }
}
