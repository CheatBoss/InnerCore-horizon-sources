package com.google.firebase.iid;

import java.util.concurrent.*;
import java.util.*;
import android.util.*;
import com.google.android.gms.common.stats.*;
import android.os.*;
import android.content.*;

public final class zzh implements ServiceConnection
{
    private boolean zzaa;
    private final Context zzv;
    private final Intent zzw;
    private final ScheduledExecutorService zzx;
    private final Queue<zzd> zzy;
    private zzf zzz;
    
    public zzh(final Context context, final String s) {
        this(context, s, new ScheduledThreadPoolExecutor(0));
    }
    
    private zzh(final Context context, final String s, final ScheduledExecutorService zzx) {
        this.zzy = new ArrayDeque<zzd>();
        this.zzaa = false;
        this.zzv = context.getApplicationContext();
        this.zzw = new Intent(s).setPackage(this.zzv.getPackageName());
        this.zzx = zzx;
    }
    
    private final void zzc() {
        synchronized (this) {
            if (Log.isLoggable("EnhancedIntentService", 3)) {
                Log.d("EnhancedIntentService", "flush queue called");
            }
            while (!this.zzy.isEmpty()) {
                if (Log.isLoggable("EnhancedIntentService", 3)) {
                    Log.d("EnhancedIntentService", "found intent to be delivered");
                }
                if (this.zzz == null || !this.zzz.isBinderAlive()) {
                    if (Log.isLoggable("EnhancedIntentService", 3)) {
                        final boolean zzaa = this.zzaa;
                        final StringBuilder sb = new StringBuilder(39);
                        sb.append("binder is dead. start connection? ");
                        sb.append(zzaa ^ true);
                        Log.d("EnhancedIntentService", sb.toString());
                    }
                    if (!this.zzaa) {
                        this.zzaa = true;
                        try {
                            if (ConnectionTracker.getInstance().bindService(this.zzv, this.zzw, (ServiceConnection)this, 65)) {
                                return;
                            }
                            Log.e("EnhancedIntentService", "binding to the service failed");
                        }
                        catch (SecurityException ex) {
                            Log.e("EnhancedIntentService", "Exception while binding the service", (Throwable)ex);
                        }
                        while (!this.zzy.isEmpty()) {
                            this.zzy.poll().finish();
                        }
                    }
                    return;
                }
                if (Log.isLoggable("EnhancedIntentService", 3)) {
                    Log.d("EnhancedIntentService", "binder is alive, sending the intent.");
                }
                this.zzz.zza(this.zzy.poll());
            }
        }
    }
    
    public final void onServiceConnected(final ComponentName componentName, final IBinder binder) {
        synchronized (this) {
            this.zzaa = false;
            this.zzz = (zzf)binder;
            if (Log.isLoggable("EnhancedIntentService", 3)) {
                final String value = String.valueOf(componentName);
                final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 20);
                sb.append("onServiceConnected: ");
                sb.append(value);
                Log.d("EnhancedIntentService", sb.toString());
            }
            this.zzc();
        }
    }
    
    public final void onServiceDisconnected(final ComponentName componentName) {
        if (Log.isLoggable("EnhancedIntentService", 3)) {
            final String value = String.valueOf(componentName);
            final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 23);
            sb.append("onServiceDisconnected: ");
            sb.append(value);
            Log.d("EnhancedIntentService", sb.toString());
        }
        this.zzc();
    }
    
    public final void zza(final Intent intent, final BroadcastReceiver$PendingResult broadcastReceiver$PendingResult) {
        synchronized (this) {
            if (Log.isLoggable("EnhancedIntentService", 3)) {
                Log.d("EnhancedIntentService", "new intent queued in the bind-strategy delivery");
            }
            this.zzy.add(new zzd(intent, broadcastReceiver$PendingResult, this.zzx));
            this.zzc();
        }
    }
}
