package org.apache.http;

@Deprecated
public interface HttpEntityEnclosingRequest extends HttpRequest
{
    boolean expectContinue();
    
    HttpEntity getEntity();
    
    void setEntity(final HttpEntity p0);
}
