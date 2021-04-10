package com.zhekasmirnov.innercore.api.log;

public enum LogType
{
    DEBUG(0), 
    ERROR(3), 
    IMPORTANT(2), 
    LOG(1);
    
    public int level;
    
    private LogType(final int level) {
        this.level = level;
    }
}
