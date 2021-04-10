package com.google.android.gms.measurement.internal;

final class zzcf implements Runnable
{
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzbv zzaqo;
    
    zzcf(final zzbv zzaqo, final zzh zzaqn) {
        this.zzaqo = zzaqo;
        this.zzaqn = zzaqn;
    }
    
    @Override
    public final void run() {
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zzd(this.zzaqn);
    }
}
