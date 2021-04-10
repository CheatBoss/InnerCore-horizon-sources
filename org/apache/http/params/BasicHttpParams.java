package org.apache.http.params;

import java.io.*;

@Deprecated
public final class BasicHttpParams extends AbstractHttpParams implements Serializable
{
    public BasicHttpParams() {
        throw new RuntimeException("Stub!");
    }
    
    public void clear() {
        throw new RuntimeException("Stub!");
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpParams copy() {
        throw new RuntimeException("Stub!");
    }
    
    protected void copyParams(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Object getParameter(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isParameterSet(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isParameterSetLocally(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean removeParameter(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpParams setParameter(final String s, final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public void setParameters(final String[] array, final Object o) {
        throw new RuntimeException("Stub!");
    }
}
