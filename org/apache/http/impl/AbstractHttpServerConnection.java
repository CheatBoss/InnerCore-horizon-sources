package org.apache.http.impl;

import org.apache.http.impl.entity.*;
import org.apache.http.params.*;
import org.apache.http.io.*;
import java.io.*;
import org.apache.http.*;

@Deprecated
public abstract class AbstractHttpServerConnection implements HttpServerConnection
{
    public AbstractHttpServerConnection() {
        throw new RuntimeException("Stub!");
    }
    
    protected abstract void assertOpen() throws IllegalStateException;
    
    protected EntityDeserializer createEntityDeserializer() {
        throw new RuntimeException("Stub!");
    }
    
    protected EntitySerializer createEntitySerializer() {
        throw new RuntimeException("Stub!");
    }
    
    protected HttpRequestFactory createHttpRequestFactory() {
        throw new RuntimeException("Stub!");
    }
    
    protected HttpMessageParser createRequestParser(final SessionInputBuffer sessionInputBuffer, final HttpRequestFactory httpRequestFactory, final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    protected HttpMessageWriter createResponseWriter(final SessionOutputBuffer sessionOutputBuffer, final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    protected void doFlush() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void flush() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpConnectionMetrics getMetrics() {
        throw new RuntimeException("Stub!");
    }
    
    protected void init(final SessionInputBuffer sessionInputBuffer, final SessionOutputBuffer sessionOutputBuffer, final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isStale() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void receiveRequestEntity(final HttpEntityEnclosingRequest httpEntityEnclosingRequest) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpRequest receiveRequestHeader() throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void sendResponseEntity(final HttpResponse httpResponse) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void sendResponseHeader(final HttpResponse httpResponse) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
}
