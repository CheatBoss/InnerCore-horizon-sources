package org.apache.http.impl.auth;

import org.apache.http.*;
import org.apache.http.util.*;
import org.apache.http.auth.*;

@Deprecated
public class NTLMScheme extends AuthSchemeBase
{
    public NTLMScheme(final NTLMEngine ntlmEngine) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Header authenticate(final Credentials credentials, final HttpRequest httpRequest) throws AuthenticationException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getParameter(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getRealm() {
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
    protected void parseChallenge(final CharArrayBuffer charArrayBuffer, final int n, final int n2) throws MalformedChallengeException {
        throw new RuntimeException("Stub!");
    }
}
