package com.google.android.gms.tasks;

import java.util.concurrent.*;

final class zzi<TResult> implements zzq<TResult>
{
    private final Object mLock;
    private final Executor zzafk;
    private OnCompleteListener<TResult> zzafs;
    
    public zzi(final Executor zzafk, final OnCompleteListener<TResult> zzafs) {
        this.mLock = new Object();
        this.zzafk = zzafk;
        this.zzafs = zzafs;
    }
    
    @Override
    public final void onComplete(final Task<TResult> task) {
        synchronized (this.mLock) {
            if (this.zzafs == null) {
                return;
            }
            // monitorexit(this.mLock)
            this.zzafk.execute(new zzj(this, task));
        }
    }
}
