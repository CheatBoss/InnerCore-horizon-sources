package com.google.android.gms.measurement.internal;

final class zzca implements Runnable
{
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ zzl zzaqp;
    
    zzca(final zzbv zzaqo, final zzl zzaqp) {
        this.zzaqo = zzaqo;
        this.zzaqp = zzaqp;
    }
    
    @Override
    public final void run() {
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zze(this.zzaqp);
    }
}
