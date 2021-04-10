package com.google.android.gms.common.util.concurrent;

import java.util.concurrent.*;
import com.google.android.gms.common.internal.*;

public class NamedThreadFactory implements ThreadFactory
{
    private final String name;
    private final int priority;
    private final ThreadFactory zzaau;
    
    public NamedThreadFactory(final String s) {
        this(s, 0);
    }
    
    public NamedThreadFactory(final String s, final int priority) {
        this.zzaau = Executors.defaultThreadFactory();
        this.name = Preconditions.checkNotNull(s, "Name must not be null");
        this.priority = priority;
    }
    
    @Override
    public Thread newThread(final Runnable runnable) {
        final Thread thread = this.zzaau.newThread(new zza(runnable, this.priority));
        thread.setName(this.name);
        return thread;
    }
}
