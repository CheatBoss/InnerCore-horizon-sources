package com.microsoft.aad.adal;

class ServerRespondingWithRetryableException extends AuthenticationException
{
    static final long serialVersionUID = 1L;
    
    public ServerRespondingWithRetryableException(final String s) {
        super(null, s);
    }
    
    public ServerRespondingWithRetryableException(final String s, final HttpWebResponse httpWebResponse) {
        super(null, s, httpWebResponse);
    }
    
    public ServerRespondingWithRetryableException(final String s, final Throwable t) {
        super(null, s, t);
    }
}
