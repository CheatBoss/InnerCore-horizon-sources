package com.google.android.gms.tasks;

import java.util.concurrent.*;

final class zzm<TResult> implements zzq<TResult>
{
    private final Object mLock;
    private final Executor zzafk;
    private OnSuccessListener<? super TResult> zzafw;
    
    public zzm(final Executor zzafk, final OnSuccessListener<? super TResult> zzafw) {
        this.mLock = new Object();
        this.zzafk = zzafk;
        this.zzafw = zzafw;
    }
    
    @Override
    public final void onComplete(final Task<TResult> task) {
        if (task.isSuccessful()) {
            synchronized (this.mLock) {
                if (this.zzafw == null) {
                    return;
                }
                // monitorexit(this.mLock)
                this.zzafk.execute(new zzn(this, task));
            }
        }
    }
}
