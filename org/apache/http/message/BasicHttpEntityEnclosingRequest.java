package org.apache.http.message;

import org.apache.http.*;

@Deprecated
public class BasicHttpEntityEnclosingRequest extends BasicHttpRequest implements HttpEntityEnclosingRequest
{
    public BasicHttpEntityEnclosingRequest(final String s, final String s2) {
        super((RequestLine)null);
        throw new RuntimeException("Stub!");
    }
    
    public BasicHttpEntityEnclosingRequest(final String s, final String s2, final ProtocolVersion protocolVersion) {
        super((RequestLine)null);
        throw new RuntimeException("Stub!");
    }
    
    public BasicHttpEntityEnclosingRequest(final RequestLine requestLine) {
        super((RequestLine)null);
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean expectContinue() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpEntity getEntity() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setEntity(final HttpEntity httpEntity) {
        throw new RuntimeException("Stub!");
    }
}
