package org.spongycastle.pqc.math.linearalgebra;

public final class CharUtils
{
    private CharUtils() {
    }
    
    public static char[] clone(final char[] array) {
        final char[] array2 = new char[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static boolean equals(final char[] array, final char[] array2) {
        if (array.length != array2.length) {
            return false;
        }
        int i = array.length - 1;
        boolean b = true;
        while (i >= 0) {
            b &= (array[i] == array2[i]);
            --i;
        }
        return b;
    }
    
    public static byte[] toByteArray(final char[] array) {
        final byte[] array2 = new byte[array.length];
        int length = array.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            array2[length] = (byte)array[length];
        }
        return array2;
    }
    
    public static byte[] toByteArrayForPBE(final char[] array) {
        final int length = array.length;
        final byte[] array2 = new byte[length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = (byte)array[i];
        }
        final int n = length * 2;
        final byte[] array3 = new byte[n + 2];
        for (int j = 0; j < length; ++j) {
            final int n2 = j * 2;
            array3[n2] = 0;
            array3[n2 + 1] = array2[j];
        }
        array3[n + 1] = (array3[n] = 0);
        return array3;
    }
}
