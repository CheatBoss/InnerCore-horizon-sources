package org.apache.http.impl.conn;

import org.apache.http.io.*;
import java.io.*;
import org.apache.http.util.*;

@Deprecated
public class LoggingSessionInputBuffer implements SessionInputBuffer
{
    public LoggingSessionInputBuffer(final SessionInputBuffer sessionInputBuffer, final Wire wire) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpTransportMetrics getMetrics() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isDataAvailable(final int n) throws IOException {
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
