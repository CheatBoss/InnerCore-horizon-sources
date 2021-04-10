package com.appboy.models.response;

public class ResponseError
{
    private final String a;
    
    public ResponseError(final String a) {
        this.a = a;
    }
    
    public String getMessage() {
        return this.a;
    }
}
