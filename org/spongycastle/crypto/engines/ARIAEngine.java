package org.spongycastle.crypto.engines;

import org.spongycastle.util.encoders.*;
import java.lang.reflect.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class ARIAEngine implements BlockCipher
{
    protected static final int BLOCK_SIZE = 16;
    private static final byte[][] C;
    private static final byte[] SB1_sbox;
    private static final byte[] SB2_sbox;
    private static final byte[] SB3_sbox;
    private static final byte[] SB4_sbox;
    private byte[][] roundKeys;
    
    static {
        C = new byte[][] { Hex.decode("517cc1b727220a94fe13abe8fa9a6ee0"), Hex.decode("6db14acc9e21c820ff28b1d5ef5de2b0"), Hex.decode("db92371d2126e9700324977504e8c90e") };
        SB1_sbox = new byte[] { 99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22 };
        SB2_sbox = new byte[] { -30, 78, 84, -4, -108, -62, 74, -52, 98, 13, 106, 70, 60, 77, -117, -47, 94, -6, 100, -53, -76, -105, -66, 43, -68, 119, 46, 3, -45, 25, 89, -63, 29, 6, 65, 107, 85, -16, -103, 105, -22, -100, 24, -82, 99, -33, -25, -69, 0, 115, 102, -5, -106, 76, -123, -28, 58, 9, 69, -86, 15, -18, 16, -21, 45, 127, -12, 41, -84, -49, -83, -111, -115, 120, -56, -107, -7, 47, -50, -51, 8, 122, -120, 56, 92, -125, 42, 40, 71, -37, -72, -57, -109, -92, 18, 83, -1, -121, 14, 49, 54, 33, 88, 72, 1, -114, 55, 116, 50, -54, -23, -79, -73, -85, 12, -41, -60, 86, 66, 38, 7, -104, 96, -39, -74, -71, 17, 64, -20, 32, -116, -67, -96, -55, -124, 4, 73, 35, -15, 79, 80, 31, 19, -36, -40, -64, -98, 87, -29, -61, 123, 101, 59, 2, -113, 62, -24, 37, -110, -27, 21, -35, -3, 23, -87, -65, -44, -102, 126, -59, 57, 103, -2, 118, -99, 67, -89, -31, -48, -11, 104, -14, 27, 52, 112, 5, -93, -118, -43, 121, -122, -88, 48, -58, 81, 75, 30, -90, 39, -10, 53, -46, 110, 36, 22, -126, 95, -38, -26, 117, -94, -17, 44, -78, 28, -97, 93, 111, -128, 10, 114, 68, -101, 108, -112, 11, 91, 51, 125, 90, 82, -13, 97, -95, -9, -80, -42, 63, 124, 109, -19, 20, -32, -91, 61, 34, -77, -8, -119, -34, 113, 26, -81, -70, -75, -127 };
        SB3_sbox = new byte[] { 82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5, 124, -29, 57, -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53, 84, 123, -108, 50, -90, -62, 35, 61, -18, 76, -107, 11, 66, -6, -61, 78, 8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37, 114, -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110, 108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115, -99, -124, -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5, -72, -77, 69, 6, -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, -36, -22, -105, -14, -49, -50, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 28, 117, -33, 110, 71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62, 75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12, 31, -35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 89, 39, -128, -20, 95, 96, 81, 127, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17, -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97, 23, 43, 4, 126, -70, 119, -42, 38, -31, 105, 20, 99, 85, 33, 12, 125 };
        SB4_sbox = new byte[] { 48, 104, -103, 27, -121, -71, 33, 120, 80, 57, -37, -31, 114, 9, 98, 60, 62, 126, 94, -114, -15, -96, -52, -93, 42, 29, -5, -74, -42, 32, -60, -115, -127, 101, -11, -119, -53, -99, 119, -58, 87, 67, 86, 23, -44, 64, 26, 77, -64, 99, 108, -29, -73, -56, 100, 106, 83, -86, 56, -104, 12, -12, -101, -19, 127, 34, 118, -81, -35, 58, 11, 88, 103, -120, 6, -61, 53, 13, 1, -117, -116, -62, -26, 95, 2, 36, 117, -109, 102, 30, -27, -30, 84, -40, 16, -50, 122, -24, 8, 44, 18, -105, 50, -85, -76, 39, 10, 35, -33, -17, -54, -39, -72, -6, -36, 49, 107, -47, -83, 25, 73, -67, 81, -106, -18, -28, -88, 65, -38, -1, -51, 85, -122, 54, -66, 97, 82, -8, -69, 14, -126, 72, 105, -102, -32, 71, -98, 92, 4, 75, 52, 21, 121, 38, -89, -34, 41, -82, -110, -41, -124, -23, -46, -70, 93, -13, -59, -80, -65, -92, 59, 113, 68, 70, 43, -4, -21, 111, -43, -10, 20, -2, 124, 112, 90, 125, -3, 47, 24, -125, 22, -91, -111, 31, 5, -107, 116, -87, -63, 91, 74, -123, 109, 19, 7, 79, 78, 69, -78, 15, -55, 28, -90, -68, -20, 115, -112, 123, -49, 89, -113, -95, -7, 45, -14, -79, 0, -108, 55, -97, -48, 46, -100, 110, 40, 63, -128, -16, 61, -45, 37, -118, -75, -25, 66, -77, -57, -22, -9, 76, 17, 51, 3, -94, -84, 96 };
    }
    
    protected static void A(final byte[] array) {
        final byte b = array[0];
        final byte b2 = array[1];
        final byte b3 = array[2];
        final byte b4 = array[3];
        final byte b5 = array[4];
        final byte b6 = array[5];
        final byte b7 = array[6];
        final byte b8 = array[7];
        final byte b9 = array[8];
        final byte b10 = array[9];
        final byte b11 = array[10];
        final byte b12 = array[11];
        final byte b13 = array[12];
        final byte b14 = array[13];
        final byte b15 = array[14];
        final byte b16 = array[15];
        array[0] = (byte)(b4 ^ b5 ^ b7 ^ b9 ^ b10 ^ b14 ^ b15);
        array[1] = (byte)(b3 ^ b6 ^ b8 ^ b9 ^ b10 ^ b13 ^ b16);
        array[2] = (byte)(b2 ^ b5 ^ b7 ^ b11 ^ b12 ^ b13 ^ b16);
        array[3] = (byte)(b ^ b6 ^ b8 ^ b11 ^ b12 ^ b14 ^ b15);
        final int n = b ^ b3;
        array[4] = (byte)(n ^ b6 ^ b9 ^ b12 ^ b15 ^ b16);
        final int n2 = b2 ^ b4;
        array[5] = (byte)(n2 ^ b5 ^ b10 ^ b11 ^ b15 ^ b16);
        array[6] = (byte)(n ^ b8 ^ b10 ^ b11 ^ b13 ^ b14);
        array[7] = (byte)(n2 ^ b7 ^ b9 ^ b12 ^ b13 ^ b14);
        final int n3 = b ^ b2;
        array[8] = (byte)(n3 ^ b5 ^ b8 ^ b11 ^ b14 ^ b16);
        array[9] = (byte)(n3 ^ b6 ^ b7 ^ b12 ^ b13 ^ b15);
        final int n4 = b3 ^ b4;
        array[10] = (byte)(n4 ^ b6 ^ b7 ^ b9 ^ b14 ^ b16);
        array[11] = (byte)(n4 ^ b5 ^ b8 ^ b10 ^ b13 ^ b15);
        final int n5 = b2 ^ b3;
        array[12] = (byte)(n5 ^ b7 ^ b8 ^ b10 ^ b12 ^ b13);
        final int n6 = b ^ b4;
        array[13] = (byte)(n6 ^ b7 ^ b8 ^ b9 ^ b11 ^ b14);
        array[14] = (byte)(n6 ^ b5 ^ b6 ^ b10 ^ b12 ^ b15);
        array[15] = (byte)(n5 ^ b5 ^ b6 ^ b9 ^ b11 ^ b16);
    }
    
    protected static void FE(final byte[] array, final byte[] array2) {
        xor(array, array2);
        SL2(array);
        A(array);
    }
    
    protected static void FO(final byte[] array, final byte[] array2) {
        xor(array, array2);
        SL1(array);
        A(array);
    }
    
    protected static byte SB1(final byte b) {
        return ARIAEngine.SB1_sbox[b & 0xFF];
    }
    
    protected static byte SB2(final byte b) {
        return ARIAEngine.SB2_sbox[b & 0xFF];
    }
    
    protected static byte SB3(final byte b) {
        return ARIAEngine.SB3_sbox[b & 0xFF];
    }
    
    protected static byte SB4(final byte b) {
        return ARIAEngine.SB4_sbox[b & 0xFF];
    }
    
    protected static void SL1(final byte[] array) {
        array[0] = SB1(array[0]);
        array[1] = SB2(array[1]);
        array[2] = SB3(array[2]);
        array[3] = SB4(array[3]);
        array[4] = SB1(array[4]);
        array[5] = SB2(array[5]);
        array[6] = SB3(array[6]);
        array[7] = SB4(array[7]);
        array[8] = SB1(array[8]);
        array[9] = SB2(array[9]);
        array[10] = SB3(array[10]);
        array[11] = SB4(array[11]);
        array[12] = SB1(array[12]);
        array[13] = SB2(array[13]);
        array[14] = SB3(array[14]);
        array[15] = SB4(array[15]);
    }
    
    protected static void SL2(final byte[] array) {
        array[0] = SB3(array[0]);
        array[1] = SB4(array[1]);
        array[2] = SB1(array[2]);
        array[3] = SB2(array[3]);
        array[4] = SB3(array[4]);
        array[5] = SB4(array[5]);
        array[6] = SB1(array[6]);
        array[7] = SB2(array[7]);
        array[8] = SB3(array[8]);
        array[9] = SB4(array[9]);
        array[10] = SB1(array[10]);
        array[11] = SB2(array[11]);
        array[12] = SB3(array[12]);
        array[13] = SB4(array[13]);
        array[14] = SB1(array[14]);
        array[15] = SB2(array[15]);
    }
    
    protected static byte[][] keySchedule(final boolean b, byte[] array) {
        final int length = array.length;
        if (length >= 16 && length <= 32 && (length & 0x7) == 0x0) {
            final int n = (length >>> 3) - 2;
            final byte[][] c = ARIAEngine.C;
            final byte[] array2 = c[n];
            final byte[] array3 = c[(n + 1) % 3];
            final byte[] array4 = c[(n + 2) % 3];
            final byte[] array5 = new byte[16];
            final byte[] array6 = new byte[16];
            System.arraycopy(array, 0, array5, 0, 16);
            System.arraycopy(array, 16, array6, 0, length - 16);
            array = new byte[16];
            final byte[] array7 = new byte[16];
            final byte[] array8 = new byte[16];
            final byte[] array9 = new byte[16];
            System.arraycopy(array5, 0, array, 0, 16);
            System.arraycopy(array, 0, array7, 0, 16);
            FO(array7, array2);
            xor(array7, array6);
            System.arraycopy(array7, 0, array8, 0, 16);
            FE(array8, array3);
            xor(array8, array);
            System.arraycopy(array8, 0, array9, 0, 16);
            FO(array9, array4);
            xor(array9, array7);
            final int n2 = n * 2 + 12;
            final Class<Byte> type = Byte.TYPE;
            int i = 1;
            final byte[][] array10 = (byte[][])Array.newInstance(type, n2 + 1, 16);
            keyScheduleRound(array10[0], array, array7, 19);
            keyScheduleRound(array10[1], array7, array8, 19);
            keyScheduleRound(array10[2], array8, array9, 19);
            keyScheduleRound(array10[3], array9, array, 19);
            keyScheduleRound(array10[4], array, array7, 31);
            keyScheduleRound(array10[5], array7, array8, 31);
            keyScheduleRound(array10[6], array8, array9, 31);
            keyScheduleRound(array10[7], array9, array, 31);
            keyScheduleRound(array10[8], array, array7, 67);
            keyScheduleRound(array10[9], array7, array8, 67);
            keyScheduleRound(array10[10], array8, array9, 67);
            keyScheduleRound(array10[11], array9, array, 67);
            keyScheduleRound(array10[12], array, array7, 97);
            if (n2 > 12) {
                keyScheduleRound(array10[13], array7, array8, 97);
                keyScheduleRound(array10[14], array8, array9, 97);
                if (n2 > 14) {
                    keyScheduleRound(array10[15], array9, array, 97);
                    keyScheduleRound(array10[16], array, array7, 109);
                }
            }
            if (!b) {
                reverseKeys(array10);
                while (i < n2) {
                    A(array10[i]);
                    ++i;
                }
            }
            return array10;
        }
        throw new IllegalArgumentException("Key length not 128/192/256 bits.");
    }
    
    protected static void keyScheduleRound(final byte[] array, final byte[] array2, final byte[] array3, int n) {
        final int n2 = n >>> 3;
        final int n3 = n & 0x7;
        n = (array3[15 - n2] & 0xFF);
        int n4;
        for (int i = 0; i < 16; ++i, n = n4) {
            n4 = (array3[i - n2 & 0xF] & 0xFF);
            array[i] = (byte)((n << 8 - n3 | n4 >>> n3) ^ (array2[i] & 0xFF));
        }
    }
    
    protected static void reverseKeys(final byte[][] array) {
        final int length = array.length;
        for (int n = length / 2, i = 0; i < n; ++i) {
            final byte[] array2 = array[i];
            final int n2 = length - 1 - i;
            array[i] = array[n2];
            array[n2] = array2;
        }
    }
    
    protected static void xor(final byte[] array, final byte[] array2) {
        for (int i = 0; i < 16; ++i) {
            array[i] ^= array2[i];
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "ARIA";
    }
    
    @Override
    public int getBlockSize() {
        return 16;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) throws IllegalArgumentException {
        if (cipherParameters instanceof KeyParameter) {
            this.roundKeys = keySchedule(b, ((KeyParameter)cipherParameters).getKey());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid parameter passed to ARIA init - ");
        sb.append(cipherParameters.getClass().getName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    @Override
    public int processBlock(final byte[] array, int i, final byte[] array2, final int n) throws DataLengthException, IllegalStateException {
        if (this.roundKeys == null) {
            throw new IllegalStateException("ARIA engine not initialised");
        }
        if (i > array.length - 16) {
            throw new DataLengthException("input buffer too short");
        }
        if (n <= array2.length - 16) {
            final byte[] array3 = new byte[16];
            System.arraycopy(array, i, array3, 0, 16);
            int length;
            byte[][] roundKeys;
            int n2;
            for (length = this.roundKeys.length, i = 0; i < length - 3; i = n2 + 1) {
                roundKeys = this.roundKeys;
                n2 = i + 1;
                FO(array3, roundKeys[i]);
                FE(array3, this.roundKeys[n2]);
            }
            final byte[][] roundKeys2 = this.roundKeys;
            final int n3 = i + 1;
            FO(array3, roundKeys2[i]);
            xor(array3, this.roundKeys[n3]);
            SL2(array3);
            xor(array3, this.roundKeys[n3 + 1]);
            System.arraycopy(array3, 0, array2, n, 16);
            return 16;
        }
        throw new OutputLengthException("output buffer too short");
    }
    
    @Override
    public void reset() {
    }
}
