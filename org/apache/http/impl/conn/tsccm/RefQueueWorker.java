package org.apache.http.impl.conn.tsccm;

import java.lang.ref.*;

@Deprecated
public class RefQueueWorker implements Runnable
{
    protected final RefQueueHandler refHandler;
    protected final ReferenceQueue<?> refQueue;
    protected volatile Thread workerThread;
    
    public RefQueueWorker(final ReferenceQueue<?> referenceQueue, final RefQueueHandler refQueueHandler) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void run() {
        throw new RuntimeException("Stub!");
    }
    
    public void shutdown() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
