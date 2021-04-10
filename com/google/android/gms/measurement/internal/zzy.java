package com.google.android.gms.measurement.internal;

import android.os.*;
import com.google.android.gms.common.internal.*;
import android.text.*;
import java.util.*;

public final class zzy
{
    final String name;
    private final String origin;
    final long timestamp;
    final long zzaic;
    final zzaa zzaid;
    final String zztt;
    
    zzy(final zzbt zzbt, final String s, final String zztt, String s2, final long timestamp, final long zzaic, final Bundle bundle) {
        Preconditions.checkNotEmpty(zztt);
        Preconditions.checkNotEmpty(s2);
        this.zztt = zztt;
        this.name = s2;
        s2 = s;
        if (TextUtils.isEmpty((CharSequence)s)) {
            s2 = null;
        }
        this.origin = s2;
        this.timestamp = timestamp;
        this.zzaic = zzaic;
        if (zzaic != 0L && zzaic > timestamp) {
            zzbt.zzgo().zzjg().zzg("Event created with reverse previous/current timestamps. appId", zzap.zzbv(zztt));
        }
        zzaa zzaid;
        if (bundle != null && !bundle.isEmpty()) {
            final Bundle bundle2 = new Bundle(bundle);
            final Iterator iterator = bundle2.keySet().iterator();
            while (iterator.hasNext()) {
                s2 = iterator.next();
                if (s2 == null) {
                    zzbt.zzgo().zzjd().zzbx("Param name can't be null");
                }
                else {
                    final Object zzh = zzbt.zzgm().zzh(s2, bundle2.get(s2));
                    if (zzh != null) {
                        zzbt.zzgm().zza(bundle2, s2, zzh);
                        continue;
                    }
                    zzbt.zzgo().zzjg().zzg("Param value can't be null", zzbt.zzgl().zzbt(s2));
                }
                iterator.remove();
            }
            zzaid = new zzaa(bundle2);
        }
        else {
            zzaid = new zzaa(new Bundle());
        }
        this.zzaid = zzaid;
    }
    
    private zzy(final zzbt zzbt, final String s, final String zztt, final String name, final long timestamp, final long zzaic, final zzaa zzaid) {
        Preconditions.checkNotEmpty(zztt);
        Preconditions.checkNotEmpty(name);
        Preconditions.checkNotNull(zzaid);
        this.zztt = zztt;
        this.name = name;
        String origin = s;
        if (TextUtils.isEmpty((CharSequence)s)) {
            origin = null;
        }
        this.origin = origin;
        this.timestamp = timestamp;
        this.zzaic = zzaic;
        if (zzaic != 0L && zzaic > timestamp) {
            zzbt.zzgo().zzjg().zze("Event created with reverse previous/current timestamps. appId, name", zzap.zzbv(zztt), zzap.zzbv(name));
        }
        this.zzaid = zzaid;
    }
    
    @Override
    public final String toString() {
        final String zztt = this.zztt;
        final String name = this.name;
        final String value = String.valueOf(this.zzaid);
        final StringBuilder sb = new StringBuilder(String.valueOf(zztt).length() + 33 + String.valueOf(name).length() + String.valueOf(value).length());
        sb.append("Event{appId='");
        sb.append(zztt);
        sb.append("', name='");
        sb.append(name);
        sb.append("', params=");
        sb.append(value);
        sb.append('}');
        return sb.toString();
    }
    
    final zzy zza(final zzbt zzbt, final long n) {
        return new zzy(zzbt, this.origin, this.zztt, this.name, this.timestamp, n, this.zzaid);
    }
}
