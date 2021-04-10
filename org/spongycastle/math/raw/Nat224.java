package org.spongycastle.math.raw;

import java.math.*;
import org.spongycastle.util.*;

public abstract class Nat224
{
    private static final long M = 4294967295L;
    
    public static int add(final int[] array, final int n, final int[] array2, final int n2, final int[] array3, final int n3) {
        final long n4 = ((long)array[n + 0] & 0xFFFFFFFFL) + ((long)array2[n2 + 0] & 0xFFFFFFFFL) + 0L;
        array3[n3 + 0] = (int)n4;
        final long n5 = (n4 >>> 32) + (((long)array[n + 1] & 0xFFFFFFFFL) + ((long)array2[n2 + 1] & 0xFFFFFFFFL));
        array3[n3 + 1] = (int)n5;
        final long n6 = (n5 >>> 32) + (((long)array[n + 2] & 0xFFFFFFFFL) + ((long)array2[n2 + 2] & 0xFFFFFFFFL));
        array3[n3 + 2] = (int)n6;
        final long n7 = (n6 >>> 32) + (((long)array[n + 3] & 0xFFFFFFFFL) + ((long)array2[n2 + 3] & 0xFFFFFFFFL));
        array3[n3 + 3] = (int)n7;
        final long n8 = (n7 >>> 32) + (((long)array[n + 4] & 0xFFFFFFFFL) + ((long)array2[n2 + 4] & 0xFFFFFFFFL));
        array3[n3 + 4] = (int)n8;
        final long n9 = (n8 >>> 32) + (((long)array[n + 5] & 0xFFFFFFFFL) + ((long)array2[n2 + 5] & 0xFFFFFFFFL));
        array3[n3 + 5] = (int)n9;
        final long n10 = (n9 >>> 32) + (((long)array[n + 6] & 0xFFFFFFFFL) + ((long)array2[n2 + 6] & 0xFFFFFFFFL));
        array3[n3 + 6] = (int)n10;
        return (int)(n10 >>> 32);
    }
    
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
        final long n6 = (n5 >>> 32) + (((long)array[5] & 0xFFFFFFFFL) + ((long)array2[5] & 0xFFFFFFFFL));
        array3[5] = (int)n6;
        final long n7 = (n6 >>> 32) + (((long)array[6] & 0xFFFFFFFFL) + ((long)array2[6] & 0xFFFFFFFFL));
        array3[6] = (int)n7;
        return (int)(n7 >>> 32);
    }
    
    public static int addBothTo(final int[] array, int n, final int[] array2, final int n2, final int[] array3, final int n3) {
        final long n4 = array[n + 0];
        final long n5 = array2[n2 + 0];
        final int n6 = n3 + 0;
        final long n7 = (n4 & 0xFFFFFFFFL) + (n5 & 0xFFFFFFFFL) + ((long)array3[n6] & 0xFFFFFFFFL) + 0L;
        array3[n6] = (int)n7;
        final long n8 = array[n + 1];
        final long n9 = array2[n2 + 1];
        final int n10 = n3 + 1;
        final long n11 = (n7 >>> 32) + ((n8 & 0xFFFFFFFFL) + (n9 & 0xFFFFFFFFL) + ((long)array3[n10] & 0xFFFFFFFFL));
        array3[n10] = (int)n11;
        final long n12 = array[n + 2];
        final long n13 = array2[n2 + 2];
        final int n14 = n3 + 2;
        final long n15 = (n11 >>> 32) + ((n12 & 0xFFFFFFFFL) + (n13 & 0xFFFFFFFFL) + ((long)array3[n14] & 0xFFFFFFFFL));
        array3[n14] = (int)n15;
        final long n16 = array[n + 3];
        final long n17 = array2[n2 + 3];
        final int n18 = n3 + 3;
        final long n19 = (n15 >>> 32) + ((n16 & 0xFFFFFFFFL) + (n17 & 0xFFFFFFFFL) + ((long)array3[n18] & 0xFFFFFFFFL));
        array3[n18] = (int)n19;
        final long n20 = array[n + 4];
        final long n21 = array2[n2 + 4];
        final int n22 = n3 + 4;
        final long n23 = (n19 >>> 32) + ((n20 & 0xFFFFFFFFL) + (n21 & 0xFFFFFFFFL) + ((long)array3[n22] & 0xFFFFFFFFL));
        array3[n22] = (int)n23;
        final long n24 = array[n + 5];
        final long n25 = array2[n2 + 5];
        final int n26 = n3 + 5;
        final long n27 = (n23 >>> 32) + ((n24 & 0xFFFFFFFFL) + (n25 & 0xFFFFFFFFL) + ((long)array3[n26] & 0xFFFFFFFFL));
        array3[n26] = (int)n27;
        final long n28 = array[n + 6];
        final long n29 = array2[n2 + 6];
        n = n3 + 6;
        final long n30 = (n27 >>> 32) + ((n28 & 0xFFFFFFFFL) + (n29 & 0xFFFFFFFFL) + ((long)array3[n] & 0xFFFFFFFFL));
        array3[n] = (int)n30;
        return (int)(n30 >>> 32);
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
        final long n6 = (n5 >>> 32) + (((long)array[5] & 0xFFFFFFFFL) + ((long)array2[5] & 0xFFFFFFFFL) + ((long)array3[5] & 0xFFFFFFFFL));
        array3[5] = (int)n6;
        final long n7 = (n6 >>> 32) + (((long)array[6] & 0xFFFFFFFFL) + ((long)array2[6] & 0xFFFFFFFFL) + ((long)array3[6] & 0xFFFFFFFFL));
        array3[6] = (int)n7;
        return (int)(n7 >>> 32);
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
        n3 = n2 + 4;
        final long n14 = (n12 >>> 32) + ((n13 & 0xFFFFFFFFL) + ((long)array2[n3] & 0xFFFFFFFFL));
        array2[n3] = (int)n14;
        final long n15 = array[n + 5];
        n3 = n2 + 5;
        final long n16 = (n14 >>> 32) + ((n15 & 0xFFFFFFFFL) + ((long)array2[n3] & 0xFFFFFFFFL));
        array2[n3] = (int)n16;
        final long n17 = array[n + 6];
        n = n2 + 6;
        final long n18 = (n16 >>> 32) + ((n17 & 0xFFFFFFFFL) + ((long)array2[n] & 0xFFFFFFFFL));
        array2[n] = (int)n18;
        return (int)(n18 >>> 32);
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
        final long n6 = (n5 >>> 32) + (((long)array[5] & 0xFFFFFFFFL) + ((long)array2[5] & 0xFFFFFFFFL));
        array2[5] = (int)n6;
        final long n7 = (n6 >>> 32) + (((long)array[6] & 0xFFFFFFFFL) + ((long)array2[6] & 0xFFFFFFFFL));
        array2[6] = (int)n7;
        return (int)(n7 >>> 32);
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
        final int n19 = n + 4;
        final long n20 = array[n19];
        final int n21 = n2 + 4;
        final long n22 = (n18 >>> 32) + ((n20 & 0xFFFFFFFFL) + ((long)array2[n21] & 0xFFFFFFFFL));
        array2[n21] = (array[n19] = (int)n22);
        final int n23 = n + 5;
        final long n24 = array[n23];
        final int n25 = n2 + 5;
        final long n26 = (n22 >>> 32) + ((n24 & 0xFFFFFFFFL) + ((long)array2[n25] & 0xFFFFFFFFL));
        array2[n25] = (array[n23] = (int)n26);
        n += 6;
        final long n27 = array[n];
        n2 += 6;
        final long n28 = (n26 >>> 32) + ((n27 & 0xFFFFFFFFL) + ((long)array2[n2] & 0xFFFFFFFFL));
        array2[n2] = (array[n] = (int)n28);
        return (int)(n28 >>> 32);
    }
    
    public static void copy(final int[] array, final int[] array2) {
        array2[0] = array[0];
        array2[1] = array[1];
        array2[2] = array[2];
        array2[3] = array[3];
        array2[4] = array[4];
        array2[5] = array[5];
        array2[6] = array[6];
    }
    
    public static int[] create() {
        return new int[7];
    }
    
    public static int[] createExt() {
        return new int[14];
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
        for (int i = 6; i >= 0; --i) {
            if (array[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }
    
    public static int[] fromBigInteger(BigInteger shiftRight) {
        if (shiftRight.signum() >= 0 && shiftRight.bitLength() <= 224) {
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
            if (n2 >= 7) {
                return 0;
            }
            n = array[n2] >>> (n & 0x1F);
        }
        return n & 0x1;
    }
    
    public static boolean gte(final int[] array, final int n, final int[] array2, final int n2) {
        for (int i = 6; i >= 0; --i) {
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
        for (int i = 6; i >= 0; --i) {
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
        for (int i = 1; i < 7; ++i) {
            if (array[i] != 0) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isZero(final int[] array) {
        for (int i = 0; i < 7; ++i) {
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
        final long n8 = (long)array2[i + 5] & 0xFFFFFFFFL;
        final long n9 = (long)array2[i + 6] & 0xFFFFFFFFL;
        final long n10 = (long)array[n + 0] & 0xFFFFFFFFL;
        final long n11 = n10 * n3 + 0L;
        array3[n2 + 0] = (int)n11;
        final long n12 = (n11 >>> 32) + n10 * n4;
        array3[n2 + 1] = (int)n12;
        final long n13 = (n12 >>> 32) + n10 * n5;
        array3[n2 + 2] = (int)n13;
        final long n14 = (n13 >>> 32) + n10 * n6;
        array3[n2 + 3] = (int)n14;
        final long n15 = (n14 >>> 32) + n10 * n7;
        array3[n2 + 4] = (int)n15;
        final long n16 = (n15 >>> 32) + n10 * n8;
        array3[n2 + 5] = (int)n16;
        final long n17 = (n16 >>> 32) + n10 * n9;
        array3[n2 + 6] = (int)n17;
        array3[n2 + 7] = (int)(n17 >>> 32);
        long n18;
        int n19;
        long n20;
        int n21;
        long n22;
        int n23;
        long n24;
        int n25;
        long n26;
        int n27;
        long n28;
        int n29;
        long n30;
        int n31;
        long n32;
        for (i = 1; i < 7; ++i) {
            ++n2;
            n18 = ((long)array[n + i] & 0xFFFFFFFFL);
            n19 = n2 + 0;
            n20 = n18 * n3 + ((long)array3[n19] & 0xFFFFFFFFL) + 0L;
            array3[n19] = (int)n20;
            n21 = n2 + 1;
            n22 = (n20 >>> 32) + (n18 * n4 + ((long)array3[n21] & 0xFFFFFFFFL));
            array3[n21] = (int)n22;
            n23 = n2 + 2;
            n24 = (n22 >>> 32) + (n18 * n5 + ((long)array3[n23] & 0xFFFFFFFFL));
            array3[n23] = (int)n24;
            n25 = n2 + 3;
            n26 = (n24 >>> 32) + (n18 * n6 + ((long)array3[n25] & 0xFFFFFFFFL));
            array3[n25] = (int)n26;
            n27 = n2 + 4;
            n28 = (n26 >>> 32) + (n18 * n7 + ((long)array3[n27] & 0xFFFFFFFFL));
            array3[n27] = (int)n28;
            n29 = n2 + 5;
            n30 = (n28 >>> 32) + (n18 * n8 + ((long)array3[n29] & 0xFFFFFFFFL));
            array3[n29] = (int)n30;
            n31 = n2 + 6;
            n32 = (n30 >>> 32) + (n18 * n9 + ((long)array3[n31] & 0xFFFFFFFFL));
            array3[n31] = (int)n32;
            array3[n2 + 7] = (int)(n32 >>> 32);
        }
    }
    
    public static void mul(final int[] array, final int[] array2, final int[] array3) {
        final long n = (long)array2[0] & 0xFFFFFFFFL;
        int i = 1;
        final long n2 = (long)array2[1] & 0xFFFFFFFFL;
        final long n3 = (long)array2[2] & 0xFFFFFFFFL;
        final long n4 = (long)array2[3] & 0xFFFFFFFFL;
        final long n5 = (long)array2[4] & 0xFFFFFFFFL;
        final long n6 = (long)array2[5] & 0xFFFFFFFFL;
        final long n7 = (long)array2[6] & 0xFFFFFFFFL;
        final long n8 = (long)array[0] & 0xFFFFFFFFL;
        final long n9 = n8 * n + 0L;
        array3[0] = (int)n9;
        final long n10 = (n9 >>> 32) + n8 * n2;
        array3[1] = (int)n10;
        final long n11 = (n10 >>> 32) + n8 * n3;
        array3[2] = (int)n11;
        final long n12 = (n11 >>> 32) + n8 * n4;
        array3[3] = (int)n12;
        final long n13 = (n12 >>> 32) + n8 * n5;
        array3[4] = (int)n13;
        final long n14 = (n13 >>> 32) + n8 * n6;
        array3[5] = (int)n14;
        final long n15 = (n14 >>> 32) + n8 * n7;
        array3[6] = (int)n15;
        array3[7] = (int)(n15 >>> 32);
        while (i < 7) {
            final long n16 = (long)array[i] & 0xFFFFFFFFL;
            final int n17 = i + 0;
            final long n18 = n16 * n + ((long)array3[n17] & 0xFFFFFFFFL) + 0L;
            array3[n17] = (int)n18;
            final int n19 = i + 1;
            final long n20 = (n18 >>> 32) + (n16 * n2 + ((long)array3[n19] & 0xFFFFFFFFL));
            array3[n19] = (int)n20;
            final int n21 = i + 2;
            final long n22 = (n20 >>> 32) + (n16 * n3 + ((long)array3[n21] & 0xFFFFFFFFL));
            array3[n21] = (int)n22;
            final int n23 = i + 3;
            final long n24 = (n22 >>> 32) + (n16 * n4 + ((long)array3[n23] & 0xFFFFFFFFL));
            array3[n23] = (int)n24;
            final int n25 = i + 4;
            final long n26 = (n24 >>> 32) + (n16 * n5 + ((long)array3[n25] & 0xFFFFFFFFL));
            array3[n25] = (int)n26;
            final int n27 = i + 5;
            final long n28 = (n26 >>> 32) + (n16 * n6 + ((long)array3[n27] & 0xFFFFFFFFL));
            array3[n27] = (int)n28;
            final int n29 = i + 6;
            final long n30 = (n28 >>> 32) + (n16 * n7 + ((long)array3[n29] & 0xFFFFFFFFL));
            array3[n29] = (int)n30;
            array3[i + 7] = (int)(n30 >>> 32);
            i = n19;
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
        final long n16 = (long)array[n2 + 5] & 0xFFFFFFFFL;
        final long n17 = (n15 >>> 32) + (n5 * n16 + n14 + ((long)array2[n3 + 5] & 0xFFFFFFFFL));
        array3[n4 + 5] = (int)n17;
        final long n18 = (long)array[n2 + 6] & 0xFFFFFFFFL;
        final long n19 = (n17 >>> 32) + (n5 * n18 + n16 + ((long)array2[n3 + 6] & 0xFFFFFFFFL));
        array3[n4 + 6] = (int)n19;
        return (n19 >>> 32) + n18;
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
        return Nat.incAt(7, array, n3, 4);
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
        return Nat.incAt(7, array, n3, 3);
    }
    
    public static int mulAddTo(final int[] array, final int n, final int[] array2, int i, final int[] array3, int n2) {
        final long n3 = (long)array2[i + 0] & 0xFFFFFFFFL;
        final long n4 = (long)array2[i + 1] & 0xFFFFFFFFL;
        final long n5 = array2[i + 2];
        final long n6 = array2[i + 3];
        final long n7 = array2[i + 4];
        final long n8 = array2[i + 5];
        final long n9 = array2[i + 6];
        i = 0;
        long n10 = 0L;
        while (i < 7) {
            final long n11 = (long)array[n + i] & 0xFFFFFFFFL;
            final int n12 = n2 + 0;
            final long n13 = n11 * n3 + ((long)array3[n12] & 0xFFFFFFFFL) + 0L;
            array3[n12] = (int)n13;
            final int n14 = n2 + 1;
            final long n15 = (n13 >>> 32) + (n11 * n4 + ((long)array3[n14] & 0xFFFFFFFFL));
            array3[n14] = (int)n15;
            final int n16 = n2 + 2;
            final long n17 = (n15 >>> 32) + (n11 * (n5 & 0xFFFFFFFFL) + ((long)array3[n16] & 0xFFFFFFFFL));
            array3[n16] = (int)n17;
            final int n18 = n2 + 3;
            final long n19 = (n17 >>> 32) + ((n6 & 0xFFFFFFFFL) * n11 + ((long)array3[n18] & 0xFFFFFFFFL));
            array3[n18] = (int)n19;
            final int n20 = n2 + 4;
            final long n21 = (n19 >>> 32) + ((n7 & 0xFFFFFFFFL) * n11 + ((long)array3[n20] & 0xFFFFFFFFL));
            array3[n20] = (int)n21;
            final int n22 = n2 + 5;
            final long n23 = (n21 >>> 32) + ((n8 & 0xFFFFFFFFL) * n11 + ((long)array3[n22] & 0xFFFFFFFFL));
            array3[n22] = (int)n23;
            final int n24 = n2 + 6;
            final long n25 = (n23 >>> 32) + (n11 * (n9 & 0xFFFFFFFFL) + ((long)array3[n24] & 0xFFFFFFFFL));
            array3[n24] = (int)n25;
            n2 += 7;
            final long n26 = (n25 >>> 32) + (n10 + ((long)array3[n2] & 0xFFFFFFFFL));
            array3[n2] = (int)n26;
            n10 = n26 >>> 32;
            ++i;
            n2 = n14;
        }
        return (int)n10;
    }
    
    public static int mulAddTo(final int[] array, final int[] array2, final int[] array3) {
        final long n = array2[0];
        final long n2 = (long)array2[1] & 0xFFFFFFFFL;
        final long n3 = array2[2];
        final long n4 = array2[3];
        final long n5 = array2[4];
        final long n6 = array2[5];
        final long n7 = array2[6];
        int i = 0;
        long n8 = 0L;
        while (i < 7) {
            final long n9 = (long)array[i] & 0xFFFFFFFFL;
            final int n10 = i + 0;
            final long n11 = (n & 0xFFFFFFFFL) * n9 + ((long)array3[n10] & 0xFFFFFFFFL) + 0L;
            array3[n10] = (int)n11;
            final int n12 = i + 1;
            final long n13 = (n11 >>> 32) + (n9 * n2 + ((long)array3[n12] & 0xFFFFFFFFL));
            array3[n12] = (int)n13;
            final int n14 = i + 2;
            final long n15 = (n13 >>> 32) + ((n3 & 0xFFFFFFFFL) * n9 + ((long)array3[n14] & 0xFFFFFFFFL));
            array3[n14] = (int)n15;
            final int n16 = i + 3;
            final long n17 = (n15 >>> 32) + ((n4 & 0xFFFFFFFFL) * n9 + ((long)array3[n16] & 0xFFFFFFFFL));
            array3[n16] = (int)n17;
            final int n18 = i + 4;
            final long n19 = (n17 >>> 32) + ((n5 & 0xFFFFFFFFL) * n9 + ((long)array3[n18] & 0xFFFFFFFFL));
            array3[n18] = (int)n19;
            final int n20 = i + 5;
            final long n21 = (n19 >>> 32) + (n9 * (n6 & 0xFFFFFFFFL) + ((long)array3[n20] & 0xFFFFFFFFL));
            array3[n20] = (int)n21;
            final int n22 = i + 6;
            final long n23 = (n21 >>> 32) + (n9 * (n7 & 0xFFFFFFFFL) + ((long)array3[n22] & 0xFFFFFFFFL));
            array3[n22] = (int)n23;
            final int n24 = i + 7;
            final long n25 = (n23 >>> 32) + (n8 + ((long)array3[n24] & 0xFFFFFFFFL));
            array3[n24] = (int)n25;
            n8 = n25 >>> 32;
            i = n12;
        }
        return (int)n8;
    }
    
    public static int mulByWord(final int n, final int[] array) {
        final long n2 = (long)n & 0xFFFFFFFFL;
        final long n3 = ((long)array[0] & 0xFFFFFFFFL) * n2 + 0L;
        array[0] = (int)n3;
        final long n4 = (n3 >>> 32) + ((long)array[1] & 0xFFFFFFFFL) * n2;
        array[1] = (int)n4;
        final long n5 = (n4 >>> 32) + ((long)array[2] & 0xFFFFFFFFL) * n2;
        array[2] = (int)n5;
        final long n6 = (n5 >>> 32) + ((long)array[3] & 0xFFFFFFFFL) * n2;
        array[3] = (int)n6;
        final long n7 = (n6 >>> 32) + ((long)array[4] & 0xFFFFFFFFL) * n2;
        array[4] = (int)n7;
        final long n8 = (n7 >>> 32) + ((long)array[5] & 0xFFFFFFFFL) * n2;
        array[5] = (int)n8;
        final long n9 = (n8 >>> 32) + n2 * ((long)array[6] & 0xFFFFFFFFL);
        array[6] = (int)n9;
        return (int)(n9 >>> 32);
    }
    
    public static int mulByWordAddTo(final int n, final int[] array, final int[] array2) {
        final long n2 = (long)n & 0xFFFFFFFFL;
        final long n3 = ((long)array2[0] & 0xFFFFFFFFL) * n2 + ((long)array[0] & 0xFFFFFFFFL) + 0L;
        array2[0] = (int)n3;
        final long n4 = (n3 >>> 32) + (((long)array2[1] & 0xFFFFFFFFL) * n2 + ((long)array[1] & 0xFFFFFFFFL));
        array2[1] = (int)n4;
        final long n5 = (n4 >>> 32) + (((long)array2[2] & 0xFFFFFFFFL) * n2 + ((long)array[2] & 0xFFFFFFFFL));
        array2[2] = (int)n5;
        final long n6 = (n5 >>> 32) + (((long)array2[3] & 0xFFFFFFFFL) * n2 + ((long)array[3] & 0xFFFFFFFFL));
        array2[3] = (int)n6;
        final long n7 = (n6 >>> 32) + (((long)array2[4] & 0xFFFFFFFFL) * n2 + ((long)array[4] & 0xFFFFFFFFL));
        array2[4] = (int)n7;
        final long n8 = (n7 >>> 32) + (((long)array2[5] & 0xFFFFFFFFL) * n2 + ((long)array[5] & 0xFFFFFFFFL));
        array2[5] = (int)n8;
        final long n9 = (n8 >>> 32) + (n2 * ((long)array2[6] & 0xFFFFFFFFL) + ((long)array[6] & 0xFFFFFFFFL));
        array2[6] = (int)n9;
        return (int)(n9 >>> 32);
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
        } while ((n = n5) < 7);
        return (int)n7;
    }
    
    public static int mulWordAddTo(int n, final int[] array, final int n2, final int[] array2, final int n3) {
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
        final long n14 = (n12 >>> 32) + ((n13 & 0xFFFFFFFFL) * n4 + ((long)array2[n] & 0xFFFFFFFFL));
        array2[n] = (int)n14;
        final long n15 = array[n2 + 5];
        n = n3 + 5;
        final long n16 = (n14 >>> 32) + ((n15 & 0xFFFFFFFFL) * n4 + ((long)array2[n] & 0xFFFFFFFFL));
        array2[n] = (int)n16;
        final long n17 = array[n2 + 6];
        n = n3 + 6;
        final long n18 = (n16 >>> 32) + (n4 * (n17 & 0xFFFFFFFFL) + ((long)array2[n] & 0xFFFFFFFFL));
        array2[n] = (int)n18;
        return (int)(n18 >>> 32);
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
        return Nat.incAt(7, array, n3, 3);
    }
    
    public static void square(final int[] array, int n, final int[] array2, final int n2) {
        final long n3 = (long)array[n + 0] & 0xFFFFFFFFL;
        int n4 = 14;
        int n5 = 6;
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
        final int n41 = n2 + 8;
        final long n42 = array2[n41];
        final long n43 = (n35 & 0xFFFFFFFFL) + n38 * n3;
        final int n44 = (int)n43;
        array2[n22] = (n34 >>> 31 | n44 << 1);
        final long n45 = (n36 & 0xFFFFFFFFL) + ((n43 >>> 32) + n38 * n14);
        final long n46 = (n37 & 0xFFFFFFFFL) + ((n45 >>> 32) + n38 * n19);
        final long n47 = (n40 & 0xFFFFFFFFL) + ((n46 >>> 32) + n38 * n28);
        final long n48 = (n42 & 0xFFFFFFFFL) + (n40 >>> 32) + (n47 >>> 32);
        final long n49 = (long)array[n + 5] & 0xFFFFFFFFL;
        final int n50 = n2 + 9;
        final long n51 = ((long)array2[n50] & 0xFFFFFFFFL) + (n48 >>> 32);
        final int n52 = n2 + 10;
        final long n53 = array2[n52];
        final long n54 = (n45 & 0xFFFFFFFFL) + n49 * n3;
        final int n55 = (int)n54;
        array2[n29] = (n44 >>> 31 | n55 << 1);
        final long n56 = (n46 & 0xFFFFFFFFL) + ((n54 >>> 32) + n49 * n14);
        final long n57 = (n47 & 0xFFFFFFFFL) + ((n56 >>> 32) + n49 * n19);
        final long n58 = (n48 & 0xFFFFFFFFL) + ((n57 >>> 32) + n49 * n28);
        final long n59 = (n51 & 0xFFFFFFFFL) + ((n58 >>> 32) + n49 * n38);
        final long n60 = (n53 & 0xFFFFFFFFL) + (n51 >>> 32) + (n59 >>> 32);
        final long n61 = (long)array[n + 6] & 0xFFFFFFFFL;
        final int n62 = n2 + 11;
        final long n63 = ((long)array2[n62] & 0xFFFFFFFFL) + (n60 >>> 32);
        n = n2 + 12;
        final long n64 = array2[n];
        final long n65 = (n56 & 0xFFFFFFFFL) + n3 * n61;
        final int n66 = (int)n65;
        array2[n31] = (n66 << 1 | n55 >>> 31);
        final long n67 = (n57 & 0xFFFFFFFFL) + ((n65 >>> 32) + n14 * n61);
        final long n68 = (n58 & 0xFFFFFFFFL) + ((n67 >>> 32) + n19 * n61);
        final long n69 = (n59 & 0xFFFFFFFFL) + ((n68 >>> 32) + n28 * n61);
        final long n70 = (n60 & 0xFFFFFFFFL) + ((n69 >>> 32) + n38 * n61);
        final long n71 = (n63 & 0xFFFFFFFFL) + ((n70 >>> 32) + n61 * n49);
        final long n72 = (n64 & 0xFFFFFFFFL) + (n63 >>> 32) + (n71 >>> 32);
        final int n73 = (int)n67;
        array2[n39] = (n66 >>> 31 | n73 << 1);
        final int n74 = (int)n68;
        array2[n41] = (n73 >>> 31 | n74 << 1);
        final int n75 = (int)n69;
        array2[n50] = (n74 >>> 31 | n75 << 1);
        final int n76 = (int)n70;
        array2[n52] = (n75 >>> 31 | n76 << 1);
        final int n77 = (int)n71;
        array2[n62] = (n76 >>> 31 | n77 << 1);
        final int n78 = (int)n72;
        array2[n] = (n77 >>> 31 | n78 << 1);
        n = n2 + 13;
        array2[n] = (n78 >>> 31 | array2[n] + (int)(n72 >>> 32) << 1);
    }
    
    public static void square(final int[] array, final int[] array2) {
        final long n = (long)array[0] & 0xFFFFFFFFL;
        int n2 = 6;
        int n3 = 14;
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
        array2[3] = (n20 >>> 31 | n27 << 1);
        final long n28 = (n22 & 0xFFFFFFFFL) + ((n26 >>> 32) + n23 * n12);
        final long n29 = (n24 & 0xFFFFFFFFL) + ((n28 >>> 32) + n23 * n16);
        final long n30 = (n25 & 0xFFFFFFFFL) + (n24 >>> 32) + (n29 >>> 32);
        final long n31 = (long)array[4] & 0xFFFFFFFFL;
        final long n32 = ((long)array2[7] & 0xFFFFFFFFL) + (n30 >>> 32);
        final long n33 = array2[8];
        final long n34 = (n28 & 0xFFFFFFFFL) + n31 * n;
        final int n35 = (int)n34;
        array2[4] = (n27 >>> 31 | n35 << 1);
        final long n36 = (n29 & 0xFFFFFFFFL) + ((n34 >>> 32) + n31 * n12);
        final long n37 = (n30 & 0xFFFFFFFFL) + ((n36 >>> 32) + n31 * n16);
        final long n38 = (n32 & 0xFFFFFFFFL) + ((n37 >>> 32) + n31 * n23);
        final long n39 = (n33 & 0xFFFFFFFFL) + (n32 >>> 32) + (n38 >>> 32);
        final long n40 = (long)array[5] & 0xFFFFFFFFL;
        final long n41 = ((long)array2[9] & 0xFFFFFFFFL) + (n39 >>> 32);
        final long n42 = array2[10];
        final long n43 = (n36 & 0xFFFFFFFFL) + n40 * n;
        final int n44 = (int)n43;
        array2[5] = (n35 >>> 31 | n44 << 1);
        final long n45 = (n37 & 0xFFFFFFFFL) + ((n43 >>> 32) + n40 * n12);
        final long n46 = (n38 & 0xFFFFFFFFL) + ((n45 >>> 32) + n40 * n16);
        final long n47 = (n39 & 0xFFFFFFFFL) + ((n46 >>> 32) + n40 * n23);
        final long n48 = (n41 & 0xFFFFFFFFL) + ((n47 >>> 32) + n40 * n31);
        final long n49 = (n42 & 0xFFFFFFFFL) + (n41 >>> 32) + (n48 >>> 32);
        final long n50 = (long)array[6] & 0xFFFFFFFFL;
        final long n51 = ((long)array2[11] & 0xFFFFFFFFL) + (n49 >>> 32);
        final long n52 = array2[12];
        final long n53 = (n45 & 0xFFFFFFFFL) + n * n50;
        final int n54 = (int)n53;
        array2[6] = (n44 >>> 31 | n54 << 1);
        final long n55 = (n46 & 0xFFFFFFFFL) + ((n53 >>> 32) + n12 * n50);
        final long n56 = (n47 & 0xFFFFFFFFL) + ((n55 >>> 32) + n16 * n50);
        final long n57 = (n48 & 0xFFFFFFFFL) + ((n56 >>> 32) + n50 * n23);
        final long n58 = (n49 & 0xFFFFFFFFL) + ((n57 >>> 32) + n31 * n50);
        final long n59 = (n51 & 0xFFFFFFFFL) + ((n58 >>> 32) + n50 * n40);
        final long n60 = (n52 & 0xFFFFFFFFL) + (n51 >>> 32) + (n59 >>> 32);
        final int n61 = (int)n55;
        array2[7] = (n54 >>> 31 | n61 << 1);
        final int n62 = (int)n56;
        array2[8] = (n61 >>> 31 | n62 << 1);
        final int n63 = (int)n57;
        array2[9] = (n62 >>> 31 | n63 << 1);
        final int n64 = (int)n58;
        array2[10] = (n63 >>> 31 | n64 << 1);
        final int n65 = (int)n59;
        array2[11] = (n64 >>> 31 | n65 << 1);
        final int n66 = (int)n60;
        array2[12] = (n65 >>> 31 | n66 << 1);
        array2[13] = (n66 >>> 31 | array2[13] + (int)(n60 >>> 32) << 1);
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
        final long n9 = (n8 >> 32) + (((long)array[n + 5] & 0xFFFFFFFFL) - ((long)array2[n2 + 5] & 0xFFFFFFFFL));
        array3[n3 + 5] = (int)n9;
        final long n10 = (n9 >> 32) + (((long)array[n + 6] & 0xFFFFFFFFL) - ((long)array2[n2 + 6] & 0xFFFFFFFFL));
        array3[n3 + 6] = (int)n10;
        return (int)(n10 >> 32);
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
        final long n6 = (n5 >> 32) + (((long)array[5] & 0xFFFFFFFFL) - ((long)array2[5] & 0xFFFFFFFFL));
        array3[5] = (int)n6;
        final long n7 = (n6 >> 32) + (((long)array[6] & 0xFFFFFFFFL) - ((long)array2[6] & 0xFFFFFFFFL));
        array3[6] = (int)n7;
        return (int)(n7 >> 32);
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
        final long n6 = (n5 >> 32) + (((long)array3[5] & 0xFFFFFFFFL) - ((long)array[5] & 0xFFFFFFFFL) - ((long)array2[5] & 0xFFFFFFFFL));
        array3[5] = (int)n6;
        final long n7 = (n6 >> 32) + (((long)array3[6] & 0xFFFFFFFFL) - ((long)array[6] & 0xFFFFFFFFL) - ((long)array2[6] & 0xFFFFFFFFL));
        array3[6] = (int)n7;
        return (int)(n7 >> 32);
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
        final int n11 = n2 + 4;
        final long n12 = (n10 >> 32) + (((long)array2[n11] & 0xFFFFFFFFL) - ((long)array[n + 4] & 0xFFFFFFFFL));
        array2[n11] = (int)n12;
        final int n13 = n2 + 5;
        final long n14 = (n12 >> 32) + (((long)array2[n13] & 0xFFFFFFFFL) - ((long)array[n + 5] & 0xFFFFFFFFL));
        array2[n13] = (int)n14;
        n2 += 6;
        final long n15 = (n14 >> 32) + (((long)array2[n2] & 0xFFFFFFFFL) - ((long)array[n + 6] & 0xFFFFFFFFL));
        array2[n2] = (int)n15;
        return (int)(n15 >> 32);
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
        final long n6 = (n5 >> 32) + (((long)array2[5] & 0xFFFFFFFFL) - ((long)array[5] & 0xFFFFFFFFL));
        array2[5] = (int)n6;
        final long n7 = (n6 >> 32) + (((long)array2[6] & 0xFFFFFFFFL) - ((long)array[6] & 0xFFFFFFFFL));
        array2[6] = (int)n7;
        return (int)(n7 >> 32);
    }
    
    public static BigInteger toBigInteger(final int[] array) {
        final byte[] array2 = new byte[28];
        for (int i = 0; i < 7; ++i) {
            final int n = array[i];
            if (n != 0) {
                Pack.intToBigEndian(n, array2, 6 - i << 2);
            }
        }
        return new BigInteger(1, array2);
    }
    
    public static void zero(final int[] array) {
        array[0] = 0;
        array[2] = (array[1] = 0);
        array[4] = (array[3] = 0);
        array[6] = (array[5] = 0);
    }
}
