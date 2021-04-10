package org.apache.http.impl.client;

import org.apache.http.client.*;
import java.util.*;
import org.apache.http.*;
import org.apache.http.protocol.*;
import org.apache.http.auth.*;

@Deprecated
public abstract class AbstractAuthenticationHandler implements AuthenticationHandler
{
    public AbstractAuthenticationHandler() {
        throw new RuntimeException("Stub!");
    }
    
    protected List<String> getAuthPreferences() {
        throw new RuntimeException("Stub!");
    }
    
    protected Map<String, Header> parseChallenges(final Header[] array) throws MalformedChallengeException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public AuthScheme selectScheme(final Map<String, Header> map, final HttpResponse httpResponse, final HttpContext httpContext) throws AuthenticationException {
        throw new RuntimeException("Stub!");
    }
}
