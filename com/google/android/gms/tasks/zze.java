package com.google.android.gms.tasks;

import java.util.concurrent.*;

final class zze<TResult, TContinuationResult> implements OnCanceledListener, OnFailureListener, OnSuccessListener<TContinuationResult>, zzq<TResult>
{
    private final Executor zzafk;
    private final Continuation<TResult, Task<TContinuationResult>> zzafl;
    private final zzu<TContinuationResult> zzafm;
    
    public zze(final Executor zzafk, final Continuation<TResult, Task<TContinuationResult>> zzafl, final zzu<TContinuationResult> zzafm) {
        this.zzafk = zzafk;
        this.zzafl = zzafl;
        this.zzafm = zzafm;
    }
    
    @Override
    public final void onCanceled() {
        this.zzafm.zzdp();
    }
    
    @Override
    public final void onComplete(final Task<TResult> task) {
        this.zzafk.execute(new zzf(this, task));
    }
    
    @Override
    public final void onFailure(final Exception exception) {
        this.zzafm.setException(exception);
    }
    
    @Override
    public final void onSuccess(final TContinuationResult result) {
        this.zzafm.setResult(result);
    }
}
