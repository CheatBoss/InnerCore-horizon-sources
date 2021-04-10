package com.google.firebase.iid;

import java.util.concurrent.*;

final class zzi
{
    private static final Executor zzab;
    
    static {
        zzab = zzj.zzac;
    }
    
    static Executor zzd() {
        return zzi.zzab;
    }
    
    static Executor zze() {
        return new ThreadPoolExecutor(0, 1, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
    }
}
