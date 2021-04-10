package com.microsoft.aad.adal;

public class ResourceAuthenticationChallengeException extends AuthenticationException
{
    static final long serialVersionUID = 1L;
    
    public ResourceAuthenticationChallengeException(final String s) {
        super(ADALError.RESOURCE_AUTHENTICATION_CHALLENGE_FAILURE, s);
    }
}
