package org.apache.http.impl.client;

import org.apache.http.*;

@Deprecated
public class EntityEnclosingRequestWrapper extends RequestWrapper implements HttpEntityEnclosingRequest
{
    public EntityEnclosingRequestWrapper(final HttpEntityEnclosingRequest httpEntityEnclosingRequest) throws ProtocolException {
        super((HttpRequest)null);
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
    public boolean isRepeatable() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setEntity(final HttpEntity httpEntity) {
        throw new RuntimeException("Stub!");
    }
}
