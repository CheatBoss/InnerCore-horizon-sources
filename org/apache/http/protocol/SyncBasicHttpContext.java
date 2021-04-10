package org.apache.http.protocol;

@Deprecated
public class SyncBasicHttpContext extends BasicHttpContext
{
    public SyncBasicHttpContext(final HttpContext httpContext) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object getAttribute(final String s) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    @Override
    public Object removeAttribute(final String s) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    @Override
    public void setAttribute(final String s, final Object o) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
}
