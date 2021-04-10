package com.google.android.gms.measurement.internal;

final class zzeu implements Runnable
{
    private final /* synthetic */ long zzafv;
    private final /* synthetic */ zzeq zzasz;
    
    zzeu(final zzeq zzasz, final long zzafv) {
        this.zzasz = zzasz;
        this.zzafv = zzafv;
    }
    
    @Override
    public final void run() {
        this.zzasz.zzan(this.zzafv);
    }
}
