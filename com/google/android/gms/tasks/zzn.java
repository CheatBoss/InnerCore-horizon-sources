package com.google.android.gms.tasks;

final class zzn implements Runnable
{
    private final /* synthetic */ Task zzafn;
    private final /* synthetic */ zzm zzafx;
    
    zzn(final zzm zzafx, final Task zzafn) {
        this.zzafx = zzafx;
        this.zzafn = zzafn;
    }
    
    @Override
    public final void run() {
        synchronized (this.zzafx.mLock) {
            if (this.zzafx.zzafw != null) {
                this.zzafx.zzafw.onSuccess(this.zzafn.getResult());
            }
        }
    }
}
