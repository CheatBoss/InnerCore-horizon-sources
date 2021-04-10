package org.apache.http.conn.params;

import org.apache.http.params.*;

@Deprecated
public final class ConnManagerParams implements ConnManagerPNames
{
    public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20;
    
    public ConnManagerParams() {
        throw new RuntimeException("Stub!");
    }
    
    public static ConnPerRoute getMaxConnectionsPerRoute(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    public static int getMaxTotalConnections(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    public static long getTimeout(final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    public static void setMaxConnectionsPerRoute(final HttpParams httpParams, final ConnPerRoute connPerRoute) {
        throw new RuntimeException("Stub!");
    }
    
    public static void setMaxTotalConnections(final HttpParams httpParams, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public static void setTimeout(final HttpParams httpParams, final long n) {
        throw new RuntimeException("Stub!");
    }
}
