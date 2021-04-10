package org.apache.http.impl.conn.tsccm;

import java.lang.ref.*;

@Deprecated
public interface RefQueueHandler
{
    void handleReference(final Reference<?> p0);
}
