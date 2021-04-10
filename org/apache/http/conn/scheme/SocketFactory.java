package org.apache.http.conn.scheme;

import org.apache.http.params.*;
import java.io.*;
import java.net.*;
import org.apache.http.conn.*;

@Deprecated
public interface SocketFactory
{
    Socket connectSocket(final Socket p0, final String p1, final int p2, final InetAddress p3, final int p4, final HttpParams p5) throws IOException, UnknownHostException, ConnectTimeoutException;
    
    Socket createSocket() throws IOException;
    
    boolean isSecure(final Socket p0) throws IllegalArgumentException;
}
