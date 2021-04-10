package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.*;
import com.google.android.gms.common.internal.*;

final class zzl
{
    private final int zzet;
    private final ConnectionResult zzeu;
    
    zzl(final ConnectionResult zzeu, final int zzet) {
        Preconditions.checkNotNull(zzeu);
        this.zzeu = zzeu;
        this.zzet = zzet;
    }
    
    final ConnectionResult getConnectionResult() {
        return this.zzeu;
    }
    
    final int zzu() {
        return this.zzet;
    }
}
