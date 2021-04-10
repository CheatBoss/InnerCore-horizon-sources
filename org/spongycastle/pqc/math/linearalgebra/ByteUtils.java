package org.spongycastle.pqc.math.linearalgebra;

public final class ByteUtils
{
    private static final char[] HEX_CHARS;
    
    static {
        HEX_CHARS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    }
    
    private ByteUtils() {
    }
    
    public static byte[] clone(final byte[] array) {
        if (array == null) {
            return null;
        }
        final byte[] array2 = new byte[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }
    
    public static byte[] concatenate(final byte[] array, final byte[] array2) {
        final byte[] array3 = new byte[array.length + array2.length];
        System.arraycopy(array, 0, array3, 0, array.length);
        System.arraycopy(array2, 0, array3, array.length, array2.length);
        return array3;
    }
    
    public static byte[] concatenate(final byte[][] array) {
        final int length = array[0].length;
        final byte[] array2 = new byte[array.length * length];
        int i = 0;
        int n = 0;
        while (i < array.length) {
            System.arraycopy(array[i], 0, array2, n, length);
            n += length;
            ++i;
        }
        return array2;
    }
    
    public static int deepHashCode(final byte[] array) {
        int n = 1;
        for (int i = 0; i < array.length; ++i) {
            n = n * 31 + array[i];
        }
        return n;
    }
    
    public static int deepHashCode(final byte[][] array) {
        int n = 1;
        for (int i = 0; i < array.length; ++i) {
            n = n * 31 + deepHashCode(array[i]);
        }
        return n;
    }
    
    public static int deepHashCode(final byte[][][] array) {
        int n = 1;
        for (int i = 0; i < array.length; ++i) {
            n = n * 31 + deepHashCode(array[i]);
        }
        return n;
    }
    
    public static boolean equals(final byte[] array, final byte[] array2) {
        if (array == null) {
            return array2 == null;
        }
        if (array2 == null) {
            return false;
        }
        if (array.length != array2.length) {
            return false;
        }
        int i = array.length - 1;
        boolean b = true;
        while (i >= 0) {
            b &= (array[i] == array2[i]);
            --i;
        }
        return b;
    }
    
    public static boolean equals(final byte[][] array, final byte[][] array2) {
        if (array.length != array2.length) {
            return false;
        }
        final int length = array.length;
        boolean b = true;
        for (int i = length - 1; i >= 0; --i) {
            b &= equals(array[i], array2[i]);
        }
        return b;
    }
    
    public static boolean equals(final byte[][][] array, final byte[][][] array2) {
        if (array.length != array2.length) {
            return false;
        }
        int i = array.length - 1;
        boolean b = true;
        while (i >= 0) {
            if (array[i].length != array2[i].length) {
                return false;
            }
            for (int j = array[i].length - 1; j >= 0; --j) {
                b &= equals(array[i][j], array2[i][j]);
            }
            --i;
        }
        return b;
    }
    
    public static byte[] fromHexString(final String s) {
        final char[] charArray = s.toUpperCase().toCharArray();
        final int n = 0;
        int i = 0;
        int n2 = 0;
        while (i < charArray.length) {
            int n3 = 0;
            Label_0061: {
                if (charArray[i] < '0' || charArray[i] > '9') {
                    n3 = n2;
                    if (charArray[i] < 'A') {
                        break Label_0061;
                    }
                    n3 = n2;
                    if (charArray[i] > 'F') {
                        break Label_0061;
                    }
                }
                n3 = n2 + 1;
            }
            ++i;
            n2 = n3;
        }
        final byte[] array = new byte[n2 + 1 >> 1];
        int n4 = n2 & 0x1;
        int n6;
        for (int j = n; j < charArray.length; ++j, n4 = n6) {
            if (charArray[j] >= '0' && charArray[j] <= '9') {
                final int n5 = n4 >> 1;
                array[n5] <<= 4;
                array[n5] |= (byte)(charArray[j] - '0');
            }
            else {
                n6 = n4;
                if (charArray[j] < 'A') {
                    continue;
                }
                n6 = n4;
                if (charArray[j] > 'F') {
                    continue;
                }
                final int n7 = n4 >> 1;
                array[n7] <<= 4;
                array[n7] |= (byte)(charArray[j] - 'A' + 10);
            }
            n6 = n4 + 1;
        }
        return array;
    }
    
    public static byte[][] split(final byte[] array, final int n) throws ArrayIndexOutOfBoundsException {
        if (n <= array.length) {
            final byte[][] array2 = { new byte[n], new byte[array.length - n] };
            System.arraycopy(array, 0, array2[0], 0, n);
            System.arraycopy(array, n, array2[1], 0, array.length - n);
            return array2;
        }
        throw new ArrayIndexOutOfBoundsException();
    }
    
    public static byte[] subArray(final byte[] array, final int n) {
        return subArray(array, n, array.length);
    }
    
    public static byte[] subArray(final byte[] array, final int n, int n2) {
        n2 -= n;
        final byte[] array2 = new byte[n2];
        System.arraycopy(array, n, array2, 0, n2);
        return array2;
    }
    
    public static String toBinaryString(final byte[] array) {
        String s = "";
        for (int i = 0; i < array.length; ++i) {
            final byte b = array[i];
            for (int j = 0; j < 8; ++j) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(b >>> j & 0x1);
                s = sb.toString();
            }
            if (i != array.length - 1) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append(" ");
                s = sb2.toString();
            }
        }
        return s;
    }
    
    public static char[] toCharArray(final byte[] array) {
        final char[] array2 = new char[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = (char)array[i];
        }
        return array2;
    }
    
    public static String toHexString(final byte[] array) {
        String string = "";
        for (int i = 0; i < array.length; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(ByteUtils.HEX_CHARS[array[i] >>> 4 & 0xF]);
            final String string2 = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append(ByteUtils.HEX_CHARS[array[i] & 0xF]);
            string = sb2.toString();
        }
        return string;
    }
    
    public static String toHexString(final byte[] array, String s, final String s2) {
        s = new String(s);
        for (int i = 0; i < array.length; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(ByteUtils.HEX_CHARS[array[i] >>> 4 & 0xF]);
            s = sb.toString();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(ByteUtils.HEX_CHARS[array[i] & 0xF]);
            final String s3 = s = sb2.toString();
            if (i < array.length - 1) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(s3);
                sb3.append(s2);
                s = sb3.toString();
            }
        }
        return s;
    }
    
    public static byte[] xor(final byte[] array, final byte[] array2) {
        final byte[] array3 = new byte[array.length];
        int length = array.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            array3[length] = (byte)(array[length] ^ array2[length]);
        }
        return array3;
    }
}
