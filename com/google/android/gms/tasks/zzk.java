package com.google.android.gms.tasks;

import java.util.concurrent.*;

final class zzk<TResult> implements zzq<TResult>
{
    private final Object mLock;
    private final Executor zzafk;
    private OnFailureListener zzafu;
    
    public zzk(final Executor zzafk, final OnFailureListener zzafu) {
        this.mLock = new Object();
        this.zzafk = zzafk;
        this.zzafu = zzafu;
    }
    
    @Override
    public final void onComplete(final Task<TResult> task) {
        if (!task.isSuccessful() && !task.isCanceled()) {
            synchronized (this.mLock) {
                if (this.zzafu == null) {
                    return;
                }
                // monitorexit(this.mLock)
                this.zzafk.execute(new zzl(this, task));
            }
        }
    }
}
