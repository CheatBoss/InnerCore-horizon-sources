package org.apache.http.client.methods;

import org.apache.http.message.*;
import org.apache.http.*;
import java.net.*;
import java.io.*;
import org.apache.http.conn.*;

@Deprecated
public abstract class HttpRequestBase extends AbstractHttpMessage implements HttpUriRequest, AbortableHttpRequest
{
    public HttpRequestBase() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void abort() {
        throw new RuntimeException("Stub!");
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public abstract String getMethod();
    
    @Override
    public ProtocolVersion getProtocolVersion() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public RequestLine getRequestLine() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public URI getURI() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isAborted() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setConnectionRequest(final ClientConnectionRequest clientConnectionRequest) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setReleaseTrigger(final ConnectionReleaseTrigger connectionReleaseTrigger) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public void setURI(final URI uri) {
        throw new RuntimeException("Stub!");
    }
}
