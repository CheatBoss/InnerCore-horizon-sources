package org.spongycastle.math.raw;

import java.math.*;
import org.spongycastle.util.*;

public abstract class Nat
{
    private static final long M = 4294967295L;
    
    public static int add(final int n, final int[] array, final int[] array2, final int[] array3) {
        long n2 = 0L;
        for (int i = 0; i < n; ++i) {
            final long n3 = n2 + (((long)array[i] & 0xFFFFFFFFL) + ((long)array2[i] & 0xFFFFFFFFL));
            array3[i] = (int)n3;
            n2 = n3 >>> 32;
        }
        return (int)n2;
    }
    
    public static int add33At(final int n, int n2, final int[] array, final int n3) {
        final int n4 = n3 + 0;
        final long n5 = ((long)array[n4] & 0xFFFFFFFFL) + ((long)n2 & 0xFFFFFFFFL);
        array[n4] = (int)n5;
        n2 = n3 + 1;
        final long n6 = (n5 >>> 32) + (((long)array[n2] & 0xFFFFFFFFL) + 1L);
        array[n2] = (int)n6;
        if (n6 >>> 32 == 0L) {
            return 0;
        }
        return incAt(n, array, n3 + 2);
    }
    
    public static int add33At(final int n, int n2, final int[] array, final int n3, final int n4) {
        final int n5 = n3 + n4;
        final long n6 = ((long)array[n5] & 0xFFFFFFFFL) + ((long)n2 & 0xFFFFFFFFL);
        array[n5] = (int)n6;
        n2 = n5 + 1;
        final long n7 = (n6 >>> 32) + (((long)array[n2] & 0xFFFFFFFFL) + 1L);
        array[n2] = (int)n7;
        if (n7 >>> 32 == 0L) {
            return 0;
        }
        return incAt(n, array, n3, n4 + 2);
    }
    
    public static int add33To(final int n, final int n2, final int[] array) {
        final long n3 = ((long)array[0] & 0xFFFFFFFFL) + ((long)n2 & 0xFFFFFFFFL);
        array[0] = (int)n3;
        final long n4 = (n3 >>> 32) + (((long)array[1] & 0xFFFFFFFFL) + 1L);
        array[1] = (int)n4;
        if (n4 >>> 32 == 0L) {
            return 0;
        }
        return incAt(n, array, 2);
    }
    
    public static int add33To(final int n, int n2, final int[] array, final int n3) {
        final int n4 = n3 + 0;
        final long n5 = ((long)array[n4] & 0xFFFFFFFFL) + ((long)n2 & 0xFFFFFFFFL);
        array[n4] = (int)n5;
        n2 = n3 + 1;
        final long n6 = (n5 >>> 32) + (((long)array[n2] & 0xFFFFFFFFL) + 1L);
        array[n2] = (int)n6;
        if (n6 >>> 32 == 0L) {
            return 0;
        }
        return incAt(n, array, n3, 2);
    }
    
    public static int addBothTo(final int n, final int[] array, final int n2, final int[] array2, final int n3, final int[] array3, final int n4) {
        int i = 0;
        long n5 = 0L;
        while (i < n) {
            final long n6 = array[n2 + i];
            final long n7 = array2[n3 + i];
            final int n8 = n4 + i;
            final long n9 = n5 + ((n6 & 0xFFFFFFFFL) + (n7 & 0xFFFFFFFFL) + ((long)array3[n8] & 0xFFFFFFFFL));
            array3[n8] = (int)n9;
            n5 = n9 >>> 32;
            ++i;
        }
        return (int)n5;
    }
    
    public static int addBothTo(final int n, final int[] array, final int[] array2, final int[] array3) {
        long n2 = 0L;
        for (int i = 0; i < n; ++i) {
            final long n3 = n2 + (((long)array[i] & 0xFFFFFFFFL) + ((long)array2[i] & 0xFFFFFFFFL) + ((long)array3[i] & 0xFFFFFFFFL));
            array3[i] = (int)n3;
            n2 = n3 >>> 32;
        }
        return (int)n2;
    }
    
    public static int addDWordAt(final int n, long n2, final int[] array, final int n3) {
        final int n4 = n3 + 0;
        final long n5 = ((long)array[n4] & 0xFFFFFFFFL) + (n2 & 0xFFFFFFFFL);
        array[n4] = (int)n5;
        final int n6 = n3 + 1;
        n2 = (n5 >>> 32) + (((long)array[n6] & 0xFFFFFFFFL) + (n2 >>> 32));
        array[n6] = (int)n2;
        if (n2 >>> 32 == 0L) {
            return 0;
        }
        return incAt(n, array, n3 + 2);
    }
    
