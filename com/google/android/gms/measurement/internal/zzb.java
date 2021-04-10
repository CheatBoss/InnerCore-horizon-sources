package com.google.android.gms.measurement.internal;

final class zzb implements Runnable
{
    private final /* synthetic */ String zzaet;
    private final /* synthetic */ long zzaft;
    private final /* synthetic */ zza zzafu;
    
    zzb(final zza zzafu, final String zzaet, final long zzaft) {
        this.zzafu = zzafu;
        this.zzaet = zzaet;
        this.zzaft = zzaft;
    }
    
    @Override
    public final void run() {
        this.zzafu.zza(this.zzaet, this.zzaft);
    }
}
