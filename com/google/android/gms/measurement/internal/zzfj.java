package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;

final class zzfj
{
    final String name;
    final String origin;
    final Object value;
    final long zzaue;
    final String zztt;
    
    zzfj(final String zztt, final String origin, final String name, final long zzaue, final Object value) {
        Preconditions.checkNotEmpty(zztt);
        Preconditions.checkNotEmpty(name);
        Preconditions.checkNotNull(value);
        this.zztt = zztt;
        this.origin = origin;
        this.name = name;
        this.zzaue = zzaue;
        this.value = value;
    }
}
