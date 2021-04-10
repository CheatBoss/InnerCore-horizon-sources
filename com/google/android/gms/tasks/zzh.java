package com.google.android.gms.tasks;

final class zzh implements Runnable
{
    private final /* synthetic */ zzg zzafr;
    
    zzh(final zzg zzafr) {
        this.zzafr = zzafr;
    }
    
    @Override
    public final void run() {
        synchronized (this.zzafr.mLock) {
            if (this.zzafr.zzafq != null) {
                this.zzafr.zzafq.onCanceled();
            }
        }
    }
}
