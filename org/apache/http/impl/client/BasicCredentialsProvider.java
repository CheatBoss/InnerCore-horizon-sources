package org.apache.http.impl.client;

import org.apache.http.client.*;
import org.apache.http.auth.*;

@Deprecated
public class BasicCredentialsProvider implements CredentialsProvider
{
    public BasicCredentialsProvider() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void clear() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    @Override
    public Credentials getCredentials(final AuthScope authScope) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    @Override
    public void setCredentials(final AuthScope authScope, final Credentials credentials) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
