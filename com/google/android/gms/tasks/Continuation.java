package com.google.android.gms.tasks;

public interface Continuation<TResult, TContinuationResult>
{
    TContinuationResult then(final Task<TResult> p0) throws Exception;
}
