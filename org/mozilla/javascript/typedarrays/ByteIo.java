package org.mozilla.javascript.typedarrays;

public class ByteIo
{
    private static short doReadInt16(final byte[] array, final int n, final boolean b) {
        if (b) {
            return (short)((array[n] & 0xFF) | (array[n + 1] & 0xFF) << 8);
        }
        return (short)((array[n] & 0xFF) << 8 | (array[n + 1] & 0xFF));
    }
    
    private static void doWriteInt16(final byte[] array, final int n, final int n2, final boolean b) {
        if (b) {
            array[n] = (byte)(n2 & 0xFF);
            array[n + 1] = (byte)(n2 >>> 8 & 0xFF);
            return;
        }
        array[n] = (byte)(n2 >>> 8 & 0xFF);
        array[n + 1] = (byte)(n2 & 0xFF);
    }
    
    public static Object readFloat32(final byte[] array, final int n, final boolean b) {
        return Float.intBitsToFloat((int)readUint32Primitive(array, n, b));
    }
    
    public static Object readFloat64(final byte[] array, final int n, final boolean b) {
        return Double.longBitsToDouble(readUint64Primitive(array, n, b));
    }
    
    public static Object readInt16(final byte[] array, final int n, final boolean b) {
        return doReadInt16(array, n, b);
    }
    
    public static Object readInt32(final byte[] array, final int n, final boolean b) {
        if (b) {
            return (array[n] & 0xFF) | (array[n + 1] & 0xFF) << 8 | (array[n + 2] & 0xFF) << 16 | (array[n + 3] & 0xFF) << 24;
        }
        return (array[n] & 0xFF) << 24 | (array[n + 1] & 0xFF) << 16 | (array[n + 2] & 0xFF) << 8 | (array[n + 3] & 0xFF);
    }
    
    public static Object readInt8(final byte[] array, final int n) {
        return array[n];
    }
    
    public static Object readUint16(final byte[] array, final int n, final boolean b) {
        return doReadInt16(array, n, b) & 0xFFFF;
    }
    
    public static Object readUint32(final byte[] array, final int n, final boolean b) {
        return readUint32Primitive(array, n, b);
    }
    
    public static long readUint32Primitive(final byte[] array, final int n, final boolean b) {
        if (b) {
            return (((long)array[n] & 0xFFL) | ((long)array[n + 1] & 0xFFL) << 8 | ((long)array[n + 2] & 0xFFL) << 16 | ((long)array[n + 3] & 0xFFL) << 24) & 0xFFFFFFFFL;
        }
        return (((long)array[n] & 0xFFL) << 24 | ((long)array[n + 1] & 0xFFL) << 16 | ((long)array[n + 2] & 0xFFL) << 8 | ((long)array[n + 3] & 0xFFL)) & 0xFFFFFFFFL;
    }
    
    public static long readUint64Primitive(final byte[] array, final int n, final boolean b) {
        if (b) {
            return ((long)array[n] & 0xFFL) | ((long)array[n + 1] & 0xFFL) << 8 | ((long)array[n + 2] & 0xFFL) << 16 | ((long)array[n + 3] & 0xFFL) << 24 | ((long)array[n + 4] & 0xFFL) << 32 | ((long)array[n + 5] & 0xFFL) << 40 | ((long)array[n + 6] & 0xFFL) << 48 | ((long)array[n + 7] & 0xFFL) << 56;
        }
        return ((long)array[n] & 0xFFL) << 56 | ((long)array[n + 1] & 0xFFL) << 48 | ((long)array[n + 2] & 0xFFL) << 40 | ((long)array[n + 3] & 0xFFL) << 32 | ((long)array[n + 4] & 0xFFL) << 24 | ((long)array[n + 5] & 0xFFL) << 16 | ((long)array[n + 6] & 0xFFL) << 8 | ((long)array[n + 7] & 0xFFL) << 0;
    }
    
