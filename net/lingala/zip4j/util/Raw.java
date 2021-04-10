package net.lingala.zip4j.util;

import net.lingala.zip4j.exception.*;
import java.io.*;

public class Raw
{
    public static byte bitArrayToByte(final int[] array) throws ZipException {
        if (array == null) {
            throw new ZipException("bit array is null, cannot calculate byte from bits");
        }
        if (array.length != 8) {
            throw new ZipException("invalid bit array length, cannot calculate byte");
        }
        if (!checkBits(array)) {
            throw new ZipException("invalid bits provided, bits contain other values than 0 or 1");
        }
        int n = 0;
        for (int i = 0; i < array.length; ++i) {
            n += (int)(Math.pow(2.0, i) * array[i]);
        }
        return (byte)n;
    }
    
    private static boolean checkBits(final int[] array) {
        for (int i = 0; i < array.length; ++i) {
            if (array[i] != 0 && array[i] != 1) {
                return false;
            }
        }
        return true;
    }
    
    public static byte[] convertCharArrayToByteArray(final char[] array) {
        if (array == null) {
            throw new NullPointerException();
        }
        final byte[] array2 = new byte[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = (byte)array[i];
        }
        return array2;
    }
    
    public static void prepareBuffAESIVBytes(final byte[] array, final int n, final int n2) {
        array[0] = (byte)n;
        array[1] = (byte)(n >> 8);
        array[2] = (byte)(n >> 16);
        array[3] = (byte)(n >> 24);
        array[5] = (array[4] = 0);
        array[7] = (array[6] = 0);
        array[9] = (array[8] = 0);
        array[11] = (array[10] = 0);
        array[13] = (array[12] = 0);
        array[15] = (array[14] = 0);
    }
    
    public static int readIntLittleEndian(final byte[] array, final int n) {
        return (array[n] & 0xFF) | (array[n + 1] & 0xFF) << 8 | ((array[n + 2] & 0xFF) | (array[n + 3] & 0xFF) << 8) << 16;
    }
    
    public static int readLeInt(final DataInput dataInput, final byte[] array) throws ZipException {
        try {
            dataInput.readFully(array, 0, 4);
            return (array[0] & 0xFF) | (array[1] & 0xFF) << 8 | ((array[2] & 0xFF) | (array[3] & 0xFF) << 8) << 16;
        }
        catch (IOException ex) {
            throw new ZipException(ex);
        }
    }
    
    public static long readLongLittleEndian(final byte[] array, final int n) {
        return (((((((0x0L | (long)(array[n + 7] & 0xFF)) << 8 | (long)(array[n + 6] & 0xFF)) << 8 | (long)(array[n + 5] & 0xFF)) << 8 | (long)(array[n + 4] & 0xFF)) << 8 | (long)(array[n + 3] & 0xFF)) << 8 | (long)(array[n + 2] & 0xFF)) << 8 | (long)(array[n + 1] & 0xFF)) << 8 | (long)(array[n] & 0xFF);
    }
    
    public static final short readShortBigEndian(final byte[] array, final int n) {
        return (short)((array[n + 1] & 0xFF) | (short)((short)((array[n] & 0xFF) | 0x0) << 8));
    }
    
    public static int readShortLittleEndian(final byte[] array, final int n) {
        return (array[n] & 0xFF) | (array[n + 1] & 0xFF) << 8;
    }
    
    public static byte[] toByteArray(final int n) {
        return new byte[] { (byte)n, (byte)(n >> 8), (byte)(n >> 16), (byte)(n >> 24) };
    }
    
    public static byte[] toByteArray(int n, final int n2) {
        final byte[] array = new byte[n2];
        byte[] byteArray;
        for (byteArray = toByteArray(n), n = 0; n < byteArray.length && n < n2; ++n) {
            array[n] = byteArray[n];
        }
        return array;
    }
    
    public static final void writeIntLittleEndian(final byte[] array, final int n, final int n2) {
        array[n + 3] = (byte)(n2 >>> 24);
        array[n + 2] = (byte)(n2 >>> 16);
        array[n + 1] = (byte)(n2 >>> 8);
        array[n] = (byte)(n2 & 0xFF);
    }
    
    public static void writeLongLittleEndian(final byte[] array, final int n, final long n2) {
        array[n + 7] = (byte)(n2 >>> 56);
        array[n + 6] = (byte)(n2 >>> 48);
        array[n + 5] = (byte)(n2 >>> 40);
        array[n + 4] = (byte)(n2 >>> 32);
        array[n + 3] = (byte)(n2 >>> 24);
        array[n + 2] = (byte)(n2 >>> 16);
        array[n + 1] = (byte)(n2 >>> 8);
        array[n] = (byte)(n2 & 0xFFL);
    }
    
    public static final void writeShortLittleEndian(final byte[] array, final int n, final short n2) {
        array[n + 1] = (byte)(n2 >>> 8);
        array[n] = (byte)(n2 & 0xFF);
    }
}
