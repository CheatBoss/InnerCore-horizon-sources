package com.google.firebase.iid;

import java.util.*;
import android.support.v4.util.*;
import android.util.*;
import com.google.android.gms.tasks.*;

final class zzap
{
    private final Map<Pair<String, String>, Task<String>> zzcl;
    
    zzap() {
        this.zzcl = new ArrayMap<Pair<String, String>, Task<String>>();
    }
    
    final Task<String> zza(String value, String value2, final zzar zzar) {
        synchronized (this) {
            final Pair pair = new Pair((Object)value, (Object)value2);
            final Task<String> task = this.zzcl.get(pair);
            if (task != null) {
                if (Log.isLoggable("FirebaseInstanceId", 3)) {
                    value = String.valueOf(pair);
                    final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 29);
                    sb.append("Joining ongoing request for: ");
                    sb.append(value);
                    Log.d("FirebaseInstanceId", sb.toString());
                }
                return task;
            }
            if (Log.isLoggable("FirebaseInstanceId", 3)) {
                value2 = String.valueOf(pair);
                final StringBuilder sb2 = new StringBuilder(String.valueOf(value2).length() + 24);
                sb2.append("Making new request for: ");
                sb2.append(value2);
                Log.d("FirebaseInstanceId", sb2.toString());
            }
            final Task<String> continueWithTask = zzar.zzr().continueWithTask(zzi.zzd(), (Continuation<String, Task<String>>)new zzaq(this, pair));
            this.zzcl.put((Pair<String, String>)pair, continueWithTask);
            return continueWithTask;
        }
    }
}
