package com.bumptech.glide.load.engine.executor;

import java.util.concurrent.atomic.*;
import android.os.*;
import java.util.concurrent.*;
import android.util.*;

public class FifoPriorityThreadPoolExecutor extends ThreadPoolExecutor
{
    private static final String TAG = "PriorityExecutor";
    private final AtomicInteger ordering;
    private final UncaughtThrowableStrategy uncaughtThrowableStrategy;
    
    public FifoPriorityThreadPoolExecutor(final int n) {
        this(n, UncaughtThrowableStrategy.LOG);
    }
    
    public FifoPriorityThreadPoolExecutor(final int n, final int n2, final long n3, final TimeUnit timeUnit, final ThreadFactory threadFactory, final UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        super(n, n2, n3, timeUnit, new PriorityBlockingQueue<Runnable>(), threadFactory);
        this.ordering = new AtomicInteger();
        this.uncaughtThrowableStrategy = uncaughtThrowableStrategy;
    }
    
    public FifoPriorityThreadPoolExecutor(final int n, final UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        this(n, n, 0L, TimeUnit.MILLISECONDS, new DefaultThreadFactory(), uncaughtThrowableStrategy);
    }
    
    @Override
    protected void afterExecute(final Runnable runnable, final Throwable t) {
        super.afterExecute(runnable, t);
        if (t == null && runnable instanceof Future) {
            final Future future = (Future)runnable;
            if (future.isDone() && !future.isCancelled()) {
                try {
                    future.get();
                }
                catch (ExecutionException ex) {
                    this.uncaughtThrowableStrategy.handle(ex);
                }
                catch (InterruptedException ex2) {
                    this.uncaughtThrowableStrategy.handle(ex2);
                }
            }
        }
    }
    
    @Override
    protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T t) {
        return new LoadTask<T>(runnable, t, this.ordering.getAndIncrement());
    }
    
    public static class DefaultThreadFactory implements ThreadFactory
    {
        int threadNum;
        
        public DefaultThreadFactory() {
            this.threadNum = 0;
        }
        
        @Override
        public Thread newThread(final Runnable runnable) {
            final StringBuilder sb = new StringBuilder();
            sb.append("fifo-pool-thread-");
            sb.append(this.threadNum);
            final Thread thread = new Thread(runnable, sb.toString()) {
                @Override
                public void run() {
                    Process.setThreadPriority(10);
                    super.run();
                }
            };
            ++this.threadNum;
            return thread;
        }
    }
    
    static class LoadTask<T> extends FutureTask<T> implements Comparable<LoadTask<?>>
    {
        private final int order;
        private final int priority;
        
        public LoadTask(final Runnable runnable, final T t, final int order) {
            super(runnable, t);
            if (!(runnable instanceof Prioritized)) {
                throw new IllegalArgumentException("FifoPriorityThreadPoolExecutor must be given Runnables that implement Prioritized");
            }
            this.priority = ((Prioritized)runnable).getPriority();
            this.order = order;
        }
        
        @Override
        public int compareTo(final LoadTask<?> loadTask) {
            int n;
            if ((n = this.priority - loadTask.priority) == 0) {
                n = this.order - loadTask.order;
            }
            return n;
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof LoadTask;
            final boolean b2 = false;
            if (b) {
                final LoadTask loadTask = (LoadTask)o;
                boolean b3 = b2;
                if (this.order == loadTask.order) {
                    b3 = b2;
                    if (this.priority == loadTask.priority) {
                        b3 = true;
                    }
                }
                return b3;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.priority * 31 + this.order;
        }
    }
    
    public enum UncaughtThrowableStrategy
    {
        IGNORE, 
        LOG {
            @Override
            protected void handle(final Throwable t) {
                if (Log.isLoggable("PriorityExecutor", 6)) {
                    Log.e("PriorityExecutor", "Request threw uncaught throwable", t);
                }
            }
        }, 
        THROW {
            @Override
            protected void handle(final Throwable t) {
                super.handle(t);
                throw new RuntimeException(t);
            }
        };
        
        protected void handle(final Throwable t) {
        }
    }
}
