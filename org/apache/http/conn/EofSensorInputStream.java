package org.apache.http.conn;

import java.io.*;

@Deprecated
public class EofSensorInputStream extends InputStream implements ConnectionReleaseTrigger
{
    protected InputStream wrappedStream;
    
    public EofSensorInputStream(final InputStream inputStream, final EofSensorWatcher eofSensorWatcher) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void abortConnection() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int available() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void checkAbort() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void checkClose() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void checkEOF(final int n) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected boolean isReadAllowed() throws IOException {
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
    public void releaseConnection() throws IOException {
        throw new RuntimeException("Stub!");
    }
}
