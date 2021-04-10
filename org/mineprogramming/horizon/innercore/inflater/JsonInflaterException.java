package org.mineprogramming.horizon.innercore.inflater;

public class JsonInflaterException extends Exception
{
    public JsonInflaterException(final String s) {
        super(s);
    }
    
    public JsonInflaterException(final String s, final Throwable t) {
        super(s, t);
    }
}
