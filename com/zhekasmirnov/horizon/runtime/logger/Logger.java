package com.zhekasmirnov.horizon.runtime.logger;

import java.io.*;
import com.zhekasmirnov.horizon.*;

public class Logger
{
    public static native void setOutputFile(final String p0);
    
    public static native void setCrashFile(final String p0);
    
    public static void setOutputFile(final File file) {
        setOutputFile(file.getAbsolutePath());
    }
    
    public static void setCrashFile(final File file) {
        setCrashFile(file.getAbsolutePath());
    }
    
    public static native void debug(final String p0, final String p1);
    
    public static void debug(final String message) {
        debug("DEBUG", message);
    }
    
    public static native void message(final String p0, final String p1);
    
    public static void message(final String message) {
        message("MESSAGE", message);
    }
    
    public static native void info(final String p0, final String p1);
    
    public static void info(final String message) {
        info("INFO", message);
    }
    
    public static void warning(final String message) {
        info("WARNING", message);
    }
    
    public static native void error(final String p0, final String p1);
    
    public static void error(final String message) {
        info("ERROR", message);
    }
    
    static {
        HorizonLibrary.include();
    }
}
