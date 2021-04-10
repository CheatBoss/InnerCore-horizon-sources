package com.microsoft.aad.adal;

class AuthenticationServerProtocolException extends AuthenticationException
{
    static final long serialVersionUID = 1L;
    
    public AuthenticationServerProtocolException(final String s) {
        super(ADALError.DEVICE_CHALLENGE_FAILURE, s);
    }
}
