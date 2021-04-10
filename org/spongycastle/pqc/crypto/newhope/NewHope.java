package org.spongycastle.pqc.crypto.newhope;

import java.security.*;
import org.spongycastle.crypto.digests.*;

class NewHope
{
    public static final int AGREEMENT_SIZE = 32;
    public static final int POLY_SIZE = 1024;
    public static final int SENDA_BYTES = 1824;
    public static final int SENDB_BYTES = 2048;
    private static final boolean STATISTICAL_TEST = false;
    
    static void decodeA(final short[] array, final byte[] array2, final byte[] array3) {
        Poly.fromBytes(array, array3);
        System.arraycopy(array3, 1792, array2, 0, 32);
    }
    
    static void decodeB(final short[] array, final short[] array2, final byte[] array3) {
        Poly.fromBytes(array, array3);
        for (int i = 0; i < 256; ++i) {
            final int n = i * 4;
            final int n2 = array3[i + 1792] & 0xFF;
            array2[n + 0] = (short)(n2 & 0x3);
            array2[n + 1] = (short)(n2 >>> 2 & 0x3);
            array2[n + 2] = (short)(n2 >>> 4 & 0x3);
            array2[n + 3] = (short)(n2 >>> 6);
        }
    }
    
    static void encodeA(final byte[] array, final short[] array2, final byte[] array3) {
        Poly.toBytes(array, array2);
        System.arraycopy(array3, 0, array, 1792, 32);
    }
    
    static void encodeB(final byte[] array, final short[] array2, final short[] array3) {
        Poly.toBytes(array, array2);
        for (int i = 0; i < 256; ++i) {
            final int n = i * 4;
            array[i + 1792] = (byte)(array3[n + 3] << 6 | (array3[n] | array3[n + 1] << 2 | array3[n + 2] << 4));
        }
    }
    
    static void generateA(final short[] array, final byte[] array2) {
        Poly.uniform(array, array2);
    }
    
    public static void keygen(final SecureRandom secureRandom, final byte[] array, short[] array2) {
        final byte[] array3 = new byte[32];
        secureRandom.nextBytes(array3);
        final short[] array4 = new short[1024];
        generateA(array4, array3);
        final byte[] array5 = new byte[32];
        secureRandom.nextBytes(array5);
        Poly.getNoise(array2, array5, (byte)0);
        Poly.toNTT(array2);
        final short[] array6 = new short[1024];
        Poly.getNoise(array6, array5, (byte)1);
        Poly.toNTT(array6);
        final short[] array7 = new short[1024];
        Poly.pointWise(array4, array2, array7);
        array2 = new short[1024];
        Poly.add(array7, array6, array2);
        encodeA(array, array2, array3);
    }
    
    static void sha3(final byte[] array) {
        final SHA3Digest sha3Digest = new SHA3Digest(256);
        sha3Digest.update(array, 0, 32);
        sha3Digest.doFinal(array, 0);
    }
    
    public static void sharedA(final byte[] array, final short[] array2, final byte[] array3) {
        final short[] array4 = new short[1024];
        final short[] array5 = new short[1024];
        decodeB(array4, array5, array3);
        final short[] array6 = new short[1024];
        Poly.pointWise(array2, array4, array6);
        Poly.fromNTT(array6);
        ErrorCorrection.rec(array, array6, array5);
        sha3(array);
    }
    
    public static void sharedB(final SecureRandom secureRandom, final byte[] array, final byte[] array2, byte[] array3) {
        final short[] array4 = new short[1024];
        final byte[] array5 = new byte[32];
        decodeA(array4, array5, array3);
        final short[] array6 = new short[1024];
        generateA(array6, array5);
        array3 = new byte[32];
        secureRandom.nextBytes(array3);
        final short[] array7 = new short[1024];
        Poly.getNoise(array7, array3, (byte)0);
        Poly.toNTT(array7);
        final short[] array8 = new short[1024];
        Poly.getNoise(array8, array3, (byte)1);
        Poly.toNTT(array8);
        final short[] array9 = new short[1024];
        Poly.pointWise(array6, array7, array9);
        Poly.add(array9, array8, array9);
        final short[] array10 = new short[1024];
        Poly.pointWise(array4, array7, array10);
        Poly.fromNTT(array10);
        final short[] array11 = new short[1024];
        Poly.getNoise(array11, array3, (byte)2);
        Poly.add(array10, array11, array10);
        final short[] array12 = new short[1024];
        ErrorCorrection.helpRec(array12, array10, array3, (byte)3);
        encodeB(array2, array9, array12);
        ErrorCorrection.rec(array, array10, array12);
        sha3(array);
    }
}
