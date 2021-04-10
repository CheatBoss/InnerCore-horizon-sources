package android.net.http;

import org.apache.http.entity.*;
import java.io.*;
import android.content.*;
import org.apache.http.client.*;
import org.apache.http.protocol.*;
import org.apache.http.client.methods.*;
import org.apache.http.*;
import org.apache.http.conn.*;
import org.apache.http.params.*;

public final class AndroidHttpClient implements HttpClient
{
    public static long DEFAULT_SYNC_MIN_GZIP_BYTES;
    
    AndroidHttpClient() {
        throw new RuntimeException("Stub!");
    }
    
    public static AbstractHttpEntity getCompressedEntity(final byte[] array, final ContentResolver contentResolver) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public static long getMinGzipSize(final ContentResolver contentResolver) {
        throw new RuntimeException("Stub!");
    }
    
    public static InputStream getUngzippedContent(final HttpEntity httpEntity) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public static void modifyRequestToAcceptGzipResponse(final HttpRequest httpRequest) {
        throw new RuntimeException("Stub!");
    }
    
    public static AndroidHttpClient newInstance(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public static AndroidHttpClient newInstance(final String s, final Context context) {
        throw new RuntimeException("Stub!");
    }
    
    public static long parseDate(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void close() {
        throw new RuntimeException("Stub!");
    }
    
    public void disableCurlLogging() {
        throw new RuntimeException("Stub!");
    }
    
    public void enableCurlLogging(final String s, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public <T> T execute(final HttpHost httpHost, final HttpRequest httpRequest, final ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    public <T> T execute(final HttpHost httpHost, final HttpRequest httpRequest, final ResponseHandler<? extends T> responseHandler, final HttpContext httpContext) throws IOException, ClientProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    public <T> T execute(final HttpUriRequest httpUriRequest, final ResponseHandler<? extends T> responseHandler) throws IOException, ClientProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    public <T> T execute(final HttpUriRequest httpUriRequest, final ResponseHandler<? extends T> responseHandler, final HttpContext httpContext) throws IOException, ClientProtocolException {
        throw new RuntimeException("Stub!");
    }
    
    public HttpResponse execute(final HttpHost httpHost, final HttpRequest httpRequest) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public HttpResponse execute(final HttpHost httpHost, final HttpRequest httpRequest, final HttpContext httpContext) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public HttpResponse execute(final HttpUriRequest httpUriRequest) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public HttpResponse execute(final HttpUriRequest httpUriRequest, final HttpContext httpContext) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected void finalize() throws Throwable {
        throw new RuntimeException("Stub!");
    }
    
    public ClientConnectionManager getConnectionManager() {
        throw new RuntimeException("Stub!");
    }
    
    public HttpParams getParams() {
        throw new RuntimeException("Stub!");
    }
}
