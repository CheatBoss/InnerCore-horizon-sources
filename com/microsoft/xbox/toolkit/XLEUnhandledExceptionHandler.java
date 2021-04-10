package com.microsoft.xbox.toolkit;

import java.util.*;

public class XLEUnhandledExceptionHandler implements UncaughtExceptionHandler
{
    public static XLEUnhandledExceptionHandler Instance;
    private UncaughtExceptionHandler oldExceptionHandler;
    
    static {
        XLEUnhandledExceptionHandler.Instance = new XLEUnhandledExceptionHandler();
    }
    
    public XLEUnhandledExceptionHandler() {
        this.oldExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    }
    
    private void printStackTrace(String string, final Throwable t) {
        new Date();
        final StackTraceElement[] stackTrace = t.getStackTrace();
        final int length = stackTrace.length;
        string = "";
        for (int i = 0; i < length; ++i) {
            final StackTraceElement stackTraceElement = stackTrace[i];
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(String.format("\t%s\n", stackTraceElement.toString()));
            string = sb.toString();
        }
    }
    
    @Override
    public void uncaughtException(final Thread thread, final Throwable t) {
        t.toString();
        if (t.getCause() != null) {
            this.printStackTrace("CAUSE STACK TRACE", t.getCause());
        }
        this.printStackTrace("MAIN THREAD STACK TRACE", t);
        this.oldExceptionHandler.uncaughtException(thread, t);
    }
}
