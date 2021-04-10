package com.google.android.gms.tasks;

public class TaskCompletionSource<TResult>
{
    private final zzu<TResult> zzafh;
    
    public TaskCompletionSource() {
        this.zzafh = new zzu<TResult>();
    }
    
    public Task<TResult> getTask() {
        return this.zzafh;
    }
    
    public void setException(final Exception exception) {
        this.zzafh.setException(exception);
    }
    
    public void setResult(final TResult result) {
        this.zzafh.setResult(result);
    }
    
    public boolean trySetException(final Exception ex) {
        return this.zzafh.trySetException(ex);
    }
    
    public boolean trySetResult(final TResult tResult) {
        return this.zzafh.trySetResult(tResult);
    }
}
