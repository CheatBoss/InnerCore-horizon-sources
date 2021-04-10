package org.apache.http.impl;

import org.apache.http.*;
import org.apache.http.params.*;
import java.io.*;
import org.apache.http.io.*;
import java.net.*;

@Deprecated
public class SocketHttpServerConnection extends AbstractHttpServerConnection implements HttpInetConnection
{
    public SocketHttpServerConnection() {
        throw new RuntimeException("Stub!");
    }
    
    protected void assertNotOpen() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected void assertOpen() {
        throw new RuntimeException("Stub!");
    }
    
    protected void bind(final Socket socket, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected SessionInputBuffer createHttpDataReceiver(final Socket socket, final int n, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected SessionOutputBuffer createHttpDataTransmitter(final Socket socket, final int n, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public InetAddress getLocalAddress() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getLocalPort() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public InetAddress getRemoteAddress() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getRemotePort() {
        throw new RuntimeException("Stub!");
    }
    
    protected Socket getSocket() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getSocketTimeout() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isOpen() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setSocketTimeout(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void shutdown() throws IOException {
        throw new RuntimeException("Stub!");
    }
}
