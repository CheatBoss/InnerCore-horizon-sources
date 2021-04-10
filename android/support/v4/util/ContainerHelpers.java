package android.support.v4.util;

class ContainerHelpers
{
    static final int[] EMPTY_INTS;
    static final long[] EMPTY_LONGS;
    static final Object[] EMPTY_OBJECTS;
    
    static {
        EMPTY_INTS = new int[0];
        EMPTY_LONGS = new long[0];
        EMPTY_OBJECTS = new Object[0];
    }
    
    static int binarySearch(final int[] array, int i, final int n) {
        int n2 = i - 1;
        i = 0;
        while (i <= n2) {
            final int n3 = i + n2 >>> 1;
            final int n4 = array[n3];
            if (n4 < n) {
                i = n3 + 1;
            }
            else {
                final int n5 = n3;
                if (n4 <= n) {
                    return n5;
                }
                n2 = n3 - 1;
            }
        }
        return ~i;
    }
    
    static int binarySearch(final long[] array, int i, final long n) {
        int n2 = i - 1;
        i = 0;
        while (i <= n2) {
            final int n3 = i + n2 >>> 1;
            final long n4 = array[n3];
            if (n4 < n) {
                i = n3 + 1;
            }
            else {
                final int n5 = n3;
                if (n4 <= n) {
                    return n5;
                }
                n2 = n3 - 1;
            }
        }
        return ~i;
    }
    
    public static boolean equal(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static int idealByteArraySize(final int n) {
        for (int i = 4; i < 32; ++i) {
            final int n2 = (1 << i) - 12;
            if (n <= n2) {
                return n2;
            }
        }
        return n;
    }
    
    public static int idealIntArraySize(final int n) {
        return idealByteArraySize(n * 4) / 4;
    }
    
    public static int idealLongArraySize(final int n) {
        return idealByteArraySize(n * 8) / 8;
    }
}
