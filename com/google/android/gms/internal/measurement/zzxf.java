package com.google.android.gms.internal.measurement;

import java.util.concurrent.*;

final class zzxf
{
    private static final zzxf zzcbs;
    private final zzxk zzcbt;
    private final ConcurrentMap<Class<?>, zzxj<?>> zzcbu;
    
    static {
        zzcbs = new zzxf();
    }
    
    private zzxf() {
        this.zzcbu = new ConcurrentHashMap<Class<?>, zzxj<?>>();
        zzxk zzxk = null;
        zzxk zzgb;
        for (int i = 0; i <= 0; ++i, zzxk = zzgb) {
            zzgb = zzgb((new String[] { "com.google.protobuf.AndroidProto3SchemaFactory" })[0]);
            if ((zzxk = zzgb) != null) {
                break;
            }
        }
        zzxk zzcbt;
        if ((zzcbt = zzxk) == null) {
            zzcbt = new zzwi();
        }
        this.zzcbt = zzcbt;
    }
    
    private static zzxk zzgb(final String s) {
        try {
            return (zzxk)Class.forName(s).getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        finally {
            return null;
        }
    }
    
    public static zzxf zzxn() {
        return zzxf.zzcbs;
    }
    
    public final <T> zzxj<T> zzag(final T t) {
        return this.zzi(t.getClass());
    }
    
    public final <T> zzxj<T> zzi(final Class<T> clazz) {
        zzvo.zza(clazz, "messageType");
        zzxj<?> zzh;
        if ((zzh = this.zzcbu.get(clazz)) == null) {
            zzh = this.zzcbt.zzh(clazz);
            zzvo.zza(clazz, "messageType");
            zzvo.zza(zzh, "schema");
            final zzxj<?> zzxj = this.zzcbu.putIfAbsent(clazz, zzh);
            if (zzxj != null) {
                zzh = zzxj;
            }
        }
        return (zzxj<T>)zzh;
    }
}
