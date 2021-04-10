package com.google.android.gms.measurement.internal;

final class zzcv implements Runnable
{
    private final /* synthetic */ String val$name;
    private final /* synthetic */ String zzaeh;
    private final /* synthetic */ zzcs zzarc;
    private final /* synthetic */ long zzard;
    private final /* synthetic */ Object zzarh;
    
    zzcv(final zzcs zzarc, final String zzaeh, final String val$name, final Object zzarh, final long zzard) {
        this.zzarc = zzarc;
        this.zzaeh = zzaeh;
        this.val$name = val$name;
        this.zzarh = zzarh;
        this.zzard = zzard;
    }
    
    @Override
    public final void run() {
        this.zzarc.zza(this.zzaeh, this.val$name, this.zzarh, this.zzard);
    }
}
