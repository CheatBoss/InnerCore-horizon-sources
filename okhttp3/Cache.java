package okhttp3;

import okhttp3.internal.cache.*;
import java.io.*;

public final class Cache implements Closeable, Flushable
{
    final DiskLruCache cache;
    final InternalCache internalCache;
    
    @Override
    public void close() throws IOException {
        this.cache.close();
    }
    
    @Override
    public void flush() throws IOException {
        this.cache.flush();
    }
}
