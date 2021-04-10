package org.spongycastle.math.raw;

import java.math.*;
import org.spongycastle.util.*;

public abstract class Nat160
{
    private static final long M = 4294967295L;
    
    public static int add(final int[] array, final int[] array2, final int[] array3) {
        final long n = ((long)array[0] & 0xFFFFFFFFL) + ((long)array2[0] & 0xFFFFFFFFL) + 0L;
        array3[0] = (int)n;
        final long n2 = (n >>> 32) + (((long)array[1] & 0xFFFFFFFFL) + ((long)array2[1] & 0xFFFFFFFFL));
        array3[1] = (int)n2;
        final long n3 = (n2 >>> 32) + (((long)array[2] & 0xFFFFFFFFL) + ((long)array2[2] & 0xFFFFFFFFL));
        array3[2] = (int)n3;
        final long n4 = (n3 >>> 32) + (((long)array[3] & 0xFFFFFFFFL) + ((long)array2[3] & 0xFFFFFFFFL));
        array3[3] = (int)n4;
        final long n5 = (n4 >>> 32) + (((long)array[4] & 0xFFFFFFFFL) + ((long)array2[4] & 0xFFFFFFFFL));
        array3[4] = (int)n5;
        return (int)(n5 >>> 32);
    }
    
    public static int addBothTo(final int[] array, final int[] array2, final int[] array3) {
        final long n = ((long)array[0] & 0xFFFFFFFFL) + ((long)array2[0] & 0xFFFFFFFFL) + ((long)array3[0] & 0xFFFFFFFFL) + 0L;
        array3[0] = (int)n;
        final long n2 = (n >>> 32) + (((long)array[1] & 0xFFFFFFFFL) + ((long)array2[1] & 0xFFFFFFFFL) + ((long)array3[1] & 0xFFFFFFFFL));
        array3[1] = (int)n2;
        final long n3 = (n2 >>> 32) + (((long)array[2] & 0xFFFFFFFFL) + ((long)array2[2] & 0xFFFFFFFFL) + ((long)array3[2] & 0xFFFFFFFFL));
        array3[2] = (int)n3;
        final long n4 = (n3 >>> 32) + (((long)array[3] & 0xFFFFFFFFL) + ((long)array2[3] & 0xFFFFFFFFL) + ((long)array3[3] & 0xFFFFFFFFL));
        array3[3] = (int)n4;
        final long n5 = (n4 >>> 32) + (((long)array[4] & 0xFFFFFFFFL) + ((long)array2[4] & 0xFFFFFFFFL) + ((long)array3[4] & 0xFFFFFFFFL));
        array3[4] = (int)n5;
        return (int)(n5 >>> 32);
    }
    
    public static int addTo(final int[] array, int n, final int[] array2, final int n2, int n3) {
        final long n4 = n3;
        final long n5 = array[n + 0];
        n3 = n2 + 0;
        final long n6 = (n4 & 0xFFFFFFFFL) + ((n5 & 0xFFFFFFFFL) + ((long)array2[n3] & 0xFFFFFFFFL));
        array2[n3] = (int)n6;
        final long n7 = array[n + 1];
        n3 = n2 + 1;
        final long n8 = (n6 >>> 32) + ((n7 & 0xFFFFFFFFL) + ((long)array2[n3] & 0xFFFFFFFFL));
        array2[n3] = (int)n8;
        final long n9 = array[n + 2];
        n3 = n2 + 2;
        final long n10 = (n8 >>> 32) + ((n9 & 0xFFFFFFFFL) + ((long)array2[n3] & 0xFFFFFFFFL));
        array2[n3] = (int)n10;
        final long n11 = array[n + 3];
        n3 = n2 + 3;
        final long n12 = (n10 >>> 32) + ((n11 & 0xFFFFFFFFL) + ((long)array2[n3] & 0xFFFFFFFFL));
        array2[n3] = (int)n12;
        final long n13 = array[n + 4];
        n = n2 + 4;
        final long n14 = (n12 >>> 32) + ((n13 & 0xFFFFFFFFL) + ((long)array2[n] & 0xFFFFFFFFL));
        array2[n] = (int)n14;
        return (int)(n14 >>> 32);
    }
    
    public static int addTo(final int[] array, final int[] array2) {
        final long n = ((long)array[0] & 0xFFFFFFFFL) + ((long)array2[0] & 0xFFFFFFFFL) + 0L;
        array2[0] = (int)n;
        final long n2 = (n >>> 32) + (((long)array[1] & 0xFFFFFFFFL) + ((long)array2[1] & 0xFFFFFFFFL));
        array2[1] = (int)n2;
        final long n3 = (n2 >>> 32) + (((long)array[2] & 0xFFFFFFFFL) + ((long)array2[2] & 0xFFFFFFFFL));
        array2[2] = (int)n3;
        final long n4 = (n3 >>> 32) + (((long)array[3] & 0xFFFFFFFFL) + ((long)array2[3] & 0xFFFFFFFFL));
        array2[3] = (int)n4;
        final long n5 = (n4 >>> 32) + (((long)array[4] & 0xFFFFFFFFL) + ((long)array2[4] & 0xFFFFFFFFL));
        array2[4] = (int)n5;
        return (int)(n5 >>> 32);
    }
    
