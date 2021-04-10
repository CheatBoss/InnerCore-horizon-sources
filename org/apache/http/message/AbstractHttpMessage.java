package org.apache.http.message;

import org.apache.http.params.*;
import org.apache.http.*;

@Deprecated
public abstract class AbstractHttpMessage implements HttpMessage
{
    protected HeaderGroup headergroup;
    protected HttpParams params;
    
    protected AbstractHttpMessage() {
        throw new RuntimeException("Stub!");
    }
    
    protected AbstractHttpMessage(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void addHeader(final String s, final String s2) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void addHeader(final Header header) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean containsHeader(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Header[] getAllHeaders() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Header getFirstHeader(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Header[] getHeaders(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Header getLastHeader(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpParams getParams() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HeaderIterator headerIterator() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HeaderIterator headerIterator(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void removeHeader(final Header header) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void removeHeaders(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setHeader(final String s, final String s2) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setHeader(final Header header) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setHeaders(final Header[] array) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setParams(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
}
