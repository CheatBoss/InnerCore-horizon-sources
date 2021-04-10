package com.google.android.gms.measurement.internal;

final class zzei implements Runnable
{
    private final /* synthetic */ zzef zzasp;
    private final /* synthetic */ zzag zzasq;
    
    zzei(final zzef zzasp, final zzag zzasq) {
        this.zzasp = zzasp;
        this.zzasq = zzasq;
    }
    
    @Override
    public final void run() {
        synchronized (this.zzasp) {
            zzef.zza(this.zzasp, false);
            if (!this.zzasp.zzasg.isConnected()) {
                this.zzasp.zzasg.zzgo().zzjk().zzbx("Connected to remote service");
                this.zzasp.zzasg.zza(this.zzasq);
            }
        }
    }
}
