package org.apache.http.impl.conn.tsccm;

import org.apache.http.impl.conn.*;
import org.apache.http.conn.*;

@Deprecated
public class BasicPooledConnAdapter extends AbstractPooledConnAdapter
{
    protected BasicPooledConnAdapter(final ThreadSafeClientConnManager threadSafeClientConnManager, final AbstractPoolEntry abstractPoolEntry) {
        super(null, (AbstractPoolEntry)null);
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected void detach() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected ClientConnectionManager getManager() {
        throw new RuntimeException("Stub!");
    }
    
    protected AbstractPoolEntry getPoolEntry() {
        throw new RuntimeException("Stub!");
    }
}
