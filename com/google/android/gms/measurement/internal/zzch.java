package com.google.android.gms.measurement.internal;

final class zzch implements Runnable
{
    private final /* synthetic */ zzbv zzaqo;
    private final /* synthetic */ String zzaqq;
    private final /* synthetic */ zzad zzaqr;
    
    zzch(final zzbv zzaqo, final zzad zzaqr, final String zzaqq) {
        this.zzaqo = zzaqo;
        this.zzaqr = zzaqr;
        this.zzaqq = zzaqq;
    }
    
    @Override
    public final void run() {
        this.zzaqo.zzamz.zzly();
        this.zzaqo.zzamz.zzc(this.zzaqr, this.zzaqq);
    }
}
