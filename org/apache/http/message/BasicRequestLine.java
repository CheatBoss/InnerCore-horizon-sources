package org.apache.http.message;

import org.apache.http.*;

@Deprecated
public class BasicRequestLine implements RequestLine
{
    public BasicRequestLine(final String s, final String s2, final ProtocolVersion protocolVersion) {
        throw new RuntimeException("Stub!");
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getMethod() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public ProtocolVersion getProtocolVersion() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getUri() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
