package com.android.dex;

import com.android.dex.util.*;

public final class EncodedValueCodec
{
    private EncodedValueCodec() {
    }
    
    public static int readSignedInt(final ByteInput byteInput, final int n) {
        int n2 = 0;
        for (int i = n; i >= 0; --i) {
            n2 = (n2 >>> 8 | (byteInput.readByte() & 0xFF) << 24);
        }
        return n2 >> (3 - n) * 8;
    }
    
    public static long readSignedLong(final ByteInput byteInput, final int n) {
        long n2 = 0L;
        for (int i = n; i >= 0; --i) {
            n2 = (n2 >>> 8 | ((long)byteInput.readByte() & 0xFFL) << 56);
        }
        return n2 >> (7 - n) * 8;
    }
    
    public static int readUnsignedInt(final ByteInput byteInput, int i, final boolean b) {
        if (!b) {
            int n = 0;
            for (int j = i; j >= 0; --j) {
                n = (n >>> 8 | (byteInput.readByte() & 0xFF) << 24);
            }
            return n >>> (3 - i) * 8;
        }
        int n2 = 0;
        while (i >= 0) {
            n2 = (n2 >>> 8 | (byteInput.readByte() & 0xFF) << 24);
            --i;
        }
        return n2;
    }
    
    public static long readUnsignedLong(final ByteInput byteInput, int i, final boolean b) {
        if (!b) {
            long n = 0L;
            for (int j = i; j >= 0; --j) {
                n = (n >>> 8 | ((long)byteInput.readByte() & 0xFFL) << 56);
            }
            return n >>> (7 - i) * 8;
        }
        long n2 = 0L;
        while (i >= 0) {
            n2 = (n2 >>> 8 | ((long)byteInput.readByte() & 0xFFL) << 56);
            --i;
        }
        return n2;
    }
    
    public static void writeRightZeroExtendedValue(final ByteOutput byteOutput, int i, long n) {
        int n2;
        if ((n2 = 64 - Long.numberOfTrailingZeros(n)) == 0) {
            n2 = 1;
        }
        final int n3 = n2 + 7 >> 3;
        n >>= 64 - n3 * 8;
        byteOutput.writeByte(n3 - 1 << 5 | i);
        for (i = n3; i > 0; --i) {
            byteOutput.writeByte((byte)n);
            n >>= 8;
        }
    }
    
    public static void writeSignedIntegralValue(final ByteOutput byteOutput, int i, long n) {
        final int n2 = 65 - Long.numberOfLeadingZeros(n ^ n >> 63) + 7 >> 3;
        byteOutput.writeByte(n2 - 1 << 5 | i);
        for (i = n2; i > 0; --i) {
            byteOutput.writeByte((byte)n);
            n >>= 8;
        }
    }
    
    public static void writeUnsignedIntegralValue(final ByteOutput byteOutput, int i, long n) {
        int n2;
        if ((n2 = 64 - Long.numberOfLeadingZeros(n)) == 0) {
            n2 = 1;
        }
        final int n3 = n2 + 7 >> 3;
        byteOutput.writeByte(n3 - 1 << 5 | i);
        for (i = n3; i > 0; --i) {
            byteOutput.writeByte((byte)n);
            n >>= 8;
        }
    }
}
