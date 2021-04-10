package org.apache.http.impl.conn;

import java.io.*;
import org.apache.http.io.*;
import org.apache.http.util.*;

@Deprecated
public class LoggingSessionOutputBuffer implements SessionOutputBuffer
{
    public LoggingSessionOutputBuffer(final SessionOutputBuffer sessionOutputBuffer, final Wire wire) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void flush() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpTransportMetrics getMetrics() {
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
