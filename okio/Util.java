package okio;

import java.nio.charset.*;

final class Util
{
    public static final Charset UTF_8;
    
    static {
        UTF_8 = Charset.forName("UTF-8");
    }
    
    public static boolean arrayRangeEquals(final byte[] array, final int n, final byte[] array2, final int n2, final int n3) {
        for (int i = 0; i < n3; ++i) {
            if (array[i + n] != array2[i + n2]) {
                return false;
            }
        }
        return true;
    }
    
    public static void checkOffsetAndCount(final long n, final long n2, final long n3) {
        if ((n2 | n3) >= 0L && n2 <= n && n - n2 >= n3) {
            return;
        }
        throw new ArrayIndexOutOfBoundsException(String.format("size=%s offset=%s byteCount=%s", n, n2, n3));
    }
    
    public static int reverseBytesInt(final int n) {
        return (n & 0xFF00) << 8 | ((0xFF000000 & n) >>> 24 | (0xFF0000 & n) >>> 8) | (n & 0xFF) << 24;
    }
    
    public static short reverseBytesShort(final short n) {
        final int n2 = n & 0xFFFF;
        return (short)((n2 & 0xFF00) >>> 8 | (n2 & 0xFF) << 8);
    }
    
    public static void sneakyRethrow(final Throwable t) {
        sneakyThrow2(t);
    }
    
    private static <T extends Throwable> void sneakyThrow2(final Throwable t) throws T, Throwable {
        throw t;
    }
}
