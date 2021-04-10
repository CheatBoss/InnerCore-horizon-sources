package org.apache.http.auth;

import org.apache.http.params.*;
import java.util.*;

@Deprecated
public final class AuthSchemeRegistry
{
    public AuthSchemeRegistry() {
        throw new RuntimeException("Stub!");
    }
    
    public AuthScheme getAuthScheme(final String s, final HttpParams httpParams) throws IllegalStateException {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public List<String> getSchemeNames() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void register(final String s, final AuthSchemeFactory authSchemeFactory) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setItems(final Map<String, AuthSchemeFactory> map) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void unregister(final String s) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
}
