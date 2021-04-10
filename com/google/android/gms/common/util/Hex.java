package com.google.android.gms.common.util;

public class Hex
{
    private static final char[] zzaaa;
    private static final char[] zzzz;
    
    static {
        zzzz = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        zzaaa = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    }
    
    public static String bytesToStringLowercase(final byte[] array) {
        final char[] array2 = new char[array.length << 1];
        int i = 0;
        int n = 0;
        while (i < array.length) {
            final int n2 = array[i] & 0xFF;
            final int n3 = n + 1;
            final char[] zzaaa = Hex.zzaaa;
            array2[n] = zzaaa[n2 >>> 4];
            n = n3 + 1;
            array2[n3] = zzaaa[n2 & 0xF];
            ++i;
        }
        return new String(array2);
    }
}
