package com.microsoft.aad.adal;

public class UsageAuthenticationException extends AuthenticationException
{
    static final long serialVersionUID = 1L;
    
    public UsageAuthenticationException(final ADALError adalError, final String s) {
        super(adalError, s);
    }
    
    public UsageAuthenticationException(final ADALError adalError, final String s, final Throwable t) {
        super(adalError, s, t);
    }
}
