package com.microsoft.xbox.toolkit;

import java.util.*;

public class ThreadSafePriorityQueue<T>
{
    private HashSet<T> hashSet;
    private PriorityQueue<T> queue;
    private Object syncObject;
    
    public ThreadSafePriorityQueue() {
        this.syncObject = new Object();
        this.queue = new PriorityQueue<T>();
        this.hashSet = new HashSet<T>();
    }
    
    public T pop() {
        final T t = null;
        final T t2 = null;
        T remove = t;
        try {
            final Object syncObject = this.syncObject;
            remove = t;
            // monitorenter(syncObject)
            while (true) {
                T t3 = t2;
                try {
                    if (this.queue.isEmpty()) {
                        t3 = t2;
                        this.syncObject.wait();
                        continue;
                    }
                    t3 = t2;
                    remove = this.queue.remove();
                    try {
                        this.hashSet.remove(remove);
                        // monitorexit(syncObject)
                        return remove;
                    }
                    finally {}
                }
                finally {
                    remove = t3;
                }
                break;
            }
        }
        // monitorexit(syncObject)
        catch (InterruptedException ex) {
            return remove;
        }
    }
    
    public void push(final T t) {
        synchronized (this.syncObject) {
            if (!this.hashSet.contains(t)) {
                this.queue.add(t);
                this.hashSet.add(t);
                this.syncObject.notifyAll();
            }
        }
    }
}
