package org.apache.http.message;

import org.apache.http.*;

@Deprecated
public class BasicHttpRequest extends AbstractHttpMessage implements HttpRequest
{
    public BasicHttpRequest(final String s, final String s2) {
        throw new RuntimeException("Stub!");
    }
    
    public BasicHttpRequest(final String s, final String s2, final ProtocolVersion protocolVersion) {
        throw new RuntimeException("Stub!");
    }
    
    public BasicHttpRequest(final RequestLine requestLine) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public ProtocolVersion getProtocolVersion() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public RequestLine getRequestLine() {
        throw new RuntimeException("Stub!");
    }
}
