package org.apache.http.impl;

import java.net.*;
import org.apache.http.params.*;
import java.io.*;

@Deprecated
public class DefaultHttpServerConnection extends SocketHttpServerConnection
{
    public DefaultHttpServerConnection() {
        throw new RuntimeException("Stub!");
    }
    
    public void bind(final Socket socket, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
