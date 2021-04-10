package org.apache.http.impl.conn;

import org.apache.http.conn.scheme.*;
import org.apache.http.conn.*;
import org.apache.http.*;
import org.apache.http.protocol.*;
import org.apache.http.params.*;
import java.io.*;
import java.net.*;

@Deprecated
public class DefaultClientConnectionOperator implements ClientConnectionOperator
{
    protected SchemeRegistry schemeRegistry;
    
    public DefaultClientConnectionOperator(final SchemeRegistry schemeRegistry) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public OperatedClientConnection createConnection() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void openConnection(final OperatedClientConnection operatedClientConnection, final HttpHost httpHost, final InetAddress inetAddress, final HttpContext httpContext, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void prepareSocket(final Socket socket, final HttpContext httpContext, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void updateSecureConnection(final OperatedClientConnection operatedClientConnection, final HttpHost httpHost, final HttpContext httpContext, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
