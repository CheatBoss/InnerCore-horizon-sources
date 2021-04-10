package org.apache.http.client.methods;

import org.apache.http.*;

@Deprecated
public abstract class HttpEntityEnclosingRequestBase extends HttpRequestBase implements HttpEntityEnclosingRequest
{
    public HttpEntityEnclosingRequestBase() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
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
