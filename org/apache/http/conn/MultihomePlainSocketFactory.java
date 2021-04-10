package org.apache.http.conn;

import org.apache.http.conn.scheme.*;
import java.net.*;
import org.apache.http.params.*;
import java.io.*;

@Deprecated
public final class MultihomePlainSocketFactory implements SocketFactory
{
    MultihomePlainSocketFactory() {
        throw new RuntimeException("Stub!");
    }
    
    public static MultihomePlainSocketFactory getSocketFactory() {
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
