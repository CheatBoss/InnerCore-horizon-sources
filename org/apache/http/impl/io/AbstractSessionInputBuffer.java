package org.apache.http.impl.io;

import org.apache.http.io.*;
import java.io.*;
import org.apache.http.params.*;
import org.apache.http.util.*;

@Deprecated
public abstract class AbstractSessionInputBuffer implements SessionInputBuffer
{
    public AbstractSessionInputBuffer() {
        throw new RuntimeException("Stub!");
    }
    
    protected int fillBuffer() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpTransportMetrics getMetrics() {
        throw new RuntimeException("Stub!");
    }
    
    protected boolean hasBufferedData() {
        throw new RuntimeException("Stub!");
    }
    
    protected void init(final InputStream inputStream, final int n, final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int read() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int read(final byte[] array) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int readLine(final CharArrayBuffer charArrayBuffer) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String readLine() throws IOException {
        throw new RuntimeException("Stub!");
    }
}
