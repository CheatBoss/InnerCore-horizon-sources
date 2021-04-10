package com.google.firebase.iid;

import android.app.*;
import com.google.android.gms.common.util.concurrent.*;
import java.util.concurrent.*;
import android.content.*;
import android.support.v4.content.*;
import android.os.*;
import android.util.*;

public abstract class zzb extends Service
{
    private final Object lock;
    final ExecutorService zzh;
    private Binder zzi;
    private int zzj;
    private int zzk;
    
    public zzb() {
        final String value = String.valueOf(this.getClass().getSimpleName());
        String concat;
        if (value.length() != 0) {
            concat = "Firebase-".concat(value);
        }
        else {
            concat = new String("Firebase-");
        }
        this.zzh = Executors.newSingleThreadExecutor(new NamedThreadFactory(concat));
        this.lock = new Object();
        this.zzk = 0;
    }
    
    private final void zza(final Intent intent) {
        if (intent != null) {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
        synchronized (this.lock) {
            final int zzk = this.zzk - 1;
            this.zzk = zzk;
            if (zzk == 0) {
                this.stopSelfResult(this.zzj);
            }
        }
    }
    
    public final IBinder onBind(final Intent intent) {
        synchronized (this) {
            if (Log.isLoggable("EnhancedIntentService", 3)) {
                Log.d("EnhancedIntentService", "Service received bind request");
            }
            if (this.zzi == null) {
                this.zzi = new zzf(this);
            }
            return (IBinder)this.zzi;
        }
    }
    
    public final int onStartCommand(final Intent intent, final int n, final int zzj) {
        Object o = this.lock;
        synchronized (o) {
            this.zzj = zzj;
            ++this.zzk;
            // monitorexit(o)
            o = this.zzb(intent);
            if (o == null) {
                this.zza(intent);
                return 2;
            }
            if (this.zzc((Intent)o)) {
                this.zza(intent);
                return 2;
            }
            this.zzh.execute(new zzc(this, (Intent)o, intent));
            return 3;
        }
    }
    
    protected Intent zzb(final Intent intent) {
        return intent;
    }
    
    public boolean zzc(final Intent intent) {
        return false;
    }
    
    public abstract void zzd(final Intent p0);
}
