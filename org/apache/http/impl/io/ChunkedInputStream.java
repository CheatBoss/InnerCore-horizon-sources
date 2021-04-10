package org.apache.http.impl.io;

import org.apache.http.io.*;
import java.io.*;
import org.apache.http.*;

@Deprecated
public class ChunkedInputStream extends InputStream
{
    public ChunkedInputStream(final SessionInputBuffer sessionInputBuffer) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void close() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public Header[] getFooters() {
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
}
