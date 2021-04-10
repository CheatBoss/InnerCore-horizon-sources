package java.lang;

import java.math.*;

public final class Long8
{
    private Long8() {
    }
    
    public static int compare(final long n, final long n2) {
        if (n < n2) {
            return -1;
        }
        if (n == n2) {
            return 0;
        }
        return 1;
    }
    
    public static int compareUnsigned(final long n, final long n2) {
        return compare(n - Long.MIN_VALUE, n2 - Long.MIN_VALUE);
    }
    
    public static long divideUnsigned(final long n, final long n2) {
        if (n2 < 0L) {
            if (compareUnsigned(n, n2) < 0) {
                return 0L;
            }
            return 1L;
        }
        else {
            if (n > 0L) {
                return n / n2;
            }
            return toUnsignedBigInteger(n).divide(toUnsignedBigInteger(n2)).longValue();
        }
    }
    
    public static int hashCode(final long n) {
        return (int)(n ^ n >>> 32);
    }
    
    public static long max(final long n, final long n2) {
        return Math.max(n, n2);
    }
    
    public static long min(final long n, final long n2) {
        return Math.min(n, n2);
    }
    
    public static long remainderUnsigned(final long n, final long n2) {
        if (n > 0L && n2 > 0L) {
            return n % n2;
        }
        if (compareUnsigned(n, n2) < 0) {
            return n;
        }
        return toUnsignedBigInteger(n).remainder(toUnsignedBigInteger(n2)).longValue();
    }
    
    public static long sum(final long n, final long n2) {
        return n + n2;
    }
    
    private static BigInteger toUnsignedBigInteger(final long n) {
        if (n >= 0L) {
            return BigInteger.valueOf(n);
        }
        return BigInteger.valueOf(Integer8.toUnsignedLong((int)(n >>> 32))).shiftLeft(32).add(BigInteger.valueOf(Integer8.toUnsignedLong((int)n)));
    }
}
