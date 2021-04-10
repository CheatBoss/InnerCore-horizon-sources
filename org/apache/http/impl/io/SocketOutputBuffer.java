package org.apache.http.impl.io;

import java.net.*;
import org.apache.http.params.*;
import java.io.*;

@Deprecated
public class SocketOutputBuffer extends AbstractSessionOutputBuffer
{
    public SocketOutputBuffer(final Socket socket, final int n, final HttpParams httpParams) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
