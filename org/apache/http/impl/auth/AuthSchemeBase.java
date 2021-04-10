package org.apache.http.impl.auth;

import org.apache.http.util.*;
import org.apache.http.auth.*;
import org.apache.http.*;

@Deprecated
public abstract class AuthSchemeBase implements AuthScheme
{
    public AuthSchemeBase() {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isProxy() {
        throw new RuntimeException("Stub!");
    }
    
    protected abstract void parseChallenge(final CharArrayBuffer p0, final int p1, final int p2) throws MalformedChallengeException;
    
    @Override
    public void processChallenge(final Header header) throws MalformedChallengeException {
        throw new RuntimeException("Stub!");
    }
}
