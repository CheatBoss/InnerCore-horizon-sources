package com.google.android.gms.measurement.internal;

final class zzd implements Runnable
{
    private final /* synthetic */ zza zzafu;
    private final /* synthetic */ long zzafv;
    
    zzd(final zza zzafu, final long zzafv) {
        this.zzafu = zzafu;
        this.zzafv = zzafv;
    }
    
    @Override
    public final void run() {
        this.zzafu.zzr(this.zzafv);
    }
}
