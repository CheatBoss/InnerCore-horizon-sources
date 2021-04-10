package org.apache.http.protocol;

@Deprecated
public final class DefaultedHttpContext implements HttpContext
{
    public DefaultedHttpContext(final HttpContext httpContext, final HttpContext httpContext2) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object getAttribute(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public HttpContext getDefaults() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object removeAttribute(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setAttribute(final String s, final Object o) {
        throw new RuntimeException("Stub!");
    }
}
