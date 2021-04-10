package com.microsoft.xbox.idp.toolkit;

public abstract class LoaderResult<T>
{
    private final T data;
    private final HttpError error;
    private final Exception exception;
    
    protected LoaderResult(final Exception exception) {
        this.data = null;
        this.error = null;
        this.exception = exception;
    }
    
    protected LoaderResult(final T data, final HttpError error) {
        this.data = data;
        this.error = error;
        this.exception = null;
    }
    
    public T getData() {
        return this.data;
    }
    
    public HttpError getError() {
        return this.error;
    }
    
    public Exception getException() {
        return this.exception;
    }
    
    public boolean hasData() {
        return this.data != null;
    }
    
    public boolean hasError() {
        return this.error != null;
    }
    
    public boolean hasException() {
        return this.exception != null;
    }
    
    public abstract boolean isReleased();
    
    public abstract void release();
}
