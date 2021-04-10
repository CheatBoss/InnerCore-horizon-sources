package org.spongycastle.crypto.engines;

import java.lang.reflect.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class AESLightEngine implements BlockCipher
{
    private static final int BLOCK_SIZE = 16;
    private static final byte[] S;
    private static final byte[] Si;
    private static final int m1 = -2139062144;
    private static final int m2 = 2139062143;
    private static final int m3 = 27;
    private static final int m4 = -1061109568;
    private static final int m5 = 1061109567;
    private static final int[] rcon;
    private int C0;
    private int C1;
    private int C2;
    private int C3;
    private int ROUNDS;
    private int[][] WorkingKey;
    private boolean forEncryption;
    
    static {
        S = new byte[] { 99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22 };
        Si = new byte[] { 82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5, 124, -29, 57, -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53, 84, 123, -108, 50, -90, -62, 35, 61, -18, 76, -107, 11, 66, -6, -61, 78, 8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37, 114, -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110, 108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115, -99, -124, -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5, -72, -77, 69, 6, -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, -36, -22, -105, -14, -49, -50, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 28, 117, -33, 110, 71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62, 75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12, 31, -35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 89, 39, -128, -20, 95, 96, 81, 127, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17, -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97, 23, 43, 4, 126, -70, 119, -42, 38, -31, 105, 20, 99, 85, 33, 12, 125 };
        rcon = new int[] { 1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212, 179, 125, 250, 239, 197, 145 };
    }
    
    public AESLightEngine() {
        this.WorkingKey = null;
    }
    
    private static int FFmulX(final int n) {
        return (n & 0x7F7F7F7F) << 1 ^ ((0x80808080 & n) >>> 7) * 27;
    }
    
    private static int FFmulX2(final int n) {
        final int n2 = 0xC0C0C0C0 & n;
        final int n3 = n2 ^ n2 >>> 1;
        return (n & 0x3F3F3F3F) << 2 ^ n3 >>> 2 ^ n3 >>> 5;
    }
    
    private void decryptBlock(final int[][] array) {
        final int c0 = this.C0;
        final int rounds = this.ROUNDS;
        int n = c0 ^ array[rounds][0];
        int n2 = this.C1 ^ array[rounds][1];
        int n3 = this.C2 ^ array[rounds][2];
        int i = rounds - 1;
        int n4 = array[rounds][3] ^ this.C3;
        while (i > 1) {
            final byte[] si = AESLightEngine.Si;
            final int n5 = inv_mcol(si[n2 >> 24 & 0xFF] << 24 ^ ((si[n & 0xFF] & 0xFF) ^ (si[n4 >> 8 & 0xFF] & 0xFF) << 8 ^ (si[n3 >> 16 & 0xFF] & 0xFF) << 16)) ^ array[i][0];
            final byte[] si2 = AESLightEngine.Si;
            final int n6 = inv_mcol(si2[n3 >> 24 & 0xFF] << 24 ^ ((si2[n2 & 0xFF] & 0xFF) ^ (si2[n >> 8 & 0xFF] & 0xFF) << 8 ^ (si2[n4 >> 16 & 0xFF] & 0xFF) << 16)) ^ array[i][1];
            final byte[] si3 = AESLightEngine.Si;
            final int n7 = inv_mcol(si3[n4 >> 24 & 0xFF] << 24 ^ ((si3[n3 & 0xFF] & 0xFF) ^ (si3[n2 >> 8 & 0xFF] & 0xFF) << 8 ^ (si3[n >> 16 & 0xFF] & 0xFF) << 16)) ^ array[i][2];
            final byte[] si4 = AESLightEngine.Si;
            final int inv_mcol = inv_mcol(si4[n >> 24 & 0xFF] << 24 ^ ((si4[n4 & 0xFF] & 0xFF) ^ (si4[n3 >> 8 & 0xFF] & 0xFF) << 8 ^ (si4[n2 >> 16 & 0xFF] & 0xFF) << 16));
            final int n8 = i - 1;
            final int n9 = inv_mcol ^ array[i][3];
            final byte[] si5 = AESLightEngine.Si;
            final int inv_mcol2 = inv_mcol(si5[n6 >> 24 & 0xFF] << 24 ^ ((si5[n5 & 0xFF] & 0xFF) ^ (si5[n9 >> 8 & 0xFF] & 0xFF) << 8 ^ (si5[n7 >> 16 & 0xFF] & 0xFF) << 16));
            final int n10 = array[n8][0];
            final byte[] si6 = AESLightEngine.Si;
            final int inv_mcol3 = inv_mcol(si6[n7 >> 24 & 0xFF] << 24 ^ ((si6[n6 & 0xFF] & 0xFF) ^ (si6[n5 >> 8 & 0xFF] & 0xFF) << 8 ^ (si6[n9 >> 16 & 0xFF] & 0xFF) << 16));
            final int n11 = array[n8][1];
            final byte[] si7 = AESLightEngine.Si;
            final int inv_mcol4 = inv_mcol(si7[n9 >> 24 & 0xFF] << 24 ^ ((si7[n5 >> 16 & 0xFF] & 0xFF) << 16 ^ ((si7[n7 & 0xFF] & 0xFF) ^ (si7[n6 >> 8 & 0xFF] & 0xFF) << 8)));
            final int n12 = array[n8][2];
            final byte[] si8 = AESLightEngine.Si;
            final int inv_mcol5 = inv_mcol((si8[n9 & 0xFF] & 0xFF) ^ (si8[n7 >> 8 & 0xFF] & 0xFF) << 8 ^ (si8[n6 >> 16 & 0xFF] & 0xFF) << 16 ^ si8[n5 >> 24 & 0xFF] << 24);
            final int n13 = array[n8][3];
            n3 = (inv_mcol4 ^ n12);
            i = n8 - 1;
            n4 = (inv_mcol5 ^ n13);
            n = (inv_mcol2 ^ n10);
            n2 = (inv_mcol3 ^ n11);
        }
        final byte[] si9 = AESLightEngine.Si;
        final int n14 = inv_mcol(si9[n2 >> 24 & 0xFF] << 24 ^ ((si9[n & 0xFF] & 0xFF) ^ (si9[n4 >> 8 & 0xFF] & 0xFF) << 8 ^ (si9[n3 >> 16 & 0xFF] & 0xFF) << 16)) ^ array[i][0];
        final byte[] si10 = AESLightEngine.Si;
        final int n15 = inv_mcol(si10[n3 >> 24 & 0xFF] << 24 ^ ((si10[n2 & 0xFF] & 0xFF) ^ (si10[n >> 8 & 0xFF] & 0xFF) << 8 ^ (si10[n4 >> 16 & 0xFF] & 0xFF) << 16)) ^ array[i][1];
        final byte[] si11 = AESLightEngine.Si;
        final int n16 = inv_mcol(si11[n4 >> 24 & 0xFF] << 24 ^ ((si11[n3 & 0xFF] & 0xFF) ^ (si11[n2 >> 8 & 0xFF] & 0xFF) << 8 ^ (si11[n >> 16 & 0xFF] & 0xFF) << 16)) ^ array[i][2];
        final byte[] si12 = AESLightEngine.Si;
        final int n17 = inv_mcol(si12[n >> 24 & 0xFF] << 24 ^ ((si12[n4 & 0xFF] & 0xFF) ^ (si12[n3 >> 8 & 0xFF] & 0xFF) << 8 ^ (si12[n2 >> 16 & 0xFF] & 0xFF) << 16)) ^ array[i][3];
        final byte[] si13 = AESLightEngine.Si;
        this.C0 = ((si13[n14 & 0xFF] & 0xFF) ^ (si13[n17 >> 8 & 0xFF] & 0xFF) << 8 ^ (si13[n16 >> 16 & 0xFF] & 0xFF) << 16 ^ si13[n15 >> 24 & 0xFF] << 24 ^ array[0][0]);
        this.C1 = ((si13[n15 & 0xFF] & 0xFF) ^ (si13[n14 >> 8 & 0xFF] & 0xFF) << 8 ^ (si13[n17 >> 16 & 0xFF] & 0xFF) << 16 ^ si13[n16 >> 24 & 0xFF] << 24 ^ array[0][1]);
        this.C2 = ((si13[n16 & 0xFF] & 0xFF) ^ (si13[n15 >> 8 & 0xFF] & 0xFF) << 8 ^ (si13[n14 >> 16 & 0xFF] & 0xFF) << 16 ^ si13[n17 >> 24 & 0xFF] << 24 ^ array[0][2]);
        this.C3 = (array[0][3] ^ ((si13[n17 & 0xFF] & 0xFF) ^ (si13[n16 >> 8 & 0xFF] & 0xFF) << 8 ^ (si13[n15 >> 16 & 0xFF] & 0xFF) << 16 ^ si13[n14 >> 24 & 0xFF] << 24));
    }
    
    private void encryptBlock(final int[][] array) {
        final int c0 = this.C0;
        final int n = array[0][0];
        final int c2 = this.C1;
        final int n2 = array[0][1];
        final int c3 = this.C2;
        final int n3 = array[0][2];
        int n4 = this.C3 ^ array[0][3];
        int n5 = c3 ^ n3;
        int n6 = c2 ^ n2;
        int n7 = c0 ^ n;
        int i;
        int n11;
        int mcol3;
        int n14;
        for (i = 1; i < this.ROUNDS - 1; i = n11 + 1, n6 = (mcol3 ^ n14)) {
            final byte[] s = AESLightEngine.S;
            final int n8 = mcol(s[n4 >> 24 & 0xFF] << 24 ^ ((s[n7 & 0xFF] & 0xFF) ^ (s[n6 >> 8 & 0xFF] & 0xFF) << 8 ^ (s[n5 >> 16 & 0xFF] & 0xFF) << 16)) ^ array[i][0];
            final byte[] s2 = AESLightEngine.S;
            final int n9 = mcol(s2[n7 >> 24 & 0xFF] << 24 ^ ((s2[n6 & 0xFF] & 0xFF) ^ (s2[n5 >> 8 & 0xFF] & 0xFF) << 8 ^ (s2[n4 >> 16 & 0xFF] & 0xFF) << 16)) ^ array[i][1];
            final byte[] s3 = AESLightEngine.S;
            final int n10 = mcol(s3[n6 >> 24 & 0xFF] << 24 ^ ((s3[n5 & 0xFF] & 0xFF) ^ (s3[n4 >> 8 & 0xFF] & 0xFF) << 8 ^ (s3[n7 >> 16 & 0xFF] & 0xFF) << 16)) ^ array[i][2];
            final byte[] s4 = AESLightEngine.S;
            final int mcol = mcol((s4[n7 >> 8 & 0xFF] & 0xFF) << 8 ^ (s4[n4 & 0xFF] & 0xFF) ^ (s4[n6 >> 16 & 0xFF] & 0xFF) << 16 ^ s4[n5 >> 24 & 0xFF] << 24);
            n11 = i + 1;
            final int n12 = array[i][3] ^ mcol;
            final byte[] s5 = AESLightEngine.S;
            final int mcol2 = mcol(s5[n12 >> 24 & 0xFF] << 24 ^ ((s5[n8 & 0xFF] & 0xFF) ^ (s5[n9 >> 8 & 0xFF] & 0xFF) << 8 ^ (s5[n10 >> 16 & 0xFF] & 0xFF) << 16));
            final int n13 = array[n11][0];
            final byte[] s6 = AESLightEngine.S;
            mcol3 = mcol(s6[n8 >> 24 & 0xFF] << 24 ^ ((s6[n9 & 0xFF] & 0xFF) ^ (s6[n10 >> 8 & 0xFF] & 0xFF) << 8 ^ (s6[n12 >> 16 & 0xFF] & 0xFF) << 16));
            n14 = array[n11][1];
            final byte[] s7 = AESLightEngine.S;
            final int mcol4 = mcol(s7[n9 >> 24 & 0xFF] << 24 ^ ((s7[n8 >> 16 & 0xFF] & 0xFF) << 16 ^ ((s7[n10 & 0xFF] & 0xFF) ^ (s7[n12 >> 8 & 0xFF] & 0xFF) << 8)));
            final int n15 = array[n11][2];
            final byte[] s8 = AESLightEngine.S;
            n4 = (mcol((s8[n12 & 0xFF] & 0xFF) ^ (s8[n8 >> 8 & 0xFF] & 0xFF) << 8 ^ (s8[n9 >> 16 & 0xFF] & 0xFF) << 16 ^ s8[n10 >> 24 & 0xFF] << 24) ^ array[n11][3]);
            n7 = (mcol2 ^ n13);
            n5 = (mcol4 ^ n15);
        }
        final byte[] s9 = AESLightEngine.S;
        final int n16 = mcol(s9[n4 >> 24 & 0xFF] << 24 ^ ((s9[n7 & 0xFF] & 0xFF) ^ (s9[n6 >> 8 & 0xFF] & 0xFF) << 8 ^ (s9[n5 >> 16 & 0xFF] & 0xFF) << 16)) ^ array[i][0];
        final byte[] s10 = AESLightEngine.S;
        final int n17 = mcol(s10[n7 >> 24 & 0xFF] << 24 ^ ((s10[n6 & 0xFF] & 0xFF) ^ (s10[n5 >> 8 & 0xFF] & 0xFF) << 8 ^ (s10[n4 >> 16 & 0xFF] & 0xFF) << 16)) ^ array[i][1];
        final byte[] s11 = AESLightEngine.S;
        final int n18 = mcol(s11[n6 >> 24 & 0xFF] << 24 ^ ((s11[n5 & 0xFF] & 0xFF) ^ (s11[n4 >> 8 & 0xFF] & 0xFF) << 8 ^ (s11[n7 >> 16 & 0xFF] & 0xFF) << 16)) ^ array[i][2];
        final byte[] s12 = AESLightEngine.S;
        final int mcol5 = mcol((s12[n7 >> 8 & 0xFF] & 0xFF) << 8 ^ (s12[n4 & 0xFF] & 0xFF) ^ (s12[n6 >> 16 & 0xFF] & 0xFF) << 16 ^ s12[n5 >> 24 & 0xFF] << 24);
        final int n19 = i + 1;
        final int n20 = array[i][3] ^ mcol5;
        final byte[] s13 = AESLightEngine.S;
        this.C0 = ((s13[n16 & 0xFF] & 0xFF) ^ (s13[n17 >> 8 & 0xFF] & 0xFF) << 8 ^ (s13[n18 >> 16 & 0xFF] & 0xFF) << 16 ^ s13[n20 >> 24 & 0xFF] << 24 ^ array[n19][0]);
        this.C1 = ((s13[n17 & 0xFF] & 0xFF) ^ (s13[n18 >> 8 & 0xFF] & 0xFF) << 8 ^ (s13[n20 >> 16 & 0xFF] & 0xFF) << 16 ^ s13[n16 >> 24 & 0xFF] << 24 ^ array[n19][1]);
        this.C2 = ((s13[n18 & 0xFF] & 0xFF) ^ (s13[n20 >> 8 & 0xFF] & 0xFF) << 8 ^ (s13[n16 >> 16 & 0xFF] & 0xFF) << 16 ^ s13[n17 >> 24 & 0xFF] << 24 ^ array[n19][2]);
        this.C3 = (array[n19][3] ^ ((s13[n20 & 0xFF] & 0xFF) ^ (s13[n16 >> 8 & 0xFF] & 0xFF) << 8 ^ (s13[n17 >> 16 & 0xFF] & 0xFF) << 16 ^ s13[n18 >> 24 & 0xFF] << 24));
    }
    
    private int[][] generateWorkingKey(final byte[] array, final boolean b) {
        final int length = array.length;
        if (length >= 16 && length <= 32 && (length & 0x7) == 0x0) {
            final int n = length >> 2;
            final int rounds = n + 6;
            this.ROUNDS = rounds;
            final Class<Integer> type = Integer.TYPE;
            final int n2 = 1;
            final int[][] array2 = (int[][])Array.newInstance(type, rounds + 1, 4);
            if (n != 4) {
                if (n != 6) {
                    if (n != 8) {
                        throw new IllegalStateException("Should never get here");
                    }
                    final int littleEndianToInt = Pack.littleEndianToInt(array, 0);
                    array2[0][0] = littleEndianToInt;
                    int littleEndianToInt2 = Pack.littleEndianToInt(array, 4);
                    array2[0][1] = littleEndianToInt2;
                    int littleEndianToInt3 = Pack.littleEndianToInt(array, 8);
                    array2[0][2] = littleEndianToInt3;
                    int littleEndianToInt4 = Pack.littleEndianToInt(array, 12);
                    array2[0][3] = littleEndianToInt4;
                    int littleEndianToInt5 = Pack.littleEndianToInt(array, 16);
                    array2[1][0] = littleEndianToInt5;
                    int littleEndianToInt6 = Pack.littleEndianToInt(array, 20);
                    array2[1][1] = littleEndianToInt6;
                    int littleEndianToInt7 = Pack.littleEndianToInt(array, 24);
                    array2[1][2] = littleEndianToInt7;
                    int littleEndianToInt8 = Pack.littleEndianToInt(array, 28);
                    array2[1][3] = littleEndianToInt8;
                    int i = 2;
                    final int n3 = 1;
                    int n4 = littleEndianToInt;
                    int n5;
                    for (n5 = n3; i < 14; i += 2, n5 <<= 1) {
                        n4 ^= (n5 ^ subWord(shift(littleEndianToInt8, 8)));
                        array2[i][0] = n4;
                        littleEndianToInt2 ^= n4;
                        array2[i][1] = littleEndianToInt2;
                        littleEndianToInt3 ^= littleEndianToInt2;
                        array2[i][2] = littleEndianToInt3;
                        littleEndianToInt4 ^= littleEndianToInt3;
                        array2[i][3] = littleEndianToInt4;
                        littleEndianToInt5 ^= subWord(littleEndianToInt4);
                        final int n6 = i + 1;
                        array2[n6][0] = littleEndianToInt5;
                        littleEndianToInt6 ^= littleEndianToInt5;
                        array2[n6][1] = littleEndianToInt6;
                        littleEndianToInt7 ^= littleEndianToInt6;
                        array2[n6][2] = littleEndianToInt7;
                        littleEndianToInt8 ^= littleEndianToInt7;
                        array2[n6][3] = littleEndianToInt8;
                    }
                    final int n7 = n5 ^ subWord(shift(littleEndianToInt8, 8)) ^ n4;
                    array2[14][0] = n7;
                    final int n8 = n7 ^ littleEndianToInt2;
                    array2[14][1] = n8;
                    final int n9 = n8 ^ littleEndianToInt3;
                    array2[14][2] = n9;
                    array2[14][3] = (n9 ^ littleEndianToInt4);
                }
                else {
                    final int littleEndianToInt9 = Pack.littleEndianToInt(array, 0);
                    array2[0][0] = littleEndianToInt9;
                    final int littleEndianToInt10 = Pack.littleEndianToInt(array, 4);
                    array2[0][1] = littleEndianToInt10;
                    final int littleEndianToInt11 = Pack.littleEndianToInt(array, 8);
                    array2[0][2] = littleEndianToInt11;
                    final int littleEndianToInt12 = Pack.littleEndianToInt(array, 12);
                    array2[0][3] = littleEndianToInt12;
                    final int littleEndianToInt13 = Pack.littleEndianToInt(array, 16);
                    array2[1][0] = littleEndianToInt13;
                    final int littleEndianToInt14 = Pack.littleEndianToInt(array, 20);
                    array2[1][1] = littleEndianToInt14;
                    int n10 = littleEndianToInt9 ^ (subWord(shift(littleEndianToInt14, 8)) ^ 0x1);
                    array2[1][2] = n10;
                    int n11 = littleEndianToInt10 ^ n10;
                    array2[1][3] = n11;
                    int n12 = littleEndianToInt11 ^ n11;
                    array2[2][0] = n12;
                    int n13 = littleEndianToInt12 ^ n12;
                    array2[2][1] = n13;
                    int n14 = littleEndianToInt13 ^ n13;
                    array2[2][2] = n14;
                    int n15 = littleEndianToInt14 ^ n14;
                    array2[2][3] = n15;
                    int n16 = 2;
                    int n17;
                    for (int j = 3; j < 12; j += 3, n16 = n17 << 1) {
                        final int subWord = subWord(shift(n15, 8));
                        n17 = n16 << 1;
                        final int n18 = n16 ^ subWord ^ n10;
                        array2[j][0] = n18;
                        final int n19 = n11 ^ n18;
                        array2[j][1] = n19;
                        final int n20 = n12 ^ n19;
                        array2[j][2] = n20;
                        final int n21 = n13 ^ n20;
                        array2[j][3] = n21;
                        final int n22 = n14 ^ n21;
                        final int n23 = j + 1;
                        array2[n23][0] = n22;
                        final int n24 = n15 ^ n22;
                        array2[n23][1] = n24;
                        n10 = (n18 ^ (subWord(shift(n24, 8)) ^ n17));
                        array2[n23][2] = n10;
                        n11 = (n19 ^ n10);
                        array2[n23][3] = n11;
                        n12 = (n20 ^ n11);
                        final int n25 = j + 2;
                        array2[n25][0] = n12;
                        n13 = (n21 ^ n12);
                        array2[n25][1] = n13;
                        n14 = (n22 ^ n13);
                        array2[n25][2] = n14;
                        n15 = (n24 ^ n14);
                        array2[n25][3] = n15;
                    }
                    final int n26 = subWord(shift(n15, 8)) ^ n16 ^ n10;
                    array2[12][0] = n26;
                    final int n27 = n26 ^ n11;
                    array2[12][1] = n27;
                    final int n28 = n27 ^ n12;
                    array2[12][2] = n28;
                    array2[12][3] = (n28 ^ n13);
                }
            }
            else {
                int littleEndianToInt15 = Pack.littleEndianToInt(array, 0);
                array2[0][0] = littleEndianToInt15;
                int littleEndianToInt16 = Pack.littleEndianToInt(array, 4);
                array2[0][1] = littleEndianToInt16;
                int littleEndianToInt17 = Pack.littleEndianToInt(array, 8);
                array2[0][2] = littleEndianToInt17;
                int littleEndianToInt18 = Pack.littleEndianToInt(array, 12);
                array2[0][3] = littleEndianToInt18;
                for (int k = 1; k <= 10; ++k) {
                    littleEndianToInt15 ^= (subWord(shift(littleEndianToInt18, 8)) ^ AESLightEngine.rcon[k - 1]);
                    array2[k][0] = littleEndianToInt15;
                    littleEndianToInt16 ^= littleEndianToInt15;
                    array2[k][1] = littleEndianToInt16;
                    littleEndianToInt17 ^= littleEndianToInt16;
                    array2[k][2] = littleEndianToInt17;
                    littleEndianToInt18 ^= littleEndianToInt17;
                    array2[k][3] = littleEndianToInt18;
                }
            }
            if (!b) {
                for (int l = n2; l < this.ROUNDS; ++l) {
                    for (int n29 = 0; n29 < 4; ++n29) {
                        array2[l][n29] = inv_mcol(array2[l][n29]);
                    }
                }
            }
            return array2;
        }
        throw new IllegalArgumentException("Key length not 128/192/256 bits.");
    }
    
    private static int inv_mcol(int n) {
        final int n2 = shift(n, 8) ^ n;
        n ^= FFmulX(n2);
        final int n3 = n2 ^ FFmulX2(n);
        return n ^ (n3 ^ shift(n3, 16));
    }
    
    private static int mcol(int n) {
        final int shift = shift(n, 8);
        n ^= shift;
        return FFmulX(n) ^ (shift ^ shift(n, 16));
    }
    
    private void packBlock(final byte[] array, int n) {
        final int n2 = n + 1;
        final int c0 = this.C0;
        array[n] = (byte)c0;
        n = n2 + 1;
        array[n2] = (byte)(c0 >> 8);
        final int n3 = n + 1;
        array[n] = (byte)(c0 >> 16);
        final int n4 = n3 + 1;
        array[n3] = (byte)(c0 >> 24);
        final int n5 = n4 + 1;
        n = this.C1;
        array[n4] = (byte)n;
        final int n6 = n5 + 1;
        array[n5] = (byte)(n >> 8);
        final int n7 = n6 + 1;
        array[n6] = (byte)(n >> 16);
        final int n8 = n7 + 1;
        array[n7] = (byte)(n >> 24);
        final int n9 = n8 + 1;
        n = this.C2;
        array[n8] = (byte)n;
        final int n10 = n9 + 1;
        array[n9] = (byte)(n >> 8);
        final int n11 = n10 + 1;
        array[n10] = (byte)(n >> 16);
        final int n12 = n11 + 1;
        array[n11] = (byte)(n >> 24);
        n = n12 + 1;
        final int c2 = this.C3;
        array[n12] = (byte)c2;
        final int n13 = n + 1;
        array[n] = (byte)(c2 >> 8);
        array[n13] = (byte)(c2 >> 16);
        array[n13 + 1] = (byte)(c2 >> 24);
    }
    
    private static int shift(final int n, final int n2) {
        return n >>> n2 | n << -n2;
    }
    
    private static int subWord(final int n) {
        final byte[] s = AESLightEngine.S;
        return s[n >> 24 & 0xFF] << 24 | ((s[n & 0xFF] & 0xFF) | (s[n >> 8 & 0xFF] & 0xFF) << 8 | (s[n >> 16 & 0xFF] & 0xFF) << 16);
    }
    
    private void unpackBlock(final byte[] array, int n) {
        final int n2 = n + 1;
        final int c0 = array[n] & 0xFF;
        this.C0 = c0;
        n = n2 + 1;
        final int c2 = c0 | (array[n2] & 0xFF) << 8;
        this.C0 = c2;
        final int n3 = n + 1;
        final int c3 = c2 | (array[n] & 0xFF) << 16;
        this.C0 = c3;
        n = n3 + 1;
        this.C0 = (c3 | array[n3] << 24);
        final int n4 = n + 1;
        final int c4 = array[n] & 0xFF;
        this.C1 = c4;
        n = n4 + 1;
        final int c5 = (array[n4] & 0xFF) << 8 | c4;
        this.C1 = c5;
        final int n5 = n + 1;
        final int c6 = c5 | (array[n] & 0xFF) << 16;
        this.C1 = c6;
        n = n5 + 1;
        this.C1 = (c6 | array[n5] << 24);
        final int n6 = n + 1;
        final int c7 = array[n] & 0xFF;
        this.C2 = c7;
        n = n6 + 1;
        final int c8 = (array[n6] & 0xFF) << 8 | c7;
        this.C2 = c8;
        final int n7 = n + 1;
        final int c9 = c8 | (array[n] & 0xFF) << 16;
        this.C2 = c9;
        n = n7 + 1;
        this.C2 = (c9 | array[n7] << 24);
        final int n8 = n + 1;
        final int c10 = array[n] & 0xFF;
        this.C3 = c10;
        n = n8 + 1;
        final int c11 = (array[n8] & 0xFF) << 8 | c10;
        this.C3 = c11;
        final int c12 = c11 | (array[n] & 0xFF) << 16;
        this.C3 = c12;
        this.C3 = (array[n + 1] << 24 | c12);
    }
    
    @Override
    public String getAlgorithmName() {
        return "AES";
    }
    
    @Override
    public int getBlockSize() {
        return 16;
    }
    
    @Override
    public void init(final boolean forEncryption, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.WorkingKey = this.generateWorkingKey(((KeyParameter)cipherParameters).getKey(), forEncryption);
            this.forEncryption = forEncryption;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid parameter passed to AES init - ");
        sb.append(cipherParameters.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        if (this.WorkingKey == null) {
            throw new IllegalStateException("AES engine not initialised");
        }
        if (n + 16 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 16 <= array2.length) {
            if (this.forEncryption) {
                this.unpackBlock(array, n);
                this.encryptBlock(this.WorkingKey);
            }
            else {
                this.unpackBlock(array, n);
                this.decryptBlock(this.WorkingKey);
            }
            this.packBlock(array2, n2);
            return 16;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
    }
}