    public static int addToEachOther(final int[] array, int n, final int[] array2, int n2) {
        final int n3 = n + 0;
        final long n4 = array[n3];
        final int n5 = n2 + 0;
        final long n6 = (n4 & 0xFFFFFFFFL) + ((long)array2[n5] & 0xFFFFFFFFL) + 0L;
        array2[n5] = (array[n3] = (int)n6);
        final int n7 = n + 1;
        final long n8 = array[n7];
        final int n9 = n2 + 1;
        final long n10 = (n6 >>> 32) + ((n8 & 0xFFFFFFFFL) + ((long)array2[n9] & 0xFFFFFFFFL));
        array2[n9] = (array[n7] = (int)n10);
        final int n11 = n + 2;
        final long n12 = array[n11];
        final int n13 = n2 + 2;
        final long n14 = (n10 >>> 32) + ((n12 & 0xFFFFFFFFL) + ((long)array2[n13] & 0xFFFFFFFFL));
        array2[n13] = (array[n11] = (int)n14);
        final int n15 = n + 3;
        final long n16 = array[n15];
        final int n17 = n2 + 3;
        final long n18 = (n14 >>> 32) + ((n16 & 0xFFFFFFFFL) + ((long)array2[n17] & 0xFFFFFFFFL));
        array2[n17] = (array[n15] = (int)n18);
        n += 4;
        final long n19 = array[n];
        n2 += 4;
        final long n20 = (n18 >>> 32) + ((n19 & 0xFFFFFFFFL) + ((long)array2[n2] & 0xFFFFFFFFL));
        array2[n2] = (array[n] = (int)n20);
        return (int)(n20 >>> 32);
    }
    
    public static void copy(final int[] array, final int[] array2) {
        array2[0] = array[0];
        array2[1] = array[1];
        array2[2] = array[2];
        array2[3] = array[3];
        array2[4] = array[4];
    }
    
    public static int[] create() {
        return new int[5];
    }
    
    public static int[] createExt() {
        return new int[10];
    }
    
    public static boolean diff(final int[] array, final int n, final int[] array2, final int n2, final int[] array3, final int n3) {
        final boolean gte = gte(array, n, array2, n2);
        if (gte) {
            sub(array, n, array2, n2, array3, n3);
            return gte;
        }
        sub(array2, n2, array, n, array3, n3);
        return gte;
    }
    
