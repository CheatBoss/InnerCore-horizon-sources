package com.google.android.gms.internal.measurement;

final class zzua
{
    private static final Class<?> zzbtv;
    private static final boolean zzbtw;
    
    static {
        zzbtv = zzfu("libcore.io.Memory");
        zzbtw = (zzfu("org.robolectric.Robolectric") != null);
    }
    
    private static <T> Class<T> zzfu(final String s) {
        try {
            return (Class<T>)Class.forName(s);
        }
        finally {
            return null;
        }
    }
    
    static boolean zzty() {
        return zzua.zzbtv != null && !zzua.zzbtw;
    }
    
    static Class<?> zztz() {
        return zzua.zzbtv;
    }
}
