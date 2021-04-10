package org.apache.http.message;

import java.util.*;
import org.apache.http.*;

@Deprecated
public class BasicHttpResponse extends AbstractHttpMessage implements HttpResponse
{
    public BasicHttpResponse(final ProtocolVersion protocolVersion, final int n, final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public BasicHttpResponse(final StatusLine statusLine) {
        throw new RuntimeException("Stub!");
    }
    
    public BasicHttpResponse(final StatusLine statusLine, final ReasonPhraseCatalog reasonPhraseCatalog, final Locale locale) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpEntity getEntity() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Locale getLocale() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public ProtocolVersion getProtocolVersion() {
        throw new RuntimeException("Stub!");
    }
    
    protected String getReason(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public StatusLine getStatusLine() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setEntity(final HttpEntity httpEntity) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setLocale(final Locale locale) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setReasonPhrase(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setStatusCode(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setStatusLine(final ProtocolVersion protocolVersion, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setStatusLine(final ProtocolVersion protocolVersion, final int n, final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setStatusLine(final StatusLine statusLine) {
        throw new RuntimeException("Stub!");
    }
}
