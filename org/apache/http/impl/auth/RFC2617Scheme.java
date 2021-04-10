package org.apache.http.impl.auth;

import java.util.*;
import org.apache.http.util.*;
import org.apache.http.auth.*;

@Deprecated
public abstract class RFC2617Scheme extends AuthSchemeBase
{
    public RFC2617Scheme() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getParameter(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    protected Map<String, String> getParameters() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getRealm() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected void parseChallenge(final CharArrayBuffer charArrayBuffer, final int n, final int n2) throws MalformedChallengeException {
        throw new RuntimeException("Stub!");
    }
}
