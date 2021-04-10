package com.google.android.gms.tasks;

import java.util.concurrent.*;

final class zzg<TResult> implements zzq<TResult>
{
    private final Object mLock;
    private final Executor zzafk;
    private OnCanceledListener zzafq;
    
    public zzg(final Executor zzafk, final OnCanceledListener zzafq) {
        this.mLock = new Object();
        this.zzafk = zzafk;
        this.zzafq = zzafq;
    }
    
    @Override
    public final void onComplete(final Task task) {
        if (task.isCanceled()) {
            synchronized (this.mLock) {
                if (this.zzafq == null) {
                    return;
                }
                // monitorexit(this.mLock)
                this.zzafk.execute(new zzh(this));
            }
        }
    }
}
