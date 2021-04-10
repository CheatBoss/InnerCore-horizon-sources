package com.appsflyer;

import android.net.*;
import java.util.concurrent.*;

public class AFExecutor
{
    private static AFExecutor \u03b9;
    final ThreadFactory \u0131;
    ScheduledExecutorService \u01c3;
    ScheduledExecutorService \u0269;
    Executor \u0399;
    
    private AFExecutor() {
        this.\u0131 = new ThreadFactory() {
            @Override
            public final Thread newThread(final Runnable runnable) {
                return new Thread(new Runnable() {
                    @Override
                    public final void run() {
                        TrafficStats.setThreadStatsTag("AppsFlyer".hashCode());
                        runnable.run();
                    }
                });
            }
        };
    }
    
    public static AFExecutor getInstance() {
        if (AFExecutor.\u03b9 == null) {
            AFExecutor.\u03b9 = new AFExecutor();
        }
        return AFExecutor.\u03b9;
    }
    
    static void \u0269(final ExecutorService executorService) {
        try {
            try {
                AFLogger.afRDLog("shut downing executor ...");
                executorService.shutdown();
                executorService.awaitTermination(10L, TimeUnit.SECONDS);
                if (!executorService.isTerminated()) {
                    AFLogger.afRDLog("killing non-finished tasks");
                }
                executorService.shutdownNow();
            }
            finally {
                if (!executorService.isTerminated()) {
                    AFLogger.afRDLog("killing non-finished tasks");
                }
                executorService.shutdownNow();
                AFLogger.afRDLog("killing non-finished tasks");
                executorService.shutdownNow();
            }
        }
        catch (InterruptedException ex) {}
    }
    
    public Executor getThreadPoolExecutor() {
        final Executor \u03b9 = this.\u0399;
        boolean b = false;
        Label_0062: {
            Label_0060: {
                if (\u03b9 != null) {
                    if (\u03b9 instanceof ThreadPoolExecutor) {
                        if (((ThreadPoolExecutor)\u03b9).isShutdown() || ((ThreadPoolExecutor)this.\u0399).isTerminated()) {
                            break Label_0060;
                        }
                        if (((ThreadPoolExecutor)this.\u0399).isTerminating()) {
                            break Label_0060;
                        }
                    }
                    b = false;
                    break Label_0062;
                }
            }
            b = true;
        }
        if (b) {
            this.\u0399 = Executors.newFixedThreadPool(2, this.\u0131);
        }
        return this.\u0399;
    }
    
    final ScheduledThreadPoolExecutor \u03b9() {
        final ScheduledExecutorService \u0269 = this.\u0269;
        if (\u0269 == null || \u0269.isShutdown() || this.\u0269.isTerminated()) {
            this.\u0269 = Executors.newScheduledThreadPool(2, this.\u0131);
        }
        return (ScheduledThreadPoolExecutor)this.\u0269;
    }
}
