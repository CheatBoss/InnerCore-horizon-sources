package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.disklrucache.*;
import java.io.*;
import android.util.*;
import com.bumptech.glide.load.*;

public class DiskLruCacheWrapper implements DiskCache
{
    private static final int APP_VERSION = 1;
    private static final String TAG = "DiskLruCacheWrapper";
    private static final int VALUE_COUNT = 1;
    private static DiskLruCacheWrapper wrapper;
    private final File directory;
    private DiskLruCache diskLruCache;
    private final int maxSize;
    private final SafeKeyGenerator safeKeyGenerator;
    private final DiskCacheWriteLocker writeLocker;
    
    static {
        DiskLruCacheWrapper.wrapper = null;
    }
    
    protected DiskLruCacheWrapper(final File directory, final int maxSize) {
        this.writeLocker = new DiskCacheWriteLocker();
        this.directory = directory;
        this.maxSize = maxSize;
        this.safeKeyGenerator = new SafeKeyGenerator();
    }
    
    public static DiskCache get(final File file, final int n) {
        synchronized (DiskLruCacheWrapper.class) {
            if (DiskLruCacheWrapper.wrapper == null) {
                DiskLruCacheWrapper.wrapper = new DiskLruCacheWrapper(file, n);
            }
            return DiskLruCacheWrapper.wrapper;
        }
    }
    
    private DiskLruCache getDiskCache() throws IOException {
        synchronized (this) {
            if (this.diskLruCache == null) {
                this.diskLruCache = DiskLruCache.open(this.directory, 1, 1, this.maxSize);
            }
            return this.diskLruCache;
        }
    }
    
    private void resetDiskCache() {
        synchronized (this) {
            this.diskLruCache = null;
        }
    }
    
    @Override
    public void clear() {
        // monitorenter(this)
        try {
            try {
                this.getDiskCache().delete();
                this.resetDiskCache();
            }
            finally {}
        }
        catch (IOException ex) {
            if (Log.isLoggable("DiskLruCacheWrapper", 5)) {
                Log.w("DiskLruCacheWrapper", "Unable to clear disk cache", (Throwable)ex);
            }
        }
        // monitorexit(this)
        return;
    }
    // monitorexit(this)
    
    @Override
    public void delete(final Key key) {
        final String safeKey = this.safeKeyGenerator.getSafeKey(key);
        try {
            this.getDiskCache().remove(safeKey);
        }
        catch (IOException ex) {
            if (Log.isLoggable("DiskLruCacheWrapper", 5)) {
                Log.w("DiskLruCacheWrapper", "Unable to delete from disk cache", (Throwable)ex);
            }
        }
    }
    
    @Override
    public File get(final Key key) {
        final String safeKey = this.safeKeyGenerator.getSafeKey(key);
        File file = null;
        try {
            final DiskLruCache.Value value = this.getDiskCache().get(safeKey);
            if (value != null) {
                file = value.getFile(0);
            }
            return file;
        }
        catch (IOException ex) {
            if (Log.isLoggable("DiskLruCacheWrapper", 5)) {
                Log.w("DiskLruCacheWrapper", "Unable to get from disk cache", (Throwable)ex);
            }
            return null;
        }
    }
    
    @Override
    public void put(final Key key, final Writer writer) {
        final String safeKey = this.safeKeyGenerator.getSafeKey(key);
        this.writeLocker.acquire(key);
        while (true) {
            try {
                try {
                    final DiskLruCache.Editor edit = this.getDiskCache().edit(safeKey);
                    if (edit != null) {
                        try {
                            if (writer.write(edit.getFile(0))) {
                                edit.commit();
                            }
                        }
                        finally {
                            edit.abortUnlessCommitted();
                        }
                    }
                    this.writeLocker.release(key);
                    return;
                }
                finally {}
            }
            catch (IOException ex) {
                if (Log.isLoggable("DiskLruCacheWrapper", 5)) {
                    Log.w("DiskLruCacheWrapper", "Unable to put to disk cache", (Throwable)ex);
                    continue;
                }
                continue;
            }
            break;
        }
        this.writeLocker.release(key);
    }
}
