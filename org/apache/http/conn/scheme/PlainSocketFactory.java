package org.apache.http.conn.scheme;

import java.net.*;
import org.apache.http.params.*;
import java.io.*;

@Deprecated
public final class PlainSocketFactory implements SocketFactory
{
    public PlainSocketFactory() {
        throw new RuntimeException("Stub!");
    }
    
    public PlainSocketFactory(final HostNameResolver hostNameResolver) {
        throw new RuntimeException("Stub!");
    }
    
    public static PlainSocketFactory getSocketFactory() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Socket connectSocket(final Socket socket, final String s, final int n, final InetAddress inetAddress, final int n2, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public Socket createSocket() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean equals(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int hashCode() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final boolean isSecure(final Socket socket) throws IllegalArgumentException {
        throw new RuntimeException("Stub!");
    }
}
