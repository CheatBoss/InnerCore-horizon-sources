package com.google.android.gms.measurement.internal;

final class zzet implements Runnable
{
    private final /* synthetic */ long zzafv;
    private final /* synthetic */ zzeq zzasz;
    
    zzet(final zzeq zzasz, final long zzafv) {
        this.zzasz = zzasz;
        this.zzafv = zzafv;
    }
    
    @Override
    public final void run() {
        this.zzasz.zzal(this.zzafv);
    }
}
