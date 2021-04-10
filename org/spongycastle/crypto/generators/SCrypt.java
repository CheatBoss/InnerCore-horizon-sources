package org.spongycastle.crypto.generators;

import org.spongycastle.crypto.engines.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class SCrypt
{
    private static void BlockMix(final int[] array, final int[] array2, final int[] array3, final int[] array4, int i) {
        System.arraycopy(array, array.length - 16, array2, 0, 16);
        final int length = array.length;
        i *= 2;
        int n = 0;
        int n2 = 0;
        while (i > 0) {
            Xor(array2, array, n, array3);
            Salsa20Engine.salsaCore(8, array3, array2);
            System.arraycopy(array2, 0, array4, n2, 16);
            n2 = (length >>> 1) + n - n2;
            n += 16;
            --i;
        }
        System.arraycopy(array4, 0, array, 0, array4.length);
    }
    
    private static void Clear(final byte[] array) {
        if (array != null) {
            Arrays.fill(array, (byte)0);
        }
    }
    
    private static void Clear(final int[] array) {
        if (array != null) {
            Arrays.fill(array, 0);
        }
    }
    
    private static void ClearAll(final int[][] array) {
        for (int i = 0; i < array.length; ++i) {
            Clear(array[i]);
        }
    }
    
    private static byte[] MFcrypt(byte[] singleIterationPBKDF2, byte[] array, final int n, final int n2, int i, final int n3) {
        final int n4 = n2 * 128;
        final byte[] singleIterationPBKDF3 = SingleIterationPBKDF2(singleIterationPBKDF2, array, i * n4);
        int[] array2;
        try {
            final int n5 = singleIterationPBKDF3.length >>> 2;
            array = (byte[])new int[n5];
            try {
                Pack.littleEndianToInt(singleIterationPBKDF3, 0, (int[])array);
                for (i = 0; i < n5; i += n4 >>> 2) {
                    SMix((int[])array, i, n, n2);
                }
                Pack.intToLittleEndian((int[])array, singleIterationPBKDF3, 0);
                singleIterationPBKDF2 = SingleIterationPBKDF2(singleIterationPBKDF2, singleIterationPBKDF3, n3);
                Clear(singleIterationPBKDF3);
                Clear((int[])array);
                return singleIterationPBKDF2;
            }
            finally {}
        }
        finally {
            array2 = null;
        }
        Clear(singleIterationPBKDF3);
        Clear(array2);
    }
    
    private static void SMix(final int[] array, final int n, final int n2, final int n3) {
        while (true) {
            final int n4 = n3 * 32;
            final int[] array2 = new int[16];
            final int[] array3 = new int[16];
            final int[] array4 = new int[n4];
            final int[] array5 = new int[n4];
            final int[][] array6 = new int[n2][];
            while (true) {
                Label_0214: {
                    try {
                        System.arraycopy(array, n, array5, 0, n4);
                        for (int i = 0; i < n2; ++i) {
                            array6[i] = Arrays.clone(array5);
                            BlockMix(array5, array2, array3, array4, n3);
                        }
                        break Label_0214;
                        // iftrue(Label_0136:, n5 >= n2)
                        Xor(array5, array6[array5[n4 - 16] & n2 - 1], 0, array5);
                        BlockMix(array5, array2, array3, array4, n3);
                        final int n5 = n5 + 1;
                        continue;
                        Label_0136: {
                            final Throwable t;
                            System.arraycopy(array5, 0, t, n, n4);
                        }
                        return;
                    }
                    finally {
                        ClearAll(array6);
                        ClearAll(new int[][] { array5, array2, array3, array4 });
                    }
                }
                final int n5 = 0;
                continue;
            }
        }
    }
    
    private static byte[] SingleIterationPBKDF2(final byte[] array, final byte[] array2, final int n) {
        final PKCS5S2ParametersGenerator pkcs5S2ParametersGenerator = new PKCS5S2ParametersGenerator(new SHA256Digest());
        pkcs5S2ParametersGenerator.init(array, array2, 1);
        return ((KeyParameter)pkcs5S2ParametersGenerator.generateDerivedMacParameters(n * 8)).getKey();
    }
    
    private static void Xor(final int[] array, final int[] array2, final int n, final int[] array3) {
        int length = array3.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            array3[length] = (array[length] ^ array2[n + length]);
        }
    }
    
    public static byte[] generate(final byte[] array, final byte[] array2, final int n, final int n2, final int n3, final int n4) {
        if (array == null) {
            throw new IllegalArgumentException("Passphrase P must be provided.");
        }
        if (array2 == null) {
            throw new IllegalArgumentException("Salt S must be provided.");
        }
        if (n <= 1) {
            throw new IllegalArgumentException("Cost parameter N must be > 1.");
        }
        if (n2 == 1 && n > 65536) {
            throw new IllegalArgumentException("Cost parameter N must be > 1 and < 65536.");
        }
        if (n2 < 1) {
            throw new IllegalArgumentException("Block size r must be >= 1.");
        }
        final int n5 = Integer.MAX_VALUE / (n2 * 128 * 8);
        if (n3 < 1 || n3 > n5) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Parallelisation parameter p must be >= 1 and <= ");
            sb.append(n5);
            sb.append(" (based on block size r of ");
            sb.append(n2);
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
        if (n4 >= 1) {
            return MFcrypt(array, array2, n, n2, n3, n4);
        }
        throw new IllegalArgumentException("Generated key length dkLen must be >= 1.");
    }
}
