package org.apache.http;

@Deprecated
public final class HttpHost
{
    public static final String DEFAULT_SCHEME_NAME = "http";
    protected final String hostname;
    protected final String lcHostname;
    protected final int port;
    protected final String schemeName;
    
    public HttpHost(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public HttpHost(final String s, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public HttpHost(final String s, final int n, final String s2) {
        throw new RuntimeException("Stub!");
    }
    
    public HttpHost(final HttpHost httpHost) {
        throw new RuntimeException("Stub!");
    }
    
    public Object clone() throws CloneNotSupportedException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean equals(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public String getHostName() {
        throw new RuntimeException("Stub!");
    }
    
    public int getPort() {
        throw new RuntimeException("Stub!");
    }
    
    public String getSchemeName() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int hashCode() {
        throw new RuntimeException("Stub!");
    }
    
    public String toHostString() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
    
    public String toURI() {
        throw new RuntimeException("Stub!");
    }
}
