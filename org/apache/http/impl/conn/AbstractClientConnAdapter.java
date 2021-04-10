package org.apache.http.impl.conn;

import org.apache.http.conn.*;
import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import org.apache.http.*;
import java.util.concurrent.*;

@Deprecated
public abstract class AbstractClientConnAdapter implements ManagedClientConnection
{
    protected AbstractClientConnAdapter(final ClientConnectionManager clientConnectionManager, final OperatedClientConnection operatedClientConnection) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void abortConnection() {
        throw new RuntimeException("Stub!");
    }
    
    protected final void assertNotAborted() throws InterruptedIOException {
        throw new RuntimeException("Stub!");
    }
    
    protected final void assertValid(final OperatedClientConnection operatedClientConnection) {
        throw new RuntimeException("Stub!");
    }
    
    protected void detach() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void flush() throws IOException {
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
    
    protected ClientConnectionManager getManager() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpConnectionMetrics getMetrics() {
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
    
    @Override
    public SSLSession getSSLSession() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getSocketTimeout() {
        throw new RuntimeException("Stub!");
    }
    
    protected OperatedClientConnection getWrappedConnection() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isMarkedReusable() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isOpen() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isResponseAvailable(final int n) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isSecure() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isStale() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void markReusable() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void receiveResponseEntity(final HttpResponse httpResponse) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void releaseConnection() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void sendRequestEntity(final HttpEntityEnclosingRequest httpEntityEnclosingRequest) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void sendRequestHeader(final HttpRequest httpRequest) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setIdleDuration(final long n, final TimeUnit timeUnit) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setSocketTimeout(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void unmarkReusable() {
        throw new RuntimeException("Stub!");
    }
}
