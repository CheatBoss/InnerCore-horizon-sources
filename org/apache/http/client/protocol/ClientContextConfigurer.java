package org.apache.http.client.protocol;

import org.apache.http.protocol.*;
import java.util.*;
import org.apache.http.auth.*;
import org.apache.http.cookie.*;
import org.apache.http.client.*;

@Deprecated
public class ClientContextConfigurer implements ClientContext
{
    public ClientContextConfigurer(final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
    
    public void setAuthSchemePref(final List<String> list) {
        throw new RuntimeException("Stub!");
    }
    
    public void setAuthSchemeRegistry(final AuthSchemeRegistry authSchemeRegistry) {
        throw new RuntimeException("Stub!");
    }
    
    public void setCookieSpecRegistry(final CookieSpecRegistry cookieSpecRegistry) {
        throw new RuntimeException("Stub!");
    }
    
    public void setCookieStore(final CookieStore cookieStore) {
        throw new RuntimeException("Stub!");
    }
    
    public void setCredentialsProvider(final CredentialsProvider credentialsProvider) {
        throw new RuntimeException("Stub!");
    }
}
