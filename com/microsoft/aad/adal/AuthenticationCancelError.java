package com.microsoft.aad.adal;

public class AuthenticationCancelError extends AuthenticationException
{
    static final long serialVersionUID = 1L;
    
    public AuthenticationCancelError() {
    }
    
    public AuthenticationCancelError(final String s) {
        super(ADALError.AUTH_FAILED_CANCELLED, s);
    }
}
