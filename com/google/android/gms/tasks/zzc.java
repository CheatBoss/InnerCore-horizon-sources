package com.google.android.gms.tasks;

import java.util.concurrent.*;

final class zzc<TResult, TContinuationResult> implements zzq<TResult>
{
    private final Executor zzafk;
    private final Continuation<TResult, TContinuationResult> zzafl;
    private final zzu<TContinuationResult> zzafm;
    
    public zzc(final Executor zzafk, final Continuation<TResult, TContinuationResult> zzafl, final zzu<TContinuationResult> zzafm) {
        this.zzafk = zzafk;
        this.zzafl = zzafl;
        this.zzafm = zzafm;
    }
    
    @Override
    public final void onComplete(final Task<TResult> task) {
        this.zzafk.execute(new zzd(this, task));
    }
}
