package org.spongycastle.crypto.engines;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class CamelliaLightEngine implements BlockCipher
{
    private static final int BLOCK_SIZE = 16;
    private static final int MASK8 = 255;
    private static final byte[] SBOX1;
    private static final int[] SIGMA;
    private boolean _keyis128;
    private boolean initialized;
    private int[] ke;
    private int[] kw;
    private int[] state;
    private int[] subkey;
    
    static {
        SIGMA = new int[] { -1600231809, 1003262091, -1233459112, 1286239154, -957401297, -380665154, 1426019237, -237801700, 283453434, -563598051, -1336506174, -1276722691 };
        SBOX1 = new byte[] { 112, -126, 44, -20, -77, 39, -64, -27, -28, -123, 87, 53, -22, 12, -82, 65, 35, -17, 107, -109, 69, 25, -91, 33, -19, 14, 79, 78, 29, 101, -110, -67, -122, -72, -81, -113, 124, -21, 31, -50, 62, 48, -36, 95, 94, -59, 11, 26, -90, -31, 57, -54, -43, 71, 93, 61, -39, 1, 90, -42, 81, 86, 108, 77, -117, 13, -102, 102, -5, -52, -80, 45, 116, 18, 43, 32, -16, -79, -124, -103, -33, 76, -53, -62, 52, 126, 118, 5, 109, -73, -87, 49, -47, 23, 4, -41, 20, 88, 58, 97, -34, 27, 17, 28, 50, 15, -100, 22, 83, 24, -14, 34, -2, 68, -49, -78, -61, -75, 122, -111, 36, 8, -24, -88, 96, -4, 105, 80, -86, -48, -96, 125, -95, -119, 98, -105, 84, 91, 30, -107, -32, -1, 100, -46, 16, -60, 0, 72, -93, -9, 117, -37, -118, 3, -26, -38, 9, 63, -35, -108, -121, 92, -125, 2, -51, 74, -112, 51, 115, 103, -10, -13, -99, 127, -65, -30, 82, -101, -40, 38, -56, 55, -58, 59, -127, -106, 111, 75, 19, -66, 99, 46, -23, 121, -89, -116, -97, 110, -68, -114, 41, -11, -7, -74, 47, -3, -76, 89, 120, -104, 6, 106, -25, 70, 113, -70, -44, 37, -85, 66, -120, -94, -115, -6, 114, 7, -71, 85, -8, -18, -84, 10, 54, 73, 42, 104, 60, 56, -15, -92, 64, 40, -45, 123, -69, -55, 67, -63, 21, -29, -83, -12, 119, -57, -128, -98 };
    }
    
    public CamelliaLightEngine() {
        this.subkey = new int[96];
        this.kw = new int[8];
        this.ke = new int[12];
        this.state = new int[4];
    }
    
    private int bytes2int(final byte[] array, final int n) {
        int i = 0;
        int n2 = 0;
        while (i < 4) {
            n2 = (n2 << 8) + (array[i + n] & 0xFF);
            ++i;
        }
        return n2;
    }
    
    private void camelliaF2(final int[] array, final int[] array2, int leftRotate) {
        final int n = array[0] ^ array2[leftRotate + 0];
        final int sbox4 = this.sbox4(n & 0xFF);
        final int sbox5 = this.sbox3(n >>> 8 & 0xFF);
        final int sbox6 = this.sbox2(n >>> 16 & 0xFF);
        final byte[] sbox7 = CamelliaLightEngine.SBOX1;
        final byte b = sbox7[n >>> 24 & 0xFF];
        final int n2 = array[1] ^ array2[leftRotate + 1];
        final int leftRotate2 = leftRotate((sbox7[n2 & 0xFF] & 0xFF) | this.sbox4(n2 >>> 8 & 0xFF) << 8 | this.sbox3(n2 >>> 16 & 0xFF) << 16 | this.sbox2(n2 >>> 24 & 0xFF) << 24, 8);
        final int n3 = ((b & 0xFF) << 24 | (sbox4 | sbox5 << 8 | sbox6 << 16)) ^ leftRotate2;
        final int n4 = leftRotate(leftRotate2, 8) ^ n3;
        final int n5 = rightRotate(n3, 8) ^ n4;
        array[2] ^= (leftRotate(n4, 16) ^ n5);
        array[3] ^= leftRotate(n5, 8);
        final int n6 = array[2] ^ array2[leftRotate + 2];
        final int sbox8 = this.sbox4(n6 & 0xFF);
        final int sbox9 = this.sbox3(n6 >>> 8 & 0xFF);
        final int sbox10 = this.sbox2(n6 >>> 16 & 0xFF);
        final byte[] sbox11 = CamelliaLightEngine.SBOX1;
        final byte b2 = sbox11[n6 >>> 24 & 0xFF];
        leftRotate = (array2[leftRotate + 3] ^ array[3]);
        leftRotate = leftRotate(this.sbox2(leftRotate >>> 24 & 0xFF) << 24 | ((sbox11[leftRotate & 0xFF] & 0xFF) | this.sbox4(leftRotate >>> 8 & 0xFF) << 8 | this.sbox3(leftRotate >>> 16 & 0xFF) << 16), 8);
        final int n7 = ((b2 & 0xFF) << 24 | (sbox9 << 8 | sbox8 | sbox10 << 16)) ^ leftRotate;
        leftRotate = (leftRotate(leftRotate, 8) ^ n7);
        final int n8 = rightRotate(n7, 8) ^ leftRotate;
        array[0] ^= (leftRotate(leftRotate, 16) ^ n8);
        array[1] ^= leftRotate(n8, 8);
    }
    
    private void camelliaFLs(final int[] array, final int[] array2, final int n) {
        array[1] ^= leftRotate(array[0] & array2[n + 0], 1);
        array[0] ^= (array2[n + 1] | array[1]);
        array[2] ^= (array2[n + 3] | array[3]);
        array[3] ^= leftRotate(array2[n + 2] & array[2], 1);
    }
    
    private static void decroldq(final int n, final int[] array, int n2, final int[] array2, int n3) {
        final int n4 = n3 + 2;
        final int n5 = n2 + 0;
        final int n6 = array[n5];
        final int n7 = n2 + 1;
        final int n8 = array[n7];
        final int n9 = 32 - n;
        array2[n4] = (n6 << n | n8 >>> n9);
        final int n10 = n3 + 3;
        final int n11 = array[n7];
        final int n12 = n2 + 2;
        array2[n10] = (n11 << n | array[n12] >>> n9);
        final int n13 = n3 + 0;
        final int n14 = array[n12];
        n2 += 3;
        array2[n13] = (n14 << n | array[n2] >>> n9);
        ++n3;
        array2[n3] = (array[n2] << n | array[n5] >>> n9);
        array[n5] = array2[n4];
        array[n7] = array2[n10];
        array[n12] = array2[n13];
        array[n2] = array2[n3];
    }
    
    private static void decroldqo32(int n, final int[] array, int n2, final int[] array2, int n3) {
        final int n4 = n3 + 2;
        final int n5 = n2 + 1;
        final int n6 = array[n5];
        final int n7 = n - 32;
        final int n8 = n2 + 2;
        final int n9 = array[n8];
        n = 64 - n;
        array2[n4] = (n6 << n7 | n9 >>> n);
        final int n10 = n3 + 3;
        final int n11 = array[n8];
        final int n12 = n2 + 3;
        array2[n10] = (n11 << n7 | array[n12] >>> n);
        final int n13 = n3 + 0;
        final int n14 = array[n12];
        n2 += 0;
        array2[n13] = (n14 << n7 | array[n2] >>> n);
        ++n3;
        array2[n3] = (array[n5] >>> n | array[n2] << n7);
        array[n2] = array2[n4];
        array[n5] = array2[n10];
        array[n8] = array2[n13];
        array[n12] = array2[n3];
    }
    
    private void int2bytes(int i, final byte[] array, final int n) {
        final int n2 = 0;
        int n3 = i;
        for (i = n2; i < 4; ++i) {
            array[3 - i + n] = (byte)n3;
            n3 >>>= 8;
        }
    }
    
    private byte lRot8(final byte b, final int n) {
        return (byte)(b << n | (b & 0xFF) >>> 8 - n);
    }
    
    private static int leftRotate(final int n, final int n2) {
        return (n << n2) + (n >>> 32 - n2);
    }
    
    private int processBlock128(final byte[] array, int n, final byte[] array2, final int n2) {
        for (int i = 0; i < 4; ++i) {
            this.state[i] = this.bytes2int(array, i * 4 + n);
            final int[] state = this.state;
            state[i] ^= this.kw[i];
        }
        this.camelliaF2(this.state, this.subkey, 0);
        this.camelliaF2(this.state, this.subkey, 4);
        this.camelliaF2(this.state, this.subkey, 8);
        this.camelliaFLs(this.state, this.ke, 0);
        this.camelliaF2(this.state, this.subkey, 12);
        this.camelliaF2(this.state, this.subkey, 16);
        this.camelliaF2(this.state, this.subkey, 20);
        this.camelliaFLs(this.state, this.ke, 4);
        this.camelliaF2(this.state, this.subkey, 24);
        this.camelliaF2(this.state, this.subkey, 28);
        this.camelliaF2(this.state, this.subkey, 32);
        final int[] state2 = this.state;
        n = state2[2];
        final int[] kw = this.kw;
        state2[2] = (kw[4] ^ n);
        state2[3] ^= kw[5];
        state2[0] ^= kw[6];
        n = state2[1];
        state2[1] = (kw[7] ^ n);
        this.int2bytes(state2[2], array2, n2);
        this.int2bytes(this.state[3], array2, n2 + 4);
        this.int2bytes(this.state[0], array2, n2 + 8);
        this.int2bytes(this.state[1], array2, n2 + 12);
        return 16;
    }
    
    private int processBlock192or256(final byte[] array, int n, final byte[] array2, final int n2) {
        for (int i = 0; i < 4; ++i) {
            this.state[i] = this.bytes2int(array, i * 4 + n);
            final int[] state = this.state;
            state[i] ^= this.kw[i];
        }
        this.camelliaF2(this.state, this.subkey, 0);
        this.camelliaF2(this.state, this.subkey, 4);
        this.camelliaF2(this.state, this.subkey, 8);
        this.camelliaFLs(this.state, this.ke, 0);
        this.camelliaF2(this.state, this.subkey, 12);
        this.camelliaF2(this.state, this.subkey, 16);
        this.camelliaF2(this.state, this.subkey, 20);
        this.camelliaFLs(this.state, this.ke, 4);
        this.camelliaF2(this.state, this.subkey, 24);
        this.camelliaF2(this.state, this.subkey, 28);
        this.camelliaF2(this.state, this.subkey, 32);
        this.camelliaFLs(this.state, this.ke, 8);
        this.camelliaF2(this.state, this.subkey, 36);
        this.camelliaF2(this.state, this.subkey, 40);
        this.camelliaF2(this.state, this.subkey, 44);
        final int[] state2 = this.state;
        n = state2[2];
        final int[] kw = this.kw;
        state2[2] = (n ^ kw[4]);
        state2[3] ^= kw[5];
        state2[0] ^= kw[6];
        n = state2[1];
        state2[1] = (kw[7] ^ n);
        this.int2bytes(state2[2], array2, n2);
        this.int2bytes(this.state[3], array2, n2 + 4);
        this.int2bytes(this.state[0], array2, n2 + 8);
        this.int2bytes(this.state[1], array2, n2 + 12);
        return 16;
    }
    
    private static int rightRotate(final int n, final int n2) {
        return (n >>> n2) + (n << 32 - n2);
    }
    
    private static void roldq(final int n, final int[] array, int n2, final int[] array2, int n3) {
        final int n4 = n3 + 0;
        final int n5 = n2 + 0;
        final int n6 = array[n5];
        final int n7 = n2 + 1;
        final int n8 = array[n7];
        final int n9 = 32 - n;
        array2[n4] = (n6 << n | n8 >>> n9);
        final int n10 = n3 + 1;
        final int n11 = array[n7];
        final int n12 = n2 + 2;
        array2[n10] = (n11 << n | array[n12] >>> n9);
        final int n13 = n3 + 2;
        final int n14 = array[n12];
        n2 += 3;
        array2[n13] = (n14 << n | array[n2] >>> n9);
        n3 += 3;
        array2[n3] = (array[n2] << n | array[n5] >>> n9);
        array[n5] = array2[n4];
        array[n7] = array2[n10];
        array[n12] = array2[n13];
        array[n2] = array2[n3];
    }
    
    private static void roldqo32(int n, final int[] array, int n2, final int[] array2, int n3) {
        final int n4 = n3 + 0;
        final int n5 = n2 + 1;
        final int n6 = array[n5];
        final int n7 = n - 32;
        final int n8 = n2 + 2;
        final int n9 = array[n8];
        n = 64 - n;
        array2[n4] = (n6 << n7 | n9 >>> n);
        final int n10 = n3 + 1;
        final int n11 = array[n8];
        final int n12 = n2 + 3;
        array2[n10] = (n11 << n7 | array[n12] >>> n);
        final int n13 = n3 + 2;
        final int n14 = array[n12];
        n2 += 0;
        array2[n13] = (n14 << n7 | array[n2] >>> n);
        n3 += 3;
        array2[n3] = (array[n5] >>> n | array[n2] << n7);
        array[n2] = array2[n4];
        array[n5] = array2[n10];
        array[n8] = array2[n13];
        array[n12] = array2[n3];
    }
    
    private int sbox2(final int n) {
        return this.lRot8(CamelliaLightEngine.SBOX1[n], 1) & 0xFF;
    }
    
    private int sbox3(final int n) {
        return this.lRot8(CamelliaLightEngine.SBOX1[n], 7) & 0xFF;
    }
    
    private int sbox4(final int n) {
        return CamelliaLightEngine.SBOX1[this.lRot8((byte)n, 1) & 0xFF] & 0xFF;
    }
    
    private void setKey(final boolean b, final byte[] array) {
        final int[] array2 = new int[8];
        final int[] array3 = new int[4];
        final int[] array4 = new int[4];
        final int[] array5 = new int[4];
        final int length = array.length;
        if (length != 16) {
            if (length != 24) {
                if (length != 32) {
                    throw new IllegalArgumentException("key sizes are only 16/24/32 bytes.");
                }
                array2[0] = this.bytes2int(array, 0);
                array2[1] = this.bytes2int(array, 4);
                array2[2] = this.bytes2int(array, 8);
                array2[3] = this.bytes2int(array, 12);
                array2[4] = this.bytes2int(array, 16);
                array2[5] = this.bytes2int(array, 20);
                array2[6] = this.bytes2int(array, 24);
                array2[7] = this.bytes2int(array, 28);
            }
            else {
                array2[0] = this.bytes2int(array, 0);
                array2[1] = this.bytes2int(array, 4);
                array2[2] = this.bytes2int(array, 8);
                array2[3] = this.bytes2int(array, 12);
                array2[4] = this.bytes2int(array, 16);
                array2[5] = this.bytes2int(array, 20);
                array2[6] = ~array2[4];
                array2[7] = ~array2[5];
            }
            this._keyis128 = false;
        }
        else {
            this._keyis128 = true;
            array2[0] = this.bytes2int(array, 0);
            array2[1] = this.bytes2int(array, 4);
            array2[2] = this.bytes2int(array, 8);
            array2[3] = this.bytes2int(array, 12);
            array2[6] = (array2[7] = 0);
            array2[4] = (array2[5] = 0);
        }
        for (int i = 0; i < 4; ++i) {
            array3[i] = (array2[i] ^ array2[i + 4]);
        }
        this.camelliaF2(array3, CamelliaLightEngine.SIGMA, 0);
        for (int j = 0; j < 4; ++j) {
            array3[j] ^= array2[j];
        }
        this.camelliaF2(array3, CamelliaLightEngine.SIGMA, 4);
        if (this._keyis128) {
            if (b) {
                final int[] kw = this.kw;
                kw[0] = array2[0];
                kw[1] = array2[1];
                kw[2] = array2[2];
                kw[3] = array2[3];
                roldq(15, array2, 0, this.subkey, 4);
                roldq(30, array2, 0, this.subkey, 12);
                roldq(15, array2, 0, array5, 0);
                final int[] subkey = this.subkey;
                subkey[18] = array5[2];
                subkey[19] = array5[3];
                roldq(17, array2, 0, this.ke, 4);
                roldq(17, array2, 0, this.subkey, 24);
                roldq(17, array2, 0, this.subkey, 32);
                final int[] subkey2 = this.subkey;
                subkey2[0] = array3[0];
                subkey2[1] = array3[1];
                subkey2[2] = array3[2];
                subkey2[3] = array3[3];
                roldq(15, array3, 0, subkey2, 8);
                roldq(15, array3, 0, this.ke, 0);
                roldq(15, array3, 0, array5, 0);
                final int[] subkey3 = this.subkey;
                subkey3[16] = array5[0];
                subkey3[17] = array5[1];
                roldq(15, array3, 0, subkey3, 20);
                roldqo32(34, array3, 0, this.subkey, 28);
                roldq(17, array3, 0, this.kw, 4);
                return;
            }
            final int[] kw2 = this.kw;
            kw2[4] = array2[0];
            kw2[5] = array2[1];
            kw2[6] = array2[2];
            kw2[7] = array2[3];
            decroldq(15, array2, 0, this.subkey, 28);
            decroldq(30, array2, 0, this.subkey, 20);
            decroldq(15, array2, 0, array5, 0);
            final int[] subkey4 = this.subkey;
            subkey4[16] = array5[0];
            subkey4[17] = array5[1];
            decroldq(17, array2, 0, this.ke, 0);
            decroldq(17, array2, 0, this.subkey, 8);
            decroldq(17, array2, 0, this.subkey, 0);
            final int[] subkey5 = this.subkey;
            subkey5[34] = array3[0];
            subkey5[35] = array3[1];
            subkey5[32] = array3[2];
            subkey5[33] = array3[3];
            decroldq(15, array3, 0, subkey5, 24);
            decroldq(15, array3, 0, this.ke, 4);
            decroldq(15, array3, 0, array5, 0);
            final int[] subkey6 = this.subkey;
            subkey6[18] = array5[2];
            subkey6[19] = array5[3];
            decroldq(15, array3, 0, subkey6, 12);
            decroldqo32(34, array3, 0, this.subkey, 4);
            roldq(17, array3, 0, this.kw, 0);
        }
        else {
            for (int k = 0; k < 4; ++k) {
                array4[k] = (array3[k] ^ array2[k + 4]);
            }
            this.camelliaF2(array4, CamelliaLightEngine.SIGMA, 8);
            if (b) {
                final int[] kw3 = this.kw;
                kw3[0] = array2[0];
                kw3[1] = array2[1];
                kw3[2] = array2[2];
                kw3[3] = array2[3];
                roldqo32(45, array2, 0, this.subkey, 16);
                roldq(15, array2, 0, this.ke, 4);
                roldq(17, array2, 0, this.subkey, 32);
                roldqo32(34, array2, 0, this.subkey, 44);
                roldq(15, array2, 4, this.subkey, 4);
                roldq(15, array2, 4, this.ke, 0);
                roldq(30, array2, 4, this.subkey, 24);
                roldqo32(34, array2, 4, this.subkey, 36);
                roldq(15, array3, 0, this.subkey, 8);
                roldq(30, array3, 0, this.subkey, 20);
                final int[] ke = this.ke;
                ke[8] = array3[1];
                ke[9] = array3[2];
                ke[10] = array3[3];
                ke[11] = array3[0];
                roldqo32(49, array3, 0, this.subkey, 40);
                final int[] subkey7 = this.subkey;
                subkey7[0] = array4[0];
                subkey7[1] = array4[1];
                subkey7[2] = array4[2];
                subkey7[3] = array4[3];
                roldq(30, array4, 0, subkey7, 12);
                roldq(30, array4, 0, this.subkey, 28);
                roldqo32(51, array4, 0, this.kw, 4);
                return;
            }
            final int[] kw4 = this.kw;
            kw4[4] = array2[0];
            kw4[5] = array2[1];
            kw4[6] = array2[2];
            kw4[7] = array2[3];
            decroldqo32(45, array2, 0, this.subkey, 28);
            decroldq(15, array2, 0, this.ke, 4);
            decroldq(17, array2, 0, this.subkey, 12);
            decroldqo32(34, array2, 0, this.subkey, 0);
            decroldq(15, array2, 4, this.subkey, 40);
            decroldq(15, array2, 4, this.ke, 8);
            decroldq(30, array2, 4, this.subkey, 20);
            decroldqo32(34, array2, 4, this.subkey, 8);
            decroldq(15, array3, 0, this.subkey, 36);
            decroldq(30, array3, 0, this.subkey, 24);
            final int[] ke2 = this.ke;
            ke2[2] = array3[1];
            ke2[3] = array3[2];
            ke2[0] = array3[3];
            ke2[1] = array3[0];
            decroldqo32(49, array3, 0, this.subkey, 4);
            final int[] subkey8 = this.subkey;
            subkey8[46] = array4[0];
            subkey8[47] = array4[1];
            subkey8[44] = array4[2];
            subkey8[45] = array4[3];
            decroldq(30, array4, 0, subkey8, 32);
            decroldq(30, array4, 0, this.subkey, 16);
            roldqo32(51, array4, 0, this.kw, 0);
        }
    }
    
    @Override
    public String getAlgorithmName() {
        return "Camellia";
    }
    
    @Override
    public int getBlockSize() {
        return 16;
    }
    
    @Override
    public void init(final boolean b, final CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.setKey(b, ((KeyParameter)cipherParameters).getKey());
            this.initialized = true;
            return;
        }
        throw new IllegalArgumentException("only simple KeyParameter expected.");
    }
    
    @Override
    public int processBlock(final byte[] array, final int n, final byte[] array2, final int n2) throws IllegalStateException {
        if (!this.initialized) {
            throw new IllegalStateException("Camellia is not initialized");
        }
        if (n + 16 > array.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n2 + 16 > array2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this._keyis128) {
            return this.processBlock128(array, n, array2, n2);
        }
        return this.processBlock192or256(array, n, array2, n2);
    }
    
    @Override
    public void reset() {
    }
}
