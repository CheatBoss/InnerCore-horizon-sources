package org.apache.http;

import org.apache.http.params.*;

@Deprecated
public interface HttpMessage
{
    void addHeader(final String p0, final String p1);
    
    void addHeader(final Header p0);
    
    boolean containsHeader(final String p0);
    
    Header[] getAllHeaders();
    
    Header getFirstHeader(final String p0);
    
    Header[] getHeaders(final String p0);
    
    Header getLastHeader(final String p0);
    
    HttpParams getParams();
    
    ProtocolVersion getProtocolVersion();
    
    HeaderIterator headerIterator();
    
    HeaderIterator headerIterator(final String p0);
    
    void removeHeader(final Header p0);
    
    void removeHeaders(final String p0);
    
    void setHeader(final String p0, final String p1);
    
    void setHeader(final Header p0);
    
    void setHeaders(final Header[] p0);
    
    void setParams(final HttpParams p0);
}
