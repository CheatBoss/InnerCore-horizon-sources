package org.apache.http.auth;

import org.apache.http.*;

@Deprecated
public interface AuthScheme
{
    Header authenticate(final Credentials p0, final HttpRequest p1) throws AuthenticationException;
    
    String getParameter(final String p0);
    
    String getRealm();
    
    String getSchemeName();
    
    boolean isComplete();
    
    boolean isConnectionBased();
    
    void processChallenge(final Header p0) throws MalformedChallengeException;
}
