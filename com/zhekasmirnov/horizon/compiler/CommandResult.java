package com.zhekasmirnov.horizon.compiler;

import android.support.annotation.*;

public class CommandResult
{
    private String message;
    private int resultCode;
    private long time;
    
    public CommandResult(final int resultCode) {
        this.time = 0L;
        this.resultCode = resultCode;
    }
    
    public CommandResult(final int resultCode, @Nullable final String message) {
        this.time = 0L;
        this.resultCode = resultCode;
        this.message = message;
    }
    
    public CommandResult(final CommandResult commandResult) {
        this(commandResult.getResultCode(), commandResult.getMessage());
    }
    
    @Override
    public String toString() {
        return "CommandResult{message='" + this.message + '\'' + ", resultCode=" + this.resultCode + ", time=" + this.time + '}';
    }
    
    public long getTime() {
        return this.time;
    }
    
    public void setTime(final long time) {
        this.time = time;
    }
    
    public int getResultCode() {
        return this.resultCode;
    }
    
    public void setResultCode(final int resultCode) {
        this.resultCode = resultCode;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public void setMessage(final String message) {
        this.message = message;
    }
}
