package org.spongycastle.pqc.math.linearalgebra;

public final class IntUtils
{
    private IntUtils() {
    }
    
    public static int[] clone(final int[] array) {
        final int[] array2 = new int[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static boolean equals(final int[] array, final int[] array2) {
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
    
    public static void fill(final int[] array, final int n) {
        int length = array.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            array[length] = n;
        }
    }
    
    private static int partition(final int[] array, int i, final int n, int n2) {
        final int n3 = array[n2];
        array[n2] = array[n];
        array[n] = n3;
        n2 = i;
        while (i < n) {
            int n4 = n2;
            if (array[i] <= n3) {
                final int n5 = array[n2];
                array[n2] = array[i];
                array[i] = n5;
                n4 = n2 + 1;
            }
            ++i;
            n2 = n4;
        }
        i = array[n2];
        array[n2] = array[n];
        array[n] = i;
        return n2;
    }
    
    public static void quicksort(final int[] array) {
        quicksort(array, 0, array.length - 1);
    }
    
    public static void quicksort(final int[] array, final int n, final int n2) {
        if (n2 > n) {
            final int partition = partition(array, n, n2, n2);
            quicksort(array, n, partition - 1);
            quicksort(array, partition + 1, n2);
        }
    }
    
    public static int[] subArray(final int[] array, final int n, int n2) {
        n2 -= n;
        final int[] array2 = new int[n2];
        System.arraycopy(array, n, array2, 0, n2);
        return array2;
    }
    
    public static String toHexString(final int[] array) {
        return ByteUtils.toHexString(BigEndianConversions.toByteArray(array));
    }
    
    public static String toString(final int[] array) {
        String string = "";
        for (int i = 0; i < array.length; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(array[i]);
            sb.append(" ");
            string = sb.toString();
        }
        return string;
    }
}
