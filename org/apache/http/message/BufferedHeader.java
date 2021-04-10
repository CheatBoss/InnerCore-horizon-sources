package org.apache.http.message;

import org.apache.http.util.*;
import org.apache.http.*;

@Deprecated
public class BufferedHeader implements FormattedHeader
{
    public BufferedHeader(final CharArrayBuffer charArrayBuffer) throws ParseException {
        throw new RuntimeException("Stub!");
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public CharArrayBuffer getBuffer() {
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
    public int getValuePos() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
