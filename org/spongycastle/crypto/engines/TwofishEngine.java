package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public final class TwofishEngine implements BlockCipher
{
    private static final int BLOCK_SIZE = 16;
    private static final int GF256_FDBK = 361;
    private static final int GF256_FDBK_2 = 180;
    private static final int GF256_FDBK_4 = 90;
    private static final int INPUT_WHITEN = 0;
    private static final int MAX_KEY_BITS = 256;
    private static final int MAX_ROUNDS = 16;
    private static final int OUTPUT_WHITEN = 4;
    private static final byte[][] P;
    private static final int P_00 = 1;
    private static final int P_01 = 0;
    private static final int P_02 = 0;
    private static final int P_03 = 1;
    private static final int P_04 = 1;
    private static final int P_10 = 0;
    private static final int P_11 = 0;
    private static final int P_12 = 1;
    private static final int P_13 = 1;
    private static final int P_14 = 0;
    private static final int P_20 = 1;
    private static final int P_21 = 1;
    private static final int P_22 = 0;
    private static final int P_23 = 0;
    private static final int P_24 = 0;
    private static final int P_30 = 0;
    private static final int P_31 = 1;
    private static final int P_32 = 1;
    private static final int P_33 = 0;
    private static final int P_34 = 1;
    private static final int ROUNDS = 16;
    private static final int ROUND_SUBKEYS = 8;
    private static final int RS_GF_FDBK = 333;
    private static final int SK_BUMP = 16843009;
    private static final int SK_ROTL = 9;
    private static final int SK_STEP = 33686018;
    private static final int TOTAL_SUBKEYS = 40;
    private boolean encrypting;
    private int[] gMDS0;
    private int[] gMDS1;
    private int[] gMDS2;
    private int[] gMDS3;
    private int[] gSBox;
    private int[] gSubKeys;
    private int k64Cnt;
    private byte[] workingKey;
    
    static {
        P = new byte[][] { { -87, 103, -77, -24, 4, -3, -93, 118, -102, -110, -128, 120, -28, -35, -47, 56, 13, -58, 53, -104, 24, -9, -20, 108, 67, 117, 55, 38, -6, 19, -108, 72, -14, -48, -117, 48, -124, 84, -33, 35, 25, 91, 61, 89, -13, -82, -94, -126, 99, 1, -125, 46, -39, 81, -101, 124, -90, -21, -91, -66, 22, 12, -29, 97, -64, -116, 58, -11, 115, 44, 37, 11, -69, 78, -119, 107, 83, 106, -76, -15, -31, -26, -67, 69, -30, -12, -74, 102, -52, -107, 3, 86, -44, 28, 30, -41, -5, -61, -114, -75, -23, -49, -65, -70, -22, 119, 57, -81, 51, -55, 98, 113, -127, 121, 9, -83, 36, -51, -7, -40, -27, -59, -71, 77, 68, 8, -122, -25, -95, 29, -86, -19, 6, 112, -78, -46, 65, 123, -96, 17, 49, -62, 39, -112, 32, -10, 96, -1, -106, 92, -79, -85, -98, -100, 82, 27, 95, -109, 10, -17, -111, -123, 73, -18, 45, 79, -113, 59, 71, -121, 109, 70, -42, 62, 105, 100, 42, -50, -53, 47, -4, -105, 5, 122, -84, 127, -43, 26, 75, 14, -89, 90, 40, 20, 63, 41, -120, 60, 76, 2, -72, -38, -80, 23, 85, 31, -118, 125, 87, -57, -115, 116, -73, -60, -97, 114, 126, 21, 34, 18, 88, 7, -103, 52, 110, 80, -34, 104, 101, -68, -37, -8, -56, -88, 43, 64, -36, -2, 50, -92, -54, 16, 33, -16, -45, 93, 15, 0, 111, -99, 54, 66, 74, 94, -63, -32 }, { 117, -13, -58, -12, -37, 123, -5, -56, 74, -45, -26, 107, 69, 125, -24, 75, -42, 50, -40, -3, 55, 113, -15, -31, 48, 15, -8, 27, -121, -6, 6, 63, 94, -70, -82, 91, -118, 0, -68, -99, 109, -63, -79, 14, -128, 93, -46, -43, -96, -124, 7, 20, -75, -112, 44, -93, -78, 115, 76, 84, -110, 116, 54, 81, 56, -80, -67, 90, -4, 96, 98, -106, 108, 66, -9, 16, 124, 40, 39, -116, 19, -107, -100, -57, 36, 70, 59, 112, -54, -29, -123, -53, 17, -48, -109, -72, -90, -125, 32, -1, -97, 119, -61, -52, 3, 111, 8, -65, 64, -25, 43, -30, 121, 12, -86, -126, 65, 58, -22, -71, -28, -102, -92, -105, 126, -38, 122, 23, 102, -108, -95, 29, 61, -16, -34, -77, 11, 114, -89, 28, -17, -47, 83, 62, -113, 51, 38, 95, -20, 118, 42, 73, -127, -120, -18, 33, -60, 26, -21, -39, -59, 57, -103, -51, -83, 49, -117, 1, 24, 35, -35, 31, 78, 45, -7, 72, 79, -14, 101, -114, 120, 92, 88, 25, -115, -27, -104, 87, 103, 127, 5, 100, -81, 99, -74, -2, -11, -73, 60, -91, -50, -23, 104, 68, -32, 77, 67, 105, 41, 46, -84, 21, 89, -88, 10, -98, 110, 71, -33, 52, 53, 106, -49, -36, 34, -55, -64, -101, -119, -44, -19, -85, 18, -94, 13, 82, -69, 2, 47, -87, -41, 97, 30, -76, 80, 4, -10, -62, 22, 37, -122, 86, 85, 9, -66, -111 } };
    }
    
    public TwofishEngine() {
        this.encrypting = false;
        this.gMDS0 = new int[256];
        this.gMDS1 = new int[256];
        this.gMDS2 = new int[256];
        this.gMDS3 = new int[256];
        this.k64Cnt = 0;
        this.workingKey = null;
        final int[] array = new int[2];
        final int[] array2 = new int[2];
        final int[] array3 = new int[2];
        for (int i = 0; i < 256; ++i) {
            final int n = TwofishEngine.P[0][i] & 0xFF;
            array[0] = n;
            array2[0] = (this.Mx_X(n) & 0xFF);
            array3[0] = (this.Mx_Y(n) & 0xFF);
            final int n2 = TwofishEngine.P[1][i] & 0xFF;
            array[1] = n2;
            array2[1] = (this.Mx_X(n2) & 0xFF);
            array3[1] = (this.Mx_Y(n2) & 0xFF);
            this.gMDS0[i] = (array[1] | array2[1] << 8 | array3[1] << 16 | array3[1] << 24);
            this.gMDS1[i] = (array3[0] | array3[0] << 8 | array2[0] << 16 | array[0] << 24);
            this.gMDS2[i] = (array3[1] << 24 | (array2[1] | array3[1] << 8 | array[1] << 16));
            this.gMDS3[i] = (array2[0] | array[0] << 8 | array3[0] << 16 | array2[0] << 24);
        }
    }
    
    private void Bits32ToBytes(final int n, final byte[] array, final int n2) {
        array[n2] = (byte)n;
        array[n2 + 1] = (byte)(n >> 8);
        array[n2 + 2] = (byte)(n >> 16);
        array[n2 + 3] = (byte)(n >> 24);
    }
    
    private int BytesTo32Bits(final byte[] array, final int n) {
        return (array[n + 3] & 0xFF) << 24 | ((array[n] & 0xFF) | (array[n + 1] & 0xFF) << 8 | (array[n + 2] & 0xFF) << 16);
    }
    
    private int F32(int b3, int[] array) {
        int b4 = this.b0(b3);
        int b5 = this.b1(b3);
        int b6 = this.b2(b3);
        b3 = this.b3(b3);
        final int n = array[0];
        final int n2 = array[1];
        final int n3 = array[2];
        final int n4 = array[3];
        final int n5 = this.k64Cnt & 0x3;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        Label_0366: {
            if (n5 != 0) {
                if (n5 == 1) {
                    final int n6 = this.gMDS0[(TwofishEngine.P[0][b4] & 0xFF) ^ this.b0(n)] ^ this.gMDS1[(TwofishEngine.P[0][b5] & 0xFF) ^ this.b1(n)] ^ this.gMDS2[(TwofishEngine.P[1][b6] & 0xFF) ^ this.b2(n)];
                    b3 = this.gMDS3[(TwofishEngine.P[1][b3] & 0xFF) ^ this.b3(n)];
                    return b3 ^ n6;
                }
                n7 = b4;
                n8 = b5;
                n9 = b6;
                n10 = b3;
                if (n5 == 2) {
                    break Label_0366;
                }
                if (n5 != 3) {
                    return 0;
                }
            }
            else {
                b4 = ((TwofishEngine.P[1][b4] & 0xFF) ^ this.b0(n4));
                b5 = ((TwofishEngine.P[0][b5] & 0xFF) ^ this.b1(n4));
                b6 = ((TwofishEngine.P[0][b6] & 0xFF) ^ this.b2(n4));
                b3 = ((TwofishEngine.P[1][b3] & 0xFF) ^ this.b3(n4));
            }
            n7 = (this.b0(n3) ^ (TwofishEngine.P[1][b4] & 0xFF));
            n8 = (this.b1(n3) ^ (TwofishEngine.P[1][b5] & 0xFF));
            n9 = (this.b2(n3) ^ (TwofishEngine.P[0][b6] & 0xFF));
            n10 = ((TwofishEngine.P[0][b3] & 0xFF) ^ this.b3(n3));
        }
        array = this.gMDS0;
        final byte[][] p2 = TwofishEngine.P;
        b3 = array[(p2[0][(p2[0][n7] & 0xFF) ^ this.b0(n2)] & 0xFF) ^ this.b0(n)];
        array = this.gMDS1;
        final byte[][] p3 = TwofishEngine.P;
        final int n11 = array[(p3[0][(p3[1][n8] & 0xFF) ^ this.b1(n2)] & 0xFF) ^ this.b1(n)];
        array = this.gMDS2;
        final byte[][] p4 = TwofishEngine.P;
        final int n6 = b3 ^ n11 ^ array[(p4[1][(p4[0][n9] & 0xFF) ^ this.b2(n2)] & 0xFF) ^ this.b2(n)];
        array = this.gMDS3;
        final byte[][] p5 = TwofishEngine.P;
        b3 = array[(p5[1][(p5[1][n10] & 0xFF) ^ this.b3(n2)] & 0xFF) ^ this.b3(n)];
        return b3 ^ n6;
    }
    
    private int Fe32_0(final int n) {
        final int[] gsBox = this.gSBox;
        return gsBox[(n >>> 24 & 0xFF) * 2 + 513] ^ (gsBox[(n & 0xFF) * 2 + 0] ^ gsBox[(n >>> 8 & 0xFF) * 2 + 1] ^ gsBox[(n >>> 16 & 0xFF) * 2 + 512]);
    }
    
    private int Fe32_3(final int n) {
        final int[] gsBox = this.gSBox;
        return gsBox[(n >>> 16 & 0xFF) * 2 + 513] ^ (gsBox[(n >>> 24 & 0xFF) * 2 + 0] ^ gsBox[(n & 0xFF) * 2 + 1] ^ gsBox[(n >>> 8 & 0xFF) * 2 + 512]);
    }
    
    private int LFSR1(final int n) {
        int n2;
        if ((n & 0x1) != 0x0) {
            n2 = 180;
        }
        else {
            n2 = 0;
        }
        return n >> 1 ^ n2;
    }
    
    private int LFSR2(final int n) {
        int n2 = 0;
        int n3;
        if ((n & 0x2) != 0x0) {
            n3 = 180;
        }
        else {
            n3 = 0;
        }
        if ((n & 0x1) != 0x0) {
            n2 = 90;
        }
        return n >> 2 ^ n3 ^ n2;
    }
    
    private int Mx_X(final int n) {
        return n ^ this.LFSR2(n);
    }
    
    private int Mx_Y(final int n) {
        return n ^ this.LFSR1(n) ^ this.LFSR2(n);
    }
    
    private int RS_MDS_Encode(int i, int j) {
        final int n = 0;
        int rs_rem = j;
        for (j = 0; j < 4; ++j) {
            rs_rem = this.RS_rem(rs_rem);
        }
        j = (i ^ rs_rem);
        for (i = n; i < 4; ++i) {
            j = this.RS_rem(j);
        }
        return j;
    }
    
    private int RS_rem(final int n) {
        final int n2 = n >>> 24 & 0xFF;
        final boolean b = false;
        int n3;
        if ((n2 & 0x80) != 0x0) {
            n3 = 333;
        }
        else {
            n3 = 0;
        }
        final int n4 = (n3 ^ n2 << 1) & 0xFF;
        int n5 = b ? 1 : 0;
        if ((n2 & 0x1) != 0x0) {
            n5 = 166;
        }
        final int n6 = n5 ^ n2 >>> 1 ^ n4;
        return n << 8 ^ n6 << 24 ^ n4 << 16 ^ n6 << 8 ^ n2;
    }
    
    private int b0(final int n) {
        return n & 0xFF;
    }
    
    private int b1(final int n) {
        return n >>> 8 & 0xFF;
    }
    
    private int b2(final int n) {
        return n >>> 16 & 0xFF;
    }
    
    private int b3(final int n) {
        return n >>> 24 & 0xFF;
    }
    
    private void decryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        int n2 = this.BytesTo32Bits(array, i) ^ this.gSubKeys[4];
        int n3 = this.BytesTo32Bits(array, i + 4) ^ this.gSubKeys[5];
        final int bytesTo32Bits = this.BytesTo32Bits(array, i + 8);
        final int n4 = this.gSubKeys[6];
        i = this.BytesTo32Bits(array, i + 12);
        final int n5 = this.gSubKeys[7];
        int n6 = 39;
        int n7 = bytesTo32Bits ^ n4;
        int n8 = i ^ n5;
        int fe32_0;
        int fe32_2;
        int[] gSubKeys;
        int n9;
        int n10;
        int n11;
        int fe32_3;
        int fe32_4;
        int[] gSubKeys2;
        int n12;
        int n13;
        for (i = 0; i < 16; i += 2) {
            fe32_0 = this.Fe32_0(n2);
            fe32_2 = this.Fe32_3(n3);
            gSubKeys = this.gSubKeys;
            n9 = n6 - 1;
            n10 = (n8 ^ fe32_2 * 2 + fe32_0 + gSubKeys[n6]);
            n11 = n9 - 1;
            n7 = ((n7 << 1 | n7 >>> 31) ^ fe32_0 + fe32_2 + gSubKeys[n9]);
            n8 = (n10 >>> 1 | n10 << 31);
            fe32_3 = this.Fe32_0(n7);
            fe32_4 = this.Fe32_3(n8);
            gSubKeys2 = this.gSubKeys;
            n12 = n11 - 1;
            n13 = (n3 ^ fe32_4 * 2 + fe32_3 + gSubKeys2[n11]);
            n6 = n12 - 1;
            n2 = ((n2 << 1 | n2 >>> 31) ^ fe32_3 + fe32_4 + gSubKeys2[n12]);
            n3 = (n13 >>> 1 | n13 << 31);
        }
        this.Bits32ToBytes(this.gSubKeys[0] ^ n7, array2, n);
        this.Bits32ToBytes(this.gSubKeys[1] ^ n8, array2, n + 4);
        this.Bits32ToBytes(this.gSubKeys[2] ^ n2, array2, n + 8);
        this.Bits32ToBytes(this.gSubKeys[3] ^ n3, array2, n + 12);
    }
    
    private void encryptBlock(final byte[] array, int n, final byte[] array2, final int n2) {
        final int bytesTo32Bits = this.BytesTo32Bits(array, n);
        final int[] gSubKeys = this.gSubKeys;
        int i = 0;
        int n3 = bytesTo32Bits ^ gSubKeys[0];
        final int n4 = this.BytesTo32Bits(array, n + 4) ^ this.gSubKeys[1];
        int n5 = this.BytesTo32Bits(array, n + 8) ^ this.gSubKeys[2];
        int n6 = this.BytesTo32Bits(array, n + 12) ^ this.gSubKeys[3];
        int n7 = 8;
        int fe32_0;
        int fe32_2;
        int[] gSubKeys2;
        int n8;
        int n9;
        int n10;
        int fe32_3;
        int fe32_4;
        int[] gSubKeys3;
        int n11;
        int n12;
        int n13;
        for (n = n4; i < 16; i += 2, n = ((n >>> 31 | n << 1) ^ fe32_3 + fe32_4 * 2 + n13), n7 = n11 + 1) {
            fe32_0 = this.Fe32_0(n3);
            fe32_2 = this.Fe32_3(n);
            gSubKeys2 = this.gSubKeys;
            n8 = n7 + 1;
            n9 = (n5 ^ fe32_0 + fe32_2 + gSubKeys2[n7]);
            n5 = (n9 >>> 1 | n9 << 31);
            n10 = n8 + 1;
            n6 = ((n6 << 1 | n6 >>> 31) ^ fe32_0 + fe32_2 * 2 + gSubKeys2[n8]);
            fe32_3 = this.Fe32_0(n5);
            fe32_4 = this.Fe32_3(n6);
            gSubKeys3 = this.gSubKeys;
            n11 = n10 + 1;
            n12 = (n3 ^ fe32_3 + fe32_4 + gSubKeys3[n10]);
            n3 = (n12 >>> 1 | n12 << 31);
            n13 = gSubKeys3[n11];
        }
        this.Bits32ToBytes(this.gSubKeys[4] ^ n5, array2, n2);
        this.Bits32ToBytes(n6 ^ this.gSubKeys[5], array2, n2 + 4);
        this.Bits32ToBytes(this.gSubKeys[6] ^ n3, array2, n2 + 8);
        this.Bits32ToBytes(this.gSubKeys[7] ^ n, array2, n2 + 12);
    }
    
    private void setKey(final byte[] array) {
        final int[] array2 = new int[4];
        final int[] array3 = new int[4];
        final int[] array4 = new int[4];
        this.gSubKeys = new int[40];
        final int k64Cnt = this.k64Cnt;
        if (k64Cnt < 1) {
            throw new IllegalArgumentException("Key size less than 64 bits");
        }
        if (k64Cnt <= 4) {
            for (int i = 0; i < this.k64Cnt; ++i) {
                final int n = i * 8;
                array2[i] = this.BytesTo32Bits(array, n);
                array3[i] = this.BytesTo32Bits(array, n + 4);
                array4[this.k64Cnt - 1 - i] = this.RS_MDS_Encode(array2[i], array3[i]);
            }
            for (int j = 0; j < 20; ++j) {
                final int n2 = 33686018 * j;
                final int f32 = this.F32(n2, array2);
                final int f33 = this.F32(n2 + 16843009, array3);
                final int n3 = f33 << 8 | f33 >>> 24;
                final int n4 = f32 + n3;
                final int[] gSubKeys = this.gSubKeys;
                final int n5 = j * 2;
                gSubKeys[n5] = n4;
                final int n6 = n4 + n3;
                gSubKeys[n5 + 1] = (n6 << 9 | n6 >>> 23);
            }
            final int n7 = array4[0];
            final int n8 = array4[1];
            final int n9 = array4[2];
            final int n10 = array4[3];
            this.gSBox = new int[1024];
            for (int k = 0; k < 256; ++k) {
                final int n11 = this.k64Cnt & 0x3;
                int n14 = 0;
                int n13 = 0;
                int n16 = 0;
                int n15 = 0;
                Label_0629: {
                    int n20;
                    int n21;
                    int n22;
                    int n23;
                    if (n11 != 0) {
                        if (n11 == 1) {
                            final int[] gsBox = this.gSBox;
                            final int n12 = k * 2;
                            gsBox[n12] = this.gMDS0[(TwofishEngine.P[0][k] & 0xFF) ^ this.b0(n7)];
                            this.gSBox[n12 + 1] = this.gMDS1[(TwofishEngine.P[0][k] & 0xFF) ^ this.b1(n7)];
                            this.gSBox[n12 + 512] = this.gMDS2[(TwofishEngine.P[1][k] & 0xFF) ^ this.b2(n7)];
                            this.gSBox[n12 + 513] = this.gMDS3[(TwofishEngine.P[1][k] & 0xFF) ^ this.b3(n7)];
                            continue;
                        }
                        if (n11 == 2) {
                            n13 = (n14 = k);
                            n15 = (n16 = n14);
                            break Label_0629;
                        }
                        if (n11 != 3) {
                            continue;
                        }
                        final int n18;
                        final int n17 = n18 = k;
                        final int n19 = n18;
                        n20 = n17;
                        n21 = n18;
                        n22 = n19;
                        n23 = n19;
                    }
                    else {
                        n20 = ((TwofishEngine.P[1][k] & 0xFF) ^ this.b0(n10));
                        n21 = ((TwofishEngine.P[0][k] & 0xFF) ^ this.b1(n10));
                        n22 = ((TwofishEngine.P[0][k] & 0xFF) ^ this.b2(n10));
                        n23 = ((TwofishEngine.P[1][k] & 0xFF) ^ this.b3(n10));
                    }
                    final int n24 = (TwofishEngine.P[1][n20] & 0xFF) ^ this.b0(n9);
                    final int n25 = (TwofishEngine.P[1][n21] & 0xFF) ^ this.b1(n9);
                    final int n26 = (TwofishEngine.P[0][n22] & 0xFF) ^ this.b2(n9);
                    n16 = ((TwofishEngine.P[0][n23] & 0xFF) ^ this.b3(n9));
                    n15 = n26;
                    n14 = n25;
                    n13 = n24;
                }
                final int[] gsBox2 = this.gSBox;
                final int n27 = k * 2;
                final int[] gmds0 = this.gMDS0;
                final byte[][] p = TwofishEngine.P;
                gsBox2[n27] = gmds0[(p[0][(p[0][n13] & 0xFF) ^ this.b0(n8)] & 0xFF) ^ this.b0(n7)];
                final int[] gsBox3 = this.gSBox;
                final int[] gmds2 = this.gMDS1;
                final byte[][] p2 = TwofishEngine.P;
                gsBox3[n27 + 1] = gmds2[(p2[0][(p2[1][n14] & 0xFF) ^ this.b1(n8)] & 0xFF) ^ this.b1(n7)];
                final int[] gsBox4 = this.gSBox;
                final int[] gmds3 = this.gMDS2;
                final byte[][] p3 = TwofishEngine.P;
                gsBox4[n27 + 512] = gmds3[(p3[1][(p3[0][n15] & 0xFF) ^ this.b2(n8)] & 0xFF) ^ this.b2(n7)];
                final int[] gsBox5 = this.gSBox;
                final int[] gmds4 = this.gMDS3;
                final byte[][] p4 = TwofishEngine.P;
                gsBox5[n27 + 513] = gmds4[(p4[1][(p4[1][n16] & 0xFF) ^ this.b3(n8)] & 0xFF) ^ this.b3(n7)];
            }
            return;
        }
        throw new IllegalArgumentException("Key size larger than 256 bits");
    }
    
    @Override
    public String getAlgorithmName() {
        return "Twofish";
    }
    
    @Override
    public int getBlockSize() {
        return 16;
    }
    
    @Override
    public void init(final boolean encrypting, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.encrypting = encrypting;
            final byte[] key = ((KeyParameter)cipherParameters).getKey();
            this.workingKey = key;
            this.k64Cnt = key.length / 8;
            this.setKey(key);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid parameter passed to Twofish init - ");
        sb.append(cipherParameters.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        if (this.workingKey == null) {
            throw new IllegalStateException("Twofish not initialised");
        }
        if (n + 16 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 16 <= array2.length) {
            if (this.encrypting) {
                this.encryptBlock(array, n, array2, n2);
            }
            else {
                this.decryptBlock(array, n, array2, n2);
            }
            return 16;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
        final byte[] workingKey = this.workingKey;
        if (workingKey != null) {
            this.setKey(workingKey);
        }
    }
}
