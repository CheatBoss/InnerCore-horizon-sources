package org.spongycastle.pqc.crypto.gmss.util;

import java.io.*;

public class GMSSUtil
{
    public int bytesToIntLittleEndian(final byte[] array) {
        return (array[3] & 0xFF) << 24 | ((array[0] & 0xFF) | (array[1] & 0xFF) << 8 | (array[2] & 0xFF) << 16);
    }
    
    public int bytesToIntLittleEndian(final byte[] array, int n) {
        final int n2 = n + 1;
        n = array[n];
        final int n3 = n2 + 1;
        return (array[n3 + 1] & 0xFF) << 24 | ((n & 0xFF) | (array[n2] & 0xFF) << 8 | (array[n3] & 0xFF) << 16);
    }
    
    public byte[] concatenateArray(final byte[][] array) {
        final byte[] array2 = new byte[array.length * array[0].length];
        int i = 0;
        int n = 0;
        while (i < array.length) {
            System.arraycopy(array[i], 0, array2, n, array[i].length);
            n += array[i].length;
            ++i;
        }
        return array2;
    }
    
    public int getLog(final int n) {
        int n2 = 1;
        for (int i = 2; i < n; i <<= 1, ++n2) {}
        return n2;
    }
    
    public byte[] intToBytesLittleEndian(final int n) {
        return new byte[] { (byte)(n & 0xFF), (byte)(n >> 8 & 0xFF), (byte)(n >> 16 & 0xFF), (byte)(n >> 24 & 0xFF) };
    }
    
    public void printArray(final String s, final byte[] array) {
        System.out.println(s);
        int i = 0;
        int n = 0;
        while (i < array.length) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append(n);
            sb.append("; ");
            sb.append(array[i]);
            out.println(sb.toString());
            ++n;
            ++i;
        }
    }
    
    public void printArray(final String s, final byte[][] array) {
        System.out.println(s);
        int i = 0;
        int n = 0;
        while (i < array.length) {
            for (int j = 0; j < array[0].length; ++j) {
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append(n);
                sb.append("; ");
                sb.append(array[i][j]);
                out.println(sb.toString());
                ++n;
            }
            ++i;
        }
    }
    
    public boolean testPowerOfTwo(final int n) {
        int i;
        for (i = 1; i < n; i <<= 1) {}
        return n == i;
    }
}
