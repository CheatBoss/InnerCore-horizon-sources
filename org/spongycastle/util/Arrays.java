package org.spongycastle.util;

import java.math.*;
import java.util.function.*;
import java.util.*;

public final class Arrays
{
    private Arrays() {
    }
    
    public static byte[] append(final byte[] array, final byte b) {
        if (array == null) {
            return new byte[] { b };
        }
        final int length = array.length;
        final byte[] array2 = new byte[length + 1];
        System.arraycopy(array, 0, array2, 0, length);
        array2[length] = b;
        return array2;
    }
    
    public static int[] append(final int[] array, final int n) {
        if (array == null) {
            return new int[] { n };
        }
        final int length = array.length;
        final int[] array2 = new int[length + 1];
        System.arraycopy(array, 0, array2, 0, length);
        array2[length] = n;
        return array2;
    }
    
    public static String[] append(final String[] array, final String s) {
        if (array == null) {
            return new String[] { s };
        }
        final int length = array.length;
        final String[] array2 = new String[length + 1];
        System.arraycopy(array, 0, array2, 0, length);
        array2[length] = s;
        return array2;
    }
    
    public static short[] append(final short[] array, final short n) {
        if (array == null) {
            return new short[] { n };
        }
        final int length = array.length;
        final short[] array2 = new short[length + 1];
        System.arraycopy(array, 0, array2, 0, length);
        array2[length] = n;
        return array2;
    }
    
