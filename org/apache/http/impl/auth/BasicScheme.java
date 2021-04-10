package org.apache.http.impl.auth;

import org.apache.http.*;
import org.apache.http.auth.*;

@Deprecated
public class BasicScheme extends RFC2617Scheme
{
    public BasicScheme() {
        throw new RuntimeException("Stub!");
    }
    
    public static Header authenticate(final Credentials credentials, final String s, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Header authenticate(final Credentials credentials, final HttpRequest httpRequest) throws AuthenticationException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getSchemeName() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isComplete() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isConnectionBased() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void processChallenge(final Header header) throws MalformedChallengeException {
        throw new RuntimeException("Stub!");
    }
}
