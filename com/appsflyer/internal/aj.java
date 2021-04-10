package com.appsflyer.internal;

public class aj
{
    public static void \u0131(int i, int n, final boolean b, int n2, final int[] array, final int[][] array2, final int[] array3) {
        if (!b) {
            \u0131(array);
        }
        int n3 = i;
        int n4;
        int n5;
        int n6;
        int n7;
        int n8;
        int n9;
        for (i = 0; i < n2; ++i, n9 = (n ^ (n7 ^ n5 + n6) + n8), n = n4, n3 = n9) {
            n4 = (n3 ^ array[i]);
            n5 = array2[0][n4 >>> 24];
            n6 = array2[1][n4 >>> 16 & 0xFF];
            n7 = array2[2][n4 >>> 8 & 0xFF];
            n8 = array2[3][n4 & 0xFF];
        }
        i = array[array.length - 2];
        n2 = array[array.length - 1];
        if (!b) {
            \u0131(array);
        }
        array3[0] = (n ^ n2);
        array3[1] = (i ^ n3);
    }
    
    private static void \u0131(final int[] array) {
        for (int i = 0; i < array.length / 2; ++i) {
            final int n = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = n;
        }
    }
}
