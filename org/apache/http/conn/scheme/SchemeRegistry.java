package org.apache.http.conn.scheme;

import org.apache.http.*;
import java.util.*;

@Deprecated
public final class SchemeRegistry
{
    public SchemeRegistry() {
        throw new RuntimeException("Stub!");
    }
    
    public final Scheme get(final String s) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final Scheme getScheme(final String s) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final Scheme getScheme(final HttpHost httpHost) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final List<String> getSchemeNames() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final Scheme register(final Scheme scheme) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void setItems(final Map<String, Scheme> map) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public final Scheme unregister(final String s) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
}
