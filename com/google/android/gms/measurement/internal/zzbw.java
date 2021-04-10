package com.google.android.gms.measurement.internal;

final class zzbw implements Runnable
{
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzbv zzaqo;
    
    zzbw(final zzbv zzaqo, final zzh zzaqn) {
        this.zzaqo = zzaqo;
        this.zzaqn = zzaqn;
    }
    
    @Override
    public final void run() {
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zze(this.zzaqn);
    }
}
