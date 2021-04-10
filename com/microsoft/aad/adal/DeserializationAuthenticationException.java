package com.microsoft.aad.adal;

class DeserializationAuthenticationException extends AuthenticationException
{
    static final long serialVersionUID = 1L;
    
    public DeserializationAuthenticationException(final String s) {
        super(ADALError.INCOMPATIBLE_BLOB_VERSION, s);
    }
}
