package org.apache.http.impl.io;

import org.apache.http.io.*;
import java.io.*;

@Deprecated
public class IdentityInputStream extends InputStream
{
    public IdentityInputStream(final SessionInputBuffer sessionInputBuffer) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int available() throws IOException {
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
    public int read(final byte[] array, final int n, final int n2) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
