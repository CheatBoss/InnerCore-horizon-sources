package com.microsoft.xbox.toolkit;

import android.os.*;

public class ThreadManager
{
    public static Handler Handler;
    public static Thread UIThread;
    
    public static void UIThreadPost(final Runnable runnable) {
        UIThreadPostDelayed(runnable, 0L);
    }
    
    public static void UIThreadPostDelayed(final Runnable runnable, final long n) {
        ThreadManager.Handler.postDelayed(runnable, n);
    }
    
    public static void UIThreadSend(final Runnable runnable) {
        if (ThreadManager.UIThread == Thread.currentThread()) {
            runnable.run();
            return;
        }
        final Ready ready = new Ready();
        ThreadManager.Handler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                runnable.run();
                ready.setReady();
            }
        });
        ready.waitForReady();
    }
}
