package org.apache.http.cookie;

import org.apache.http.params.*;
import java.util.*;

@Deprecated
public final class CookieSpecRegistry
{
    public CookieSpecRegistry() {
        throw new RuntimeException("Stub!");
    }
    
    public CookieSpec getCookieSpec(final String s) throws IllegalStateException {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public CookieSpec getCookieSpec(final String s, final HttpParams httpParams) throws IllegalStateException {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public List<String> getSpecNames() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void register(final String s, final CookieSpecFactory cookieSpecFactory) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setItems(final Map<String, CookieSpecFactory> map) {
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
