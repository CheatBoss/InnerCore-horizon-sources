package com.google.android.gms.tasks;

final class zzl implements Runnable
{
    private final /* synthetic */ Task zzafn;
    private final /* synthetic */ zzk zzafv;
    
    zzl(final zzk zzafv, final Task zzafn) {
        this.zzafv = zzafv;
        this.zzafn = zzafn;
    }
    
    @Override
    public final void run() {
        synchronized (this.zzafv.mLock) {
            if (this.zzafv.zzafu != null) {
                this.zzafv.zzafu.onFailure(this.zzafn.getException());
            }
        }
    }
}
