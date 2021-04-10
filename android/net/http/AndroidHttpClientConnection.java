package android.net.http;

import org.apache.http.params.*;
import java.io.*;
import java.net.*;
import org.apache.http.*;

public class AndroidHttpClientConnection implements HttpInetConnection, HttpConnection
{
    public AndroidHttpClientConnection() {
        throw new RuntimeException("Stub!");
    }
    
    public void bind(final Socket socket, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void doFlush() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public void flush() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public InetAddress getLocalAddress() {
        throw new RuntimeException("Stub!");
    }
    
    public int getLocalPort() {
        throw new RuntimeException("Stub!");
    }
    
    public HttpConnectionMetrics getMetrics() {
        throw new RuntimeException("Stub!");
    }
    
    public InetAddress getRemoteAddress() {
        throw new RuntimeException("Stub!");
    }
    
    public int getRemotePort() {
        throw new RuntimeException("Stub!");
    }
    
    public int getSocketTimeout() {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isOpen() {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isStale() {
        throw new RuntimeException("Stub!");
    }
    
    public StatusLine parseResponseHeader(final Headers headers) throws IOException, ParseException {
        throw new RuntimeException("Stub!");
    }
    
    public HttpEntity receiveResponseEntity(final Headers headers) {
        throw new RuntimeException("Stub!");
    }
    
    public void sendRequestEntity(final HttpEntityEnclosingRequest httpEntityEnclosingRequest) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    public void sendRequestHeader(final HttpRequest httpRequest) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    public void setSocketTimeout(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public void shutdown() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
