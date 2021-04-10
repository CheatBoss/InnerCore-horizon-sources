package com.google.android.gms.internal.measurement;

import java.io.*;

public final class zzwl<K, V>
{
    static <K, V> int zza(final zzwm<K, V> zzwm, final K k, final V v) {
        return zzvd.zza(zzwm.zzcar, 1, k) + zzvd.zza(zzwm.zzcat, 2, v);
    }
    
    static <K, V> void zza(final zzut zzut, final zzwm<K, V> zzwm, final K k, final V v) throws IOException {
        zzvd.zza(zzut, zzwm.zzcar, 1, k);
        zzvd.zza(zzut, zzwm.zzcat, 2, v);
    }
}
