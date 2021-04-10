package org.apache.http.impl.io;

import org.apache.http.io.*;
import java.io.*;
import org.apache.http.params.*;
import org.apache.http.util.*;

@Deprecated
public abstract class AbstractSessionOutputBuffer implements SessionOutputBuffer
{
    public AbstractSessionOutputBuffer() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void flush() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void flushBuffer() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpTransportMetrics getMetrics() {
        throw new RuntimeException("Stub!");
    }
    
    protected void init(final OutputStream outputStream, final int n, final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void write(final int n) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void write(final byte[] array) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void write(final byte[] array, final int n, final int n2) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void writeLine(final String s) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void writeLine(final CharArrayBuffer charArrayBuffer) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
