package com.google.android.gms.measurement.internal;

final class zzck implements Runnable
{
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ zzfh zzaqs;
    
    zzck(final zzbv zzaqo, final zzfh zzaqs, final zzh zzaqn) {
        this.zzaqo = zzaqo;
        this.zzaqs = zzaqs;
        this.zzaqn = zzaqn;
    }
    
    @Override
    public final void run() {
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zzb(this.zzaqs, this.zzaqn);
    }
}
