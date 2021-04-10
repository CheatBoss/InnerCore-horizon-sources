package org.apache.http.impl.io;

import org.apache.http.io.*;
import java.io.*;

@Deprecated
public class ContentLengthOutputStream extends OutputStream
{
    public ContentLengthOutputStream(final SessionOutputBuffer sessionOutputBuffer, final long n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void flush() throws IOException {
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
}
