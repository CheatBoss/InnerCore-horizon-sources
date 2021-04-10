package com.zhekasmirnov.apparatus.job;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;

@SynthesizedClassMap({ -$$Lambda$MainThreadJobExecutor$xg21-rtOVC7VlBJiAmNHsaZbR7I.class })
public class MainThreadJobExecutor implements JobExecutor
{
    private final String name;
    private final MainThreadQueue threadQueue;
    
    public MainThreadJobExecutor(final MainThreadQueue mainThreadQueue) {
        this(mainThreadQueue, "UnnamedJobExecutor");
    }
    
    public MainThreadJobExecutor(final MainThreadQueue threadQueue, final String name) {
        this.threadQueue = threadQueue;
        this.name = name;
    }
    
    @Override
    public void add(final Job job) {
        this.threadQueue.enqueue(new -$$Lambda$MainThreadJobExecutor$xg21-rtOVC7VlBJiAmNHsaZbR7I(this, job));
    }
}
