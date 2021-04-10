package org.apache.http;

@Deprecated
public interface HttpRequestFactory
{
    HttpRequest newHttpRequest(final String p0, final String p1) throws MethodNotSupportedException;
    
    HttpRequest newHttpRequest(final RequestLine p0) throws MethodNotSupportedException;
}
