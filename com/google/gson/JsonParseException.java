package com.google.gson;

public class JsonParseException extends RuntimeException
{
    public JsonParseException(final String s) {
        super(s);
    }
    
    public JsonParseException(final Throwable t) {
        super(t);
    }
}
