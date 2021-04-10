package com.android.dex;

import java.io.*;
import com.android.dex.util.*;

public final class Mutf8
{
    private Mutf8() {
    }
    
    private static long countBytes(final String s, final boolean b) throws UTFDataFormatException {
        long n = 0L;
        for (int length = s.length(), i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 != '\0' && char1 <= '\u007f') {
                ++n;
            }
            else if (char1 <= '\u07ff') {
                n += 2L;
            }
            else {
                n += 3L;
            }
            if (b && n > 65535L) {
                throw new UTFDataFormatException("String more than 65535 UTF bytes long");
            }
        }
        return n;
    }
    
    public static String decode(final ByteInput byteInput, final char[] array) throws UTFDataFormatException {
        int n = 0;
        while (true) {
            final char c = (char)(byteInput.readByte() & 0xFF);
            if (c == '\0') {
                return new String(array, 0, n);
            }
            if ((array[n] = c) < '\u0080') {
                ++n;
            }
            else if ((c & '\u00e0') == 0xC0) {
                final int n2 = byteInput.readByte() & 0xFF;
                if ((n2 & 0xC0) != 0x80) {
                    throw new UTFDataFormatException("bad second byte");
                }
                final int n3 = n + 1;
                array[n] = (char)((c & '\u001f') << 6 | (n2 & 0x3F));
                n = n3;
            }
            else {
                if ((c & '\u00f0') != 0xE0) {
                    throw new UTFDataFormatException("bad byte");
                }
                final int n4 = byteInput.readByte() & 0xFF;
                final int n5 = byteInput.readByte() & 0xFF;
                if ((n4 & 0xC0) != 0x80 || (n5 & 0xC0) != 0x80) {
                    throw new UTFDataFormatException("bad second or third byte");
                }
                final int n6 = n + 1;
                array[n] = (char)((c & '\u000f') << 12 | (n4 & 0x3F) << 6 | (n5 & 0x3F));
                n = n6;
            }
        }
    }
    
    public static void encode(final byte[] array, int n, final String s) {
        for (int length = s.length(), i = 0; i < length; ++i) {
            final char char1 = s.charAt(i);
            if (char1 != '\0' && char1 <= '\u007f') {
                final int n2 = n + 1;
                array[n] = (byte)char1;
                n = n2;
            }
            else if (char1 <= '\u07ff') {
                final int n3 = n + 1;
                array[n] = (byte)((char1 >> 6 & 0x1F) | 0xC0);
                n = n3 + 1;
                array[n3] = (byte)((char1 & '?') | 0x80);
            }
            else {
                final int n4 = n + 1;
                array[n] = (byte)((char1 >> 12 & 0xF) | 0xE0);
                final int n5 = n4 + 1;
                array[n4] = (byte)((char1 >> 6 & 0x3F) | 0x80);
                n = n5 + 1;
                array[n5] = (byte)((char1 & '?') | 0x80);
            }
        }
    }
    
    public static byte[] encode(final String s) throws UTFDataFormatException {
        final byte[] array = new byte[(int)countBytes(s, true)];
        encode(array, 0, s);
        return array;
    }
}
