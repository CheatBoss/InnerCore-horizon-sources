package com.zhekasmirnov.innercore.utils;

import com.zhekasmirnov.innercore.api.runtime.other.*;
import com.zhekasmirnov.innercore.api.log.*;

public class OperationTimeLogger
{
    private final String logTag;
    private final boolean showToast;
    private long start;
    
    public OperationTimeLogger(final String logTag, final boolean showToast) {
        this.start = 0L;
        this.logTag = logTag;
        this.showToast = showToast;
    }
    
    public OperationTimeLogger(final boolean b) {
        this("Time-Logger", b);
    }
    
    public OperationTimeLogger finish(String format) {
        format = String.format(format, (System.currentTimeMillis() - this.start) / 1000.0);
        if (this.showToast) {
            PrintStacking.print(format);
        }
        ICLog.d("Time-Logger", format);
        return this;
    }
    
    public OperationTimeLogger start() {
        this.start = System.currentTimeMillis();
        return this;
    }
}
