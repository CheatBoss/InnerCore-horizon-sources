package com.cedarsoftware.util.io;

public class JsonIoException extends RuntimeException
{
    public JsonIoException() {
    }
    
    public JsonIoException(final String s) {
        super(s);
    }
    
    public JsonIoException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public JsonIoException(final Throwable t) {
        super(t);
    }
}
