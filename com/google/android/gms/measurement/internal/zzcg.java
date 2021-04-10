package com.google.android.gms.measurement.internal;

final class zzcg implements Runnable
{
    private final /* synthetic */ zzh zzaqn;
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ zzad zzaqr;
    
    zzcg(final zzbv zzaqo, final zzad zzaqr, final zzh zzaqn) {
        this.zzaqo = zzaqo;
        this.zzaqr = zzaqr;
        this.zzaqn = zzaqn;
    }
    
    @Override
    public final void run() {
        final zzad zzb = this.zzaqo.zzb(this.zzaqr, this.zzaqn);
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zzc(zzb, this.zzaqn);
    }
}
