package com.google.android.gms.measurement.internal;

final class zzek implements Runnable
{
    private final /* synthetic */ zzef zzasp;
    
    zzek(final zzef zzasp) {
        this.zzasp = zzasp;
    }
    
    @Override
    public final void run() {
        zzdr.zza(this.zzasp.zzasg, (zzag)null);
        this.zzasp.zzasg.zzlf();
    }
}