    public static int addDWordAt(final int n, long n2, final int[] array, final int n3, final int n4) {
        final int n5 = n3 + n4;
        final long n6 = ((long)array[n5] & 0xFFFFFFFFL) + (n2 & 0xFFFFFFFFL);
        array[n5] = (int)n6;
        final int n7 = n5 + 1;
        n2 = (n6 >>> 32) + (((long)array[n7] & 0xFFFFFFFFL) + (n2 >>> 32));
        array[n7] = (int)n2;
        if (n2 >>> 32 == 0L) {
            return 0;
        }
        return incAt(n, array, n3, n4 + 2);
    }
    
    public static int addDWordTo(final int n, long n2, final int[] array) {
        final long n3 = ((long)array[0] & 0xFFFFFFFFL) + (n2 & 0xFFFFFFFFL);
        array[0] = (int)n3;
        n2 = (n3 >>> 32) + (((long)array[1] & 0xFFFFFFFFL) + (n2 >>> 32));
        array[1] = (int)n2;
        if (n2 >>> 32 == 0L) {
            return 0;
        }
        return incAt(n, array, 2);
    }
    
    public static int addDWordTo(final int n, long n2, final int[] array, final int n3) {
        final int n4 = n3 + 0;
        final long n5 = ((long)array[n4] & 0xFFFFFFFFL) + (n2 & 0xFFFFFFFFL);
        array[n4] = (int)n5;
        final int n6 = n3 + 1;
        n2 = (n5 >>> 32) + (((long)array[n6] & 0xFFFFFFFFL) + (n2 >>> 32));
        array[n6] = (int)n2;
        if (n2 >>> 32 == 0L) {
            return 0;
        }
        return incAt(n, array, n3, 2);
    }
    
    public static int addTo(final int n, final int[] array, final int n2, final int[] array2, final int n3) {
        int i = 0;
        long n4 = 0L;
        while (i < n) {
            final long n5 = array[n2 + i];
            final int n6 = n3 + i;
            final long n7 = n4 + ((n5 & 0xFFFFFFFFL) + ((long)array2[n6] & 0xFFFFFFFFL));
            array2[n6] = (int)n7;
            n4 = n7 >>> 32;
            ++i;
        }
        return (int)n4;
    }
    
    public static int addTo(final int n, final int[] array, final int[] array2) {
        long n2 = 0L;
        for (int i = 0; i < n; ++i) {
            final long n3 = n2 + (((long)array[i] & 0xFFFFFFFFL) + ((long)array2[i] & 0xFFFFFFFFL));
            array2[i] = (int)n3;
            n2 = n3 >>> 32;
        }
        return (int)n2;
    }
    
    public static int addWordAt(final int n, final int n2, final int[] array, final int n3) {
        final long n4 = ((long)n2 & 0xFFFFFFFFL) + ((long)array[n3] & 0xFFFFFFFFL);
        array[n3] = (int)n4;
        if (n4 >>> 32 == 0L) {
            return 0;
        }
        return incAt(n, array, n3 + 1);
    }
    
    public static int addWordAt(final int n, int n2, final int[] array, final int n3, final int n4) {
        final long n5 = n2;
        n2 = n3 + n4;
        final long n6 = (n5 & 0xFFFFFFFFL) + ((long)array[n2] & 0xFFFFFFFFL);
        array[n2] = (int)n6;
        if (n6 >>> 32 == 0L) {
            return 0;
        }
        return incAt(n, array, n3, n4 + 1);
    }
    
    public static int addWordTo(final int n, final int n2, final int[] array) {
        final long n3 = ((long)n2 & 0xFFFFFFFFL) + ((long)array[0] & 0xFFFFFFFFL);
        array[0] = (int)n3;
        if (n3 >>> 32 == 0L) {
            return 0;
        }
        return incAt(n, array, 1);
    }
    
    public static int addWordTo(final int n, final int n2, final int[] array, final int n3) {
        final long n4 = ((long)n2 & 0xFFFFFFFFL) + ((long)array[n3] & 0xFFFFFFFFL);
        array[n3] = (int)n4;
        if (n4 >>> 32 == 0L) {
            return 0;
        }
        return incAt(n, array, n3, 1);
    }
    
    public static void copy(final int n, final int[] array, final int[] array2) {
        System.arraycopy(array, 0, array2, 0, n);
    }
    
