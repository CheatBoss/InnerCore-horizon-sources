package com.google.android.gms.internal.measurement;

final class zzxc
{
    private static final zzxa zzcbq;
    private static final zzxa zzcbr;
    
    static {
        zzcbq = zzxm();
        zzcbr = new zzxb();
    }
    
    static zzxa zzxk() {
        return zzxc.zzcbq;
    }
    
    static zzxa zzxl() {
        return zzxc.zzcbr;
    }
    
    private static zzxa zzxm() {
        try {
            return (zzxa)Class.forName("com.google.protobuf.NewInstanceSchemaFull").getDeclaredConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]);
        }
        catch (Exception ex) {
            return null;
        }
    }
}
