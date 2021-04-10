package org.apache.http.conn;

import org.apache.http.entity.*;
import org.apache.http.*;
import java.io.*;

@Deprecated
public class BasicManagedEntity extends HttpEntityWrapper implements ConnectionReleaseTrigger, EofSensorWatcher
{
    protected final boolean attemptReuse;
    protected ManagedClientConnection managedConn;
    
    public BasicManagedEntity(final HttpEntity httpEntity, final ManagedClientConnection managedClientConnection, final boolean b) {
        super(null);
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void abortConnection() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void consumeContent() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean eofDetected(final InputStream inputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public InputStream getContent() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isRepeatable() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void releaseConnection() throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected void releaseManagedConnection() throws IOException {
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
    
    @Override
    public void writeTo(final OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
