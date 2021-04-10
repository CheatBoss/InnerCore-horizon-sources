package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class RC2Engine implements BlockCipher
{
    private static final int BLOCK_SIZE = 8;
    private static byte[] piTable;
    private boolean encrypting;
    private int[] workingKey;
    
    static {
        RC2Engine.piTable = new byte[] { -39, 120, -7, -60, 25, -35, -75, -19, 40, -23, -3, 121, 74, -96, -40, -99, -58, 126, 55, -125, 43, 118, 83, -114, 98, 76, 100, -120, 68, -117, -5, -94, 23, -102, 89, -11, -121, -77, 79, 19, 97, 69, 109, -115, 9, -127, 125, 50, -67, -113, 64, -21, -122, -73, 123, 11, -16, -107, 33, 34, 92, 107, 78, -126, 84, -42, 101, -109, -50, 96, -78, 28, 115, 86, -64, 20, -89, -116, -15, -36, 18, 117, -54, 31, 59, -66, -28, -47, 66, 61, -44, 48, -93, 60, -74, 38, 111, -65, 14, -38, 70, 105, 7, 87, 39, -14, 29, -101, -68, -108, 67, 3, -8, 17, -57, -10, -112, -17, 62, -25, 6, -61, -43, 47, -56, 102, 30, -41, 8, -24, -22, -34, -128, 82, -18, -9, -124, -86, 114, -84, 53, 77, 106, 42, -106, 26, -46, 113, 90, 21, 73, 116, 75, -97, -48, 94, 4, 24, -92, -20, -62, -32, 65, 110, 15, 81, -53, -52, 36, -111, -81, 80, -95, -12, 112, 57, -103, 124, 58, -123, 35, -72, -76, 122, -4, 2, 54, 91, 37, 85, -105, 49, 45, 93, -6, -104, -29, -118, -110, -82, 5, -33, 41, 16, 103, 108, -70, -55, -45, 0, -26, -49, -31, -98, -88, 44, 99, 22, 1, 63, 88, -30, -119, -87, 13, 56, 52, 27, -85, 51, -1, -80, -69, 72, 12, 95, -71, -79, -51, 46, -59, -13, -37, 71, -27, -91, -100, 119, 10, -90, 32, 104, -2, 127, -63, -83 };
    }
    
    private void decryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        int n2 = ((array[i + 7] & 0xFF) << 8) + (array[i + 6] & 0xFF);
        int n3 = ((array[i + 5] & 0xFF) << 8) + (array[i + 4] & 0xFF);
        int n4 = ((array[i + 3] & 0xFF) << 8) + (array[i + 2] & 0xFF);
        i = ((array[i + 1] & 0xFF) << 8) + (array[i + 0] & 0xFF);
        for (int j = 60; j >= 44; j -= 4) {
            n2 = this.rotateWordLeft(n2, 11) - ((~n3 & i) + (n4 & n3) + this.workingKey[j + 3]);
            n3 = this.rotateWordLeft(n3, 13) - ((~n4 & n2) + (i & n4) + this.workingKey[j + 2]);
            n4 = this.rotateWordLeft(n4, 14) - ((~i & n3) + (n2 & i) + this.workingKey[j + 1]);
            i = this.rotateWordLeft(i, 15) - ((~n2 & n4) + (n3 & n2) + this.workingKey[j]);
        }
        final int[] workingKey = this.workingKey;
        int n5 = n2 - workingKey[n3 & 0x3F];
        int n6 = n3 - workingKey[n4 & 0x3F];
        int n7 = n4 - workingKey[i & 0x3F];
        i -= workingKey[n5 & 0x3F];
        for (int k = 40; k >= 20; k -= 4) {
            n5 = this.rotateWordLeft(n5, 11) - ((~n6 & i) + (n7 & n6) + this.workingKey[k + 3]);
            n6 = this.rotateWordLeft(n6, 13) - ((~n7 & n5) + (i & n7) + this.workingKey[k + 2]);
            n7 = this.rotateWordLeft(n7, 14) - ((~i & n6) + (n5 & i) + this.workingKey[k + 1]);
            i = this.rotateWordLeft(i, 15) - ((~n5 & n7) + (n6 & n5) + this.workingKey[k]);
        }
        final int[] workingKey2 = this.workingKey;
        int n8 = n5 - workingKey2[n6 & 0x3F];
        int n9 = n6 - workingKey2[n7 & 0x3F];
        int n10 = n7 - workingKey2[i & 0x3F];
        int n11 = i - workingKey2[n8 & 0x3F];
        for (i = 16; i >= 0; i -= 4) {
            n8 = this.rotateWordLeft(n8, 11) - ((~n9 & n11) + (n10 & n9) + this.workingKey[i + 3]);
            n9 = this.rotateWordLeft(n9, 13) - ((~n10 & n8) + (n11 & n10) + this.workingKey[i + 2]);
            n10 = this.rotateWordLeft(n10, 14) - ((~n11 & n9) + (n8 & n11) + this.workingKey[i + 1]);
            n11 = this.rotateWordLeft(n11, 15) - ((~n8 & n10) + (n9 & n8) + this.workingKey[i]);
        }
        array2[n + 0] = (byte)n11;
        array2[n + 1] = (byte)(n11 >> 8);
        array2[n + 2] = (byte)n10;
        array2[n + 3] = (byte)(n10 >> 8);
        array2[n + 4] = (byte)n9;
        array2[n + 5] = (byte)(n9 >> 8);
        array2[n + 6] = (byte)n8;
        array2[n + 7] = (byte)(n8 >> 8);
    }
    
    private void encryptBlock(final byte[] array, int i, final byte[] array2, final int n) {
        int rotateWordLeft = ((array[i + 7] & 0xFF) << 8) + (array[i + 6] & 0xFF);
        int rotateWordLeft2 = ((array[i + 5] & 0xFF) << 8) + (array[i + 4] & 0xFF);
        int rotateWordLeft3 = ((array[i + 3] & 0xFF) << 8) + (array[i + 2] & 0xFF);
        final byte b = array[i + 1];
        final int n2 = 0;
        final int n3 = ((b & 0xFF) << 8) + (array[i + 0] & 0xFF);
        i = n2;
        int rotateWordLeft4 = n3;
        while (i <= 16) {
            rotateWordLeft4 = this.rotateWordLeft(rotateWordLeft4 + (~rotateWordLeft & rotateWordLeft3) + (rotateWordLeft2 & rotateWordLeft) + this.workingKey[i], 1);
            rotateWordLeft3 = this.rotateWordLeft(rotateWordLeft3 + (~rotateWordLeft4 & rotateWordLeft2) + (rotateWordLeft & rotateWordLeft4) + this.workingKey[i + 1], 2);
            rotateWordLeft2 = this.rotateWordLeft(rotateWordLeft2 + (~rotateWordLeft3 & rotateWordLeft) + (rotateWordLeft4 & rotateWordLeft3) + this.workingKey[i + 2], 3);
            rotateWordLeft = this.rotateWordLeft(rotateWordLeft + (~rotateWordLeft2 & rotateWordLeft4) + (rotateWordLeft3 & rotateWordLeft2) + this.workingKey[i + 3], 5);
            i += 4;
        }
        final int[] workingKey = this.workingKey;
        int rotateWordLeft5 = rotateWordLeft4 + workingKey[rotateWordLeft & 0x3F];
        int rotateWordLeft6 = rotateWordLeft3 + workingKey[rotateWordLeft5 & 0x3F];
        int rotateWordLeft7 = rotateWordLeft2 + workingKey[rotateWordLeft6 & 0x3F];
        int rotateWordLeft8 = rotateWordLeft + workingKey[rotateWordLeft7 & 0x3F];
        for (i = 20; i <= 40; i += 4) {
            rotateWordLeft5 = this.rotateWordLeft(rotateWordLeft5 + (~rotateWordLeft8 & rotateWordLeft6) + (rotateWordLeft7 & rotateWordLeft8) + this.workingKey[i], 1);
            rotateWordLeft6 = this.rotateWordLeft(rotateWordLeft6 + (~rotateWordLeft5 & rotateWordLeft7) + (rotateWordLeft8 & rotateWordLeft5) + this.workingKey[i + 1], 2);
            rotateWordLeft7 = this.rotateWordLeft(rotateWordLeft7 + (~rotateWordLeft6 & rotateWordLeft8) + (rotateWordLeft5 & rotateWordLeft6) + this.workingKey[i + 2], 3);
            rotateWordLeft8 = this.rotateWordLeft(rotateWordLeft8 + (~rotateWordLeft7 & rotateWordLeft5) + (rotateWordLeft6 & rotateWordLeft7) + this.workingKey[i + 3], 5);
        }
        final int[] workingKey2 = this.workingKey;
        int rotateWordLeft9 = rotateWordLeft5 + workingKey2[rotateWordLeft8 & 0x3F];
        int rotateWordLeft10 = rotateWordLeft6 + workingKey2[rotateWordLeft9 & 0x3F];
        int rotateWordLeft11 = rotateWordLeft7 + workingKey2[rotateWordLeft10 & 0x3F];
        int rotateWordLeft12 = rotateWordLeft8 + workingKey2[rotateWordLeft11 & 0x3F];
        for (i = 44; i < 64; i += 4) {
            rotateWordLeft9 = this.rotateWordLeft(rotateWordLeft9 + (~rotateWordLeft12 & rotateWordLeft10) + (rotateWordLeft11 & rotateWordLeft12) + this.workingKey[i], 1);
            rotateWordLeft10 = this.rotateWordLeft(rotateWordLeft10 + (~rotateWordLeft9 & rotateWordLeft11) + (rotateWordLeft12 & rotateWordLeft9) + this.workingKey[i + 1], 2);
            rotateWordLeft11 = this.rotateWordLeft(rotateWordLeft11 + (~rotateWordLeft10 & rotateWordLeft12) + (rotateWordLeft9 & rotateWordLeft10) + this.workingKey[i + 2], 3);
            rotateWordLeft12 = this.rotateWordLeft(rotateWordLeft12 + (~rotateWordLeft11 & rotateWordLeft9) + (rotateWordLeft10 & rotateWordLeft11) + this.workingKey[i + 3], 5);
        }
        array2[n + 0] = (byte)rotateWordLeft9;
        array2[n + 1] = (byte)(rotateWordLeft9 >> 8);
        array2[n + 2] = (byte)rotateWordLeft10;
        array2[n + 3] = (byte)(rotateWordLeft10 >> 8);
        array2[n + 4] = (byte)rotateWordLeft11;
        array2[n + 5] = (byte)(rotateWordLeft11 >> 8);
        array2[n + 6] = (byte)rotateWordLeft12;
        array2[n + 7] = (byte)(rotateWordLeft12 >> 8);
    }
    
    private int[] generateWorkingKey(byte[] piTable, int i) {
        final int[] array = new int[128];
        final int n = 0;
        for (int j = 0; j != piTable.length; ++j) {
            array[j] = (piTable[j] & 0xFF);
        }
        int length = piTable.length;
        if (length < 128) {
            int n2 = array[length - 1];
            int n3 = 0;
            while (true) {
                n2 = (RC2Engine.piTable[n2 + array[n3] & 0xFF] & 0xFF);
                final int n4 = length + 1;
                array[length] = n2;
                if (n4 >= 128) {
                    break;
                }
                ++n3;
                length = n4;
            }
        }
        final int n5 = i + 7 >> 3;
        piTable = RC2Engine.piTable;
        int n6 = 128 - n5;
        i = (piTable[255 >> (-i & 0x7) & array[n6]] & 0xFF);
        array[n6] = i;
        while (true) {
            --n6;
            if (n6 < 0) {
                break;
            }
            i = (RC2Engine.piTable[i ^ array[n6 + n5]] & 0xFF);
            array[n6] = i;
        }
        final int[] array2 = new int[64];
        int n7;
        for (i = n; i != 64; ++i) {
            n7 = i * 2;
            array2[i] = array[n7] + (array[n7 + 1] << 8);
        }
        return array2;
    }
    
    private int rotateWordLeft(int n, final int n2) {
        n &= 0xFFFF;
        return n << n2 | n >> 16 - n2;
    }
    
    @Override
    public String getAlgorithmName() {
        return "RC2";
    }
    
    @Override
    public int getBlockSize() {
        return 8;
    }
    
    @Override
    public void init(final boolean encrypting, final CipherParameters cipherParameters) {
        this.encrypting = encrypting;
        byte[] array;
        int effectiveKeyBits;
        if (cipherParameters instanceof RC2Parameters) {
            final RC2Parameters rc2Parameters = (RC2Parameters)cipherParameters;
            array = rc2Parameters.getKey();
            effectiveKeyBits = rc2Parameters.getEffectiveKeyBits();
        }
        else {
            if (!(cipherParameters instanceof KeyParameter)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invalid parameter passed to RC2 init - ");
                sb.append(cipherParameters.getClass().getName());
                throw new IllegalArgumentException(sb.toString());
            }
            array = ((KeyParameter)cipherParameters).getKey();
            effectiveKeyBits = array.length * 8;
        }
        this.workingKey = this.generateWorkingKey(array, effectiveKeyBits);
    }
    
    @Override
    public final int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) {
        if (this.workingKey == null) {
            throw new IllegalStateException("RC2 engine not initialised");
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
