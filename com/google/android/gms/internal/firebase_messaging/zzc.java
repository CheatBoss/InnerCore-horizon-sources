package com.google.android.gms.internal.firebase_messaging;

import java.util.concurrent.*;
import java.util.*;
import java.lang.ref.*;

final class zzc
{
    private final ConcurrentHashMap<zzd, List<Throwable>> zzd;
    private final ReferenceQueue<Throwable> zze;
    
    zzc() {
        this.zzd = new ConcurrentHashMap<zzd, List<Throwable>>(16, 0.75f, 10);
        this.zze = new ReferenceQueue<Throwable>();
    }
    
    public final List<Throwable> zza(final Throwable t, final boolean b) {
        while (true) {
            final Reference<? extends Throwable> poll = this.zze.poll();
            if (poll == null) {
                break;
            }
            this.zzd.remove(poll);
        }
        final List<Throwable> list = this.zzd.get(new zzd(t, null));
        if (list != null) {
            return list;
        }
        final Vector<Throwable> vector = new Vector<Throwable>(2);
        final Vector<Throwable> vector2 = this.zzd.putIfAbsent(new zzd(t, this.zze), vector);
        if (vector2 == null) {
            return vector;
        }
        return vector2;
    }
}
