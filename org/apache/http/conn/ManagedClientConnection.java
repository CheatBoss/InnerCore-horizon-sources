package org.apache.http.conn;

import org.apache.http.conn.routing.*;
import javax.net.ssl.*;
import org.apache.http.protocol.*;
import org.apache.http.params.*;
import java.io.*;
import java.util.concurrent.*;
import org.apache.http.*;

@Deprecated
public interface ManagedClientConnection extends HttpClientConnection, HttpInetConnection, ConnectionReleaseTrigger
{
    HttpRoute getRoute();
    
    SSLSession getSSLSession();
    
    Object getState();
    
    boolean isMarkedReusable();
    
    boolean isSecure();
    
    void layerProtocol(final HttpContext p0, final HttpParams p1) throws IOException;
    
    void markReusable();
    
    void open(final HttpRoute p0, final HttpContext p1, final HttpParams p2) throws IOException;
    
    void setIdleDuration(final long p0, final TimeUnit p1);
    
    void setState(final Object p0);
    
    void tunnelProxy(final HttpHost p0, final boolean p1, final HttpParams p2) throws IOException;
    
    void tunnelTarget(final boolean p0, final HttpParams p1) throws IOException;
    
    void unmarkReusable();
}
