package org.apache.http.message;

import org.apache.http.*;

@Deprecated
public class BasicHeaderElement implements HeaderElement
{
    public BasicHeaderElement(final String s, final String s2) {
        throw new RuntimeException("Stub!");
    }
    
    public BasicHeaderElement(final String s, final String s2, final NameValuePair[] array) {
        throw new RuntimeException("Stub!");
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean equals(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getName() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public NameValuePair getParameter(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public NameValuePair getParameterByName(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getParameterCount() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public NameValuePair[] getParameters() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getValue() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int hashCode() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
