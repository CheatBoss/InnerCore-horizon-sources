package com.zhekasmirnov.horizon.runtime.logger;

import com.zhekasmirnov.horizon.*;

public class Profiler
{
    public static native void startSection(final String p0);
    
    public static native void endSection();
    
    public static native void endStartSection(final String p0);
    
    public static native void setCallbackProfilingEnabled(final boolean p0);
    
    public static native void setExtremeSignalHandlingEnabled(final boolean p0);
    
    static {
        HorizonLibrary.include();
    }
}
