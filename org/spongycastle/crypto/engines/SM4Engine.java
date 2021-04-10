package org.spongycastle.crypto.engines;

import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class SM4Engine implements BlockCipher
{
    private static final int BLOCK_SIZE = 16;
    private static final int[] CK;
    private static final int[] FK;
    private static final byte[] Sbox;
    private final int[] X;
    private int[] rk;
    
    static {
        Sbox = new byte[] { -42, -112, -23, -2, -52, -31, 61, -73, 22, -74, 20, -62, 40, -5, 44, 5, 43, 103, -102, 118, 42, -66, 4, -61, -86, 68, 19, 38, 73, -122, 6, -103, -100, 66, 80, -12, -111, -17, -104, 122, 51, 84, 11, 67, -19, -49, -84, 98, -28, -77, 28, -87, -55, 8, -24, -107, -128, -33, -108, -6, 117, -113, 63, -90, 71, 7, -89, -4, -13, 115, 23, -70, -125, 89, 60, 25, -26, -123, 79, -88, 104, 107, -127, -78, 113, 100, -38, -117, -8, -21, 15, 75, 112, 86, -99, 53, 30, 36, 14, 94, 99, 88, -47, -94, 37, 34, 124, 59, 1, 33, 120, -121, -44, 0, 70, 87, -97, -45, 39, 82, 76, 54, 2, -25, -96, -60, -56, -98, -22, -65, -118, -46, 64, -57, 56, -75, -93, -9, -14, -50, -7, 97, 21, -95, -32, -82, 93, -92, -101, 52, 26, 85, -83, -109, 50, 48, -11, -116, -79, -29, 29, -10, -30, 46, -126, 102, -54, 96, -64, 41, 35, -85, 13, 83, 78, 111, -43, -37, 55, 69, -34, -3, -114, 47, 3, -1, 106, 114, 109, 108, 91, 81, -115, 27, -81, -110, -69, -35, -68, 127, 17, -39, 92, 65, 31, 16, 90, -40, 10, -63, 49, -120, -91, -51, 123, -67, 45, 116, -48, 18, -72, -27, -76, -80, -119, 105, -105, 74, 12, -106, 119, 126, 101, -71, -15, 9, -59, 110, -58, -124, 24, -16, 125, -20, 58, -36, 77, 32, 121, -18, 95, 62, -41, -53, 57, 72 };
        CK = new int[] { 462357, 472066609, 943670861, 1415275113, 1886879365, -1936483679, -1464879427, -993275175, -521670923, -66909679, 404694573, 876298825, 1347903077, 1819507329, -2003855715, -1532251463, -1060647211, -589042959, -117504499, 337322537, 808926789, 1280531041, 1752135293, -2071227751, -1599623499, -1128019247, -656414995, -184876535, 269950501, 741554753, 1213159005, 1684763257 };
        FK = new int[] { -1548633402, 1453994832, 1736282519, -1301273892 };
    }
    
    public SM4Engine() {
        this.X = new int[4];
    }
    
    private int F0(final int[] array, final int n) {
        return this.T(array[3] ^ (array[1] ^ array[2]) ^ n) ^ array[0];
    }
    
    private int F1(final int[] array, final int n) {
        return this.T(array[0] ^ (array[2] ^ array[3]) ^ n) ^ array[1];
    }
    
    private int F2(final int[] array, final int n) {
        return this.T(array[1] ^ (array[3] ^ array[0]) ^ n) ^ array[2];
    }
    
    private int F3(final int[] array, final int n) {
        return this.T(array[2] ^ (array[0] ^ array[1]) ^ n) ^ array[3];
    }
    
    private int L(final int n) {
        return n ^ this.rotateLeft(n, 2) ^ this.rotateLeft(n, 10) ^ this.rotateLeft(n, 18) ^ this.rotateLeft(n, 24);
    }
    
    private int L_ap(final int n) {
        return n ^ this.rotateLeft(n, 13) ^ this.rotateLeft(n, 23);
    }
    
    private void R(final int[] array, final int n) {
        final int n2 = n + 1;
        final int n3 = n + 2;
        final int n4 = n + 3;
        array[n] ^= array[n4];
        array[n4] ^= array[n];
        array[n] ^= array[n4];
        array[n2] ^= array[n3];
        array[n3] ^= array[n2];
        array[n2] ^= array[n3];
    }
    
    private int T(final int n) {
        return this.L(this.tau(n));
    }
    
    private int T_ap(final int n) {
        return this.L_ap(this.tau(n));
    }
    
    private int[] expandKey(final boolean b, final byte[] array) {
        final int[] array2 = new int[32];
        int i = 4;
        final int[] array3 = { Pack.bigEndianToInt(array, 0), Pack.bigEndianToInt(array, 4), Pack.bigEndianToInt(array, 8), Pack.bigEndianToInt(array, 12) };
        final int[] array4 = new int[4];
        final int n = array3[0];
        final int[] fk = SM4Engine.FK;
        array4[0] = (n ^ fk[0]);
        array4[1] = (array3[1] ^ fk[1]);
        array4[2] = (array3[2] ^ fk[2]);
        array4[3] = (array3[3] ^ fk[3]);
        if (b) {
            array2[0] = (array4[0] ^ this.T_ap(array4[1] ^ array4[2] ^ array4[3] ^ SM4Engine.CK[0]));
            array2[1] = (array4[1] ^ this.T_ap(array4[2] ^ array4[3] ^ array2[0] ^ SM4Engine.CK[1]));
            array2[2] = (array4[2] ^ this.T_ap(array4[3] ^ array2[0] ^ array2[1] ^ SM4Engine.CK[2]));
            array2[3] = (array4[3] ^ this.T_ap(array2[0] ^ array2[1] ^ array2[2] ^ SM4Engine.CK[3]));
            while (i < 32) {
                array2[i] = (array2[i - 4] ^ this.T_ap(array2[i - 3] ^ array2[i - 2] ^ array2[i - 1] ^ SM4Engine.CK[i]));
                ++i;
            }
        }
        else {
            array2[31] = (array4[0] ^ this.T_ap(array4[1] ^ array4[2] ^ array4[3] ^ SM4Engine.CK[0]));
            array2[30] = (array4[1] ^ this.T_ap(array4[2] ^ array4[3] ^ array2[31] ^ SM4Engine.CK[1]));
            array2[29] = (array4[2] ^ this.T_ap(array4[3] ^ array2[31] ^ array2[30] ^ SM4Engine.CK[2]));
            array2[28] = (array4[3] ^ this.T_ap(array2[31] ^ array2[30] ^ array2[29] ^ SM4Engine.CK[3]));
            for (int j = 27; j >= 0; --j) {
                array2[j] = (array2[j + 4] ^ this.T_ap(array2[j + 3] ^ array2[j + 2] ^ array2[j + 1] ^ SM4Engine.CK[31 - j]));
            }
        }
        return array2;
    }
    
    private int rotateLeft(final int n, final int n2) {
        return n << n2 | n >>> -n2;
    }
    
    private int tau(final int n) {
        final byte[] sbox = SM4Engine.Sbox;
        return (sbox[n & 0xFF] & 0xFF) | ((sbox[n >> 24 & 0xFF] & 0xFF) << 24 | (sbox[n >> 16 & 0xFF] & 0xFF) << 16 | (sbox[n >> 8 & 0xFF] & 0xFF) << 8);
    }
    
    @Override
    public String getAlgorithmName() {
        return "SM4";
    }
    
    @Override
    public int getBlockSize() {
        return 16;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (!(cipherParameters instanceof KeyParameter)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid parameter passed to SM4 init - ");
            sb.append(cipherParameters.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
        final byte[] key = ((KeyParameter)cipherParameters).getKey();
        if (key.length == 16) {
            this.rk = this.expandKey(b, key);
            return;
        }
        throw new IllegalArgumentException("SM4 requires a 128 bit key");
    }
    
    @Override
    public int processBlock(final byte[] array, int i, final byte[] array2, final int n) throws DataLengthException, IllegalStateException {
        if (this.rk == null) {
            throw new IllegalStateException("SM4 not initialised");
        }
        if (i + 16 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n + 16 <= array2.length) {
            this.X[0] = Pack.bigEndianToInt(array, i);
            this.X[1] = Pack.bigEndianToInt(array, i + 4);
            this.X[2] = Pack.bigEndianToInt(array, i + 8);
            this.X[3] = Pack.bigEndianToInt(array, i + 12);
            int[] x;
            int[] x2;
            int[] x3;
            int[] x4;
            for (i = 0; i < 32; i += 4) {
                x = this.X;
                x[0] = this.F0(x, this.rk[i]);
                x2 = this.X;
                x2[1] = this.F1(x2, this.rk[i + 1]);
                x3 = this.X;
                x3[2] = this.F2(x3, this.rk[i + 2]);
                x4 = this.X;
                x4[3] = this.F3(x4, this.rk[i + 3]);
            }
            this.R(this.X, 0);
            Pack.intToBigEndian(this.X[0], array2, n);
            Pack.intToBigEndian(this.X[1], array2, n + 4);
            Pack.intToBigEndian(this.X[2], array2, n + 8);
            Pack.intToBigEndian(this.X[3], array2, n + 12);
            return 16;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
    }
}
