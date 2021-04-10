package com.google.gson;

public final class JsonIOException extends JsonParseException
{
    public JsonIOException(final String s) {
        super(s);
    }
    
    public JsonIOException(final Throwable t) {
        super(t);
    }
}
