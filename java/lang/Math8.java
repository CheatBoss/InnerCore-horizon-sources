package java.lang;

public final class Math8
{
    private Math8() {
    }
    
    public static int addExact(final int n, final int n2) {
        final int n3 = n + n2;
        if (((n ^ n3) & (n2 ^ n3)) < 0) {
            throw new ArithmeticException("integer overflow");
        }
        return n3;
    }
    
    public static long addExact(final long n, final long n2) {
        final long n3 = n + n2;
        if (((n ^ n3) & (n2 ^ n3)) < 0L) {
            throw new ArithmeticException("long overflow");
        }
        return n3;
    }
    
    public static int decrementExact(final int n) {
        if (n == Integer.MIN_VALUE) {
            throw new ArithmeticException("integer overflow");
        }
        return n - 1;
    }
    
    public static long decrementExact(final long n) {
        if (n == Long.MIN_VALUE) {
            throw new ArithmeticException("long overflow");
        }
        return n - 1L;
    }
    
    public static int floorDiv(final int n, final int n2) {
        int n4;
        final int n3 = n4 = n / n2;
        if ((n ^ n2) < 0) {
            n4 = n3;
            if (n3 * n2 != n) {
                n4 = n3 - 1;
            }
        }
        return n4;
    }
    
    public static long floorDiv(final long n, final int n2) {
        return floorDiv(n, (long)n2);
    }
    
    public static long floorDiv(final long n, final long n2) {
        long n4;
        final long n3 = n4 = n / n2;
        if ((n ^ n2) < 0L) {
            n4 = n3;
            if (n3 * n2 != n) {
                n4 = n3 - 1L;
            }
        }
        return n4;
    }
    
    public static int floorMod(final int n, final int n2) {
        return n - floorDiv(n, n2) * n2;
    }
    
    public static int floorMod(final long n, final int n2) {
        return (int)(n - floorDiv(n, n2) * n2);
    }
    
    public static long floorMod(final long n, final long n2) {
        return n - floorDiv(n, n2) * n2;
    }
    
    public static int incrementExact(final int n) {
        if (n == Integer.MAX_VALUE) {
            throw new ArithmeticException("integer overflow");
        }
        return n + 1;
    }
    
    public static long incrementExact(final long n) {
        if (n == Long.MAX_VALUE) {
            throw new ArithmeticException("long overflow");
        }
        return n + 1L;
    }
    
    public static int multiplyExact(final int n, final int n2) {
        final long n3 = n * (long)n2;
        if ((int)n3 != n3) {
            throw new ArithmeticException("integer overflow");
        }
        return (int)n3;
    }
    
    public static long multiplyExact(final long n, final int n2) {
        return multiplyExact(n, (long)n2);
    }
    
    public static long multiplyExact(final long n, final long n2) {
        final long n3 = n * n2;
        if ((Math.abs(n) | Math.abs(n2)) >>> 31 != 0L && ((n2 != 0L && n3 / n2 != n) || (n == Long.MIN_VALUE && n2 == -1L))) {
            throw new ArithmeticException("long overflow");
        }
        return n3;
    }
    
    public static int negateExact(final int n) {
        if (n == Integer.MIN_VALUE) {
            throw new ArithmeticException("integer overflow");
        }
        return -n;
    }
    
    public static long negateExact(final long n) {
        if (n == Long.MIN_VALUE) {
            throw new ArithmeticException("long overflow");
        }
        return -n;
    }
    
    public static double nextDown(final double n) {
        if (Double.isNaN(n)) {
            return n;
        }
        if (n == Double.NEGATIVE_INFINITY) {
            return n;
        }
        if (n == 0.0) {
            return -4.9E-324;
        }
        final long doubleToRawLongBits = Double.doubleToRawLongBits(n);
        long n2;
        if (n > 0.0) {
            n2 = -1L;
        }
        else {
            n2 = 1L;
        }
        return Double.longBitsToDouble(doubleToRawLongBits + n2);
    }
    
    public static float nextDown(final float n) {
        if (Float.isNaN(n)) {
            return n;
        }
        if (n == Float.NEGATIVE_INFINITY) {
            return n;
        }
        if (n == 0.0f) {
            return -1.4E-45f;
        }
        final int floatToRawIntBits = Float.floatToRawIntBits(n);
        int n2;
        if (n > 0.0f) {
            n2 = -1;
        }
        else {
            n2 = 1;
        }
        return Float.intBitsToFloat(floatToRawIntBits + n2);
    }
    
    public static int subtractExact(final int n, final int n2) {
        final int n3 = n - n2;
        if (((n ^ n2) & (n ^ n3)) < 0) {
            throw new ArithmeticException("integer overflow");
        }
        return n3;
    }
    
    public static long subtractExact(final long n, final long n2) {
        final long n3 = n - n2;
        if (((n ^ n2) & (n ^ n3)) < 0L) {
            throw new ArithmeticException("long overflow");
        }
        return n3;
    }
    
    public static int toIntExact(final long n) {
        if ((int)n != n) {
            throw new ArithmeticException("integer overflow");
        }
        return (int)n;
    }
}
