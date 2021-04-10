package org.apache.http.conn;

import java.io.*;

@Deprecated
public class BasicEofSensorWatcher implements EofSensorWatcher
{
    protected boolean attemptReuse;
    protected ManagedClientConnection managedConn;
    
    public BasicEofSensorWatcher(final ManagedClientConnection managedClientConnection, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean eofDetected(final InputStream inputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean streamAbort(final InputStream inputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean streamClosed(final InputStream inputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
