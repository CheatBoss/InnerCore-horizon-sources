package com.android.internal.http.multipart;

import java.io.*;

public abstract class Part
{
    @Deprecated
    protected static final String BOUNDARY = "----------------314159265358979323846";
    @Deprecated
    protected static final byte[] BOUNDARY_BYTES;
    protected static final String CHARSET = "; charset=";
    protected static final byte[] CHARSET_BYTES;
    protected static final String CONTENT_DISPOSITION = "Content-Disposition: form-data; name=";
    protected static final byte[] CONTENT_DISPOSITION_BYTES;
    protected static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding: ";
    protected static final byte[] CONTENT_TRANSFER_ENCODING_BYTES;
    protected static final String CONTENT_TYPE = "Content-Type: ";
    protected static final byte[] CONTENT_TYPE_BYTES;
    protected static final String CRLF = "\r\n";
    protected static final byte[] CRLF_BYTES;
    protected static final String EXTRA = "--";
    protected static final byte[] EXTRA_BYTES;
    protected static final String QUOTE = "\"";
    protected static final byte[] QUOTE_BYTES;
    
    public Part() {
        throw new RuntimeException("Stub!");
    }
    
    @Deprecated
    public static String getBoundary() {
        throw new RuntimeException("Stub!");
    }
    
    public static long getLengthOfParts(final Part[] array) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public static long getLengthOfParts(final Part[] array, final byte[] array2) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public static void sendParts(final OutputStream outputStream, final Part[] array) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public static void sendParts(final OutputStream outputStream, final Part[] array, final byte[] array2) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public abstract String getCharSet();
    
    public abstract String getContentType();
    
    public abstract String getName();
    
    protected byte[] getPartBoundary() {
        throw new RuntimeException("Stub!");
    }
    
    public abstract String getTransferEncoding();
    
    public boolean isRepeatable() {
        throw new RuntimeException("Stub!");
    }
    
    public long length() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected abstract long lengthOfData() throws IOException;
    
    public void send(final OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void sendContentTypeHeader(final OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected abstract void sendData(final OutputStream p0) throws IOException;
    
    protected void sendDispositionHeader(final OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void sendEnd(final OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void sendEndOfHeader(final OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void sendStart(final OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void sendTransferEncodingHeader(final OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
