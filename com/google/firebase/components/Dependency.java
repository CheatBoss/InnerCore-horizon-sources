package com.google.firebase.components;

import com.google.android.gms.common.internal.*;

public final class Dependency
{
    private final Class<?> zza;
    private final int zzb;
    private final int zzc;
    
    private Dependency(final Class<?> clazz, final int zzb, final int zzc) {
        this.zza = Preconditions.checkNotNull(clazz, "Null dependency anInterface.");
        this.zzb = zzb;
        this.zzc = zzc;
    }
    
    public static Dependency required(final Class<?> clazz) {
        return new Dependency(clazz, 1, 0);
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o instanceof Dependency) {
            final Dependency dependency = (Dependency)o;
            if (this.zza == dependency.zza && this.zzb == dependency.zzb && this.zzc == dependency.zzc) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public final int hashCode() {
        return ((this.zza.hashCode() ^ 0xF4243) * 1000003 ^ this.zzb) * 1000003 ^ this.zzc;
    }
    
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder("Dependency{anInterface=");
        sb.append(this.zza);
        sb.append(", required=");
        final int zzb = this.zzb;
        final boolean b = false;
        sb.append(zzb == 1);
        sb.append(", direct=");
        boolean b2 = b;
        if (this.zzc == 0) {
            b2 = true;
        }
        sb.append(b2);
        sb.append("}");
        return sb.toString();
    }
    
    public final Class<?> zza() {
        return this.zza;
    }
    
    public final boolean zzb() {
        return this.zzb == 1;
    }
    
    public final boolean zzc() {
        return this.zzc == 0;
    }
}
