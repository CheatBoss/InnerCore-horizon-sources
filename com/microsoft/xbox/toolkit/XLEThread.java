package com.microsoft.xbox.toolkit;

public class XLEThread extends Thread
{
    public XLEThread(final Runnable runnable, final String s) {
        super(runnable, s);
        this.setUncaughtExceptionHandler((UncaughtExceptionHandler)XLEUnhandledExceptionHandler.Instance);
    }
}
