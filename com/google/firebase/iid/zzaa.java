package com.google.firebase.iid;

import android.content.*;
import com.google.android.gms.tasks.*;
import android.util.*;
import java.util.concurrent.*;
import android.os.*;

public final class zzaa
{
    private static zzaa zzbq;
    private final ScheduledExecutorService zzbr;
    private zzac zzbs;
    private int zzbt;
    private final Context zzv;
    
    private zzaa(final Context context, final ScheduledExecutorService zzbr) {
        this.zzbs = new zzac(this, null);
        this.zzbt = 1;
        this.zzbr = zzbr;
        this.zzv = context.getApplicationContext();
    }
    
    private final <T> Task<T> zza(final zzaj<T> zzaj) {
        synchronized (this) {
            if (Log.isLoggable("MessengerIpcClient", 3)) {
                final String value = String.valueOf(zzaj);
                final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 9);
                sb.append("Queueing ");
                sb.append(value);
                Log.d("MessengerIpcClient", sb.toString());
            }
            if (!this.zzbs.zzb(zzaj)) {
                (this.zzbs = new zzac(this, null)).zzb(zzaj);
            }
            return zzaj.zzcd.getTask();
        }
    }
    
    public static zzaa zzc(final Context context) {
        synchronized (zzaa.class) {
            if (zzaa.zzbq == null) {
                zzaa.zzbq = new zzaa(context, Executors.newSingleThreadScheduledExecutor());
            }
            return zzaa.zzbq;
        }
    }
    
    private final int zzw() {
        synchronized (this) {
            return this.zzbt++;
        }
    }
    
    public final Task<Void> zza(final int n, final Bundle bundle) {
        return this.zza((zzaj<Void>)new zzai(this.zzw(), 2, bundle));
    }
    
    public final Task<Bundle> zzb(final int n, final Bundle bundle) {
        return this.zza((zzaj<Bundle>)new zzal(this.zzw(), 1, bundle));
    }
}
