package com.microsoft.xal.logging;

public class LogEntry
{
    private final XalLogger.LogLevel m_level;
    private final String m_message;
    
    public LogEntry(final XalLogger.LogLevel level, final String message) {
        this.m_level = level;
        this.m_message = message;
    }
    
    public int Level() {
        return this.m_level.ToInt();
    }
    
    public String Message() {
        return this.m_message;
    }
}
