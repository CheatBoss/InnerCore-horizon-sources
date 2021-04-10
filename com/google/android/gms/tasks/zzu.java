package com.google.android.gms.tasks;

import com.google.android.gms.common.internal.*;
import java.util.concurrent.*;

final class zzu<TResult> extends Task<TResult>
{
    private final Object mLock;
    private final zzr<TResult> zzage;
    private boolean zzagf;
    private TResult zzagg;
    private Exception zzagh;
    private volatile boolean zzfi;
    
    zzu() {
        this.mLock = new Object();
        this.zzage = new zzr<TResult>();
    }
    
    private final void zzdq() {
        Preconditions.checkState(this.zzagf, "Task is not yet complete");
    }
    
    private final void zzdr() {
        Preconditions.checkState(this.zzagf ^ true, "Task is already complete");
    }
    
    private final void zzds() {
        if (!this.zzfi) {
            return;
        }
        throw new CancellationException("Task is already canceled.");
    }
    
    private final void zzdt() {
        synchronized (this.mLock) {
            if (!this.zzagf) {
                return;
            }
            // monitorexit(this.mLock)
            this.zzage.zza(this);
        }
    }
    
    @Override
    public final Task<TResult> addOnCanceledListener(final Executor executor, final OnCanceledListener onCanceledListener) {
        this.zzage.zza(new zzg<TResult>(executor, onCanceledListener));
        this.zzdt();
        return this;
    }
    
    @Override
    public final Task<TResult> addOnCompleteListener(final OnCompleteListener<TResult> onCompleteListener) {
        return this.addOnCompleteListener(TaskExecutors.MAIN_THREAD, onCompleteListener);
    }
    
    @Override
    public final Task<TResult> addOnCompleteListener(final Executor executor, final OnCompleteListener<TResult> onCompleteListener) {
        this.zzage.zza(new zzi<TResult>(executor, onCompleteListener));
        this.zzdt();
        return this;
    }
    
    @Override
    public final Task<TResult> addOnFailureListener(final Executor executor, final OnFailureListener onFailureListener) {
        this.zzage.zza(new zzk<TResult>(executor, onFailureListener));
        this.zzdt();
        return this;
    }
    
    @Override
    public final Task<TResult> addOnSuccessListener(final Executor executor, final OnSuccessListener<? super TResult> onSuccessListener) {
        this.zzage.zza(new zzm<TResult>(executor, onSuccessListener));
        this.zzdt();
        return this;
    }
    
    @Override
    public final <TContinuationResult> Task<TContinuationResult> continueWith(final Executor executor, final Continuation<TResult, TContinuationResult> continuation) {
        final zzu<Object> zzu = new zzu<Object>();
        this.zzage.zza(new zzc<TResult, Object>(executor, (Continuation<Object, Object>)continuation, zzu));
        this.zzdt();
        return (Task<TContinuationResult>)zzu;
    }
    
    @Override
    public final <TContinuationResult> Task<TContinuationResult> continueWithTask(final Executor executor, final Continuation<TResult, Task<TContinuationResult>> continuation) {
        final zzu<Object> zzu = new zzu<Object>();
        this.zzage.zza(new zze<TResult, Object>(executor, (Continuation<Object, Task<Object>>)continuation, zzu));
        this.zzdt();
        return (Task<TContinuationResult>)zzu;
    }
    
    @Override
    public final Exception getException() {
        synchronized (this.mLock) {
            return this.zzagh;
        }
    }
    
    @Override
    public final TResult getResult() {
        synchronized (this.mLock) {
            this.zzdq();
            this.zzds();
            if (this.zzagh == null) {
                return this.zzagg;
            }
            throw new RuntimeExecutionException(this.zzagh);
        }
    }
    
    @Override
    public final <X extends Throwable> TResult getResult(final Class<X> clazz) throws X, Throwable {
        synchronized (this.mLock) {
            this.zzdq();
            this.zzds();
            if (clazz.isInstance(this.zzagh)) {
                throw clazz.cast(this.zzagh);
            }
            if (this.zzagh == null) {
                return this.zzagg;
            }
            throw new RuntimeExecutionException(this.zzagh);
        }
    }
    
    @Override
    public final boolean isCanceled() {
        return this.zzfi;
    }
    
    @Override
    public final boolean isComplete() {
        synchronized (this.mLock) {
            return this.zzagf;
        }
    }
    
    @Override
    public final boolean isSuccessful() {
        while (true) {
            synchronized (this.mLock) {
                if (this.zzagf && !this.zzfi && this.zzagh == null) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public final void setException(final Exception zzagh) {
        Preconditions.checkNotNull(zzagh, "Exception must not be null");
        synchronized (this.mLock) {
            this.zzdr();
            this.zzagf = true;
            this.zzagh = zzagh;
            // monitorexit(this.mLock)
            this.zzage.zza(this);
        }
    }
    
    public final void setResult(final TResult zzagg) {
        synchronized (this.mLock) {
            this.zzdr();
            this.zzagf = true;
            this.zzagg = zzagg;
            // monitorexit(this.mLock)
            this.zzage.zza(this);
        }
    }
    
    public final boolean trySetException(final Exception zzagh) {
        Preconditions.checkNotNull(zzagh, "Exception must not be null");
        synchronized (this.mLock) {
            if (this.zzagf) {
                return false;
            }
            this.zzagf = true;
            this.zzagh = zzagh;
            // monitorexit(this.mLock)
            this.zzage.zza(this);
            return true;
        }
    }
    
    public final boolean trySetResult(final TResult zzagg) {
        synchronized (this.mLock) {
            if (this.zzagf) {
                return false;
            }
            this.zzagf = true;
            this.zzagg = zzagg;
            // monitorexit(this.mLock)
            this.zzage.zza(this);
            return true;
        }
    }
    
    public final boolean zzdp() {
        synchronized (this.mLock) {
            if (this.zzagf) {
                return false;
            }
            this.zzagf = true;
            this.zzfi = true;
            // monitorexit(this.mLock)
            this.zzage.zza(this);
            return true;
        }
    }
}
