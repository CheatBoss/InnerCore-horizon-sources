package org.apache.http.impl.auth;

import org.apache.http.*;
import org.apache.http.auth.*;

@Deprecated
public class DigestScheme extends RFC2617Scheme
{
    public DigestScheme() {
        throw new RuntimeException("Stub!");
    }
    
    public static String createCnonce() {
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
    
    public void overrideParamter(final String s, final String s2) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void processChallenge(final Header header) throws MalformedChallengeException {
        throw new RuntimeException("Stub!");
    }
}
