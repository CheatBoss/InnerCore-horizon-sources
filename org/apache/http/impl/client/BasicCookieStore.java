package org.apache.http.impl.client;

import org.apache.http.client.*;
import org.apache.http.cookie.*;
import java.util.*;

@Deprecated
public class BasicCookieStore implements CookieStore
{
    public BasicCookieStore() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void addCookie(final Cookie cookie) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void addCookies(final Cookie[] array) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    @Override
    public void clear() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    @Override
    public boolean clearExpired(final Date date) {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    @Override
    public List<Cookie> getCookies() {
        synchronized (this) {
            throw new RuntimeException("Stub!");
        }
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
