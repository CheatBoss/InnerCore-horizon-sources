package okhttp3.internal.ws;

import okio.*;

public final class WebSocketProtocol
{
    static String closeCodeExceptionMessage(final int n) {
        StringBuilder sb;
        if (n >= 1000 && n < 5000) {
            if ((n < 1004 || n > 1006) && (n < 1012 || n > 2999)) {
                return null;
            }
            sb = new StringBuilder();
            sb.append("Code ");
            sb.append(n);
            sb.append(" is reserved and may not be used.");
        }
        else {
            sb = new StringBuilder();
            sb.append("Code must be in range [1000,5000): ");
            sb.append(n);
        }
        return sb.toString();
    }
    
    static void toggleMask(final Buffer.UnsafeCursor unsafeCursor, final byte[] array) {
        final int length = array.length;
        int n = 0;
        do {
            final byte[] data = unsafeCursor.data;
            int n2;
            for (int i = unsafeCursor.start; i < unsafeCursor.end; ++i, n = n2 + 1) {
                n2 = n % length;
                data[i] ^= array[n2];
            }
        } while (unsafeCursor.next() != -1);
    }
    
    static void validateCloseCode(final int n) {
        final String closeCodeExceptionMessage = closeCodeExceptionMessage(n);
        if (closeCodeExceptionMessage == null) {
            return;
        }
        throw new IllegalArgumentException(closeCodeExceptionMessage);
    }
}
