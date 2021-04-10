package okio;

import java.util.concurrent.*;
import java.io.*;

public class Timeout
{
    public static final Timeout NONE;
    private long deadlineNanoTime;
    private boolean hasDeadline;
    private long timeoutNanos;
    
    static {
        NONE = new Timeout() {
            @Override
            public Timeout deadlineNanoTime(final long n) {
                return this;
            }
            
            @Override
            public void throwIfReached() throws IOException {
            }
            
            @Override
            public Timeout timeout(final long n, final TimeUnit timeUnit) {
                return this;
            }
        };
    }
    
    public Timeout clearDeadline() {
        this.hasDeadline = false;
        return this;
    }
    
    public Timeout clearTimeout() {
        this.timeoutNanos = 0L;
        return this;
    }
    
    public long deadlineNanoTime() {
        if (this.hasDeadline) {
            return this.deadlineNanoTime;
        }
        throw new IllegalStateException("No deadline");
    }
    
    public Timeout deadlineNanoTime(final long deadlineNanoTime) {
        this.hasDeadline = true;
        this.deadlineNanoTime = deadlineNanoTime;
        return this;
    }
    
    public boolean hasDeadline() {
        return this.hasDeadline;
    }
    
    public void throwIfReached() throws IOException {
        if (Thread.interrupted()) {
            throw new InterruptedIOException("thread interrupted");
        }
        if (!this.hasDeadline) {
            return;
        }
        if (this.deadlineNanoTime - System.nanoTime() > 0L) {
            return;
        }
        throw new InterruptedIOException("deadline reached");
    }
    
    public Timeout timeout(final long n, final TimeUnit timeUnit) {
        if (n < 0L) {
            final StringBuilder sb = new StringBuilder();
            sb.append("timeout < 0: ");
            sb.append(n);
            throw new IllegalArgumentException(sb.toString());
        }
        if (timeUnit != null) {
            this.timeoutNanos = timeUnit.toNanos(n);
            return this;
        }
        throw new IllegalArgumentException("unit == null");
    }
    
    public long timeoutNanos() {
        return this.timeoutNanos;
    }
}
