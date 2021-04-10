package okio;

import javax.annotation.*;
import java.util.concurrent.*;
import java.io.*;

public class AsyncTimeout extends Timeout
{
    private static final long IDLE_TIMEOUT_MILLIS;
    private static final long IDLE_TIMEOUT_NANOS;
    @Nullable
    static AsyncTimeout head;
    private boolean inQueue;
    @Nullable
    private AsyncTimeout next;
    private long timeoutAt;
    
    static {
        IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60L);
        IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(AsyncTimeout.IDLE_TIMEOUT_MILLIS);
    }
    
    @Nullable
    static AsyncTimeout awaitTimeout() throws InterruptedException {
        final AsyncTimeout next = AsyncTimeout.head.next;
        final AsyncTimeout asyncTimeout = null;
        if (next == null) {
            final long nanoTime = System.nanoTime();
            AsyncTimeout.class.wait(AsyncTimeout.IDLE_TIMEOUT_MILLIS);
            AsyncTimeout head = asyncTimeout;
            if (AsyncTimeout.head.next == null) {
                head = asyncTimeout;
                if (System.nanoTime() - nanoTime >= AsyncTimeout.IDLE_TIMEOUT_NANOS) {
                    head = AsyncTimeout.head;
                }
            }
            return head;
        }
        final long remainingNanos = next.remainingNanos(System.nanoTime());
        if (remainingNanos > 0L) {
            final long n = remainingNanos / 1000000L;
            AsyncTimeout.class.wait(n, (int)(remainingNanos - 1000000L * n));
            return null;
        }
        AsyncTimeout.head.next = next.next;
        next.next = null;
        return next;
    }
    
    private static boolean cancelScheduledTimeout(final AsyncTimeout asyncTimeout) {
        synchronized (AsyncTimeout.class) {
            for (AsyncTimeout asyncTimeout2 = AsyncTimeout.head; asyncTimeout2 != null; asyncTimeout2 = asyncTimeout2.next) {
                if (asyncTimeout2.next == asyncTimeout) {
                    asyncTimeout2.next = asyncTimeout.next;
                    asyncTimeout.next = null;
                    return false;
                }
            }
            return true;
        }
    }
    
    private long remainingNanos(final long n) {
        return this.timeoutAt - n;
    }
    
    private static void scheduleTimeout(final AsyncTimeout next, long remainingNanos, final boolean b) {
        synchronized (AsyncTimeout.class) {
            if (AsyncTimeout.head == null) {
                AsyncTimeout.head = new AsyncTimeout();
                new Watchdog().start();
            }
            final long nanoTime = System.nanoTime();
            if (remainingNanos != 0L && b) {
                next.timeoutAt = Math.min(remainingNanos, next.deadlineNanoTime() - nanoTime) + nanoTime;
            }
            else if (remainingNanos != 0L) {
                next.timeoutAt = remainingNanos + nanoTime;
            }
            else {
                if (!b) {
                    throw new AssertionError();
                }
                next.timeoutAt = next.deadlineNanoTime();
            }
            AsyncTimeout asyncTimeout;
            for (remainingNanos = next.remainingNanos(nanoTime), asyncTimeout = AsyncTimeout.head; asyncTimeout.next != null && remainingNanos >= asyncTimeout.next.remainingNanos(nanoTime); asyncTimeout = asyncTimeout.next) {}
            next.next = asyncTimeout.next;
            asyncTimeout.next = next;
            if (asyncTimeout == AsyncTimeout.head) {
                AsyncTimeout.class.notify();
            }
        }
    }
    
    public final void enter() {
        if (this.inQueue) {
            throw new IllegalStateException("Unbalanced enter/exit");
        }
        final long timeoutNanos = this.timeoutNanos();
        final boolean hasDeadline = this.hasDeadline();
        if (timeoutNanos == 0L && !hasDeadline) {
            return;
        }
        this.inQueue = true;
        scheduleTimeout(this, timeoutNanos, hasDeadline);
    }
    
    final IOException exit(final IOException ex) throws IOException {
        if (!this.exit()) {
            return ex;
        }
        return this.newTimeoutException(ex);
    }
    
    final void exit(final boolean b) throws IOException {
        if (!this.exit()) {
            return;
        }
        if (!b) {
            return;
        }
        throw this.newTimeoutException(null);
    }
    
    public final boolean exit() {
        if (!this.inQueue) {
            return false;
        }
        this.inQueue = false;
        return cancelScheduledTimeout(this);
    }
    
    protected IOException newTimeoutException(@Nullable final IOException ex) {
        final InterruptedIOException ex2 = new InterruptedIOException("timeout");
        if (ex != null) {
            ex2.initCause(ex);
        }
        return ex2;
    }
    
    public final Sink sink(final Sink sink) {
        return new Sink() {
            @Override
            public void close() throws IOException {
                AsyncTimeout.this.enter();
                try {
                    try {
                        sink.close();
                        AsyncTimeout.this.exit(true);
                        return;
                    }
                    finally {}
                }
                catch (IOException ex) {
                    throw AsyncTimeout.this.exit(ex);
                }
                AsyncTimeout.this.exit(false);
            }
            
            @Override
            public void flush() throws IOException {
                AsyncTimeout.this.enter();
                try {
                    try {
                        sink.flush();
                        AsyncTimeout.this.exit(true);
                        return;
                    }
                    finally {}
                }
                catch (IOException ex) {
                    throw AsyncTimeout.this.exit(ex);
                }
                AsyncTimeout.this.exit(false);
            }
            
            @Override
            public Timeout timeout() {
                return AsyncTimeout.this;
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("AsyncTimeout.sink(");
                sb.append(sink);
                sb.append(")");
                return sb.toString();
            }
            
            @Override
            public void write(final Buffer buffer, long n) throws IOException {
                Util.checkOffsetAndCount(buffer.size, 0L, n);
                while (true) {
                    long n2 = 0L;
                    if (n <= 0L) {
                        return;
                    }
                    Segment segment = buffer.head;
                    long n3;
                    while (true) {
                        n3 = n2;
                        if (n2 >= 65536L) {
                            break;
                        }
                        n2 += segment.limit - segment.pos;
                        if (n2 >= n) {
                            n3 = n;
                            break;
                        }
                        segment = segment.next;
                    }
                    AsyncTimeout.this.enter();
                    try {
                        try {
                            sink.write(buffer, n3);
                            AsyncTimeout.this.exit(true);
                            n -= n3;
                        }
                        finally {}
                    }
                    catch (IOException ex) {
                        throw AsyncTimeout.this.exit(ex);
                    }
                    AsyncTimeout.this.exit(false);
                }
            }
        };
    }
    
    public final Source source(final Source source) {
        return new Source() {
            @Override
            public void close() throws IOException {
                try {
                    try {
                        source.close();
                        AsyncTimeout.this.exit(true);
                        return;
                    }
                    finally {}
                }
                catch (IOException ex) {
                    throw AsyncTimeout.this.exit(ex);
                }
                AsyncTimeout.this.exit(false);
            }
            
            @Override
            public long read(final Buffer buffer, long read) throws IOException {
                AsyncTimeout.this.enter();
                try {
                    try {
                        read = source.read(buffer, read);
                        AsyncTimeout.this.exit(true);
                        return read;
                    }
                    finally {}
                }
                catch (IOException ex) {
                    throw AsyncTimeout.this.exit(ex);
                }
                AsyncTimeout.this.exit(false);
            }
            
            @Override
            public Timeout timeout() {
                return AsyncTimeout.this;
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("AsyncTimeout.source(");
                sb.append(source);
                sb.append(")");
                return sb.toString();
            }
        };
    }
    
    protected void timedOut() {
    }
    
    private static final class Watchdog extends Thread
    {
        Watchdog() {
            super("Okio Watchdog");
            this.setDaemon(true);
        }
        
        @Override
        public void run() {
            while (true) {
                try {
                    while (true) {
                        synchronized (AsyncTimeout.class) {
                            final AsyncTimeout awaitTimeout = AsyncTimeout.awaitTimeout();
                            if (awaitTimeout == null) {
                                continue;
                            }
                            if (awaitTimeout == AsyncTimeout.head) {
                                AsyncTimeout.head = null;
                                return;
                            }
                            // monitorexit(AsyncTimeout.class)
                            awaitTimeout.timedOut();
                            continue;
                        }
                    }
                }
                catch (InterruptedException ex) {
                    continue;
                }
                break;
            }
        }
    }
}
