package com.zhekasmirnov.innercore.api.log;

import java.util.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import org.mozilla.javascript.*;
import java.io.*;

public class ICLog
{
    private static HashMap<Long, IEventHandler> eventHandlerForThread;
    private static LogFilter logFilter;
    private static LogWriter logWriter;
    
    static {
        ICLog.logFilter = new LogFilter();
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_WORK);
        sb.append("inner-core.log");
        (ICLog.logWriter = new LogWriter(new File(sb.toString()))).clear();
        ICLog.eventHandlerForThread = new HashMap<Long, IEventHandler>();
    }
    
    public static void d(final String s, final String s2) {
        final IEventHandler eventHandlerForCurrentThread = getEventHandlerForCurrentThread();
        if (eventHandlerForCurrentThread != null) {
            eventHandlerForCurrentThread.onDebugEvent(s, s2);
        }
        logMsg(LogType.DEBUG, s, s2);
        Logger.debug(s, s2);
    }
    
    public static void e(final String s, String string, final Throwable t) {
        final IEventHandler eventHandlerForCurrentThread = getEventHandlerForCurrentThread();
        if (eventHandlerForCurrentThread != null) {
            eventHandlerForCurrentThread.onErrorEvent(s, string, t);
        }
        final LogType error = LogType.ERROR;
        final StringBuilder sb = new StringBuilder();
        String string2;
        if (s != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("[");
            sb2.append(s);
            sb2.append("] ");
            string2 = sb2.toString();
        }
        else {
            string2 = "";
        }
        sb.append(string2);
        sb.append(string);
        String string3;
        if (t != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("\n");
            sb3.append(getStackTrace(t));
            string3 = sb3.toString();
        }
        else {
            string3 = "";
        }
        sb.append(string3);
        logMsg(error, "ERROR", sb.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(string);
        if (t != null) {
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("\n");
            sb5.append(getStackTrace(t));
            string = sb5.toString();
        }
        else {
            string = "";
        }
        sb4.append(string);
        Logger.error(s, sb4.toString());
        if (t != null) {
            t.printStackTrace();
        }
    }
    
    public static void flush() {
        ICLog.logWriter.flush();
    }
    
    public static IEventHandler getEventHandlerForCurrentThread() {
        return ICLog.eventHandlerForThread.get(getThreadId());
    }
    
    public static LogFilter getLogFilter() {
        return ICLog.logFilter;
    }
    
    public static LogWriter getLogWriter() {
        return ICLog.logWriter;
    }
    
    public static String getStackTrace(final Throwable t) {
        String scriptStackTrace = null;
        if (t instanceof RhinoException) {
            scriptStackTrace = ((RhinoException)t).getScriptStackTrace();
        }
        final StringWriter stringWriter = new StringWriter();
        t.printStackTrace(new PrintWriter(stringWriter));
        if (scriptStackTrace != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("JS STACK TRACE:\n");
            sb.append(scriptStackTrace);
            sb.append("\n\nFULL STACK TRACE:\n");
            sb.append(stringWriter.toString());
            return sb.toString();
        }
        return stringWriter.toString();
    }
    
    private static long getThreadId() {
        return Thread.currentThread().getId();
    }
    
    public static void i(final String s, final String s2) {
        final IEventHandler eventHandlerForCurrentThread = getEventHandlerForCurrentThread();
        if (eventHandlerForCurrentThread != null) {
            eventHandlerForCurrentThread.onImportantEvent(s, s2);
        }
        logMsg(LogType.IMPORTANT, s, s2);
        if (s.toUpperCase().equals("ERROR")) {
            Logger.error(s, s2);
            return;
        }
        Logger.info(s, s2);
    }
    
    public static void l(final String s, final String s2) {
        final IEventHandler eventHandlerForCurrentThread = getEventHandlerForCurrentThread();
        if (eventHandlerForCurrentThread != null) {
            eventHandlerForCurrentThread.onLogEvent(s, s2);
        }
        logMsg(LogType.LOG, s, s2);
        Logger.debug(s, s2);
    }
    
    private static void logMsg(final LogType logType, final String s, final String s2) {
        ICLog.logFilter.log(logType, s, s2);
        ICLog.logWriter.logMsg(logType, s, s2);
    }
    
    public static void setupEventHandlerForCurrentThread(final IEventHandler eventHandler) {
        ICLog.eventHandlerForThread.put(getThreadId(), eventHandler);
    }
    
    public static void showIfErrorsAreFound() {
        if (LogFilter.isContainingErrorTags()) {
            DialogHelper.reportStartupErrors("Some errors are occured during Inner Core startup and loading.");
        }
    }
}
