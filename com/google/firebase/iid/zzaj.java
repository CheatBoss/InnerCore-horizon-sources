package com.google.firebase.iid;

import com.google.android.gms.tasks.*;
import android.os.*;
import android.util.*;

abstract class zzaj<T>
{
    final int what;
    final int zzcc;
    final TaskCompletionSource<T> zzcd;
    final Bundle zzce;
    
    zzaj(final int zzcc, final int what, final Bundle zzce) {
        this.zzcd = new TaskCompletionSource<T>();
        this.zzcc = zzcc;
        this.what = what;
        this.zzce = zzce;
    }
    
    final void finish(final T result) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            final String value = String.valueOf(this);
            final String value2 = String.valueOf(result);
            final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 16 + String.valueOf(value2).length());
            sb.append("Finishing ");
            sb.append(value);
            sb.append(" with ");
            sb.append(value2);
            Log.d("MessengerIpcClient", sb.toString());
        }
        this.zzcd.setResult(result);
    }
    
    @Override
    public String toString() {
        final int what = this.what;
        final int zzcc = this.zzcc;
        final boolean zzaa = this.zzaa();
        final StringBuilder sb = new StringBuilder(55);
        sb.append("Request { what=");
        sb.append(what);
        sb.append(" id=");
        sb.append(zzcc);
        sb.append(" oneWay=");
        sb.append(zzaa);
        sb.append("}");
        return sb.toString();
    }
    
    final void zza(final zzak exception) {
        if (Log.isLoggable("MessengerIpcClient", 3)) {
            final String value = String.valueOf(this);
            final String value2 = String.valueOf(exception);
            final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 14 + String.valueOf(value2).length());
            sb.append("Failing ");
            sb.append(value);
            sb.append(" with ");
            sb.append(value2);
            Log.d("MessengerIpcClient", sb.toString());
        }
        this.zzcd.setException(exception);
    }
    
    abstract boolean zzaa();
    
    abstract void zzb(final Bundle p0);
}
