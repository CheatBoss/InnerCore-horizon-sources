package org.apache.http.impl.io;

import org.apache.http.io.*;
import java.io.*;

@Deprecated
public class ChunkedOutputStream extends OutputStream
{
    public ChunkedOutputStream(final SessionOutputBuffer sessionOutputBuffer) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public ChunkedOutputStream(final SessionOutputBuffer sessionOutputBuffer, final int n) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public void finish() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void flush() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void flushCache() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void flushCacheWithAppend(final byte[] array, final int n, final int n2) throws IOException {
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
    
    protected void writeClosingChunk() throws IOException {
        throw new RuntimeException("Stub!");
    }
}
