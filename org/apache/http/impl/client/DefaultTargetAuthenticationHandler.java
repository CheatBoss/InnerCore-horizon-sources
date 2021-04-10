package org.apache.http.impl.client;

import org.apache.http.protocol.*;
import java.util.*;
import org.apache.http.*;
import org.apache.http.auth.*;

@Deprecated
public class DefaultTargetAuthenticationHandler extends AbstractAuthenticationHandler
{
    public DefaultTargetAuthenticationHandler() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Map<String, Header> getChallenges(final HttpResponse httpResponse, final HttpContext httpContext) throws MalformedChallengeException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isAuthenticationRequested(final HttpResponse httpResponse, final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
}
