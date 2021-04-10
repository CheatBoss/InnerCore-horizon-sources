package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.*;
import java.util.concurrent.locks.*;
import java.util.*;

final class DiskCacheWriteLocker
{
    private final Map<Key, WriteLock> locks;
    private final WriteLockPool writeLockPool;
    
    DiskCacheWriteLocker() {
        this.locks = new HashMap<Key, WriteLock>();
        this.writeLockPool = new WriteLockPool();
    }
    
    void acquire(final Key key) {
        synchronized (this) {
            Object obtain;
            if ((obtain = this.locks.get(key)) == null) {
                obtain = this.writeLockPool.obtain();
                this.locks.put(key, (WriteLock)obtain);
            }
            ++((WriteLock)obtain).interestedThreads;
            // monitorexit(this)
            ((WriteLock)obtain).lock.lock();
        }
    }
    
    void release(final Key key) {
        synchronized (this) {
            final WriteLock writeLock = this.locks.get(key);
            if (writeLock != null && writeLock.interestedThreads > 0) {
                if (--writeLock.interestedThreads == 0) {
                    final WriteLock writeLock2 = this.locks.remove(key);
                    if (!writeLock2.equals(writeLock)) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Removed the wrong lock, expected to remove: ");
                        sb.append(writeLock);
                        sb.append(", but actually removed: ");
                        sb.append(writeLock2);
                        sb.append(", key: ");
                        sb.append(key);
                        throw new IllegalStateException(sb.toString());
                    }
                    this.writeLockPool.offer(writeLock2);
                }
                // monitorexit(this)
                writeLock.lock.unlock();
                return;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Cannot release a lock that is not held, key: ");
            sb2.append(key);
            sb2.append(", interestedThreads: ");
            int interestedThreads;
            if (writeLock == null) {
                interestedThreads = 0;
            }
            else {
                interestedThreads = writeLock.interestedThreads;
            }
            sb2.append(interestedThreads);
            throw new IllegalArgumentException(sb2.toString());
        }
    }
    
    private static class WriteLock
    {
        int interestedThreads;
        final Lock lock;
        
        private WriteLock() {
            this.lock = new ReentrantLock();
        }
    }
    
    private static class WriteLockPool
    {
        private static final int MAX_POOL_SIZE = 10;
        private final Queue<WriteLock> pool;
        
        private WriteLockPool() {
            this.pool = new ArrayDeque<WriteLock>();
        }
        
        WriteLock obtain() {
            final Queue<WriteLock> pool = this.pool;
            // monitorenter(pool)
            try {
                final WriteLock writeLock = this.pool.poll();
                try {
                    // monitorexit(pool)
                    WriteLock writeLock2 = writeLock;
                    if (writeLock == null) {
                        writeLock2 = new WriteLock();
                    }
                    return writeLock2;
                }
                finally {}
            }
            finally {}
        }
        // monitorexit(pool)
        
        void offer(final WriteLock writeLock) {
            synchronized (this.pool) {
                if (this.pool.size() < 10) {
                    this.pool.offer(writeLock);
                }
            }
        }
    }
}
