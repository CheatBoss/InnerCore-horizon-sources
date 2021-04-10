package com.zhekasmirnov.innercore.api.runtime;

import com.zhekasmirnov.innercore.api.log.*;
import java.util.*;

public class MainThreadQueue
{
    public static final MainThreadQueue localThread;
    public static final MainThreadQueue serverThread;
    private final Map<Runnable, Integer> delayedQueue;
    private final ArrayList<Runnable> nextTickQueue;
    private final ArrayList<Runnable> queue;
    private boolean queueLocked;
    
    static {
        localThread = new MainThreadQueue();
        serverThread = new MainThreadQueue();
    }
    
    public MainThreadQueue() {
        this.queueLocked = false;
        this.queue = new ArrayList<Runnable>();
        this.nextTickQueue = new ArrayList<Runnable>();
        this.delayedQueue = new HashMap<Runnable, Integer>();
    }
    
    public void clearQueue() {
        synchronized (this.queue) {
            this.queue.clear();
        }
    }
    
    public void enqueue(final Runnable runnable) {
        synchronized (this.queue) {
            if (this.queueLocked) {
                this.nextTickQueue.add(runnable);
            }
            else {
                this.queue.add(runnable);
            }
        }
    }
    
    public void enqueueDelayed(final int n, final Runnable runnable) {
        synchronized (this.delayedQueue) {
            this.delayedQueue.put(runnable, n);
        }
    }
    
    public void executeQueue() {
        final long currentTimeMillis = System.currentTimeMillis();
        Object o = this.queue;
        synchronized (o) {
            this.queueLocked = true;
            try {
                final Iterator<Runnable> iterator = this.queue.iterator();
                while (iterator.hasNext()) {
                    iterator.next().run();
                }
            }
            catch (Throwable t) {
                ICLog.e("ERROR", "Error occurred in main thread queue, all posted actions was cleared", t);
                DialogHelper.reportNonFatalError("Error occurred in main thread queue, all posted actions was cleared", t);
            }
            this.queueLocked = false;
            this.queue.clear();
            this.queue.addAll(this.nextTickQueue);
            this.nextTickQueue.clear();
            // monitorexit(o)
            o = this.delayedQueue;
            // monitorenter(o)
            try {
                try {
                    final Iterator<Map.Entry<Runnable, Integer>> iterator2 = this.delayedQueue.entrySet().iterator();
                    while (iterator2.hasNext()) {
                        final Map.Entry<Runnable, Integer> entry = iterator2.next();
                        final int intValue = entry.getValue();
                        if (intValue <= 0) {
                            entry.getKey().run();
                            iterator2.remove();
                        }
                        entry.setValue(intValue - 1);
                    }
                }
                finally {
                    // monitorexit(o)
                    // monitorexit(o)
                    final long currentTimeMillis2 = System.currentTimeMillis();
                    // iftrue(Label_0304:, currentTimeMillis - currentTimeMillis2 <= 8L)
                    Block_14: {
                        break Block_14;
                        Label_0304: {
                            return;
                        }
                    }
                    o = new StringBuilder();
                    ((StringBuilder)o).append("main thread tick taking too long: ");
                    ((StringBuilder)o).append(currentTimeMillis - currentTimeMillis2);
                    ((StringBuilder)o).append(" ms");
                    ICLog.i("WARNING", ((StringBuilder)o).toString());
                }
            }
            catch (Throwable t2) {}
        }
    }
}
