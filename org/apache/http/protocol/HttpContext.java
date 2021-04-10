package org.apache.http.protocol;

@Deprecated
public interface HttpContext
{
    public static final String RESERVED_PREFIX = "http.";
    
    Object getAttribute(final String p0);
    
    Object removeAttribute(final String p0);
    
    void setAttribute(final String p0, final Object p1);
}
