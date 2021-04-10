package com.google.android.gms.measurement.internal;

final class zzc implements Runnable
{
    private final /* synthetic */ String zzaet;
    private final /* synthetic */ long zzaft;
    private final /* synthetic */ zza zzafu;
    
    zzc(final zza zzafu, final String zzaet, final long zzaft) {
        this.zzafu = zzafu;
        this.zzaet = zzaet;
        this.zzaft = zzaft;
    }
    
    @Override
    public final void run() {
        this.zzafu.zzb(this.zzaet, this.zzaft);
    }
}
