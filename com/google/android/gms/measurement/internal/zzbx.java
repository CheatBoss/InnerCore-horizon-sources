package com.google.android.gms.measurement.internal;

final class zzbx implements Runnable
{
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ zzl zzaqp;
    
    zzbx(final zzbv zzaqo, final zzl zzaqp, final zzh zzaqn) {
        this.zzaqo = zzaqo;
        this.zzaqp = zzaqp;
        this.zzaqn = zzaqn;
    }
    
    @Override
    public final void run() {
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zzc(this.zzaqp, this.zzaqn);
    }
}
