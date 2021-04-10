package org.apache.http.message;

import org.apache.http.*;

@Deprecated
public class BasicStatusLine implements StatusLine
{
    public BasicStatusLine(final ProtocolVersion protocolVersion, final int n, final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public ProtocolVersion getProtocolVersion() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getReasonPhrase() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getStatusCode() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
