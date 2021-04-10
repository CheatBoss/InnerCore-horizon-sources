package org.apache.http.impl.conn.tsccm;

import org.apache.http.conn.routing.*;
import java.util.*;

@Deprecated
public class RouteSpecificPool
{
    protected final LinkedList<BasicPoolEntry> freeEntries;
    protected final int maxEntries;
    protected int numEntries;
    protected final HttpRoute route;
    protected final Queue<WaitingThread> waitingThreads;
    
    public RouteSpecificPool(final HttpRoute httpRoute, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public BasicPoolEntry allocEntry(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public void createdEntry(final BasicPoolEntry basicPoolEntry) {
        throw new RuntimeException("Stub!");
    }
    
    public boolean deleteEntry(final BasicPoolEntry basicPoolEntry) {
        throw new RuntimeException("Stub!");
    }
    
    public void dropEntry() {
        throw new RuntimeException("Stub!");
    }
    
    public void freeEntry(final BasicPoolEntry basicPoolEntry) {
        throw new RuntimeException("Stub!");
    }
    
    public int getCapacity() {
        throw new RuntimeException("Stub!");
    }
    
    public final int getEntryCount() {
        throw new RuntimeException("Stub!");
    }
    
    public final int getMaxEntries() {
        throw new RuntimeException("Stub!");
    }
    
    public final HttpRoute getRoute() {
        throw new RuntimeException("Stub!");
    }
    
    public boolean hasThread() {
        throw new RuntimeException("Stub!");
    }
    
    public boolean isUnused() {
        throw new RuntimeException("Stub!");
    }
    
    public WaitingThread nextThread() {
        throw new RuntimeException("Stub!");
    }
    
    public void queueThread(final WaitingThread waitingThread) {
        throw new RuntimeException("Stub!");
    }
    
    public void removeThread(final WaitingThread waitingThread) {
        throw new RuntimeException("Stub!");
    }
}
