package com.google.android.gms.tasks;

import java.util.concurrent.*;
import android.os.*;

public final class TaskExecutors
{
    public static final Executor MAIN_THREAD;
    static final Executor zzagd;
    
    static {
        MAIN_THREAD = new zza();
        zzagd = new zzt();
    }
    
    private static final class zza implements Executor
    {
        private final Handler mHandler;
        
        public zza() {
            this.mHandler = new Handler(Looper.getMainLooper());
        }
        
        @Override
        public final void execute(final Runnable runnable) {
            this.mHandler.post(runnable);
        }
    }
}
