package java.lang;

public final class Integer8
{
    private Integer8() {
    }
    
    public static int compare(final int n, final int n2) {
        if (n < n2) {
            return -1;
        }
        if (n == n2) {
            return 0;
        }
        return 1;
    }
    
    public static int compareUnsigned(final int n, final int n2) {
        return compare(n - Integer.MIN_VALUE, Integer.MIN_VALUE + n2);
    }
    
    public static int divideUnsigned(final int n, final int n2) {
        return (int)(toUnsignedLong(n) / toUnsignedLong(n2));
    }
    
    public static int hashCode(final int n) {
        return n;
    }
    
    public static int max(final int n, final int n2) {
        return Math.max(n, n2);
    }
    
    public static int min(final int n, final int n2) {
        return Math.min(n, n2);
    }
    
    public static int remainderUnsigned(final int n, final int n2) {
        return (int)(toUnsignedLong(n) % toUnsignedLong(n2));
    }
    
    public static int sum(final int n, final int n2) {
        return n + n2;
    }
    
    public static long toUnsignedLong(final int n) {
        return (long)n & 0xFFFFFFFFL;
    }
}
