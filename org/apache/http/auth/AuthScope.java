package org.apache.http.auth;

@Deprecated
public class AuthScope
{
    public static final AuthScope ANY;
    public static final String ANY_HOST;
    public static final int ANY_PORT = -1;
    public static final String ANY_REALM;
    public static final String ANY_SCHEME;
    
    public AuthScope(final String s, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public AuthScope(final String s, final int n, final String s2) {
        throw new RuntimeException("Stub!");
    }
    
    public AuthScope(final String s, final int n, final String s2, final String s3) {
        throw new RuntimeException("Stub!");
    }
    
    public AuthScope(final AuthScope authScope) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean equals(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public String getHost() {
        throw new RuntimeException("Stub!");
    }
    
    public int getPort() {
        throw new RuntimeException("Stub!");
    }
    
    public String getRealm() {
        throw new RuntimeException("Stub!");
    }
    
    public String getScheme() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int hashCode() {
        throw new RuntimeException("Stub!");
    }
    
    public int match(final AuthScope authScope) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
