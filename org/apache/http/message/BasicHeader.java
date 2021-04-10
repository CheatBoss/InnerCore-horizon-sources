package org.apache.http.message;

import org.apache.http.*;

@Deprecated
public class BasicHeader implements Header
{
    public BasicHeader(final String s, final String s2) {
        throw new RuntimeException("Stub!");
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HeaderElement[] getElements() throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getName() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getValue() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
