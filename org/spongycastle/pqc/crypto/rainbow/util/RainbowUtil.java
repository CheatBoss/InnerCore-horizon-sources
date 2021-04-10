package org.spongycastle.pqc.crypto.rainbow.util;

import java.lang.reflect.*;

public class RainbowUtil
{
    public static byte[] convertArray(final short[] array) {
        final byte[] array2 = new byte[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = (byte)array[i];
        }
        return array2;
    }
    
    public static short[] convertArray(final byte[] array) {
        final short[] array2 = new short[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = (short)(array[i] & 0xFF);
        }
        return array2;
    }
    
    public static byte[][] convertArray(final short[][] array) {
        final byte[][] array2 = (byte[][])Array.newInstance(Byte.TYPE, array.length, array[0].length);
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[0].length; ++j) {
                array2[i][j] = (byte)array[i][j];
            }
        }
        return array2;
    }
    
    public static short[][] convertArray(final byte[][] array) {
        final short[][] array2 = (short[][])Array.newInstance(Short.TYPE, array.length, array[0].length);
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[0].length; ++j) {
                array2[i][j] = (short)(array[i][j] & 0xFF);
            }
        }
        return array2;
    }
    
    public static byte[][][] convertArray(final short[][][] array) {
        final byte[][][] array2 = (byte[][][])Array.newInstance(Byte.TYPE, array.length, array[0].length, array[0][0].length);
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[0].length; ++j) {
                for (int k = 0; k < array[0][0].length; ++k) {
                    array2[i][j][k] = (byte)array[i][j][k];
                }
            }
        }
        return array2;
    }
    
    public static short[][][] convertArray(final byte[][][] array) {
        final short[][][] array2 = (short[][][])Array.newInstance(Short.TYPE, array.length, array[0].length, array[0][0].length);
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[0].length; ++j) {
                for (int k = 0; k < array[0][0].length; ++k) {
                    array2[i][j][k] = (short)(array[i][j][k] & 0xFF);
                }
            }
        }
        return array2;
    }
    
    public static int[] convertArraytoInt(final byte[] array) {
        final int[] array2 = new int[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = (array[i] & 0xFF);
        }
        return array2;
    }
    
    public static byte[] convertIntArray(final int[] array) {
        final byte[] array2 = new byte[array.length];
        for (int i = 0; i < array.length; ++i) {
            array2[i] = (byte)array[i];
        }
        return array2;
    }
    
    public static boolean equals(final short[] array, final short[] array2) {
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
    
    public static boolean equals(final short[][] array, final short[][] array2) {
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
    
    public static boolean equals(final short[][][] array, final short[][][] array2) {
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
}
