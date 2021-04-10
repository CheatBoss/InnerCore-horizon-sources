package com.googleplay.util;

public class Base64
{
    private static final byte[] ALPHABET;
    private static final byte[] DECODABET;
    public static final boolean DECODE = false;
    public static final boolean ENCODE = true;
    private static final byte EQUALS_SIGN = 61;
    private static final byte EQUALS_SIGN_ENC = -1;
    private static final byte NEW_LINE = 10;
    private static final byte[] WEBSAFE_ALPHABET;
    private static final byte[] WEBSAFE_DECODABET;
    private static final byte WHITE_SPACE_ENC = -5;
    
    static {
        ALPHABET = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47 };
        WEBSAFE_ALPHABET = new byte[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95 };
        DECODABET = new byte[] { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9 };
        WEBSAFE_DECODABET = new byte[] { -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9, -9 };
    }
    
    private Base64() {
    }
    
    public static byte[] decode(final String s) throws Base64DecoderException {
        final byte[] bytes = s.getBytes();
        return decode(bytes, 0, bytes.length);
    }
    
    public static byte[] decode(final byte[] array) throws Base64DecoderException {
        return decode(array, 0, array.length);
    }
    
    public static byte[] decode(final byte[] array, final int n, final int n2) throws Base64DecoderException {
        return decode(array, n, n2, Base64.DECODABET);
    }
    
    public static byte[] decode(byte[] array, int n, final int n2, final byte[] array2) throws Base64DecoderException {
        final byte[] array3 = new byte[n2 * 3 / 4 + 2];
        final byte[] array4 = new byte[4];
        int i = 0;
        int n3 = 0;
        int n4 = 0;
        while (i < n2) {
            final int n5 = i + n;
            final byte b = (byte)(array[n5] & 0x7F);
            final byte b2 = array2[b];
            if (b2 < -5) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Bad Base64 input character at ");
                sb.append(i);
                sb.append(": ");
                sb.append(array[n5]);
                sb.append("(decimal)");
                throw new Base64DecoderException(sb.toString());
            }
            if (b2 >= -1) {
                if (b == 61) {
                    final int n6 = n2 - i;
                    n = (byte)(array[n2 - 1 + n] & 0x7F);
                    if (n3 == 0 || n3 == 1) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("invalid padding byte '=' at byte offset ");
                        sb2.append(i);
                        throw new Base64DecoderException(sb2.toString());
                    }
                    if ((n3 == 3 && n6 > 2) || (n3 == 4 && n6 > 1)) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("padding byte '=' falsely signals end of encoded value at offset ");
                        sb3.append(i);
                        throw new Base64DecoderException(sb3.toString());
                    }
                    if (n == 61) {
                        break;
                    }
                    if (n == 10) {
                        break;
                    }
                    throw new Base64DecoderException("encoded value has invalid trailing byte");
                }
                else {
                    final int n7 = n3 + 1;
                    array4[n3] = b;
                    if (n7 == 4) {
                        n4 += decode4to3(array4, 0, array3, n4, array2);
                        n3 = 0;
                    }
                    else {
                        n3 = n7;
                    }
                }
            }
            ++i;
        }
        n = n4;
        if (n3 != 0) {
            if (n3 == 1) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("single trailing character at offset ");
                sb4.append(n2 - 1);
                throw new Base64DecoderException(sb4.toString());
            }
            array4[n3] = 61;
            n = n4 + decode4to3(array4, 0, array3, n4, array2);
        }
        array = new byte[n];
        System.arraycopy(array3, 0, array, 0, n);
        return array;
    }
    
    private static int decode4to3(final byte[] array, int n, final byte[] array2, final int n2, final byte[] array3) {
        final int n3 = n + 2;
        if (array[n3] == 61) {
            array2[n2] = (byte)((array3[array[n + 1]] << 24 >>> 12 | array3[array[n]] << 24 >>> 6) >>> 16);
            return 1;
        }
        final int n4 = n + 3;
        if (array[n4] == 61) {
            final byte b = array3[array[n]];
            n = array3[array[n + 1]];
            n = (array3[array[n3]] << 24 >>> 18 | (n << 24 >>> 12 | b << 24 >>> 6));
            array2[n2] = (byte)(n >>> 16);
            array2[n2 + 1] = (byte)(n >>> 8);
            return 2;
        }
        final byte b2 = array3[array[n]];
        n = array3[array[n + 1]];
        n = (array3[array[n4]] << 24 >>> 24 | (n << 24 >>> 12 | b2 << 24 >>> 6 | array3[array[n3]] << 24 >>> 18));
        array2[n2] = (byte)(n >> 16);
        array2[n2 + 1] = (byte)(n >> 8);
        array2[n2 + 2] = (byte)n;
        return 3;
    }
    
    public static byte[] decodeWebSafe(final String s) throws Base64DecoderException {
        final byte[] bytes = s.getBytes();
        return decodeWebSafe(bytes, 0, bytes.length);
    }
    
    public static byte[] decodeWebSafe(final byte[] array) throws Base64DecoderException {
        return decodeWebSafe(array, 0, array.length);
    }
    
    public static byte[] decodeWebSafe(final byte[] array, final int n, final int n2) throws Base64DecoderException {
        return decode(array, n, n2, Base64.WEBSAFE_DECODABET);
    }
    
    public static String encode(final byte[] array) {
        return encode(array, 0, array.length, Base64.ALPHABET, true);
    }
    
    public static String encode(byte[] encode, int length, int n, final byte[] array, final boolean b) {
        encode = encode(encode, length, n, array, Integer.MAX_VALUE);
        for (length = encode.length; !b && length > 0; length = n) {
            n = length - 1;
            if (encode[n] != 61) {
                break;
            }
        }
        return new String(encode, 0, length);
    }
    
    public static byte[] encode(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) {
        final int n4 = (n2 + 2) / 3 * 4;
        final byte[] array3 = new byte[n4 + n4 / n3];
        int i = 0;
        int n5 = 0;
        int n6 = 0;
        while (i < n2 - 2) {
            final int n7 = array[i + n] << 24 >>> 8 | array[i + 1 + n] << 24 >>> 16 | array[i + 2 + n] << 24 >>> 24;
            array3[n5] = array2[n7 >>> 18];
            final int n8 = n5 + 1;
            array3[n8] = array2[n7 >>> 12 & 0x3F];
            array3[n5 + 2] = array2[n7 >>> 6 & 0x3F];
            array3[n5 + 3] = array2[n7 & 0x3F];
            n6 += 4;
            if (n6 == n3) {
                array3[n5 + 4] = 10;
                n6 = 0;
                n5 = n8;
            }
            i += 3;
            n5 += 4;
        }
        if (i < n2) {
            encode3to4(array, n + i, n2 - i, array3, n5, array2);
            if (n6 + 4 == n3) {
                array3[n5 + 4] = 10;
            }
        }
        return array3;
    }
    
    private static byte[] encode3to4(final byte[] array, int n, final int n2, final byte[] array2, final int n3, final byte[] array3) {
        int n4 = 0;
        int n5;
        if (n2 > 0) {
            n5 = array[n] << 24 >>> 8;
        }
        else {
            n5 = 0;
        }
        int n6;
        if (n2 > 1) {
            n6 = array[n + 1] << 24 >>> 16;
        }
        else {
            n6 = 0;
        }
        if (n2 > 2) {
            n4 = array[n + 2] << 24 >>> 24;
        }
        n = (n5 | n6 | n4);
        if (n2 == 1) {
            array2[n3] = array3[n >>> 18];
            array2[n3 + 1] = array3[n >>> 12 & 0x3F];
            array2[n3 + 3] = (array2[n3 + 2] = 61);
            return array2;
        }
        if (n2 == 2) {
            array2[n3] = array3[n >>> 18];
            array2[n3 + 1] = array3[n >>> 12 & 0x3F];
            array2[n3 + 2] = array3[n >>> 6 & 0x3F];
            array2[n3 + 3] = 61;
            return array2;
        }
        if (n2 != 3) {
            return array2;
        }
        array2[n3] = array3[n >>> 18];
        array2[n3 + 1] = array3[n >>> 12 & 0x3F];
        array2[n3 + 2] = array3[n >>> 6 & 0x3F];
        array2[n3 + 3] = array3[n & 0x3F];
        return array2;
    }
    
    public static String encodeWebSafe(final byte[] array, final boolean b) {
        return encode(array, 0, array.length, Base64.WEBSAFE_ALPHABET, b);
    }
}
