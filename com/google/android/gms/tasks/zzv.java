package com.google.android.gms.tasks;

import java.util.concurrent.*;

final class zzv implements Runnable
{
    private final /* synthetic */ Callable val$callable;
    private final /* synthetic */ zzu zzagj;
    
    @Override
    public final void run() {
        try {
            this.zzagj.setResult(this.val$callable.call());
        }
        catch (Exception exception) {
            this.zzagj.setException(exception);
        }
    }
}
