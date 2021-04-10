package com.google.android.gms.internal.firebase_messaging;

import java.lang.ref.*;

final class zzd extends WeakReference<Throwable>
{
    private final int zzf;
    
    public zzd(final Throwable t, final ReferenceQueue<Throwable> referenceQueue) {
        super(t, referenceQueue);
        if (t != null) {
            this.zzf = System.identityHashCode(t);
            return;
        }
        throw new NullPointerException("The referent cannot be null");
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o != null) {
            if (o.getClass() != this.getClass()) {
                return false;
            }
            if (this == o) {
                return true;
            }
            final zzd zzd = (zzd)o;
            if (this.zzf == zzd.zzf && this.get() == zzd.get()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public final int hashCode() {
        return this.zzf;
    }
}
