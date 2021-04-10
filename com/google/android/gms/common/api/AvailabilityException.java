package com.google.android.gms.common.api;

import android.support.v4.util.*;
import com.google.android.gms.common.api.internal.*;
import com.google.android.gms.common.*;
import android.text.*;
import java.util.*;

public class AvailabilityException extends Exception
{
    private final ArrayMap<zzh<?>, ConnectionResult> zzcc;
    
    public AvailabilityException(final ArrayMap<zzh<?>, ConnectionResult> zzcc) {
        this.zzcc = zzcc;
    }
    
    @Override
    public String getMessage() {
        final ArrayList<String> list = new ArrayList<String>();
        final Iterator<zzh<?>> iterator = this.zzcc.keySet().iterator();
        boolean b = true;
        while (iterator.hasNext()) {
            final zzh<?> zzh = iterator.next();
            final ConnectionResult connectionResult = this.zzcc.get(zzh);
            if (connectionResult.isSuccess()) {
                b = false;
            }
            final String zzq = zzh.zzq();
            final String value = String.valueOf(connectionResult);
            final StringBuilder sb = new StringBuilder(String.valueOf(zzq).length() + 2 + String.valueOf(value).length());
            sb.append(zzq);
            sb.append(": ");
            sb.append(value);
            list.add(sb.toString());
        }
        final StringBuilder sb2 = new StringBuilder();
        String s;
        if (b) {
            s = "None of the queried APIs are available. ";
        }
        else {
            s = "Some of the queried APIs are unavailable. ";
        }
        sb2.append(s);
        sb2.append(TextUtils.join((CharSequence)"; ", (Iterable)list));
        return sb2.toString();
    }
}
