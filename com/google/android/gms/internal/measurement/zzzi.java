package com.google.android.gms.internal.measurement;

import java.util.*;

final class zzzi
{
    final int tag;
    final byte[] zzbug;
    
    zzzi(final int tag, final byte[] zzbug) {
        this.tag = tag;
        this.zzbug = zzbug;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof zzzi)) {
            return false;
        }
        final zzzi zzzi = (zzzi)o;
        return this.tag == zzzi.tag && Arrays.equals(this.zzbug, zzzi.zzbug);
    }
    
    @Override
    public final int hashCode() {
        return (this.tag + 527) * 31 + Arrays.hashCode(this.zzbug);
    }
}
