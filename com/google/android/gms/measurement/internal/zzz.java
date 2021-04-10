package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;

final class zzz
{
    final String name;
    final long zzaie;
    final long zzaif;
    final long zzaig;
    final long zzaih;
    final Long zzaii;
    final Long zzaij;
    final Long zzaik;
    final Boolean zzail;
    final String zztt;
    
    zzz(final String zztt, final String name, final long zzaie, final long zzaif, final long zzaig, final long zzaih, final Long zzaii, final Long zzaij, final Long zzaik, final Boolean zzail) {
        Preconditions.checkNotEmpty(zztt);
        Preconditions.checkNotEmpty(name);
        final boolean b = false;
        Preconditions.checkArgument(zzaie >= 0L);
        Preconditions.checkArgument(zzaif >= 0L);
        boolean b2 = b;
        if (zzaih >= 0L) {
            b2 = true;
        }
        Preconditions.checkArgument(b2);
        this.zztt = zztt;
        this.name = name;
        this.zzaie = zzaie;
        this.zzaif = zzaif;
        this.zzaig = zzaig;
        this.zzaih = zzaih;
        this.zzaii = zzaii;
        this.zzaij = zzaij;
        this.zzaik = zzaik;
        this.zzail = zzail;
    }
    
    final zzz zza(final long n, final long n2) {
        return new zzz(this.zztt, this.name, this.zzaie, this.zzaif, this.zzaig, n, n2, this.zzaij, this.zzaik, this.zzail);
    }
    
    final zzz zza(final Long n, final Long n2, Boolean b) {
        if (b != null && !b) {
            b = null;
        }
        return new zzz(this.zztt, this.name, this.zzaie, this.zzaif, this.zzaig, this.zzaih, this.zzaii, n, n2, b);
    }
    
    final zzz zzai(final long n) {
        return new zzz(this.zztt, this.name, this.zzaie, this.zzaif, n, this.zzaih, this.zzaii, this.zzaij, this.zzaik, this.zzail);
    }
    
    final zzz zziu() {
        return new zzz(this.zztt, this.name, this.zzaie + 1L, this.zzaif + 1L, this.zzaig, this.zzaih, this.zzaii, this.zzaij, this.zzaik, this.zzail);
    }
}
