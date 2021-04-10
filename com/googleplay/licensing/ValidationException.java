package com.googleplay.licensing;

public class ValidationException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public ValidationException() {
    }
    
    public ValidationException(final String s) {
        super(s);
    }
}
