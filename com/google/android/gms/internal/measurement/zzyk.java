package com.google.android.gms.internal.measurement;

final class zzyk
{
    private static void zza(final byte b, final byte b2, final byte b3, final byte b4, final char[] array, final int n) throws zzvt {
        if (!zzg(b2) && (b << 28) + (b2 + 112) >> 30 == 0 && !zzg(b3) && !zzg(b4)) {
            final int n2 = (b & 0x7) << 18 | (b2 & 0x3F) << 12 | (b3 & 0x3F) << 6 | (b4 & 0x3F);
            array[n] = (char)((n2 >>> 10) + 55232);
            array[n + 1] = (char)((n2 & 0x3FF) + 56320);
            return;
        }
        throw zzvt.zzwr();
    }
    
    private static void zza(final byte b, final byte b2, final byte b3, final char[] array, final int n) throws zzvt {
        if (!zzg(b2) && (b != -32 || b2 >= -96) && (b != -19 || b2 < -96) && !zzg(b3)) {
            array[n] = (char)((b & 0xF) << 12 | (b2 & 0x3F) << 6 | (b3 & 0x3F));
            return;
        }
        throw zzvt.zzwr();
    }
    
    private static void zza(final byte b, final byte b2, final char[] array, final int n) throws zzvt {
        if (b >= -62 && !zzg(b2)) {
            array[n] = (char)((b & 0x1F) << 6 | (b2 & 0x3F));
            return;
        }
        throw zzvt.zzwr();
    }
    
    private static void zza(final byte b, final char[] array, final int n) {
        array[n] = (char)b;
    }
    
    private static boolean zzd(final byte b) {
        return b >= 0;
    }
    
    private static boolean zze(final byte b) {
        return b < -32;
    }
    
    private static boolean zzf(final byte b) {
        return b < -16;
    }
    
    private static boolean zzg(final byte b) {
        return b > -65;
    }
}
