package org.apache.http.impl.conn;

import org.apache.http.impl.*;
import org.apache.http.conn.*;
import java.io.*;
import org.apache.http.params.*;
import java.net.*;
import org.apache.http.io.*;
import org.apache.http.*;

@Deprecated
public class DefaultClientConnection extends SocketHttpClientConnection implements OperatedClientConnection
{
    public DefaultClientConnection() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected HttpMessageParser createResponseParser(final SessionInputBuffer sessionInputBuffer, final HttpResponseFactory httpResponseFactory, final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected SessionInputBuffer createSessionInputBuffer(final Socket socket, final int n, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected SessionOutputBuffer createSessionOutputBuffer(final Socket socket, final int n, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final Socket getSocket() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final HttpHost getTargetHost() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public final boolean isSecure() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void openCompleted(final boolean b, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void opening(final Socket socket, final HttpHost httpHost) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void sendRequestHeader(final HttpRequest httpRequest) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void shutdown() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void update(final Socket socket, final HttpHost httpHost, final boolean b, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