    public static Object readUint8(final byte[] array, final int n) {
        return array[n] & 0xFF;
    }
    
    public static void writeFloat32(final byte[] array, final int n, final double n2, final boolean b) {
        writeUint32(array, n, Float.floatToIntBits((float)n2), b);
    }
    
    public static void writeFloat64(final byte[] array, final int n, final double n2, final boolean b) {
        writeUint64(array, n, Double.doubleToLongBits(n2), b);
    }
    
    public static void writeInt16(final byte[] array, final int n, final int n2, final boolean b) {
        doWriteInt16(array, n, n2, b);
    }
    
    public static void writeInt32(final byte[] array, final int n, final int n2, final boolean b) {
        if (b) {
            array[n] = (byte)(n2 & 0xFF);
            array[n + 1] = (byte)(n2 >>> 8 & 0xFF);
            array[n + 2] = (byte)(n2 >>> 16 & 0xFF);
            array[n + 3] = (byte)(n2 >>> 24 & 0xFF);
            return;
        }
        array[n] = (byte)(n2 >>> 24 & 0xFF);
        array[n + 1] = (byte)(n2 >>> 16 & 0xFF);
        array[n + 2] = (byte)(n2 >>> 8 & 0xFF);
        array[n + 3] = (byte)(n2 & 0xFF);
    }
    
    public static void writeInt8(final byte[] array, final int n, final int n2) {
        array[n] = (byte)n2;
    }
    
    public static void writeUint16(final byte[] array, final int n, final int n2, final boolean b) {
        doWriteInt16(array, n, 0xFFFF & n2, b);
    }
    
    public static void writeUint32(final byte[] array, final int n, final long n2, final boolean b) {
        if (b) {
            array[n] = (byte)(n2 & 0xFFL);
            array[n + 1] = (byte)(n2 >>> 8 & 0xFFL);
            array[n + 2] = (byte)(n2 >>> 16 & 0xFFL);
            array[n + 3] = (byte)(n2 >>> 24 & 0xFFL);
            return;
        }
        array[n] = (byte)(n2 >>> 24 & 0xFFL);
        array[n + 1] = (byte)(n2 >>> 16 & 0xFFL);
        array[n + 2] = (byte)(n2 >>> 8 & 0xFFL);
        array[n + 3] = (byte)(n2 & 0xFFL);
    }
    
    public static void writeUint64(final byte[] array, final int n, final long n2, final boolean b) {
        if (b) {
            array[n] = (byte)(n2 & 0xFFL);
            array[n + 1] = (byte)(n2 >>> 8 & 0xFFL);
            array[n + 2] = (byte)(n2 >>> 16 & 0xFFL);
            array[n + 3] = (byte)(n2 >>> 24 & 0xFFL);
            array[n + 4] = (byte)(n2 >>> 32 & 0xFFL);
            array[n + 5] = (byte)(n2 >>> 40 & 0xFFL);
            array[n + 6] = (byte)(n2 >>> 48 & 0xFFL);
            array[n + 7] = (byte)(n2 >>> 56 & 0xFFL);
            return;
        }
        array[n] = (byte)(n2 >>> 56 & 0xFFL);
        array[n + 1] = (byte)(n2 >>> 48 & 0xFFL);
        array[n + 2] = (byte)(n2 >>> 40 & 0xFFL);
        array[n + 3] = (byte)(n2 >>> 32 & 0xFFL);
        array[n + 4] = (byte)(n2 >>> 24 & 0xFFL);
        array[n + 5] = (byte)(n2 >>> 16 & 0xFFL);
        array[n + 6] = (byte)(n2 >>> 8 & 0xFFL);
        array[n + 7] = (byte)(n2 & 0xFFL);
    }
    
    public static void writeUint8(final byte[] array, final int n, final int n2) {
        array[n] = (byte)(n2 & 0xFF);
    }
}
