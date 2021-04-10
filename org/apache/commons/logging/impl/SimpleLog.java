package org.apache.commons.logging.impl;

import org.apache.commons.logging.*;
import java.io.*;
import java.text.*;
import java.util.*;

@Deprecated
public class SimpleLog implements Log, Serializable
{
    protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
    public static final int LOG_LEVEL_ALL = 0;
    public static final int LOG_LEVEL_DEBUG = 2;
    public static final int LOG_LEVEL_ERROR = 5;
    public static final int LOG_LEVEL_FATAL = 6;
    public static final int LOG_LEVEL_INFO = 3;
    public static final int LOG_LEVEL_OFF = 7;
    public static final int LOG_LEVEL_TRACE = 1;
    public static final int LOG_LEVEL_WARN = 4;
    protected static DateFormat dateFormatter;
    protected static String dateTimeFormat;
    protected static boolean showDateTime = false;
    protected static boolean showLogName = false;
    protected static boolean showShortName = false;
    protected static final Properties simpleLogProps;
    protected static final String systemPrefix = "org.apache.commons.logging.simplelog.";
    protected int currentLogLevel;
    protected String logName;
    
    public SimpleLog(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public final void debug(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public final void debug(final Object o, final Throwable t) {
        throw new RuntimeException("Stub!");
    }
    
    public final void error(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public final void error(final Object o, final Throwable t) {
        throw new RuntimeException("Stub!");
    }
    
    public final void fatal(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public final void fatal(final Object o, final Throwable t) {
        throw new RuntimeException("Stub!");
    }
    
    public int getLevel() {
        throw new RuntimeException("Stub!");
    }
    
    public final void info(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public final void info(final Object o, final Throwable t) {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean isDebugEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean isErrorEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean isFatalEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean isInfoEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    protected boolean isLevelEnabled(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean isTraceEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean isWarnEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    protected void log(final int n, final Object o, final Throwable t) {
        throw new RuntimeException("Stub!");
    }
    
    public void setLevel(final int n) {
        throw new RuntimeException("Stub!");
    }
    
    public final void trace(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public final void trace(final Object o, final Throwable t) {
        throw new RuntimeException("Stub!");
    }
    
    public final void warn(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public final void warn(final Object o, final Throwable t) {
        throw new RuntimeException("Stub!");
    }
    
    protected void write(final StringBuffer sb) {
        throw new RuntimeException("Stub!");
    }
}
