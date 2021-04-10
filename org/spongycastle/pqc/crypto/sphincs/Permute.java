package org.spongycastle.pqc.crypto.sphincs;

import org.spongycastle.util.*;

class Permute
{
    private static final int CHACHA_ROUNDS = 12;
    
    public static void permute(int i, final int[] array) {
        if (array.length != 16) {
            throw new IllegalArgumentException();
        }
        if (i % 2 == 0) {
            int n = array[0];
            int n2 = array[1];
            int n3 = array[2];
            int n4 = array[3];
            int rotl = array[4];
            int rotl2 = array[5];
            int rotl3 = array[6];
            int rotl4 = array[7];
            int n5 = array[8];
            int n6 = array[9];
            int n7 = array[10];
            int n8 = array[11];
            int rotl5 = array[12];
            int rotl6 = array[13];
            int rotl7 = array[14];
            int rotl8 = array[15];
            while (i > 0) {
                final int n9 = n + rotl;
                final int rotl9 = rotl(rotl5 ^ n9, 16);
                final int n10 = n5 + rotl9;
                final int rotl10 = rotl(rotl ^ n10, 12);
                final int n11 = n9 + rotl10;
                final int rotl11 = rotl(rotl9 ^ n11, 8);
                final int n12 = n10 + rotl11;
                final int rotl12 = rotl(rotl10 ^ n12, 7);
                final int n13 = n2 + rotl2;
                final int rotl13 = rotl(rotl6 ^ n13, 16);
                final int n14 = n6 + rotl13;
                final int rotl14 = rotl(rotl2 ^ n14, 12);
                final int n15 = n13 + rotl14;
                final int rotl15 = rotl(rotl13 ^ n15, 8);
                final int n16 = n14 + rotl15;
                final int rotl16 = rotl(rotl14 ^ n16, 7);
                final int n17 = n3 + rotl3;
                final int rotl17 = rotl(rotl7 ^ n17, 16);
                final int n18 = n7 + rotl17;
                final int rotl18 = rotl(rotl3 ^ n18, 12);
                final int n19 = n17 + rotl18;
                final int rotl19 = rotl(rotl17 ^ n19, 8);
                final int n20 = n18 + rotl19;
                final int rotl20 = rotl(rotl18 ^ n20, 7);
                final int n21 = n4 + rotl4;
                final int rotl21 = rotl(rotl8 ^ n21, 16);
                final int n22 = n8 + rotl21;
                final int rotl22 = rotl(rotl4 ^ n22, 12);
                final int n23 = n21 + rotl22;
                final int rotl23 = rotl(rotl21 ^ n23, 8);
                final int n24 = n22 + rotl23;
                final int rotl24 = rotl(rotl22 ^ n24, 7);
                final int n25 = n11 + rotl16;
                final int rotl25 = rotl(rotl23 ^ n25, 16);
                final int n26 = n20 + rotl25;
                final int rotl26 = rotl(rotl16 ^ n26, 12);
                n = n25 + rotl26;
                rotl8 = rotl(rotl25 ^ n, 8);
                n7 = n26 + rotl8;
                rotl2 = rotl(rotl26 ^ n7, 7);
                final int n27 = n15 + rotl20;
                final int rotl27 = rotl(rotl11 ^ n27, 16);
                final int n28 = n24 + rotl27;
                final int rotl28 = rotl(rotl20 ^ n28, 12);
                n2 = n27 + rotl28;
                rotl5 = rotl(rotl27 ^ n2, 8);
                n8 = n28 + rotl5;
                rotl3 = rotl(rotl28 ^ n8, 7);
                final int n29 = n19 + rotl24;
                final int rotl29 = rotl(rotl15 ^ n29, 16);
                final int n30 = n12 + rotl29;
                final int rotl30 = rotl(rotl24 ^ n30, 12);
                n3 = n29 + rotl30;
                rotl6 = rotl(rotl29 ^ n3, 8);
                n5 = n30 + rotl6;
                rotl4 = rotl(rotl30 ^ n5, 7);
                final int n31 = n23 + rotl12;
                final int rotl31 = rotl(rotl19 ^ n31, 16);
                final int n32 = n16 + rotl31;
                final int rotl32 = rotl(rotl12 ^ n32, 12);
                n4 = n31 + rotl32;
                rotl7 = rotl(rotl31 ^ n4, 8);
                n6 = n32 + rotl7;
                rotl = rotl(rotl32 ^ n6, 7);
                i -= 2;
            }
            array[0] = n;
            array[1] = n2;
            array[2] = n3;
            array[3] = n4;
            array[4] = rotl;
            array[5] = rotl2;
            array[6] = rotl3;
            array[7] = rotl4;
            array[8] = n5;
            array[9] = n6;
            array[10] = n7;
            array[11] = n8;
            array[12] = rotl5;
            array[13] = rotl6;
            array[14] = rotl7;
            array[15] = rotl8;
            return;
        }
        throw new IllegalArgumentException("Number of rounds must be even");
    }
    
    protected static int rotl(final int n, final int n2) {
        return n << n2 | n >>> -n2;
    }
    
    void chacha_permute(final byte[] array, final byte[] array2) {
        final int[] array3 = new int[16];
        final int n = 0;
        for (int i = 0; i < 16; ++i) {
            array3[i] = Pack.littleEndianToInt(array2, i * 4);
        }
        permute(12, array3);
        for (int j = n; j < 16; ++j) {
            Pack.intToLittleEndian(array3[j], array, j * 4);
        }
    }
}
