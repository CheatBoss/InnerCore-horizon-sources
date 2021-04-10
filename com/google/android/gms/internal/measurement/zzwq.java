package com.google.android.gms.internal.measurement;

final class zzwq
{
    private static final zzwo zzcav;
    private static final zzwo zzcaw;
    
    static {
        zzcav = zzxf();
        zzcaw = new zzwp();
    }
    
    static zzwo zzxd() {
        return zzwq.zzcav;
    }
    
    static zzwo zzxe() {
        return zzwq.zzcaw;
    }
    
    private static zzwo zzxf() {
        try {
            return (zzwo)Class.forName("com.google.protobuf.MapFieldSchemaFull").getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (Exception ex) {
            return null;
        }
    }
}
