package com.google.android.gms.common;

import android.os.*;
import com.google.android.gms.common.internal.*;
import java.util.concurrent.*;
import android.content.*;

public class BlockingServiceConnection implements ServiceConnection
{
    private boolean zzaj;
    private final BlockingQueue<IBinder> zzak;
    
    public BlockingServiceConnection() {
        this.zzaj = false;
        this.zzak = new LinkedBlockingQueue<IBinder>();
    }
    
    public IBinder getServiceWithTimeout(final long n, final TimeUnit timeUnit) throws InterruptedException, TimeoutException {
        Preconditions.checkNotMainThread("BlockingServiceConnection.getServiceWithTimeout() called on main thread");
        if (this.zzaj) {
            throw new IllegalStateException("Cannot call get on this connection more than once");
        }
        this.zzaj = true;
        final IBinder binder = this.zzak.poll(n, timeUnit);
        if (binder != null) {
            return binder;
        }
        throw new TimeoutException("Timed out waiting for the service connection");
    }
    
    public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
        this.zzak.add(binder);
    }
    
    public void onServiceDisconnected(final ComponentName componentName) {
    }
}
