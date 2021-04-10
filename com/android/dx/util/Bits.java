package com.android.dx.util;

public final class Bits
{
    private Bits() {
    }
    
    public static boolean anyInRange(final int[] array, int first, final int n) {
        first = findFirst(array, first);
        return first >= 0 && first < n;
    }
    
    public static int bitCount(final int[] array) {
        final int length = array.length;
        int n = 0;
        for (int i = 0; i < length; ++i) {
            n += Integer.bitCount(array[i]);
        }
        return n;
    }
    
    public static void clear(final int[] array, final int n) {
        final int n2 = n >> 5;
        array[n2] &= ~(1 << (n & 0x1F));
    }
    
    public static int findFirst(int numberOfTrailingZeros, final int n) {
        numberOfTrailingZeros = Integer.numberOfTrailingZeros(numberOfTrailingZeros & ~((1 << n) - 1));
        if (numberOfTrailingZeros == 32) {
            return -1;
        }
        return numberOfTrailingZeros;
    }
    
    public static int findFirst(final int[] array, int i) {
        final int length = array.length;
        int n = i & 0x1F;
        int n2;
        int first;
        for (i >>= 5; i < length; ++i) {
            n2 = array[i];
            if (n2 != 0) {
                first = findFirst(n2, n);
                if (first >= 0) {
                    return (i << 5) + first;
                }
            }
            n = 0;
        }
        return -1;
    }
    
    public static boolean get(final int[] array, final int n) {
        return (array[n >> 5] & 1 << (n & 0x1F)) != 0x0;
    }
    
    public static int getMax(final int[] array) {
        return array.length * 32;
    }
    
    public static boolean isEmpty(final int[] array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            if (array[i] != 0) {
                return false;
            }
        }
        return true;
    }
    
    public static int[] makeBitSet(final int n) {
        return new int[n + 31 >> 5];
    }
    
    public static void or(final int[] array, final int[] array2) {
        for (int i = 0; i < array2.length; ++i) {
            array[i] |= array2[i];
        }
    }
    
    public static void set(final int[] array, final int n) {
        final int n2 = n >> 5;
        array[n2] |= 1 << (n & 0x1F);
    }
    
    public static void set(final int[] array, int n, final boolean b) {
        final int n2 = n >> 5;
        n = 1 << (n & 0x1F);
        if (b) {
            array[n2] |= n;
            return;
        }
        array[n2] &= ~n;
    }
    
    public static String toHuman(final int[] array) {
        final StringBuilder sb = new StringBuilder();
        int n = 0;
        sb.append('{');
        int n2;
        for (int length = array.length, i = 0; i < length * 32; ++i, n = n2) {
            n2 = n;
            if (get(array, i)) {
                if (n != 0) {
                    sb.append(',');
                }
                n2 = 1;
                sb.append(i);
            }
        }
        sb.append('}');
        return sb.toString();
    }
}
