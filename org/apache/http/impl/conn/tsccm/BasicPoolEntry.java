package org.apache.http.impl.conn.tsccm;

import org.apache.http.impl.conn.*;
import org.apache.http.conn.routing.*;
import java.lang.ref.*;
import org.apache.http.conn.*;

@Deprecated
public class BasicPoolEntry extends AbstractPoolEntry
{
    public BasicPoolEntry(final ClientConnectionOperator clientConnectionOperator, final HttpRoute httpRoute, final ReferenceQueue<Object> referenceQueue) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }
    
    protected final OperatedClientConnection getConnection() {
        throw new RuntimeException("Stub!");
    }
    
    protected final HttpRoute getPlannedRoute() {
        throw new RuntimeException("Stub!");
    }
    
    protected final BasicPoolEntryRef getWeakRef() {
        throw new RuntimeException("Stub!");
    }
}
