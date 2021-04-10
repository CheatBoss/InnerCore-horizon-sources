package com.microsoft.xbox.toolkit.network;

import com.microsoft.xbox.toolkit.*;
import java.util.concurrent.*;

public class XLEThreadPool
{
    public static XLEThreadPool biOperationsThreadPool;
    public static XLEThreadPool nativeOperationsThreadPool;
    public static XLEThreadPool networkOperationsThreadPool;
    public static XLEThreadPool textureThreadPool;
    private ExecutorService executor;
    private String name;
    
    static {
        XLEThreadPool.nativeOperationsThreadPool = new XLEThreadPool(true, 4, "XLENativeOperationsPool");
        XLEThreadPool.networkOperationsThreadPool = new XLEThreadPool(false, 3, "XLENetworkOperationsPool");
        XLEThreadPool.textureThreadPool = new XLEThreadPool(false, 1, "XLETexturePool");
        XLEThreadPool.biOperationsThreadPool = new XLEThreadPool(false, 1, "XLEPerfMarkerOperationsPool");
    }
    
    public XLEThreadPool(final boolean b, final int n, final String name) {
        this.name = name;
        final ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(final Runnable runnable) {
                final XLEThread xleThread = new XLEThread(runnable, XLEThreadPool.this.name);
                xleThread.setDaemon(true);
                xleThread.setPriority(n);
                return xleThread;
            }
        };
        ExecutorService executor;
        if (b) {
            executor = Executors.newSingleThreadExecutor(threadFactory);
        }
        else {
            executor = Executors.newCachedThreadPool(threadFactory);
        }
        this.executor = executor;
    }
    
    public void run(final Runnable runnable) {
        this.executor.execute(runnable);
    }
}