    public static boolean eq(final int[] array, final int[] array2) {
        for (int i = 4; i >= 0; --i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static int[] fromBigInteger(BigInteger shiftRight) {
        if (shiftRight.signum() >= 0 && shiftRight.bitLength() <= 160) {
            final int[] create = create();
            for (int n = 0; shiftRight.signum() != 0; shiftRight = shiftRight.shiftRight(32), ++n) {
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
            if (n2 >= 5) {
                return 0;
            }
            n = array[n2] >>> (n & 0x1F);
        }
        return n & 0x1;
    }
    
    public static boolean gte(final int[] array, final int n, final int[] array2, final int n2) {
        for (int i = 4; i >= 0; --i) {
            final int n3 = array[n + i] ^ Integer.MIN_VALUE;
            final int n4 = Integer.MIN_VALUE ^ array2[n2 + i];
            if (n3 < n4) {
                return false;
            }
            if (n3 > n4) {
                return true;
            }
        }
        return true;
    }
    
    public static boolean gte(final int[] array, final int[] array2) {
        for (int i = 4; i >= 0; --i) {
            final int n = array[i] ^ Integer.MIN_VALUE;
            final int n2 = Integer.MIN_VALUE ^ array2[i];
            if (n < n2) {
                return false;
            }
            if (n > n2) {
                return true;
            }
        }
        return true;
    }
    
    public static boolean isOne(final int[] array) {
        if (array[0] != 1) {
            return false;
        }
        for (int i = 1; i < 5; ++i) {
            if (array[i] != 0) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isZero(final int[] array) {
        for (int i = 0; i < 5; ++i) {
            if (array[i] != 0) {
                return false;
            }
        }
        return true;
    }
    
    public static void mul(final int[] array, final int n, final int[] array2, int i, final int[] array3, int n2) {
        final long n3 = (long)array2[i + 0] & 0xFFFFFFFFL;
        final long n4 = (long)array2[i + 1] & 0xFFFFFFFFL;
        final long n5 = (long)array2[i + 2] & 0xFFFFFFFFL;
        final long n6 = (long)array2[i + 3] & 0xFFFFFFFFL;
        final long n7 = (long)array2[i + 4] & 0xFFFFFFFFL;
        final long n8 = (long)array[n + 0] & 0xFFFFFFFFL;
        final long n9 = n8 * n3 + 0L;
        array3[n2 + 0] = (int)n9;
        final long n10 = (n9 >>> 32) + n8 * n4;
        array3[n2 + 1] = (int)n10;
        final long n11 = (n10 >>> 32) + n8 * n5;
        array3[n2 + 2] = (int)n11;
        final long n12 = (n11 >>> 32) + n8 * n6;
        array3[n2 + 3] = (int)n12;
        final long n13 = (n12 >>> 32) + n8 * n7;
        array3[n2 + 4] = (int)n13;
        array3[n2 + 5] = (int)(n13 >>> 32);
        long n14;
        int n15;
        long n16;
        int n17;
        long n18;
        int n19;
        long n20;
        int n21;
        long n22;
        int n23;
        long n24;
        for (i = 1; i < 5; ++i) {
            ++n2;
            n14 = ((long)array[n + i] & 0xFFFFFFFFL);
            n15 = n2 + 0;
            n16 = n14 * n3 + ((long)array3[n15] & 0xFFFFFFFFL) + 0L;
            array3[n15] = (int)n16;
            n17 = n2 + 1;
            n18 = (n16 >>> 32) + (n14 * n4 + ((long)array3[n17] & 0xFFFFFFFFL));
            array3[n17] = (int)n18;
            n19 = n2 + 2;
            n20 = (n18 >>> 32) + (n14 * n5 + ((long)array3[n19] & 0xFFFFFFFFL));
            array3[n19] = (int)n20;
            n21 = n2 + 3;
            n22 = (n20 >>> 32) + (n14 * n6 + ((long)array3[n21] & 0xFFFFFFFFL));
            array3[n21] = (int)n22;
            n23 = n2 + 4;
            n24 = (n22 >>> 32) + (n14 * n7 + ((long)array3[n23] & 0xFFFFFFFFL));
            array3[n23] = (int)n24;
            array3[n2 + 5] = (int)(n24 >>> 32);
        }
    }
    
    public static void mul(final int[] array, final int[] array2, final int[] array3) {
        final long n = (long)array2[0] & 0xFFFFFFFFL;
        int i = 1;
        final long n2 = (long)array2[1] & 0xFFFFFFFFL;
        final long n3 = (long)array2[2] & 0xFFFFFFFFL;
        final long n4 = (long)array2[3] & 0xFFFFFFFFL;
        final long n5 = (long)array2[4] & 0xFFFFFFFFL;
        final long n6 = (long)array[0] & 0xFFFFFFFFL;
        final long n7 = n6 * n + 0L;
        array3[0] = (int)n7;
        final long n8 = (n7 >>> 32) + n6 * n2;
        array3[1] = (int)n8;
        final long n9 = (n8 >>> 32) + n6 * n3;
        array3[2] = (int)n9;
        final long n10 = (n9 >>> 32) + n6 * n4;
        array3[3] = (int)n10;
        final long n11 = (n10 >>> 32) + n6 * n5;
        array3[4] = (int)n11;
        array3[5] = (int)(n11 >>> 32);
        while (i < 5) {
            final long n12 = (long)array[i] & 0xFFFFFFFFL;
            final int n13 = i + 0;
            final long n14 = n12 * n + ((long)array3[n13] & 0xFFFFFFFFL) + 0L;
            array3[n13] = (int)n14;
            final int n15 = i + 1;
            final long n16 = (n14 >>> 32) + (n12 * n2 + ((long)array3[n15] & 0xFFFFFFFFL));
            array3[n15] = (int)n16;
            final int n17 = i + 2;
            final long n18 = (n16 >>> 32) + (n12 * n3 + ((long)array3[n17] & 0xFFFFFFFFL));
            array3[n17] = (int)n18;
            final int n19 = i + 3;
            final long n20 = (n18 >>> 32) + (n12 * n4 + ((long)array3[n19] & 0xFFFFFFFFL));
            array3[n19] = (int)n20;
            final int n21 = i + 4;
            final long n22 = (n20 >>> 32) + (n12 * n5 + ((long)array3[n21] & 0xFFFFFFFFL));
            array3[n21] = (int)n22;
            array3[i + 5] = (int)(n22 >>> 32);
            i = n15;
        }
    }
    
    public static long mul33Add(final int n, final int[] array, final int n2, final int[] array2, final int n3, final int[] array3, final int n4) {
        final long n5 = (long)n & 0xFFFFFFFFL;
        final long n6 = (long)array[n2 + 0] & 0xFFFFFFFFL;
        final long n7 = n5 * n6 + ((long)array2[n3 + 0] & 0xFFFFFFFFL) + 0L;
        array3[n4 + 0] = (int)n7;
        final long n8 = (long)array[n2 + 1] & 0xFFFFFFFFL;
        final long n9 = (n7 >>> 32) + (n5 * n8 + n6 + ((long)array2[n3 + 1] & 0xFFFFFFFFL));
        array3[n4 + 1] = (int)n9;
        final long n10 = (long)array[n2 + 2] & 0xFFFFFFFFL;
        final long n11 = (n9 >>> 32) + (n5 * n10 + n8 + ((long)array2[n3 + 2] & 0xFFFFFFFFL));
        array3[n4 + 2] = (int)n11;
        final long n12 = (long)array[n2 + 3] & 0xFFFFFFFFL;
        final long n13 = (n11 >>> 32) + (n5 * n12 + n10 + ((long)array2[n3 + 3] & 0xFFFFFFFFL));
        array3[n4 + 3] = (int)n13;
        final long n14 = (long)array[n2 + 4] & 0xFFFFFFFFL;
        final long n15 = (n13 >>> 32) + (n5 * n14 + n12 + ((long)array2[n3 + 4] & 0xFFFFFFFFL));
        array3[n4 + 4] = (int)n15;
        return (n15 >>> 32) + n14;
    }
    
    public static int mul33DWordAdd(int n, long n2, final int[] array, final int n3) {
        final long n4 = (long)n & 0xFFFFFFFFL;
        final long n5 = n2 & 0xFFFFFFFFL;
        n = n3 + 0;
        final long n6 = n4 * n5 + ((long)array[n] & 0xFFFFFFFFL) + 0L;
        array[n] = (int)n6;
        n2 >>>= 32;
        n = n3 + 1;
        final long n7 = (n6 >>> 32) + (n4 * n2 + n5 + ((long)array[n] & 0xFFFFFFFFL));
        array[n] = (int)n7;
        n = n3 + 2;
        n2 = (n7 >>> 32) + (n2 + ((long)array[n] & 0xFFFFFFFFL));
        array[n] = (int)n2;
        n = n3 + 3;
        n2 = (n2 >>> 32) + ((long)array[n] & 0xFFFFFFFFL);
        array[n] = (int)n2;
        if (n2 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(5, array, n3, 4);
    }
    
    public static int mul33WordAdd(int n, final int n2, final int[] array, final int n3) {
        final long n4 = n;
        final long n5 = (long)n2 & 0xFFFFFFFFL;
        n = n3 + 0;
        final long n6 = (n4 & 0xFFFFFFFFL) * n5 + ((long)array[n] & 0xFFFFFFFFL) + 0L;
        array[n] = (int)n6;
        n = n3 + 1;
        final long n7 = (n6 >>> 32) + (n5 + ((long)array[n] & 0xFFFFFFFFL));
        array[n] = (int)n7;
        n = n3 + 2;
        final long n8 = (n7 >>> 32) + ((long)array[n] & 0xFFFFFFFFL);
        array[n] = (int)n8;
        if (n8 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(5, array, n3, 3);
    }
    
    public static int mulAddTo(final int[] array, final int n, final int[] array2, int i, final int[] array3, int n2) {
        final long n3 = (long)array2[i + 0] & 0xFFFFFFFFL;
        final long n4 = (long)array2[i + 1] & 0xFFFFFFFFL;
        final long n5 = array2[i + 2];
        final long n6 = array2[i + 3];
        final long n7 = array2[i + 4];
        i = 0;
        long n8 = 0L;
        while (i < 5) {
            final long n9 = (long)array[n + i] & 0xFFFFFFFFL;
            final int n10 = n2 + 0;
            final long n11 = n9 * n3 + ((long)array3[n10] & 0xFFFFFFFFL) + 0L;
            array3[n10] = (int)n11;
            final int n12 = n2 + 1;
            final long n13 = (n11 >>> 32) + (n9 * n4 + ((long)array3[n12] & 0xFFFFFFFFL));
            array3[n12] = (int)n13;
            final int n14 = n2 + 2;
            final long n15 = (n13 >>> 32) + (n9 * (n5 & 0xFFFFFFFFL) + ((long)array3[n14] & 0xFFFFFFFFL));
            array3[n14] = (int)n15;
            final int n16 = n2 + 3;
            final long n17 = (n15 >>> 32) + ((n6 & 0xFFFFFFFFL) * n9 + ((long)array3[n16] & 0xFFFFFFFFL));
            array3[n16] = (int)n17;
            final int n18 = n2 + 4;
            final long n19 = (n17 >>> 32) + (n9 * (n7 & 0xFFFFFFFFL) + ((long)array3[n18] & 0xFFFFFFFFL));
            array3[n18] = (int)n19;
            n2 += 5;
            final long n20 = (n19 >>> 32) + (n8 + ((long)array3[n2] & 0xFFFFFFFFL));
            array3[n2] = (int)n20;
            n8 = n20 >>> 32;
            ++i;
            n2 = n12;
        }
        return (int)n8;
    }
    
    public static int mulAddTo(final int[] array, final int[] array2, final int[] array3) {
        int i = 0;
        final long n = (long)array2[0] & 0xFFFFFFFFL;
        final long n2 = (long)array2[1] & 0xFFFFFFFFL;
        final long n3 = array2[2];
        final long n4 = array2[3];
        final long n5 = array2[4];
        long n6 = 0L;
        while (i < 5) {
            final long n7 = (long)array[i] & 0xFFFFFFFFL;
            final int n8 = i + 0;
            final long n9 = n7 * n + ((long)array3[n8] & 0xFFFFFFFFL) + 0L;
            array3[n8] = (int)n9;
            final int n10 = i + 1;
            final long n11 = (n9 >>> 32) + (n7 * n2 + ((long)array3[n10] & 0xFFFFFFFFL));
            array3[n10] = (int)n11;
            final int n12 = i + 2;
            final long n13 = (n11 >>> 32) + (n7 * (n3 & 0xFFFFFFFFL) + ((long)array3[n12] & 0xFFFFFFFFL));
            array3[n12] = (int)n13;
            final int n14 = i + 3;
            final long n15 = (n13 >>> 32) + ((n4 & 0xFFFFFFFFL) * n7 + ((long)array3[n14] & 0xFFFFFFFFL));
            array3[n14] = (int)n15;
            final int n16 = i + 4;
            final long n17 = (n15 >>> 32) + (n7 * (n5 & 0xFFFFFFFFL) + ((long)array3[n16] & 0xFFFFFFFFL));
            array3[n16] = (int)n17;
            final int n18 = i + 5;
            final long n19 = (n17 >>> 32) + (n6 + ((long)array3[n18] & 0xFFFFFFFFL));
            array3[n18] = (int)n19;
            n6 = n19 >>> 32;
            i = n10;
        }
        return (int)n6;
    }
    
    public static int mulWord(int n, final int[] array, final int[] array2, final int n2) {
        final long n3 = n;
        long n4 = 0L;
        n = 0;
        int n5;
        long n7;
        do {
            final long n6 = n4 + ((long)array[n] & 0xFFFFFFFFL) * (n3 & 0xFFFFFFFFL);
            array2[n2 + n] = (int)n6;
            n7 = n6 >>> 32;
            n5 = n + 1;
            n4 = n7;
        } while ((n = n5) < 5);
        return (int)n7;
    }
    
    public static int mulWordAddExt(int n, final int[] array, final int n2, final int[] array2, final int n3) {
        final long n4 = (long)n & 0xFFFFFFFFL;
        final long n5 = array[n2 + 0];
        n = n3 + 0;
        final long n6 = (n5 & 0xFFFFFFFFL) * n4 + ((long)array2[n] & 0xFFFFFFFFL) + 0L;
        array2[n] = (int)n6;
        final long n7 = array[n2 + 1];
        n = n3 + 1;
        final long n8 = (n6 >>> 32) + ((n7 & 0xFFFFFFFFL) * n4 + ((long)array2[n] & 0xFFFFFFFFL));
        array2[n] = (int)n8;
        final long n9 = array[n2 + 2];
        n = n3 + 2;
        final long n10 = (n8 >>> 32) + ((n9 & 0xFFFFFFFFL) * n4 + ((long)array2[n] & 0xFFFFFFFFL));
        array2[n] = (int)n10;
        final long n11 = array[n2 + 3];
        n = n3 + 3;
        final long n12 = (n10 >>> 32) + ((n11 & 0xFFFFFFFFL) * n4 + ((long)array2[n] & 0xFFFFFFFFL));
        array2[n] = (int)n12;
        final long n13 = array[n2 + 4];
        n = n3 + 4;
        final long n14 = (n12 >>> 32) + (n4 * (n13 & 0xFFFFFFFFL) + ((long)array2[n] & 0xFFFFFFFFL));
        array2[n] = (int)n14;
        return (int)(n14 >>> 32);
    }
    
    public static int mulWordDwordAdd(int n, long n2, final int[] array, final int n3) {
        final long n4 = (long)n & 0xFFFFFFFFL;
        n = n3 + 0;
        final long n5 = (n2 & 0xFFFFFFFFL) * n4 + ((long)array[n] & 0xFFFFFFFFL) + 0L;
        array[n] = (int)n5;
        n = n3 + 1;
        n2 = (n5 >>> 32) + (n4 * (n2 >>> 32) + ((long)array[n] & 0xFFFFFFFFL));
        array[n] = (int)n2;
        n = n3 + 2;
        n2 = (n2 >>> 32) + ((long)array[n] & 0xFFFFFFFFL);
        array[n] = (int)n2;
        if (n2 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(5, array, n3, 3);
    }
    
    public static int mulWordsAdd(int n, final int n2, final int[] array, final int n3) {
        final long n4 = n;
        final long n5 = n2;
        n = n3 + 0;
        final long n6 = (n5 & 0xFFFFFFFFL) * (n4 & 0xFFFFFFFFL) + ((long)array[n] & 0xFFFFFFFFL) + 0L;
        array[n] = (int)n6;
        n = n3 + 1;
        final long n7 = (n6 >>> 32) + ((long)array[n] & 0xFFFFFFFFL);
        array[n] = (int)n7;
        if (n7 >>> 32 == 0L) {
            return 0;
        }
        return Nat.incAt(5, array, n3, 2);
    }
    
    public static void square(final int[] array, int n, final int[] array2, final int n2) {
        final long n3 = (long)array[n + 0] & 0xFFFFFFFFL;
        int n4 = 10;
        int n5 = 4;
        int n6 = 0;
        while (true) {
            final int n7 = n5 - 1;
            final long n8 = (long)array[n + n5] & 0xFFFFFFFFL;
            final long n9 = n8 * n8;
            final int n10 = n4 - 1;
            array2[n2 + n10] = ((int)(n9 >>> 33) | n6 << 31);
            n4 = n10 - 1;
            array2[n2 + n4] = (int)(n9 >>> 1);
            n6 = (int)n9;
            if (n7 <= 0) {
                break;
            }
            n5 = n7;
        }
        final long n11 = n3 * n3;
        final long n12 = n6 << 31;
        array2[n2 + 0] = (int)n11;
        final int n13 = (int)(n11 >>> 32);
        final long n14 = (long)array[n + 1] & 0xFFFFFFFFL;
        final int n15 = n2 + 2;
        final long n16 = array2[n15];
        final long n17 = ((n12 & 0xFFFFFFFFL) | n11 >>> 33) + n14 * n3;
        final int n18 = (int)n17;
        array2[n2 + 1] = (n18 << 1 | (n13 & 0x1));
        final long n19 = (long)array[n + 2] & 0xFFFFFFFFL;
        final int n20 = n2 + 3;
        final long n21 = array2[n20];
        final int n22 = n2 + 4;
        final long n23 = array2[n22];
        final long n24 = (n16 & 0xFFFFFFFFL) + (n17 >>> 32) + n19 * n3;
        final int n25 = (int)n24;
        array2[n15] = (n18 >>> 31 | n25 << 1);
        final long n26 = (n21 & 0xFFFFFFFFL) + ((n24 >>> 32) + n19 * n14);
        final long n27 = (n23 & 0xFFFFFFFFL) + (n26 >>> 32);
        final long n28 = (long)array[n + 3] & 0xFFFFFFFFL;
        final int n29 = n2 + 5;
        final long n30 = ((long)array2[n29] & 0xFFFFFFFFL) + (n27 >>> 32);
        final int n31 = n2 + 6;
        final long n32 = array2[n31];
        final long n33 = (n26 & 0xFFFFFFFFL) + n28 * n3;
        final int n34 = (int)n33;
        array2[n20] = (n34 << 1 | n25 >>> 31);
        final long n35 = (n27 & 0xFFFFFFFFL) + ((n33 >>> 32) + n28 * n14);
        final long n36 = (n30 & 0xFFFFFFFFL) + ((n35 >>> 32) + n28 * n19);
        final long n37 = (n32 & 0xFFFFFFFFL) + (n30 >>> 32) + (n36 >>> 32);
        final long n38 = (long)array[n + 4] & 0xFFFFFFFFL;
        final int n39 = n2 + 7;
        final long n40 = ((long)array2[n39] & 0xFFFFFFFFL) + (n37 >>> 32);
        n = n2 + 8;
        final long n41 = array2[n];
        final long n42 = (n35 & 0xFFFFFFFFL) + n3 * n38;
        final int n43 = (int)n42;
        array2[n22] = (n34 >>> 31 | n43 << 1);
        final long n44 = (n36 & 0xFFFFFFFFL) + ((n42 >>> 32) + n14 * n38);
        final long n45 = (n37 & 0xFFFFFFFFL) + ((n44 >>> 32) + n19 * n38);
        final long n46 = (n40 & 0xFFFFFFFFL) + ((n45 >>> 32) + n38 * n28);
        final long n47 = (n41 & 0xFFFFFFFFL) + (n40 >>> 32) + (n46 >>> 32);
        final int n48 = (int)n44;
        array2[n29] = (n43 >>> 31 | n48 << 1);
        final int n49 = (int)n45;
        array2[n31] = (n48 >>> 31 | n49 << 1);
        final int n50 = (int)n46;
        array2[n39] = (n49 >>> 31 | n50 << 1);
        final int n51 = (int)n47;
        array2[n] = (n50 >>> 31 | n51 << 1);
        n = n2 + 9;
        array2[n] = (n51 >>> 31 | array2[n] + (int)(n47 >>> 32) << 1);
    }
    
    public static void square(final int[] array, final int[] array2) {
        final long n = (long)array[0] & 0xFFFFFFFFL;
        int n2 = 4;
        int n3 = 10;
        int n4 = 0;
        while (true) {
            final int n5 = n2 - 1;
            final long n6 = (long)array[n2] & 0xFFFFFFFFL;
            final long n7 = n6 * n6;
            final int n8 = n3 - 1;
            array2[n8] = ((int)(n7 >>> 33) | n4 << 31);
            n3 = n8 - 1;
            array2[n3] = (int)(n7 >>> 1);
            n4 = (int)n7;
            if (n5 <= 0) {
                break;
            }
            n2 = n5;
        }
        final long n9 = n * n;
        final long n10 = n4 << 31;
        array2[0] = (int)n9;
        final int n11 = (int)(n9 >>> 32);
        final long n12 = (long)array[1] & 0xFFFFFFFFL;
        final long n13 = array2[2];
        final long n14 = ((n10 & 0xFFFFFFFFL) | n9 >>> 33) + n12 * n;
        final int n15 = (int)n14;
        array2[1] = (n15 << 1 | (n11 & 0x1));
        final long n16 = (long)array[2] & 0xFFFFFFFFL;
        final long n17 = array2[3];
        final long n18 = array2[4];
        final long n19 = (n13 & 0xFFFFFFFFL) + (n14 >>> 32) + n16 * n;
        final int n20 = (int)n19;
        array2[2] = (n20 << 1 | n15 >>> 31);
        final long n21 = (n17 & 0xFFFFFFFFL) + ((n19 >>> 32) + n16 * n12);
        final long n22 = (n18 & 0xFFFFFFFFL) + (n21 >>> 32);
        final long n23 = (long)array[3] & 0xFFFFFFFFL;
        final long n24 = ((long)array2[5] & 0xFFFFFFFFL) + (n22 >>> 32);
        final long n25 = array2[6];
        final long n26 = (n21 & 0xFFFFFFFFL) + n23 * n;
        final int n27 = (int)n26;
        array2[3] = (n27 << 1 | n20 >>> 31);
        final long n28 = (n22 & 0xFFFFFFFFL) + ((n26 >>> 32) + n23 * n12);
        final long n29 = (n24 & 0xFFFFFFFFL) + ((n28 >>> 32) + n23 * n16);
        final long n30 = (n25 & 0xFFFFFFFFL) + (n24 >>> 32) + (n29 >>> 32);
        final long n31 = (long)array[4] & 0xFFFFFFFFL;
        final long n32 = ((long)array2[7] & 0xFFFFFFFFL) + (n30 >>> 32);
        final long n33 = array2[8];
        final long n34 = (n28 & 0xFFFFFFFFL) + n31 * n;
        final int n35 = (int)n34;
        array2[4] = (n27 >>> 31 | n35 << 1);
        final long n36 = (n29 & 0xFFFFFFFFL) + ((n34 >>> 32) + n12 * n31);
        final long n37 = (n30 & 0xFFFFFFFFL) + ((n36 >>> 32) + n16 * n31);
        final long n38 = (n32 & 0xFFFFFFFFL) + ((n37 >>> 32) + n31 * n23);
        final long n39 = (n33 & 0xFFFFFFFFL) + (n32 >>> 32) + (n38 >>> 32);
        final int n40 = (int)n36;
        array2[5] = (n35 >>> 31 | n40 << 1);
        final int n41 = (int)n37;
        array2[6] = (n40 >>> 31 | n41 << 1);
        final int n42 = (int)n38;
        array2[7] = (n41 >>> 31 | n42 << 1);
        final int n43 = (int)n39;
        array2[8] = (n42 >>> 31 | n43 << 1);
        array2[9] = (n43 >>> 31 | array2[9] + (int)(n39 >>> 32) << 1);
    }
    
    public static int sub(final int[] array, final int n, final int[] array2, final int n2, final int[] array3, final int n3) {
        final long n4 = ((long)array[n + 0] & 0xFFFFFFFFL) - ((long)array2[n2 + 0] & 0xFFFFFFFFL) + 0L;
        array3[n3 + 0] = (int)n4;
        final long n5 = (n4 >> 32) + (((long)array[n + 1] & 0xFFFFFFFFL) - ((long)array2[n2 + 1] & 0xFFFFFFFFL));
        array3[n3 + 1] = (int)n5;
        final long n6 = (n5 >> 32) + (((long)array[n + 2] & 0xFFFFFFFFL) - ((long)array2[n2 + 2] & 0xFFFFFFFFL));
        array3[n3 + 2] = (int)n6;
        final long n7 = (n6 >> 32) + (((long)array[n + 3] & 0xFFFFFFFFL) - ((long)array2[n2 + 3] & 0xFFFFFFFFL));
        array3[n3 + 3] = (int)n7;
        final long n8 = (n7 >> 32) + (((long)array[n + 4] & 0xFFFFFFFFL) - ((long)array2[n2 + 4] & 0xFFFFFFFFL));
        array3[n3 + 4] = (int)n8;
        return (int)(n8 >> 32);
    }
    
    public static int sub(final int[] array, final int[] array2, final int[] array3) {
        final long n = ((long)array[0] & 0xFFFFFFFFL) - ((long)array2[0] & 0xFFFFFFFFL) + 0L;
        array3[0] = (int)n;
        final long n2 = (n >> 32) + (((long)array[1] & 0xFFFFFFFFL) - ((long)array2[1] & 0xFFFFFFFFL));
        array3[1] = (int)n2;
        final long n3 = (n2 >> 32) + (((long)array[2] & 0xFFFFFFFFL) - ((long)array2[2] & 0xFFFFFFFFL));
        array3[2] = (int)n3;
        final long n4 = (n3 >> 32) + (((long)array[3] & 0xFFFFFFFFL) - ((long)array2[3] & 0xFFFFFFFFL));
        array3[3] = (int)n4;
        final long n5 = (n4 >> 32) + (((long)array[4] & 0xFFFFFFFFL) - ((long)array2[4] & 0xFFFFFFFFL));
        array3[4] = (int)n5;
        return (int)(n5 >> 32);
    }
    
    public static int subBothFrom(final int[] array, final int[] array2, final int[] array3) {
        final long n = ((long)array3[0] & 0xFFFFFFFFL) - ((long)array[0] & 0xFFFFFFFFL) - ((long)array2[0] & 0xFFFFFFFFL) + 0L;
        array3[0] = (int)n;
        final long n2 = (n >> 32) + (((long)array3[1] & 0xFFFFFFFFL) - ((long)array[1] & 0xFFFFFFFFL) - ((long)array2[1] & 0xFFFFFFFFL));
        array3[1] = (int)n2;
        final long n3 = (n2 >> 32) + (((long)array3[2] & 0xFFFFFFFFL) - ((long)array[2] & 0xFFFFFFFFL) - ((long)array2[2] & 0xFFFFFFFFL));
        array3[2] = (int)n3;
        final long n4 = (n3 >> 32) + (((long)array3[3] & 0xFFFFFFFFL) - ((long)array[3] & 0xFFFFFFFFL) - ((long)array2[3] & 0xFFFFFFFFL));
        array3[3] = (int)n4;
        final long n5 = (n4 >> 32) + (((long)array3[4] & 0xFFFFFFFFL) - ((long)array[4] & 0xFFFFFFFFL) - ((long)array2[4] & 0xFFFFFFFFL));
        array3[4] = (int)n5;
        return (int)(n5 >> 32);
    }
    
    public static int subFrom(final int[] array, final int n, final int[] array2, int n2) {
        final int n3 = n2 + 0;
        final long n4 = ((long)array2[n3] & 0xFFFFFFFFL) - ((long)array[n + 0] & 0xFFFFFFFFL) + 0L;
        array2[n3] = (int)n4;
        final int n5 = n2 + 1;
        final long n6 = (n4 >> 32) + (((long)array2[n5] & 0xFFFFFFFFL) - ((long)array[n + 1] & 0xFFFFFFFFL));
        array2[n5] = (int)n6;
        final int n7 = n2 + 2;
        final long n8 = (n6 >> 32) + (((long)array2[n7] & 0xFFFFFFFFL) - ((long)array[n + 2] & 0xFFFFFFFFL));
        array2[n7] = (int)n8;
        final int n9 = n2 + 3;
        final long n10 = (n8 >> 32) + (((long)array2[n9] & 0xFFFFFFFFL) - ((long)array[n + 3] & 0xFFFFFFFFL));
        array2[n9] = (int)n10;
        n2 += 4;
        final long n11 = (n10 >> 32) + (((long)array2[n2] & 0xFFFFFFFFL) - ((long)array[n + 4] & 0xFFFFFFFFL));
        array2[n2] = (int)n11;
        return (int)(n11 >> 32);
    }
    
    public static int subFrom(final int[] array, final int[] array2) {
        final long n = ((long)array2[0] & 0xFFFFFFFFL) - ((long)array[0] & 0xFFFFFFFFL) + 0L;
        array2[0] = (int)n;
        final long n2 = (n >> 32) + (((long)array2[1] & 0xFFFFFFFFL) - ((long)array[1] & 0xFFFFFFFFL));
        array2[1] = (int)n2;
        final long n3 = (n2 >> 32) + (((long)array2[2] & 0xFFFFFFFFL) - ((long)array[2] & 0xFFFFFFFFL));
        array2[2] = (int)n3;
        final long n4 = (n3 >> 32) + (((long)array2[3] & 0xFFFFFFFFL) - ((long)array[3] & 0xFFFFFFFFL));
        array2[3] = (int)n4;
        final long n5 = (n4 >> 32) + (((long)array2[4] & 0xFFFFFFFFL) - ((long)array[4] & 0xFFFFFFFFL));
        array2[4] = (int)n5;
        return (int)(n5 >> 32);
    }
    
    public static BigInteger toBigInteger(final int[] array) {
        final byte[] array2 = new byte[20];
        for (int i = 0; i < 5; ++i) {
            final int n = array[i];
            if (n != 0) {
                Pack.intToBigEndian(n, array2, 4 - i << 2);
            }
        }
        return new BigInteger(1, array2);
    }
    
    public static void zero(final int[] array) {
        array[0] = 0;
        array[2] = (array[1] = 0);
        array[4] = (array[3] = 0);
    }
}
