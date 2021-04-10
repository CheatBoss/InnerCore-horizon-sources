package com.google.android.gms.tasks;

import java.util.concurrent.*;

public abstract class Task<TResult>
{
    public Task<TResult> addOnCanceledListener(final Executor executor, final OnCanceledListener onCanceledListener) {
        throw new UnsupportedOperationException("addOnCanceledListener is not implemented");
    }
    
    public Task<TResult> addOnCompleteListener(final OnCompleteListener<TResult> onCompleteListener) {
        throw new UnsupportedOperationException("addOnCompleteListener is not implemented");
    }
    
    public Task<TResult> addOnCompleteListener(final Executor executor, final OnCompleteListener<TResult> onCompleteListener) {
        throw new UnsupportedOperationException("addOnCompleteListener is not implemented");
    }
    
    public abstract Task<TResult> addOnFailureListener(final Executor p0, final OnFailureListener p1);
    
    public abstract Task<TResult> addOnSuccessListener(final Executor p0, final OnSuccessListener<? super TResult> p1);
    
    public <TContinuationResult> Task<TContinuationResult> continueWith(final Executor executor, final Continuation<TResult, TContinuationResult> continuation) {
        throw new UnsupportedOperationException("continueWith is not implemented");
    }
    
    public <TContinuationResult> Task<TContinuationResult> continueWithTask(final Executor executor, final Continuation<TResult, Task<TContinuationResult>> continuation) {
        throw new UnsupportedOperationException("continueWithTask is not implemented");
    }
    
    public abstract Exception getException();
    
    public abstract TResult getResult();
    
    public abstract <X extends Throwable> TResult getResult(final Class<X> p0) throws X, Throwable;
    
    public abstract boolean isCanceled();
    
    public abstract boolean isComplete();
    
    public abstract boolean isSuccessful();
}