    public static boolean areEqual(final byte[] array, final byte[] array2) {
        if (array == array2) {
            return true;
        }
        if (array == null) {
            return false;
        }
        if (array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i != array.length; ++i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean areEqual(final char[] array, final char[] array2) {
        if (array == array2) {
            return true;
        }
        if (array == null) {
            return false;
        }
        if (array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i != array.length; ++i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean areEqual(final int[] array, final int[] array2) {
        if (array == array2) {
            return true;
        }
        if (array == null) {
            return false;
        }
        if (array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i != array.length; ++i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean areEqual(final long[] array, final long[] array2) {
        if (array == array2) {
            return true;
        }
        if (array == null) {
            return false;
        }
        if (array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i != array.length; ++i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean areEqual(final Object[] array, final Object[] array2) {
        if (array == array2) {
            return true;
        }
        if (array == null) {
            return false;
        }
        if (array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i != array.length; ++i) {
            final Object o = array[i];
            final Object o2 = array2[i];
            if (o == null) {
                if (o2 != null) {
                    return false;
                }
            }
            else if (!o.equals(o2)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean areEqual(final short[] array, final short[] array2) {
        if (array == array2) {
            return true;
        }
        if (array == null) {
            return false;
        }
        if (array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i != array.length; ++i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean areEqual(final boolean[] array, final boolean[] array2) {
        if (array == array2) {
            return true;
        }
        if (array == null) {
            return false;
        }
        if (array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        for (int i = 0; i != array.length; ++i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static byte[] clone(final byte[] array) {
        if (array == null) {
            return null;
        }
        final byte[] array2 = new byte[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static byte[] clone(final byte[] array, final byte[] array2) {
        if (array == null) {
            return null;
        }
        if (array2 != null && array2.length == array.length) {
            System.arraycopy(array, 0, array2, 0, array2.length);
            return array2;
        }
        return clone(array);
    }
    
    public static char[] clone(final char[] array) {
        if (array == null) {
            return null;
        }
        final char[] array2 = new char[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static int[] clone(final int[] array) {
        if (array == null) {
            return null;
        }
        final int[] array2 = new int[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static long[] clone(final long[] array) {
        if (array == null) {
            return null;
        }
        final long[] array2 = new long[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static long[] clone(final long[] array, final long[] array2) {
        if (array == null) {
            return null;
        }
        if (array2 != null && array2.length == array.length) {
            System.arraycopy(array, 0, array2, 0, array2.length);
            return array2;
        }
        return clone(array);
    }
    
    public static BigInteger[] clone(final BigInteger[] array) {
        if (array == null) {
            return null;
        }
        final BigInteger[] array2 = new BigInteger[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static short[] clone(final short[] array) {
        if (array == null) {
            return null;
        }
        final short[] array2 = new short[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static byte[][] clone(final byte[][] array) {
        if (array == null) {
            return null;
        }
        final int length = array.length;
        final byte[][] array2 = new byte[length][];
        for (int i = 0; i != length; ++i) {
            array2[i] = clone(array[i]);
        }
        return array2;
    }
    
    public static byte[][][] clone(final byte[][][] array) {
        if (array == null) {
            return null;
        }
        final int length = array.length;
        final byte[][][] array2 = new byte[length][][];
        for (int i = 0; i != length; ++i) {
            array2[i] = clone(array[i]);
        }
        return array2;
    }
    
    public static int compareUnsigned(final byte[] array, final byte[] array2) {
        if (array == array2) {
            return 0;
        }
        if (array == null) {
            return -1;
        }
        if (array2 == null) {
            return 1;
        }
        for (int min = Math.min(array.length, array2.length), i = 0; i < min; ++i) {
            final int n = array[i] & 0xFF;
            final int n2 = array2[i] & 0xFF;
            if (n < n2) {
                return -1;
            }
            if (n > n2) {
                return 1;
            }
        }
        if (array.length < array2.length) {
            return -1;
        }
        if (array.length > array2.length) {
            return 1;
        }
        return 0;
    }
    
    public static byte[] concatenate(final byte[] array, final byte[] array2) {
        if (array != null && array2 != null) {
            final byte[] array3 = new byte[array.length + array2.length];
            System.arraycopy(array, 0, array3, 0, array.length);
            System.arraycopy(array2, 0, array3, array.length, array2.length);
            return array3;
        }
        if (array2 != null) {
            return clone(array2);
        }
        return clone(array);
    }
    
    public static byte[] concatenate(final byte[] array, final byte[] array2, final byte[] array3) {
        if (array != null && array2 != null && array3 != null) {
            final byte[] array4 = new byte[array.length + array2.length + array3.length];
            System.arraycopy(array, 0, array4, 0, array.length);
            System.arraycopy(array2, 0, array4, array.length, array2.length);
            System.arraycopy(array3, 0, array4, array.length + array2.length, array3.length);
            return array4;
        }
        if (array == null) {
            return concatenate(array2, array3);
        }
        if (array2 == null) {
            return concatenate(array, array3);
        }
        return concatenate(array, array2);
    }
    
    public static byte[] concatenate(final byte[] array, final byte[] array2, final byte[] array3, final byte[] array4) {
        if (array != null && array2 != null && array3 != null && array4 != null) {
            final byte[] array5 = new byte[array.length + array2.length + array3.length + array4.length];
            System.arraycopy(array, 0, array5, 0, array.length);
            System.arraycopy(array2, 0, array5, array.length, array2.length);
            System.arraycopy(array3, 0, array5, array.length + array2.length, array3.length);
            System.arraycopy(array4, 0, array5, array.length + array2.length + array3.length, array4.length);
            return array5;
        }
        if (array4 == null) {
            return concatenate(array, array2, array3);
        }
        if (array3 == null) {
            return concatenate(array, array2, array4);
        }
        if (array2 == null) {
            return concatenate(array, array3, array4);
        }
        return concatenate(array2, array3, array4);
    }
    
    public static byte[] concatenate(final byte[][] array) {
        int i = 0;
        int n = 0;
        while (i != array.length) {
            n += array[i].length;
            ++i;
        }
        final byte[] array2 = new byte[n];
        int j = 0;
        int n2 = 0;
        while (j != array.length) {
            System.arraycopy(array[j], 0, array2, n2, array[j].length);
            n2 += array[j].length;
            ++j;
        }
        return array2;
    }
    
    public static int[] concatenate(final int[] array, final int[] array2) {
        if (array == null) {
            return clone(array2);
        }
        if (array2 == null) {
            return clone(array);
        }
        final int[] array3 = new int[array.length + array2.length];
        System.arraycopy(array, 0, array3, 0, array.length);
        System.arraycopy(array2, 0, array3, array.length, array2.length);
        return array3;
    }
    
    public static boolean constantTimeAreEqual(final byte[] array, final byte[] array2) {
        if (array == array2) {
            return true;
        }
        if (array != null) {
            if (array2 == null) {
                return false;
            }
            if (array.length != array2.length) {
                return constantTimeAreEqual(array, array) ^ true;
            }
            int i = 0;
            int n = 0;
            while (i != array.length) {
                n |= (array[i] ^ array2[i]);
                ++i;
            }
            if (n == 0) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean contains(final int[] array, final int n) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == n) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean contains(final short[] array, final short n) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == n) {
                return true;
            }
        }
        return false;
    }
    
    public static byte[] copyOf(final byte[] array, final int n) {
        final byte[] array2 = new byte[n];
        if (n < array.length) {
            System.arraycopy(array, 0, array2, 0, n);
            return array2;
        }
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static char[] copyOf(final char[] array, final int n) {
        final char[] array2 = new char[n];
        if (n < array.length) {
            System.arraycopy(array, 0, array2, 0, n);
            return array2;
        }
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static int[] copyOf(final int[] array, final int n) {
        final int[] array2 = new int[n];
        if (n < array.length) {
            System.arraycopy(array, 0, array2, 0, n);
            return array2;
        }
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static long[] copyOf(final long[] array, final int n) {
        final long[] array2 = new long[n];
        if (n < array.length) {
            System.arraycopy(array, 0, array2, 0, n);
            return array2;
        }
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static BigInteger[] copyOf(final BigInteger[] array, final int n) {
        final BigInteger[] array2 = new BigInteger[n];
        if (n < array.length) {
            System.arraycopy(array, 0, array2, 0, n);
            return array2;
        }
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static byte[] copyOfRange(final byte[] array, final int n, int length) {
        length = getLength(n, length);
        final byte[] array2 = new byte[length];
        if (array.length - n < length) {
            System.arraycopy(array, n, array2, 0, array.length - n);
            return array2;
        }
        System.arraycopy(array, n, array2, 0, length);
        return array2;
    }
    
    public static int[] copyOfRange(final int[] array, final int n, int length) {
        length = getLength(n, length);
        final int[] array2 = new int[length];
        if (array.length - n < length) {
            System.arraycopy(array, n, array2, 0, array.length - n);
            return array2;
        }
        System.arraycopy(array, n, array2, 0, length);
        return array2;
    }
    
    public static long[] copyOfRange(final long[] array, final int n, int length) {
        length = getLength(n, length);
        final long[] array2 = new long[length];
        if (array.length - n < length) {
            System.arraycopy(array, n, array2, 0, array.length - n);
            return array2;
        }
        System.arraycopy(array, n, array2, 0, length);
        return array2;
    }
    
    public static BigInteger[] copyOfRange(final BigInteger[] array, final int n, int length) {
        length = getLength(n, length);
        final BigInteger[] array2 = new BigInteger[length];
        if (array.length - n < length) {
            System.arraycopy(array, n, array2, 0, array.length - n);
            return array2;
        }
        System.arraycopy(array, n, array2, 0, length);
        return array2;
    }
    
    public static void fill(final byte[] array, final byte b) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = b;
        }
    }
    
    public static void fill(final char[] array, final char c) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = c;
        }
    }
    
    public static void fill(final int[] array, final int n) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = n;
        }
    }
    
    public static void fill(final long[] array, final long n) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = n;
        }
    }
    
    public static void fill(final short[] array, final short n) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = n;
        }
    }
    
    private static int getLength(final int n, final int n2) {
        final int n3 = n2 - n;
        if (n3 >= 0) {
            return n3;
        }
        final StringBuffer sb = new StringBuffer(n);
        sb.append(" > ");
        sb.append(n2);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static int hashCode(final byte[] array) {
        if (array == null) {
            return 0;
        }
        int length = array.length;
        int n = length + 1;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            n = (n * 257 ^ array[length]);
        }
        return n;
    }
    
    public static int hashCode(final byte[] array, final int n, int n2) {
        if (array == null) {
            return 0;
        }
        final int n3 = n2 + 1;
        int n4 = n2;
        n2 = n3;
        while (true) {
            --n4;
            if (n4 < 0) {
                break;
            }
            n2 = (n2 * 257 ^ array[n + n4]);
        }
        return n2;
    }
    
    public static int hashCode(final char[] array) {
        if (array == null) {
            return 0;
        }
        int length = array.length;
        int n = length + 1;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            n = (n * 257 ^ array[length]);
        }
        return n;
    }
    
    public static int hashCode(final int[] array) {
        if (array == null) {
            return 0;
        }
        int length = array.length;
        int n = length + 1;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            n = (n * 257 ^ array[length]);
        }
        return n;
    }
    
    public static int hashCode(final int[] array, final int n, int n2) {
        if (array == null) {
            return 0;
        }
        final int n3 = n2 + 1;
        int n4 = n2;
        n2 = n3;
        while (true) {
            --n4;
            if (n4 < 0) {
                break;
            }
            n2 = (n2 * 257 ^ array[n + n4]);
        }
        return n2;
    }
    
    public static int hashCode(final long[] array) {
        if (array == null) {
            return 0;
        }
        int length = array.length;
        int n = length + 1;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            final long n2 = array[length];
            n = ((n * 257 ^ (int)n2) * 257 ^ (int)(n2 >>> 32));
        }
        return n;
    }
    
    public static int hashCode(final long[] array, final int n, int n2) {
        if (array == null) {
            return 0;
        }
        final int n3 = n2 + 1;
        int n4 = n2;
        n2 = n3;
        while (true) {
            --n4;
            if (n4 < 0) {
                break;
            }
            final long n5 = array[n + n4];
            n2 = ((n2 * 257 ^ (int)n5) * 257 ^ (int)(n5 >>> 32));
        }
        return n2;
    }
    
    public static int hashCode(final Object[] array) {
        if (array == null) {
            return 0;
        }
        int length = array.length;
        int n = length + 1;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            n = (n * 257 ^ array[length].hashCode());
        }
        return n;
    }
    
    public static int hashCode(final short[] array) {
        if (array == null) {
            return 0;
        }
        int length = array.length;
        int n = length + 1;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            n = (n * 257 ^ (array[length] & 0xFF));
        }
        return n;
    }
    
    public static int hashCode(final int[][] array) {
        int i = 0;
        int n = 0;
        while (i != array.length) {
            n = n * 257 + hashCode(array[i]);
            ++i;
        }
        return n;
    }
    
    public static int hashCode(final short[][] array) {
        int i = 0;
        int n = 0;
        while (i != array.length) {
            n = n * 257 + hashCode(array[i]);
            ++i;
        }
        return n;
    }
    
    public static int hashCode(final short[][][] array) {
        int i = 0;
        int n = 0;
        while (i != array.length) {
            n = n * 257 + hashCode(array[i]);
            ++i;
        }
        return n;
    }
    
    public static byte[] prepend(final byte[] array, final byte b) {
        if (array == null) {
            return new byte[] { b };
        }
        final int length = array.length;
        final byte[] array2 = new byte[length + 1];
        System.arraycopy(array, 0, array2, 1, length);
        array2[0] = b;
        return array2;
    }
    
    public static int[] prepend(final int[] array, final int n) {
        if (array == null) {
            return new int[] { n };
        }
        final int length = array.length;
        final int[] array2 = new int[length + 1];
        System.arraycopy(array, 0, array2, 1, length);
        array2[0] = n;
        return array2;
    }
    
    public static short[] prepend(final short[] array, final short n) {
        if (array == null) {
            return new short[] { n };
        }
        final int length = array.length;
        final short[] array2 = new short[length + 1];
        System.arraycopy(array, 0, array2, 1, length);
        array2[0] = n;
        return array2;
    }
    
    public static byte[] reverse(final byte[] array) {
        if (array == null) {
            return null;
        }
        int n = 0;
        int length = array.length;
        final byte[] array2 = new byte[length];
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            array2[length] = array[n];
            ++n;
        }
        return array2;
    }
    
    public static int[] reverse(final int[] array) {
        if (array == null) {
            return null;
        }
        int n = 0;
        int length = array.length;
        final int[] array2 = new int[length];
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            array2[length] = array[n];
            ++n;
        }
        return array2;
    }
    
    public static class Iterator<T> implements java.util.Iterator<T>
    {
        private final T[] dataArray;
        private int position;
        
        public Iterator(final T[] dataArray) {
            this.position = 0;
            this.dataArray = dataArray;
        }
        
        @Override
        public void forEachRemaining(final Consumer<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public boolean hasNext() {
            return this.position < this.dataArray.length;
        }
        
        @Override
        public T next() {
            final int position = this.position;
            final T[] dataArray = this.dataArray;
            if (position != dataArray.length) {
                this.position = position + 1;
                return dataArray[position];
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Out of elements: ");
            sb.append(this.position);
            throw new NoSuchElementException(sb.toString());
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException("Cannot remove element from an Array.");
        }
    }
}
