package org.apache.http.conn;

import java.net.*;
import org.apache.http.*;
import org.apache.http.params.*;
import java.io.*;

@Deprecated
public interface OperatedClientConnection extends HttpClientConnection, HttpInetConnection
{
    Socket getSocket();
    
    HttpHost getTargetHost();
    
    boolean isSecure();
    
    void openCompleted(final boolean p0, final HttpParams p1) throws IOException;
    
    void opening(final Socket p0, final HttpHost p1) throws IOException;
    
    void update(final Socket p0, final HttpHost p1, final boolean p2, final HttpParams p3) throws IOException;
}
