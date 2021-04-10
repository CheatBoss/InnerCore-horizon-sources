package com.google.android.gms.measurement.internal;

final class zzw implements Runnable
{
    private final /* synthetic */ zzcq zzahx;
    private final /* synthetic */ zzv zzahy;
    
    zzw(final zzv zzahy, final zzcq zzahx) {
        this.zzahy = zzahy;
        this.zzahx = zzahx;
    }
    
    @Override
    public final void run() {
        this.zzahx.zzgr();
        if (zzk.isMainThread()) {
            this.zzahx.zzgn().zzc(this);
            return;
        }
        final boolean zzej = this.zzahy.zzej();
        zzv.zza(this.zzahy, 0L);
        if (zzej) {
            this.zzahy.run();
        }
    }
}
