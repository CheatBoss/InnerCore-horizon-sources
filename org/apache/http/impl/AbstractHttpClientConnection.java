package org.apache.http.impl;

import org.apache.http.impl.entity.*;
import org.apache.http.params.*;
import org.apache.http.io.*;
import java.io.*;
import org.apache.http.*;

@Deprecated
public abstract class AbstractHttpClientConnection implements HttpClientConnection
{
    public AbstractHttpClientConnection() {
        throw new RuntimeException("Stub!");
    }
    
    protected abstract void assertOpen() throws IllegalStateException;
    
    protected EntityDeserializer createEntityDeserializer() {
        throw new RuntimeException("Stub!");
    }
    
    protected EntitySerializer createEntitySerializer() {
        throw new RuntimeException("Stub!");
    }
    
    protected HttpResponseFactory createHttpResponseFactory() {
        throw new RuntimeException("Stub!");
    }
    
    protected HttpMessageWriter createRequestWriter(final SessionOutputBuffer sessionOutputBuffer, final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    protected HttpMessageParser createResponseParser(final SessionInputBuffer sessionInputBuffer, final HttpResponseFactory httpResponseFactory, final HttpParams httpParams) {
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
    public boolean isResponseAvailable(final int n) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isStale() {
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
    public void sendRequestEntity(final HttpEntityEnclosingRequest httpEntityEnclosingRequest) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void sendRequestHeader(final HttpRequest httpRequest) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
}