    public static int[] copy(final int n, final int[] array) {
        final int[] array2 = new int[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
    
    public static int[] create(final int n) {
        return new int[n];
    }
    
    public static long[] create64(final int n) {
        return new long[n];
    }
    
    public static int dec(final int n, final int[] array) {
        for (int i = 0; i < n; ++i) {
            if (--array[i] != -1) {
                return 0;
            }
        }
        return -1;
    }
    
    public static int dec(final int n, final int[] array, final int[] array2) {
        int i = 0;
        while (i < n) {
            final int n2 = array[i] - 1;
            array2[i] = n2;
            int j = ++i;
            if (n2 != -1) {
                while (j < n) {
                    array2[j] = array[j];
                    ++j;
                }
                return 0;
            }
        }
        return -1;
    }
    
    public static int decAt(final int n, final int[] array, int i) {
        while (i < n) {
            if (--array[i] != -1) {
                return 0;
            }
            ++i;
        }
        return -1;
    }
    
    public static int decAt(final int n, final int[] array, final int n2, int i) {
        while (i < n) {
            final int n3 = n2 + i;
            if (--array[n3] != -1) {
                return 0;
            }
            ++i;
        }
        return -1;
    }
    
    public static boolean eq(int i, final int[] array, final int[] array2) {
        for (--i; i >= 0; --i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static int[] fromBigInteger(int n, BigInteger shiftRight) {
        if (shiftRight.signum() >= 0 && shiftRight.bitLength() <= n) {
            final int[] create = create(n + 31 >> 5);
            for (n = 0; shiftRight.signum() != 0; shiftRight = shiftRight.shiftRight(32), ++n) {
                create[n] = shiftRight.intValue();
            }
            return create;
        }
        throw new IllegalArgumentException();
    }
    
    public static int getBit(final int[] array, int n) {
        if (n == 0) {
            n = array[0];
        }
        else {
            final int n2 = n >> 5;
            if (n2 < 0) {
                return 0;
            }
            if (n2 >= array.length) {
                return 0;
            }
            n = array[n2] >>> (n & 0x1F);
        }
        return n & 0x1;
    }
    
    public static boolean gte(int i, final int[] array, final int[] array2) {
        int n;
        int n2;
        for (--i; i >= 0; --i) {
            n = (array[i] ^ Integer.MIN_VALUE);
            n2 = (Integer.MIN_VALUE ^ array2[i]);
            if (n < n2) {
                return false;
            }
            if (n > n2) {
                return true;
            }
        }
        return true;
    }
    
    public static int inc(final int n, final int[] array) {
        for (int i = 0; i < n; ++i) {
            if (++array[i] != 0) {
                return 0;
            }
        }
        return 1;
    }
    
    public static int inc(final int n, final int[] array, final int[] array2) {
        int i = 0;
        while (i < n) {
            final int n2 = array[i] + 1;
            array2[i] = n2;
            int j = ++i;
            if (n2 != 0) {
                while (j < n) {
                    array2[j] = array[j];
                    ++j;
                }
                return 0;
            }
        }
        return 1;
    }
    
    public static int incAt(final int n, final int[] array, int i) {
        while (i < n) {
            if (++array[i] != 0) {
                return 0;
            }
            ++i;
        }
        return 1;
    }
    
    public static int incAt(final int n, final int[] array, final int n2, int i) {
        while (i < n) {
            final int n3 = n2 + i;
            if (++array[n3] != 0) {
                return 0;
            }
            ++i;
        }
        return 1;
    }
    
    public static boolean isOne(final int n, final int[] array) {
        if (array[0] != 1) {
            return false;
        }
        for (int i = 1; i < n; ++i) {
            if (array[i] != 0) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isZero(final int n, final int[] array) {
        for (int i = 0; i < n; ++i) {
            if (array[i] != 0) {
                return false;
            }
        }
        return true;
    }
    
    public static void mul(final int n, final int[] array, final int n2, final int[] array2, final int n3, final int[] array3, final int n4) {
        array3[n4 + n] = mulWord(n, array[n2], array2, n3, array3, n4);
        for (int i = 1; i < n; ++i) {
            final int n5 = n4 + i;
            array3[n5 + n] = mulWordAddTo(n, array[n2 + i], array2, n3, array3, n5);
        }
    }
    
    public static void mul(final int n, final int[] array, final int[] array2, final int[] array3) {
        array3[n] = mulWord(n, array[0], array2, array3);
        for (int i = 1; i < n; ++i) {
            array3[i + n] = mulWordAddTo(n, array[i], array2, 0, array3, i);
        }
    }
    
    public static int mul31BothAdd(final int n, int n2, final int[] array, int i, final int[] array2, final int[] array3, final int n3) {
        final long n4 = n2;
        final long n5 = i;
        n2 = 0;
        long n6 = 0L;
        long n10;
        do {
            final long n7 = array[n2];
            final long n8 = array2[n2];
            i = n3 + n2;
            final long n9 = n6 + ((n7 & 0xFFFFFFFFL) * (n4 & 0xFFFFFFFFL) + (n8 & 0xFFFFFFFFL) * (n5 & 0xFFFFFFFFL) + ((long)array3[i] & 0xFFFFFFFFL));
            array3[i] = (int)n9;
            n10 = n9 >>> 32;
            i = ++n2;
            n6 = n10;
        } while (i < n);
        return (int)n10;
    }
    
    public static int mulAddTo(final int n, final int[] array, final int n2, final int[] array2, final int n3, final int[] array3, int i) {
        long n4 = 0L;
        final int n5 = 0;
        int n6 = i;
        long n7;
        int n8;
        long n9;
        for (i = n5; i < n; ++i) {
            n7 = mulWordAddTo(n, array[n2 + i], array2, n3, array3, n6);
            n8 = n6 + n;
            n9 = (n7 & 0xFFFFFFFFL) + (n4 + ((long)array3[n8] & 0xFFFFFFFFL));
            array3[n8] = (int)n9;
            n4 = n9 >>> 32;
            ++n6;
        }
        return (int)n4;
    }
    
    public static int mulAddTo(final int n, final int[] array, final int[] array2, final int[] array3) {
        long n2 = 0L;
        for (int i = 0; i < n; ++i) {
            final long n3 = mulWordAddTo(n, array[i], array2, 0, array3, i);
            final int n4 = i + n;
            final long n5 = (n3 & 0xFFFFFFFFL) + (n2 + ((long)array3[n4] & 0xFFFFFFFFL));
            array3[n4] = (int)n5;
            n2 = n5 >>> 32;
        }
        return (int)n2;
    }
    
    public static int mulWord(final int n, int n2, final int[] array, final int n3, final int[] array2, final int n4) {
        final long n5 = n2;
        long n6 = 0L;
        n2 = 0;
        int n7;
        long n9;
        do {
            final long n8 = n6 + ((long)array[n3 + n2] & 0xFFFFFFFFL) * (n5 & 0xFFFFFFFFL);
            array2[n4 + n2] = (int)n8;
            n9 = n8 >>> 32;
            n7 = n2 + 1;
            n6 = n9;
        } while ((n2 = n7) < n);
        return (int)n9;
    }
    
    public static int mulWord(final int n, int n2, final int[] array, final int[] array2) {
        final long n3 = n2;
        long n4 = 0L;
        n2 = 0;
        int n5;
        long n7;
        do {
            final long n6 = n4 + ((long)array[n2] & 0xFFFFFFFFL) * (n3 & 0xFFFFFFFFL);
            array2[n2] = (int)n6;
            n7 = n6 >>> 32;
            n5 = n2 + 1;
            n4 = n7;
        } while ((n2 = n5) < n);
        return (int)n7;
    }
    
    public static int mulWordAddTo(final int n, int n2, final int[] array, final int n3, final int[] array2, final int n4) {
        final long n5 = n2;
        n2 = 0;
        long n6 = 0L;
        int i;
        long n10;
        do {
            final long n7 = array[n3 + n2];
            final int n8 = n4 + n2;
            final long n9 = n6 + ((n7 & 0xFFFFFFFFL) * (n5 & 0xFFFFFFFFL) + ((long)array2[n8] & 0xFFFFFFFFL));
            array2[n8] = (int)n9;
            n10 = n9 >>> 32;
            i = ++n2;
            n6 = n10;
        } while (i < n);
        return (int)n10;
    }
    
    public static int mulWordDwordAddAt(final int n, int n2, long n3, final int[] array, final int n4) {
        final long n5 = (long)n2 & 0xFFFFFFFFL;
        n2 = n4 + 0;
        final long n6 = (n3 & 0xFFFFFFFFL) * n5 + ((long)array[n2] & 0xFFFFFFFFL) + 0L;
        array[n2] = (int)n6;
        n2 = n4 + 1;
        n3 = (n6 >>> 32) + (n5 * (n3 >>> 32) + ((long)array[n2] & 0xFFFFFFFFL));
        array[n2] = (int)n3;
        n2 = n4 + 2;
        n3 = (n3 >>> 32) + ((long)array[n2] & 0xFFFFFFFFL);
        array[n2] = (int)n3;
        if (n3 >>> 32 == 0L) {
            return 0;
        }
        return incAt(n, array, n4 + 3);
    }
    
    public static int shiftDownBit(int n, final int[] array, int n2) {
        while (true) {
            --n;
            if (n < 0) {
                break;
            }
            final int n3 = array[n];
            array[n] = (n2 << 31 | n3 >>> 1);
            n2 = n3;
        }
        return n2 << 31;
    }
    
    public static int shiftDownBit(int n, final int[] array, final int n2, int n3) {
        while (true) {
            --n;
            if (n < 0) {
                break;
            }
            final int n4 = n2 + n;
            final int n5 = array[n4];
            array[n4] = (n3 << 31 | n5 >>> 1);
            n3 = n5;
        }
        return n3 << 31;
    }
    
    public static int shiftDownBit(int n, final int[] array, final int n2, int n3, final int[] array2, final int n4) {
        while (true) {
            --n;
            if (n < 0) {
                break;
            }
            final int n5 = array[n2 + n];
            array2[n4 + n] = (n3 << 31 | n5 >>> 1);
            n3 = n5;
        }
        return n3 << 31;
    }
    
    public static int shiftDownBit(int n, final int[] array, int n2, final int[] array2) {
        while (true) {
            --n;
            if (n < 0) {
                break;
            }
            final int n3 = array[n];
            array2[n] = (n2 << 31 | n3 >>> 1);
            n2 = n3;
        }
        return n2 << 31;
    }
    
    public static int shiftDownBits(int n, final int[] array, final int n2, int n3) {
        while (true) {
            --n;
            if (n < 0) {
                break;
            }
            final int n4 = array[n];
            array[n] = (n3 << -n2 | n4 >>> n2);
            n3 = n4;
        }
        return n3 << -n2;
    }
    
    public static int shiftDownBits(int n, final int[] array, final int n2, final int n3, int n4) {
        while (true) {
            --n;
            if (n < 0) {
                break;
            }
            final int n5 = n2 + n;
            final int n6 = array[n5];
            array[n5] = (n4 << -n3 | n6 >>> n3);
            n4 = n6;
        }
        return n4 << -n3;
    }
    
    public static int shiftDownBits(int n, final int[] array, final int n2, final int n3, int n4, final int[] array2, final int n5) {
        while (true) {
            --n;
            if (n < 0) {
                break;
            }
            final int n6 = array[n2 + n];
            array2[n5 + n] = (n4 << -n3 | n6 >>> n3);
            n4 = n6;
        }
        return n4 << -n3;
    }
    
    public static int shiftDownBits(int n, final int[] array, final int n2, int n3, final int[] array2) {
        while (true) {
            --n;
            if (n < 0) {
                break;
            }
            final int n4 = array[n];
            array2[n] = (n3 << -n2 | n4 >>> n2);
            n3 = n4;
        }
        return n3 << -n2;
    }
    
    public static int shiftDownWord(int n, final int[] array, int n2) {
        while (true) {
            --n;
            if (n < 0) {
                break;
            }
            final int n3 = array[n];
            array[n] = n2;
            n2 = n3;
        }
        return n2;
    }
    
    public static int shiftUpBit(final int n, final int[] array, int i) {
        final int n2 = 0;
        int n3 = i;
        int n4;
        for (i = n2; i < n; ++i, n3 = n4) {
            n4 = array[i];
            array[i] = (n3 >>> 31 | n4 << 1);
        }
        return n3 >>> 31;
    }
    
    public static int shiftUpBit(final int n, final int[] array, final int n2, int i) {
        final int n3 = 0;
        int n4 = i;
        int n5;
        int n6;
        for (i = n3; i < n; ++i, n4 = n6) {
            n5 = n2 + i;
            n6 = array[n5];
            array[n5] = (n4 >>> 31 | n6 << 1);
        }
        return n4 >>> 31;
    }
    
    public static int shiftUpBit(final int n, final int[] array, final int n2, int i, final int[] array2, final int n3) {
        final int n4 = 0;
        int n5 = i;
        int n6;
        for (i = n4; i < n; ++i, n5 = n6) {
            n6 = array[n2 + i];
            array2[n3 + i] = (n5 >>> 31 | n6 << 1);
        }
        return n5 >>> 31;
    }
    
    public static int shiftUpBit(final int n, final int[] array, int i, final int[] array2) {
        final int n2 = 0;
        int n3 = i;
        int n4;
        for (i = n2; i < n; ++i, n3 = n4) {
            n4 = array[i];
            array2[i] = (n3 >>> 31 | n4 << 1);
        }
        return n3 >>> 31;
    }
    
    public static long shiftUpBit64(final int n, final long[] array, final int n2, long n3, final long[] array2, final int n4) {
        long n5;
        for (int i = 0; i < n; ++i, n3 = n5) {
            n5 = array[n2 + i];
            array2[n4 + i] = (n3 >>> 63 | n5 << 1);
        }
        return n3 >>> 63;
    }
    
    public static int shiftUpBits(final int n, final int[] array, final int n2, int i) {
        final int n3 = 0;
        int n4 = i;
        int n5;
        for (i = n3; i < n; ++i, n4 = n5) {
            n5 = array[i];
            array[i] = (n4 >>> -n2 | n5 << n2);
        }
        return n4 >>> -n2;
    }
    
    public static int shiftUpBits(final int n, final int[] array, final int n2, final int n3, int i) {
        final int n4 = 0;
        int n5 = i;
        int n6;
        int n7;
        for (i = n4; i < n; ++i, n5 = n7) {
            n6 = n2 + i;
            n7 = array[n6];
            array[n6] = (n5 >>> -n3 | n7 << n3);
        }
        return n5 >>> -n3;
    }
    
    public static int shiftUpBits(final int n, final int[] array, final int n2, final int n3, int i, final int[] array2, final int n4) {
        final int n5 = 0;
        int n6 = i;
        int n7;
        for (i = n5; i < n; ++i, n6 = n7) {
            n7 = array[n2 + i];
            array2[n4 + i] = (n6 >>> -n3 | n7 << n3);
        }
        return n6 >>> -n3;
    }
    
    public static int shiftUpBits(final int n, final int[] array, final int n2, int i, final int[] array2) {
        final int n3 = 0;
        int n4 = i;
        int n5;
        for (i = n3; i < n; ++i, n4 = n5) {
            n5 = array[i];
            array2[i] = (n4 >>> -n2 | n5 << n2);
        }
        return n4 >>> -n2;
    }
    
    public static long shiftUpBits64(final int n, final long[] array, final int n2, final int n3, long n4) {
        long n6;
        for (int i = 0; i < n; ++i, n4 = n6) {
            final int n5 = n2 + i;
            n6 = array[n5];
            array[n5] = (n4 >>> -n3 | n6 << n3);
        }
        return n4 >>> -n3;
    }
    
    public static long shiftUpBits64(final int n, final long[] array, final int n2, final int n3, long n4, final long[] array2, final int n5) {
        long n6;
        for (int i = 0; i < n; ++i, n4 = n6) {
            n6 = array[n2 + i];
            array2[n5 + i] = (n4 >>> -n3 | n6 << n3);
        }
        return n4 >>> -n3;
    }
    
    public static void square(final int n, final int[] array, final int n2, final int[] array2, final int n3) {
        final int n4 = n << 1;
        int n5 = n;
        int n6 = n4;
        int n7 = 0;
        int n8;
        int n9;
        do {
            n9 = 1;
            n8 = n5 - 1;
            final long n10 = (long)array[n2 + n8] & 0xFFFFFFFFL;
            final long n11 = n10 * n10;
            final int n12 = n6 - 1;
            array2[n3 + n12] = (n7 << 31 | (int)(n11 >>> 33));
            n6 = n12 - 1;
            array2[n3 + n6] = (int)(n11 >>> 1);
            n7 = (int)n11;
        } while ((n5 = n8) > 0);
        for (int i = n9; i < n; ++i) {
            addWordAt(n4, squareWordAdd(array, n2, i, array2, n3), array2, n3, i << 1);
        }
        shiftUpBit(n4, array2, n3, array[n2] << 31);
    }
    
    public static void square(final int n, final int[] array, final int[] array2) {
        final int n2 = n << 1;
        int n3 = n;
        int n4 = n2;
        int n5 = 0;
        int n6;
        int n7;
        do {
            n7 = 1;
            n6 = n3 - 1;
            final long n8 = (long)array[n6] & 0xFFFFFFFFL;
            final long n9 = n8 * n8;
            final int n10 = n4 - 1;
            array2[n10] = (n5 << 31 | (int)(n9 >>> 33));
            n4 = n10 - 1;
            array2[n4] = (int)(n9 >>> 1);
            n5 = (int)n9;
        } while ((n3 = n6) > 0);
        for (int i = n7; i < n; ++i) {
            addWordAt(n2, squareWordAdd(array, i, array2), array2, i << 1);
        }
        shiftUpBit(n2, array2, array[0] << 31);
    }
    
    public static int squareWordAdd(final int[] array, final int n, final int n2, final int[] array2, int n3) {
        final long n4 = array[n + n2];
        final int n5 = 0;
        long n6 = 0L;
        int n7 = n3;
        n3 = n5;
        int i;
        long n11;
        do {
            final long n8 = array[n + n3];
            final int n9 = n2 + n7;
            final long n10 = n6 + ((n8 & 0xFFFFFFFFL) * (n4 & 0xFFFFFFFFL) + ((long)array2[n9] & 0xFFFFFFFFL));
            array2[n9] = (int)n10;
            n11 = n10 >>> 32;
            ++n7;
            i = ++n3;
            n6 = n11;
        } while (i < n2);
        return (int)n11;
    }
    
    public static int squareWordAdd(final int[] array, final int n, final int[] array2) {
        final long n2 = array[n];
        long n3 = 0L;
        int n4 = 0;
        int n5;
        long n9;
        do {
            final long n6 = array[n4];
            final int n7 = n + n4;
            final long n8 = n3 + ((n6 & 0xFFFFFFFFL) * (n2 & 0xFFFFFFFFL) + ((long)array2[n7] & 0xFFFFFFFFL));
            array2[n7] = (int)n8;
            n9 = n8 >>> 32;
            n5 = n4 + 1;
            n3 = n9;
        } while ((n4 = n5) < n);
        return (int)n9;
    }
    
    public static int sub(final int n, final int[] array, final int n2, final int[] array2, final int n3, final int[] array3, final int n4) {
        int i = 0;
        long n5 = 0L;
        while (i < n) {
            final long n6 = n5 + (((long)array[n2 + i] & 0xFFFFFFFFL) - ((long)array2[n3 + i] & 0xFFFFFFFFL));
            array3[n4 + i] = (int)n6;
            n5 = n6 >> 32;
            ++i;
        }
        return (int)n5;
    }
    
    public static int sub(final int n, final int[] array, final int[] array2, final int[] array3) {
        long n2 = 0L;
        for (int i = 0; i < n; ++i) {
            final long n3 = n2 + (((long)array[i] & 0xFFFFFFFFL) - ((long)array2[i] & 0xFFFFFFFFL));
            array3[i] = (int)n3;
            n2 = n3 >> 32;
        }
        return (int)n2;
    }
    
    public static int sub33At(final int n, int n2, final int[] array, final int n3) {
        final int n4 = n3 + 0;
        final long n5 = ((long)array[n4] & 0xFFFFFFFFL) - ((long)n2 & 0xFFFFFFFFL);
        array[n4] = (int)n5;
        n2 = n3 + 1;
        final long n6 = (n5 >> 32) + (((long)array[n2] & 0xFFFFFFFFL) - 1L);
        array[n2] = (int)n6;
        if (n6 >> 32 == 0L) {
            return 0;
        }
        return decAt(n, array, n3 + 2);
    }
    
    public static int sub33At(final int n, int n2, final int[] array, final int n3, final int n4) {
        final int n5 = n3 + n4;
        final long n6 = ((long)array[n5] & 0xFFFFFFFFL) - ((long)n2 & 0xFFFFFFFFL);
        array[n5] = (int)n6;
        n2 = n5 + 1;
        final long n7 = (n6 >> 32) + (((long)array[n2] & 0xFFFFFFFFL) - 1L);
        array[n2] = (int)n7;
        if (n7 >> 32 == 0L) {
            return 0;
        }
        return decAt(n, array, n3, n4 + 2);
    }
    
    public static int sub33From(final int n, final int n2, final int[] array) {
        final long n3 = ((long)array[0] & 0xFFFFFFFFL) - ((long)n2 & 0xFFFFFFFFL);
        array[0] = (int)n3;
        final long n4 = (n3 >> 32) + (((long)array[1] & 0xFFFFFFFFL) - 1L);
        array[1] = (int)n4;
        if (n4 >> 32 == 0L) {
            return 0;
        }
        return decAt(n, array, 2);
    }
    
    public static int sub33From(final int n, int n2, final int[] array, final int n3) {
        final int n4 = n3 + 0;
        final long n5 = ((long)array[n4] & 0xFFFFFFFFL) - ((long)n2 & 0xFFFFFFFFL);
        array[n4] = (int)n5;
        n2 = n3 + 1;
        final long n6 = (n5 >> 32) + (((long)array[n2] & 0xFFFFFFFFL) - 1L);
        array[n2] = (int)n6;
        if (n6 >> 32 == 0L) {
            return 0;
        }
        return decAt(n, array, n3, 2);
    }
    
    public static int subBothFrom(final int n, final int[] array, final int n2, final int[] array2, final int n3, final int[] array3, final int n4) {
        int i = 0;
        long n5 = 0L;
        while (i < n) {
            final int n6 = n4 + i;
            final long n7 = n5 + (((long)array3[n6] & 0xFFFFFFFFL) - ((long)array[n2 + i] & 0xFFFFFFFFL) - ((long)array2[n3 + i] & 0xFFFFFFFFL));
            array3[n6] = (int)n7;
            n5 = n7 >> 32;
            ++i;
        }
        return (int)n5;
    }
    
    public static int subBothFrom(final int n, final int[] array, final int[] array2, final int[] array3) {
        long n2 = 0L;
        for (int i = 0; i < n; ++i) {
            final long n3 = n2 + (((long)array3[i] & 0xFFFFFFFFL) - ((long)array[i] & 0xFFFFFFFFL) - ((long)array2[i] & 0xFFFFFFFFL));
            array3[i] = (int)n3;
            n2 = n3 >> 32;
        }
        return (int)n2;
    }
    
    public static int subDWordAt(final int n, long n2, final int[] array, final int n3) {
        final int n4 = n3 + 0;
        final long n5 = ((long)array[n4] & 0xFFFFFFFFL) - (n2 & 0xFFFFFFFFL);
        array[n4] = (int)n5;
        final int n6 = n3 + 1;
        n2 = (n5 >> 32) + (((long)array[n6] & 0xFFFFFFFFL) - (n2 >>> 32));
        array[n6] = (int)n2;
        if (n2 >> 32 == 0L) {
            return 0;
        }
        return decAt(n, array, n3 + 2);
    }
    
    public static int subDWordAt(final int n, long n2, final int[] array, final int n3, final int n4) {
        final int n5 = n3 + n4;
        final long n6 = ((long)array[n5] & 0xFFFFFFFFL) - (n2 & 0xFFFFFFFFL);
        array[n5] = (int)n6;
        final int n7 = n5 + 1;
        n2 = (n6 >> 32) + (((long)array[n7] & 0xFFFFFFFFL) - (n2 >>> 32));
        array[n7] = (int)n2;
        if (n2 >> 32 == 0L) {
            return 0;
        }
        return decAt(n, array, n3, n4 + 2);
    }
    
    public static int subDWordFrom(final int n, long n2, final int[] array) {
        final long n3 = ((long)array[0] & 0xFFFFFFFFL) - (n2 & 0xFFFFFFFFL);
        array[0] = (int)n3;
        n2 = (n3 >> 32) + (((long)array[1] & 0xFFFFFFFFL) - (n2 >>> 32));
        array[1] = (int)n2;
        if (n2 >> 32 == 0L) {
            return 0;
        }
        return decAt(n, array, 2);
    }
    
    public static int subDWordFrom(final int n, long n2, final int[] array, final int n3) {
        final int n4 = n3 + 0;
        final long n5 = ((long)array[n4] & 0xFFFFFFFFL) - (n2 & 0xFFFFFFFFL);
        array[n4] = (int)n5;
        final int n6 = n3 + 1;
        n2 = (n5 >> 32) + (((long)array[n6] & 0xFFFFFFFFL) - (n2 >>> 32));
        array[n6] = (int)n2;
        if (n2 >> 32 == 0L) {
            return 0;
        }
        return decAt(n, array, n3, 2);
    }
    
    public static int subFrom(final int n, final int[] array, final int n2, final int[] array2, final int n3) {
        int i = 0;
        long n4 = 0L;
        while (i < n) {
            final int n5 = n3 + i;
            final long n6 = n4 + (((long)array2[n5] & 0xFFFFFFFFL) - ((long)array[n2 + i] & 0xFFFFFFFFL));
            array2[n5] = (int)n6;
            n4 = n6 >> 32;
            ++i;
        }
        return (int)n4;
    }
    
    public static int subFrom(final int n, final int[] array, final int[] array2) {
        long n2 = 0L;
        for (int i = 0; i < n; ++i) {
            final long n3 = n2 + (((long)array2[i] & 0xFFFFFFFFL) - ((long)array[i] & 0xFFFFFFFFL));
            array2[i] = (int)n3;
            n2 = n3 >> 32;
        }
        return (int)n2;
    }
    
    public static int subWordAt(final int n, final int n2, final int[] array, final int n3) {
        final long n4 = ((long)array[n3] & 0xFFFFFFFFL) - ((long)n2 & 0xFFFFFFFFL);
        array[n3] = (int)n4;
        if (n4 >> 32 == 0L) {
            return 0;
        }
        return decAt(n, array, n3 + 1);
    }
    
    public static int subWordAt(final int n, final int n2, final int[] array, final int n3, final int n4) {
        final int n5 = n3 + n4;
        final long n6 = ((long)array[n5] & 0xFFFFFFFFL) - ((long)n2 & 0xFFFFFFFFL);
        array[n5] = (int)n6;
        if (n6 >> 32 == 0L) {
            return 0;
        }
        return decAt(n, array, n3, n4 + 1);
    }
    
    public static int subWordFrom(final int n, final int n2, final int[] array) {
        final long n3 = ((long)array[0] & 0xFFFFFFFFL) - ((long)n2 & 0xFFFFFFFFL);
        array[0] = (int)n3;
        if (n3 >> 32 == 0L) {
            return 0;
        }
        return decAt(n, array, 1);
    }
    
    public static int subWordFrom(final int n, final int n2, final int[] array, final int n3) {
        final int n4 = n3 + 0;
        final long n5 = ((long)array[n4] & 0xFFFFFFFFL) - ((long)n2 & 0xFFFFFFFFL);
        array[n4] = (int)n5;
        if (n5 >> 32 == 0L) {
            return 0;
        }
        return decAt(n, array, n3, 1);
    }
    
    public static BigInteger toBigInteger(final int n, final int[] array) {
        final byte[] array2 = new byte[n << 2];
        for (int i = 0; i < n; ++i) {
            final int n2 = array[i];
            if (n2 != 0) {
                Pack.intToBigEndian(n2, array2, n - 1 - i << 2);
            }
        }
        return new BigInteger(1, array2);
    }
    
    public static void zero(final int n, final int[] array) {
        for (int i = 0; i < n; ++i) {
            array[i] = 0;
        }
    }
    
    public static void zero64(final int n, final long[] array) {
        for (int i = 0; i < n; ++i) {
            array[i] = 0L;
        }
    }
}
