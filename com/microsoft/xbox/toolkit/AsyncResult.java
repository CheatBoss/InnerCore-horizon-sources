package com.microsoft.xbox.toolkit;

public class AsyncResult<T>
{
    private final XLEException exception;
    private final T result;
    private final Object sender;
    private AsyncActionStatus status;
    
    public AsyncResult(final T t, final Object o, final XLEException ex) {
        AsyncActionStatus asyncActionStatus;
        if (ex == null) {
            asyncActionStatus = AsyncActionStatus.SUCCESS;
        }
        else {
            asyncActionStatus = AsyncActionStatus.FAIL;
        }
        this(t, o, ex, asyncActionStatus);
    }
    
    public AsyncResult(final T result, final Object sender, final XLEException exception, final AsyncActionStatus status) {
        this.sender = sender;
        this.exception = exception;
        this.result = result;
        this.status = status;
    }
    
    public XLEException getException() {
        return this.exception;
    }
    
    public T getResult() {
        return this.result;
    }
    
    public Object getSender() {
        return this.sender;
    }
    
    public AsyncActionStatus getStatus() {
        return this.status;
    }
}
