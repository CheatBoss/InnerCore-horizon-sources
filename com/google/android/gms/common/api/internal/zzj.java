package com.google.android.gms.common.api.internal;

import android.support.v4.util.*;
import com.google.android.gms.common.*;
import com.google.android.gms.tasks.*;
import com.google.android.gms.common.api.*;
import java.util.*;

public final class zzj
{
    private final ArrayMap<zzh<?>, ConnectionResult> zzcc;
    private final ArrayMap<zzh<?>, String> zzei;
    private final TaskCompletionSource<Map<zzh<?>, String>> zzej;
    private int zzek;
    private boolean zzel;
    
    public final void zza(final zzh<?> zzh, final ConnectionResult connectionResult, final String s) {
        this.zzcc.put(zzh, connectionResult);
        this.zzei.put(zzh, s);
        --this.zzek;
        if (!connectionResult.isSuccess()) {
            this.zzel = true;
        }
        if (this.zzek == 0) {
            if (this.zzel) {
                this.zzej.setException(new AvailabilityException(this.zzcc));
                return;
            }
            this.zzej.setResult(this.zzei);
        }
    }
    
    public final Set<zzh<?>> zzs() {
        return this.zzcc.keySet();
    }
}
