package com.zhekasmirnov.innercore.api.log;

public interface IEventHandler
{
    void onDebugEvent(final String p0, final String p1);
    
    void onErrorEvent(final String p0, final String p1, final Throwable p2);
    
    void onImportantEvent(final String p0, final String p1);
    
    void onLogEvent(final String p0, final String p1);
}
