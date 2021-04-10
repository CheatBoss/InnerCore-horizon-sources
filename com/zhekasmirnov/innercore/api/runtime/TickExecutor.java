package com.zhekasmirnov.innercore.api.runtime;

import java.util.*;
import java.util.concurrent.*;
import com.zhekasmirnov.innercore.api.log.*;

public class TickExecutor
{
    private static final TickExecutor instance;
    private int additionalThreadPriority;
    private ThreadPoolExecutor executor;
    private int threadCount;
    
    static {
        instance = new TickExecutor();
    }
    
    public TickExecutor() {
        this.threadCount = 0;
        this.additionalThreadPriority = 3;
        this.executor = null;
    }
    
    public static TickExecutor getInstance() {
        return TickExecutor.instance;
    }
    
    public void blockUntilExecuted() {
        if (this.executor != null) {
            try {
                final BlockingQueue<Runnable> queue = this.executor.getQueue();
                while (queue.size() > 0) {
                    final Runnable runnable = queue.poll(0L, TimeUnit.MILLISECONDS);
                    if (runnable == null) {
                        break;
                    }
                    runnable.run();
                }
                while (this.executor.getActiveCount() > 0) {
                    Thread.sleep(2L);
                }
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void execute(final Runnable runnable) {
        if (this.executor != null) {
            this.executor.execute(runnable);
            return;
        }
        runnable.run();
    }
    
    public void execute(final Collection<Runnable> collection) {
        if (this.executor != null) {
            final Iterator<Runnable> iterator = collection.iterator();
            while (iterator.hasNext()) {
                this.executor.execute(iterator.next());
            }
        }
        else {
            final Iterator<Runnable> iterator2 = collection.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().run();
            }
        }
    }
    
    public boolean isAvailable() {
        return this.executor != null;
    }
    
    public void setAdditionalThreadCount(int max) {
        max = Math.max(0, Math.min(7, max));
        if (this.threadCount != max) {
            this.threadCount = max;
            if (this.executor != null) {
                this.executor.shutdown();
            }
            if (max > 0) {
                this.executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(this.threadCount, new ThreadFactory() {
                    final /* synthetic */ ThreadFactory val$defaultThreadFactory = Executors.defaultThreadFactory();
                    
                    @Override
                    public Thread newThread(final Runnable runnable) {
                        final Thread thread = this.val$defaultThreadFactory.newThread(runnable);
                        thread.setPriority(TickExecutor.this.additionalThreadPriority);
                        return thread;
                    }
                });
            }
            else {
                this.executor = null;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("set additional thread count to ");
        sb.append(this.threadCount);
        ICLog.d("TickExecutor", sb.toString());
    }
    
    public void setAdditionalThreadPriority(final int n) {
        this.additionalThreadPriority = Math.max(1, Math.min(10, n));
        final StringBuilder sb = new StringBuilder();
        sb.append("set additional thread priority to ");
        sb.append(this.additionalThreadPriority);
        ICLog.d("TickExecutor", sb.toString());
    }
}
