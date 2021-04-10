package org.spongycastle.asn1;

import java.io.*;
import java.nio.channels.*;

class StreamUtil
{
    private static final long MAX_MEMORY;
    
    static {
        MAX_MEMORY = Runtime.getRuntime().maxMemory();
    }
    
    static int calculateBodyLength(int n) {
        int n2 = 1;
        final int n3 = 1;
        if (n > 127) {
            final int n4 = 1;
            int n5 = n;
            n = n4;
            while (true) {
                n5 >>>= 8;
                if (n5 == 0) {
                    break;
                }
                ++n;
            }
            int n6 = (n - 1) * 8;
            n = n3;
            while (true) {
                n2 = n;
                if (n6 < 0) {
                    break;
                }
                ++n;
                n6 -= 8;
            }
        }
        return n2;
    }
    
    static int calculateTagLength(int n) throws IOException {
        int n2 = 1;
        if (n >= 31) {
            if (n < 128) {
                return 2;
            }
            final byte[] array = new byte[5];
            int n3 = 4;
            int n4;
            int n5;
            do {
                n4 = n >> 7;
                n5 = n3 - 1;
                array[n5] = (byte)((n4 & 0x7F) | 0x80);
                n3 = n5;
            } while ((n = n4) > 127);
            n2 = 1 + (5 - n5);
        }
        return n2;
    }
    
    static int findLimit(final InputStream inputStream) {
        if (inputStream instanceof LimitedInputStream) {
            return ((LimitedInputStream)inputStream).getRemaining();
        }
        if (inputStream instanceof ASN1InputStream) {
            return ((ASN1InputStream)inputStream).getLimit();
        }
        if (inputStream instanceof ByteArrayInputStream) {
            return ((ByteArrayInputStream)inputStream).available();
        }
        if (inputStream instanceof FileInputStream) {
            try {
                final FileChannel channel = ((FileInputStream)inputStream).getChannel();
                long size;
                if (channel != null) {
                    size = channel.size();
                }
                else {
                    size = 2147483647L;
                }
                if (size < 2147483647L) {
                    return (int)size;
                }
            }
            catch (IOException ex) {}
        }
        final long max_MEMORY = StreamUtil.MAX_MEMORY;
        if (max_MEMORY > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)max_MEMORY;
    }
}
