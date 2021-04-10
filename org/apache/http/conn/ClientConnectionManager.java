package org.apache.http.conn;

import java.util.concurrent.*;
import org.apache.http.conn.scheme.*;
import org.apache.http.conn.routing.*;

@Deprecated
public interface ClientConnectionManager
{
    void closeExpiredConnections();
    
    void closeIdleConnections(final long p0, final TimeUnit p1);
    
    SchemeRegistry getSchemeRegistry();
    
    void releaseConnection(final ManagedClientConnection p0, final long p1, final TimeUnit p2);
    
    ClientConnectionRequest requestConnection(final HttpRoute p0, final Object p1);
    
    void shutdown();
}
