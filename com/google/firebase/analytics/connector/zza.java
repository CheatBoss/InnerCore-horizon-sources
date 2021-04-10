package com.google.firebase.analytics.connector;

import java.util.concurrent.*;

final class zza implements Executor
{
    static final Executor zzbsi;
    
    static {
        zzbsi = new zza();
    }
    
    private zza() {
    }
    
    @Override
    public final void execute(final Runnable runnable) {
        runnable.run();
    }
}
