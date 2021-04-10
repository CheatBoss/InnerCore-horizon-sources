package org.spongycastle.pqc.math.linearalgebra;

import java.io.*;

public final class PolynomialRingGF2
{
    private PolynomialRingGF2() {
    }
    
    public static int add(final int n, final int n2) {
        return n ^ n2;
    }
    
    public static int degree(int i) {
        int n = -1;
        while (i != 0) {
            ++n;
            i >>>= 1;
        }
        return n;
    }
    
    public static int degree(long n) {
        int n2 = 0;
        while (n != 0L) {
            ++n2;
            n >>>= 1;
        }
        return n2 - 1;
    }
    
    public static int gcd(int n, int i) {
        while (i != 0) {
            final int remainder = remainder(n, i);
            n = i;
            i = remainder;
        }
        return n;
    }
    
    public static int getIrreduciblePolynomial(final int n) {
        PrintStream printStream;
        String s;
        if (n < 0) {
            printStream = System.err;
            s = "The Degree is negative";
        }
        else if (n > 31) {
            printStream = System.err;
            s = "The Degree is more then 31";
        }
        else {
            if (n == 0) {
                return 1;
            }
            for (int i = (1 << n) + 1; i < 1 << n + 1; i += 2) {
                if (isIrreducible(i)) {
                    return i;
                }
            }
            return 0;
        }
        printStream.println(s);
        return 0;
    }
    
    public static boolean isIrreducible(final int n) {
        if (n == 0) {
            return false;
        }
        final int degree = degree(n);
        int i = 0;
        int modMultiply = 2;
        while (i < degree >>> 1) {
            modMultiply = modMultiply(modMultiply, modMultiply, n);
            if (gcd(modMultiply ^ 0x2, n) != 1) {
                return false;
            }
            ++i;
        }
        return true;
    }
    
    public static int modMultiply(int remainder, int n, final int n2) {
        int remainder2 = remainder(remainder, n2);
        remainder = remainder(n, n2);
        int n3 = 0;
        n = 0;
        if (remainder != 0) {
            final int degree = degree(n2);
            while (true) {
                n3 = n;
                if (remainder2 == 0) {
                    break;
                }
                int n4 = n;
                if ((byte)(remainder2 & 0x1) == 1) {
                    n4 = (n ^ remainder);
                }
                final int n5 = remainder2 >>> 1;
                final int n6 = remainder << 1;
                n = n4;
                remainder2 = n5;
                if ((remainder = n6) < 1 << degree) {
                    continue;
                }
                remainder = (n6 ^ n2);
                n = n4;
                remainder2 = n5;
            }
        }
        return n3;
    }
    
    public static long multiply(int n, final int n2) {
        long n4;
        long n3 = n4 = 0L;
        if (n2 != 0) {
            long n5 = (long)n2 & 0xFFFFFFFFL;
            while (true) {
                n4 = n3;
                if (n == 0) {
                    break;
                }
                long n6 = n3;
                if ((byte)(n & 0x1) == 1) {
                    n6 = (n3 ^ n5);
                }
                n >>>= 1;
                n5 <<= 1;
                n3 = n6;
            }
        }
        return n4;
    }
    
    public static int remainder(int n, final int n2) {
        if (n2 == 0) {
            System.err.println("Error: to be divided by 0");
            return 0;
        }
        while (degree(n) >= degree(n2)) {
            n ^= n2 << degree(n) - degree(n2);
        }
        return n;
    }
    
    public static int rest(long n, final int n2) {
        if (n2 == 0) {
            System.err.println("Error: to be divided by 0");
            return 0;
        }
        for (long n3 = (long)n2 & 0xFFFFFFFFL; n >>> 32 != 0L; n ^= n3 << degree(n) - degree(n3)) {}
        int n4;
        for (n4 = (int)(n & -1L); degree(n4) >= degree(n2); n4 ^= n2 << degree(n4) - degree(n2)) {}
        return n4;
    }
}
