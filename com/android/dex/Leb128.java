package com.android.dex;

import com.android.dex.util.*;

public final class Leb128
{
    private Leb128() {
    }
    
    public static int readSignedLeb128(final ByteInput byteInput) {
        int n = 0;
        int n2 = 0;
        int n3 = -1;
        int i;
        int n4;
        int n5;
        int n6;
        do {
            n4 = (byteInput.readByte() & 0xFF);
            n5 = (n | (n4 & 0x7F) << n2 * 7);
            n6 = n3 << 7;
            i = n2 + 1;
            if ((n4 & 0x80) != 0x80) {
                break;
            }
            n = n5;
            n2 = i;
            n3 = n6;
        } while (i < 5);
        if ((n4 & 0x80) == 0x80) {
            throw new DexException("invalid LEB128 sequence");
        }
        int n7 = n5;
        if ((n6 >> 1 & n5) != 0x0) {
            n7 = (n5 | n6);
        }
        return n7;
    }
    
    public static int readUnsignedLeb128(final ByteInput byteInput) {
        int n = 0;
        int n2 = 0;
        int n3;
        int n4;
        int n5;
        do {
            n4 = (byteInput.readByte() & 0xFF);
            n5 = (n | (n4 & 0x7F) << n2 * 7);
            n3 = n2 + 1;
            if ((n4 & 0x80) != 0x80) {
                break;
            }
            n = n5;
        } while ((n2 = n3) < 5);
        if ((n4 & 0x80) == 0x80) {
            throw new DexException("invalid LEB128 sequence");
        }
        return n5;
    }
    
    public static int signedLeb128Size(int n) {
        int n2 = n >> 7;
        int n3 = 0;
        int i = 1;
        int n4;
        int n5;
        if ((Integer.MIN_VALUE & n) == 0x0) {
            n4 = 0;
            n5 = n;
        }
        else {
            n4 = -1;
            n5 = n;
        }
        while (i != 0) {
            final int n6 = n = 1;
            if (n2 == n4) {
                if ((n2 & 0x1) != (n5 >> 6 & 0x1)) {
                    n = n6;
                }
                else {
                    n = 0;
                }
            }
            n5 = n2;
            n2 >>= 7;
            ++n3;
            i = n;
        }
        return n3;
    }
    
    public static int unsignedLeb128Size(int n) {
        int i;
        for (i = n >> 7, n = 0; i != 0; i >>= 7, ++n) {}
        return n + 1;
    }
    
    public static void writeSignedLeb128(final ByteOutput byteOutput, int n) {
        int n2 = n >> 7;
        int i = 1;
        int n3;
        int n4;
        if ((Integer.MIN_VALUE & n) == 0x0) {
            n3 = 0;
            n4 = n;
        }
        else {
            n3 = -1;
            n4 = n;
        }
        while (i != 0) {
            final int n5 = n = 1;
            if (n2 == n3) {
                if ((n2 & 0x1) != (n4 >> 6 & 0x1)) {
                    n = n5;
                }
                else {
                    n = 0;
                }
            }
            int n6;
            if (n != 0) {
                n6 = 128;
            }
            else {
                n6 = 0;
            }
            byteOutput.writeByte((byte)((n4 & 0x7F) | n6));
            n4 = n2;
            n2 >>= 7;
            i = n;
        }
    }
    
    public static void writeUnsignedLeb128(final ByteOutput byteOutput, int i) {
        final int n = i >>> 7;
        int n2 = i;
        for (i = n; i != 0; i >>>= 7) {
            byteOutput.writeByte((byte)((n2 & 0x7F) | 0x80));
            n2 = i;
        }
        byteOutput.writeByte((byte)(n2 & 0x7F));
    }
}
