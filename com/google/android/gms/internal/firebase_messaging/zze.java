package com.google.android.gms.internal.firebase_messaging;

final class zze extends zzb
{
    private final zzc zzg;
    
    zze() {
        this.zzg = new zzc();
    }
    
    @Override
    public final void zza(final Throwable t, final Throwable t2) {
        if (t2 == t) {
            throw new IllegalArgumentException("Self suppression is not allowed.", t2);
        }
        if (t2 != null) {
            this.zzg.zza(t, true).add(t2);
            return;
        }
        throw new NullPointerException("The suppressed exception cannot be null.");
    }
}
