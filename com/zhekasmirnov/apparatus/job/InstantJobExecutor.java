package com.zhekasmirnov.apparatus.job;

import com.zhekasmirnov.apparatus.adapter.innercore.*;

public class InstantJobExecutor implements JobExecutor
{
    private final String name;
    
    public InstantJobExecutor() {
        this("Unknown Instant Executor");
    }
    
    public InstantJobExecutor(final String name) {
        this.name = name;
    }
    
    @Override
    public void add(final Job job) {
        try {
            job.run();
        }
        catch (Throwable t) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Main thread job executor \"");
            sb.append(this.name);
            sb.append("\" failed to execute job with pending exception.");
            UserDialog.dialog("NON-FATAL ERROR", sb.toString(), t, false);
        }
    }
}
