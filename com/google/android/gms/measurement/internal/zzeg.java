package com.google.android.gms.measurement.internal;

final class zzeg implements Runnable
{
    private final /* synthetic */ zzag zzaso;
    private final /* synthetic */ zzef zzasp;
    
    zzeg(final zzef zzasp, final zzag zzaso) {
        this.zzasp = zzasp;
        this.zzaso = zzaso;
    }
    
    @Override
    public final void run() {
        synchronized (this.zzasp) {
            zzef.zza(this.zzasp, false);
            if (!this.zzasp.zzasg.isConnected()) {
                this.zzasp.zzasg.zzgo().zzjl().zzbx("Connected to service");
                this.zzasp.zzasg.zza(this.zzaso);
            }
        }
    }
}
