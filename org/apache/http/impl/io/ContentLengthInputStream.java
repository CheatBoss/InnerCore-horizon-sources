package org.apache.http.impl.io;

import org.apache.http.io.*;
import java.io.*;

@Deprecated
public class ContentLengthInputStream extends InputStream
{
    public ContentLengthInputStream(final SessionInputBuffer sessionInputBuffer, final long n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void close() throws IOException {
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
    public long skip(final long n) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
