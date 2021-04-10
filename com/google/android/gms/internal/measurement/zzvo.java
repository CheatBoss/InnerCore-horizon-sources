package com.google.android.gms.internal.measurement;

import java.nio.charset.*;
import java.nio.*;

public final class zzvo
{
    private static final Charset ISO_8859_1;
    static final Charset UTF_8;
    public static final byte[] zzbzj;
    private static final ByteBuffer zzbzk;
    private static final zzuo zzbzl;
    
    static {
        UTF_8 = Charset.forName("UTF-8");
        ISO_8859_1 = Charset.forName("ISO-8859-1");
        zzbzk = ByteBuffer.wrap(zzbzj = new byte[0]);
        final byte[] zzbzj2 = zzvo.zzbzj;
        zzbzl = zzuo.zza(zzbzj2, 0, zzbzj2.length, false);
    }
    
    static <T> T checkNotNull(final T t) {
        if (t != null) {
            return t;
        }
        throw null;
    }
    
    public static int hashCode(final byte[] array) {
        final int length = array.length;
        int zza;
        if ((zza = zza(length, array, 0, length)) == 0) {
            zza = 1;
        }
        return zza;
    }
    
    static int zza(int i, final byte[] array, final int n, final int n2) {
        int n3 = i;
        for (i = n; i < n + n2; ++i) {
            n3 = n3 * 31 + array[i];
        }
        return n3;
    }
    
    static <T> T zza(final T t, final String s) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(s);
    }
    
    static Object zzb(final Object o, final Object o2) {
        return ((zzwt)o).zzwd().zza((zzwt)o2).zzwi();
    }
    
    public static int zzbf(final long n) {
        return (int)(n ^ n >>> 32);
    }
    
    static boolean zzf(final zzwt zzwt) {
        return false;
    }
    
    public static boolean zzl(final byte[] array) {
        return zzyj.zzl(array);
    }
    
    public static String zzm(final byte[] array) {
        return new String(array, zzvo.UTF_8);
    }
    
    public static int zzw(final boolean b) {
        if (b) {
            return 1231;
        }
        return 1237;
    }
}
