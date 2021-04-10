package android.net.http;

import java.io.*;
import java.util.*;
import java.net.*;

public final class HttpResponseCache extends ResponseCache
{
    HttpResponseCache() {
        throw new RuntimeException("Stub!");
    }
    
    public static HttpResponseCache getInstalled() {
        throw new RuntimeException("Stub!");
    }
    
    public static HttpResponseCache install(final File file, final long n) throws IOException {
        synchronized (HttpResponseCache.class) {
            throw new RuntimeException("Stub!");
        }
    }
    
    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public void delete() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public void flush() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public CacheResponse get(final URI uri, final String s, final Map<String, List<String>> map) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public int getHitCount() {
        throw new RuntimeException("Stub!");
    }
    
    public int getNetworkCount() {
        throw new RuntimeException("Stub!");
    }
    
    public int getRequestCount() {
        throw new RuntimeException("Stub!");
    }
    
    public long maxSize() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public CacheRequest put(final URI uri, final URLConnection urlConnection) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public long size() {
        throw new RuntimeException("Stub!");
    }
}
