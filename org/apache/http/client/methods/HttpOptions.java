package org.apache.http.client.methods;

import java.net.*;
import org.apache.http.*;
import java.util.*;

@Deprecated
public class HttpOptions extends HttpRequestBase
{
    public static final String METHOD_NAME = "OPTIONS";
    
    public HttpOptions() {
        throw new RuntimeException("Stub!");
    }
    
    public HttpOptions(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public HttpOptions(final URI uri) {
        throw new RuntimeException("Stub!");
    }
    
    public Set<String> getAllowedMethods(final HttpResponse httpResponse) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getMethod() {
        throw new RuntimeException("Stub!");
    }
}
