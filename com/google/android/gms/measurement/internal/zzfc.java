package com.google.android.gms.measurement.internal;

import java.util.*;

final class zzfc implements zzav
{
    private final /* synthetic */ zzfa zzaty;
    private final /* synthetic */ String zzatz;
    
    zzfc(final zzfa zzaty, final String zzatz) {
        this.zzaty = zzaty;
        this.zzatz = zzatz;
    }
    
    @Override
    public final void zza(final String s, final int n, final Throwable t, final byte[] array, final Map<String, List<String>> map) {
        this.zzaty.zza(n, t, array, this.zzatz);
    }
}
