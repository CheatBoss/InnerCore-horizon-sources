package org.apache.http.impl.io;

import java.net.*;
import org.apache.http.params.*;
import java.io.*;

@Deprecated
public class SocketInputBuffer extends AbstractSessionInputBuffer
{
    public SocketInputBuffer(final Socket socket, final int n, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isDataAvailable(final int n) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isStale() throws IOException {
        throw new RuntimeException("Stub!");
    }
}
