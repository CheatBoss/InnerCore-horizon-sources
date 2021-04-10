package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class SkipjackEngine implements BlockCipher
{
    static final int BLOCK_SIZE = 8;
    static short[] ftable;
    private boolean encrypting;
    private int[] key0;
    private int[] key1;
    private int[] key2;
    private int[] key3;
    
    static {
        SkipjackEngine.ftable = new short[] { 163, 215, 9, 131, 248, 72, 246, 244, 179, 33, 21, 120, 153, 177, 175, 249, 231, 45, 77, 138, 206, 76, 202, 46, 82, 149, 217, 30, 78, 56, 68, 40, 10, 223, 2, 160, 23, 241, 96, 104, 18, 183, 122, 195, 233, 250, 61, 83, 150, 132, 107, 186, 242, 99, 154, 25, 124, 174, 229, 245, 247, 22, 106, 162, 57, 182, 123, 15, 193, 147, 129, 27, 238, 180, 26, 234, 208, 145, 47, 184, 85, 185, 218, 133, 63, 65, 191, 224, 90, 88, 128, 95, 102, 11, 216, 144, 53, 213, 192, 167, 51, 6, 101, 105, 69, 0, 148, 86, 109, 152, 155, 118, 151, 252, 178, 194, 176, 254, 219, 32, 225, 235, 214, 228, 221, 71, 74, 29, 66, 237, 158, 110, 73, 60, 205, 67, 39, 210, 7, 212, 222, 199, 103, 24, 137, 203, 48, 31, 141, 198, 143, 170, 200, 116, 220, 201, 93, 92, 49, 164, 112, 136, 97, 44, 159, 13, 43, 135, 80, 130, 84, 100, 38, 125, 3, 64, 52, 75, 28, 115, 209, 196, 253, 59, 204, 251, 127, 171, 230, 62, 91, 165, 173, 4, 35, 156, 20, 81, 34, 240, 41, 121, 113, 126, 255, 140, 14, 226, 12, 239, 188, 114, 117, 111, 55, 161, 236, 211, 142, 98, 139, 134, 16, 232, 8, 119, 17, 190, 146, 79, 36, 197, 50, 54, 157, 207, 243, 166, 187, 172, 94, 108, 169, 19, 87, 37, 181, 227, 189, 168, 58, 1, 5, 89, 42, 70 };
    }
    
    private int g(final int n, int n2) {
        final int n3 = n2 & 0xFF;
        final short[] ftable = SkipjackEngine.ftable;
        n2 = ((n2 >> 8 & 0xFF) ^ ftable[this.key0[n] ^ n3]);
        final int n4 = n3 ^ ftable[this.key1[n] ^ n2];
        n2 ^= ftable[this.key2[n] ^ n4];
        return (n2 << 8) + (ftable[this.key3[n] ^ n2] ^ n4);
    }
    
    private int h(final int n, int n2) {
        final int n3 = n2 >> 8 & 0xFF;
        final short[] ftable = SkipjackEngine.ftable;
        n2 = ((n2 & 0xFF) ^ ftable[this.key3[n] ^ n3]);
        final int n4 = n3 ^ ftable[this.key2[n] ^ n2];
        n2 ^= ftable[this.key1[n] ^ n4];
        return ((ftable[this.key0[n] ^ n2] ^ n4) << 8) + n2;
    }
    
    public int decryptBlock(final byte[] array, int n, final byte[] array2, final int n2) {
        final int n3 = (array[n + 0] << 8) + (array[n + 1] & 0xFF);
        int n4 = (array[n + 2] << 8) + (array[n + 3] & 0xFF);
        int n5 = (array[n + 4] << 8) + (array[n + 5] & 0xFF);
        int n6 = (array[n + 6] << 8) + (array[n + 7] & 0xFF);
        int i = 0;
        int n7 = 31;
        n = n3;
        while (i < 2) {
            final int n8 = n;
            final int n9 = 0;
            n = n6;
            int n10 = n7;
            int n11 = n5;
            int h = n8;
            int h2;
            int n12;
            int n13;
            for (int j = n9; j < 8; ++j, n12 = n10 - 1, n13 = h2, n4 = (n11 ^ h2 ^ n10 + 1), n11 = n, n = h, h = n13, n10 = n12) {
                h2 = this.h(n10, n4);
            }
            int n14 = 0;
            int n15 = n;
            n = n11;
            int n16 = n4;
            int n17;
            int n18;
            int n19;
            while (true) {
                n17 = n15;
                n18 = n10;
                n19 = h;
                if (n14 >= 8) {
                    break;
                }
                h = this.h(n18, n16);
                ++n14;
                final int n20 = n18 - 1;
                n15 = (n16 ^ n19 ^ n18 + 1);
                n16 = n;
                n = n17;
                n10 = n20;
            }
            final int n21 = i + 1;
            final int n22 = n19;
            n5 = n;
            n6 = n17;
            n = n22;
            n4 = n16;
            n7 = n18;
            i = n21;
        }
        array2[n2 + 0] = (byte)(n >> 8);
        array2[n2 + 1] = (byte)n;
        array2[n2 + 2] = (byte)(n4 >> 8);
        array2[n2 + 3] = (byte)n4;
        array2[n2 + 4] = (byte)(n5 >> 8);
        array2[n2 + 5] = (byte)n5;
        array2[n2 + 6] = (byte)(n6 >> 8);
        array2[n2 + 7] = (byte)n6;
        return 8;
    }
    
    public int encryptBlock(final byte[] array, int n, final byte[] array2, final int n2) {
        int n3 = (array[n + 0] << 8) + (array[n + 1] & 0xFF);
        int n4 = (array[n + 2] << 8) + (array[n + 3] & 0xFF);
        int n5 = (array[n + 4] << 8) + (array[n + 5] & 0xFF);
        n = (array[n + 6] << 8) + (array[n + 7] & 0xFF);
        int i = 0;
        int n6 = 0;
        while (i < 2) {
            int g;
            int n7;
            for (int j = 0; j < 8; ++j, n7 = g, n3 = (n ^ g ^ n6), n = n5, n5 = n4, n4 = n7) {
                g = this.g(n6, n3);
                ++n6;
            }
            int n8;
            int g2;
            int n9;
            for (int k = 0; k < 8; ++k, n6 = n8, n9 = (n3 ^ n4 ^ n8), n3 = n, n = n5, n5 = n9, n4 = g2) {
                n8 = n6 + 1;
                g2 = this.g(n6, n3);
            }
            ++i;
        }
        array2[n2 + 0] = (byte)(n3 >> 8);
        array2[n2 + 1] = (byte)n3;
        array2[n2 + 2] = (byte)(n4 >> 8);
        array2[n2 + 3] = (byte)n4;
        array2[n2 + 4] = (byte)(n5 >> 8);
        array2[n2 + 5] = (byte)n5;
        array2[n2 + 6] = (byte)(n >> 8);
        array2[n2 + 7] = (byte)n;
        return 8;
    }
    
    @Override
    public String getAlgorithmName() {
        return "SKIPJACK";
    }
    
    @Override
    public int getBlockSize() {
        return 8;
    }
    
    @Override
    public void init(final boolean encrypting, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            final byte[] key = ((KeyParameter)cipherParameters).getKey();
            this.encrypting = encrypting;
            this.key0 = new int[32];
            this.key1 = new int[32];
            this.key2 = new int[32];
            this.key3 = new int[32];
            for (int i = 0; i < 32; ++i) {
                final int[] key2 = this.key0;
                final int n = i * 4;
                key2[i] = (key[n % 10] & 0xFF);
                this.key1[i] = (key[(n + 1) % 10] & 0xFF);
                this.key2[i] = (key[(n + 2) % 10] & 0xFF);
                this.key3[i] = (key[(n + 3) % 10] & 0xFF);
            }
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid parameter passed to SKIPJACK init - ");
        sb.append(cipherParameters.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        if (this.key1 == null) {
            throw new IllegalStateException("SKIPJACK engine not initialised");
        }
        if (n + 8 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 8 <= array2.length) {
            if (this.encrypting) {
                this.encryptBlock(array, n, array2, n2);
            }
            else {
                this.decryptBlock(array, n, array2, n2);
            }
            return 8;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
    }
}
