package com.google.firebase.iid;

import java.util.concurrent.*;

final class zzj implements Executor
{
    static final Executor zzac;
    
    static {
        zzac = new zzj();
    }
    
    private zzj() {
    }
    
    @Override
    public final void execute(final Runnable runnable) {
        runnable.run();
    }
}
