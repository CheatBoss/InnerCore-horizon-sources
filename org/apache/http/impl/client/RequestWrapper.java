package org.apache.http.impl.client;

import org.apache.http.message.*;
import org.apache.http.client.methods.*;
import org.apache.http.*;
import java.net.*;

@Deprecated
public class RequestWrapper extends AbstractHttpMessage implements HttpUriRequest
{
    public RequestWrapper(final HttpRequest httpRequest) throws ProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void abort() throws UnsupportedOperationException {
        throw new RuntimeException("Stub!");
    }
    
    public int getExecCount() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getMethod() {
        throw new RuntimeException("Stub!");
    }
    
    public HttpRequest getOriginal() {
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
    
    @Override
    public URI getURI() {
        throw new RuntimeException("Stub!");
    }
    
    public void incrementExecCount() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isAborted() {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isRepeatable() {
        throw new RuntimeException("Stub!");
    }
    
    public void resetHeaders() {
        throw new RuntimeException("Stub!");
    }
    
    public void setMethod(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setProtocolVersion(final ProtocolVersion protocolVersion) {
        throw new RuntimeException("Stub!");
    }
    
    public void setURI(final URI uri) {
        throw new RuntimeException("Stub!");
    }
}
