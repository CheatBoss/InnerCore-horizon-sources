package bo.app;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ax implements ThreadFactory
{
    private final AtomicInteger a;
    private final String b;
    private Thread.UncaughtExceptionHandler c;
    
    public ax() {
        this.a = new AtomicInteger(1);
        this.b = ax.class.getSimpleName();
    }
    
    public ax(final String b) {
        this.a = new AtomicInteger(1);
        this.b = b;
    }
    
    public void a(final Thread.UncaughtExceptionHandler c) {
        this.c = c;
    }
    
    @Override
    public Thread newThread(final Runnable runnable) {
        if (this.c != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.b);
            sb.append(" #");
            sb.append(this.a.getAndIncrement());
            final Thread thread = new Thread(runnable, sb.toString());
            thread.setUncaughtExceptionHandler(this.c);
            return thread;
        }
        throw new IllegalStateException("No UncaughtExceptionHandler. You must call setUncaughtExceptionHandler before creating a new thread");
    }
}
