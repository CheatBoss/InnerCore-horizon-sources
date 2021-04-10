package org.apache.http.impl.conn;

import org.apache.http.*;
import java.util.concurrent.*;

@Deprecated
public class IdleConnectionHandler
{
    public IdleConnectionHandler() {
        throw new RuntimeException("Stub!");
    }
    
    public void add(final HttpConnection httpConnection, final long n, final TimeUnit timeUnit) {
        throw new RuntimeException("Stub!");
    }
    
    public void closeExpiredConnections() {
        throw new RuntimeException("Stub!");
    }
    
    public void closeIdleConnections(final long n) {
        throw new RuntimeException("Stub!");
    }
    
    public boolean remove(final HttpConnection httpConnection) {
        throw new RuntimeException("Stub!");
    }
    
    public void removeAll() {
        throw new RuntimeException("Stub!");
    }
}
