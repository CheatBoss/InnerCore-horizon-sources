package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.*;
import java.util.*;
import com.google.android.gms.tasks.*;

public final class zzaa
{
    private final Map<BasePendingResult<?>, Boolean> zzgw;
    private final Map<TaskCompletionSource<?>, Boolean> zzgx;
    
    public zzaa() {
        this.zzgw = Collections.synchronizedMap(new WeakHashMap<BasePendingResult<?>, Boolean>());
        this.zzgx = Collections.synchronizedMap(new WeakHashMap<TaskCompletionSource<?>, Boolean>());
    }
    
    private final void zza(final boolean b, final Status status) {
        Object o = this.zzgw;
        synchronized (o) {
            final HashMap<Object, Object> hashMap = new HashMap<Object, Object>(this.zzgw);
            // monitorexit(o)
            Object zzgx = this.zzgx;
            synchronized (zzgx) {
                o = new HashMap<Object, Object>(this.zzgx);
                // monitorexit(zzgx)
                final Iterator<Map.Entry<Object, Object>> iterator = hashMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    zzgx = iterator.next();
                    if (b || ((Map.Entry<K, Boolean>)zzgx).getValue()) {
                        ((BasePendingResult)((Map.Entry)zzgx).getKey()).zzb(status);
                    }
                }
                o = ((Map<Object, Object>)o).entrySet().iterator();
                while (((Iterator)o).hasNext()) {
                    final Map.Entry<K, Boolean> entry = ((Iterator<Map.Entry<K, Boolean>>)o).next();
                    if (b || entry.getValue()) {
                        ((TaskCompletionSource)entry.getKey()).trySetException(new ApiException(status));
                    }
                }
            }
        }
    }
    
    final <TResult> void zza(final TaskCompletionSource<TResult> taskCompletionSource, final boolean b) {
        this.zzgx.put(taskCompletionSource, b);
        taskCompletionSource.getTask().addOnCompleteListener((OnCompleteListener<TResult>)new zzac(this, taskCompletionSource));
    }
    
    final boolean zzaj() {
        return !this.zzgw.isEmpty() || !this.zzgx.isEmpty();
    }
    
    public final void zzak() {
        this.zza(false, GoogleApiManager.zzjj);
    }
    
    public final void zzal() {
        this.zza(true, zzck.zzmm);
    }
}
