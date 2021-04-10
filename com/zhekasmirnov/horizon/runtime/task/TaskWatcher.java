package com.zhekasmirnov.horizon.runtime.task;

public class TaskWatcher
{
    private final Task task;
    private final Callback callback;
    private boolean isComplete;
    
    TaskWatcher(final Task task, final Callback callback) {
        this.isComplete = false;
        this.task = task;
        this.callback = callback;
    }
    
    void onComplete() {
        this.isComplete = true;
        if (this.callback != null) {
            this.callback.complete(this.task);
        }
    }
    
    void onError(final Throwable error) {
        if (this.callback == null || !this.callback.error(this.task, error)) {
            throw new RuntimeException("Exception in thread " + Thread.currentThread() + " while executing task " + this.task, error);
        }
    }
    
    public boolean isComplete() {
        return this.isComplete;
    }
    
    public abstract static class Callback
    {
        public abstract void complete(final Task p0);
        
        public boolean error(final Task task, final Throwable error) {
            return false;
        }
    }
}
