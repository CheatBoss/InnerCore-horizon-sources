package com.google.android.gms.internal.measurement;

import java.util.*;

public class zzuz
{
    private static volatile boolean zzbvj;
    private static final Class<?> zzbvk;
    private static volatile zzuz zzbvl;
    static final zzuz zzbvm;
    private final Map<zza, zzvm.zzd<?, ?>> zzbvn;
    
    static {
        zzbvk = zzvn();
        zzbvm = new zzuz(true);
    }
    
    zzuz() {
        this.zzbvn = new HashMap<zza, zzvm.zzd<?, ?>>();
    }
    
    private zzuz(final boolean b) {
        this.zzbvn = Collections.emptyMap();
    }
    
    static zzuz zzvm() {
        return zzvk.zzd(zzuz.class);
    }
    
    private static Class<?> zzvn() {
        try {
            return Class.forName("com.google.protobuf.Extension");
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    public static zzuz zzvo() {
        return zzuy.zzvl();
    }
    
    public static zzuz zzvp() {
        final zzuz zzbvl = zzuz.zzbvl;
        if (zzbvl == null) {
            synchronized (zzuz.class) {
                zzuz zzbvl2;
                if ((zzbvl2 = zzuz.zzbvl) == null) {
                    zzbvl2 = (zzuz.zzbvl = zzuy.zzvm());
                }
                return zzbvl2;
            }
        }
        return zzbvl;
    }
    
    public final <ContainingType extends zzwt> zzvm.zzd<ContainingType, ?> zza(final ContainingType containingType, final int n) {
        return (zzvm.zzd<ContainingType, ?>)(zzvm.zzd)this.zzbvn.get(new zza(containingType, n));
    }
    
    static final class zza
    {
        private final int number;
        private final Object object;
        
        zza(final Object object, final int number) {
            this.object = object;
            this.number = number;
        }
        
        @Override
        public final boolean equals(final Object o) {
            if (!(o instanceof zza)) {
                return false;
            }
            final zza zza = (zza)o;
            return this.object == zza.object && this.number == zza.number;
        }
        
        @Override
        public final int hashCode() {
            return System.identityHashCode(this.object) * 65535 + this.number;
        }
    }
}
