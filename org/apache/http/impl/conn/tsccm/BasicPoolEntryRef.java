package org.apache.http.impl.conn.tsccm;

import java.lang.ref.*;
import org.apache.http.conn.routing.*;

@Deprecated
public class BasicPoolEntryRef extends WeakReference<BasicPoolEntry>
{
    public BasicPoolEntryRef(final BasicPoolEntry basicPoolEntry, final ReferenceQueue<Object> referenceQueue) {
        super(null, null);
        throw new RuntimeException("Stub!");
    }
    
    public final HttpRoute getRoute() {
        throw new RuntimeException("Stub!");
    }
}
