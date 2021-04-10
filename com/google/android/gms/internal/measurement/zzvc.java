package com.google.android.gms.internal.measurement;

final class zzvc
{
    private static final zzva<?> zzbvo;
    private static final zzva<?> zzbvp;
    
    static {
        zzbvo = new zzvb();
        zzbvp = zzvq();
    }
    
    private static zzva<?> zzvq() {
        try {
            return (zzva<?>)Class.forName("com.google.protobuf.ExtensionSchemaFull").getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    static zzva<?> zzvr() {
        return zzvc.zzbvo;
    }
    
    static zzva<?> zzvs() {
        final zzva<?> zzbvp = zzvc.zzbvp;
        if (zzbvp != null) {
            return zzbvp;
        }
        throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
    }
}
