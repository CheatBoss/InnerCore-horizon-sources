package org.spongycastle.crypto.digests;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public final class WhirlpoolDigest implements ExtendedDigest, Memoable
{
    private static final int BITCOUNT_ARRAY_SIZE = 32;
    private static final int BYTE_LENGTH = 64;
    private static final long[] C0;
    private static final long[] C1;
    private static final long[] C2;
    private static final long[] C3;
    private static final long[] C4;
    private static final long[] C5;
    private static final long[] C6;
    private static final long[] C7;
    private static final int DIGEST_LENGTH_BYTES = 64;
    private static final short[] EIGHT;
    private static final int REDUCTION_POLYNOMIAL = 285;
    private static final int ROUNDS = 10;
    private static final int[] SBOX;
    private long[] _K;
    private long[] _L;
    private short[] _bitCount;
    private long[] _block;
    private byte[] _buffer;
    private int _bufferPos;
    private long[] _hash;
    private final long[] _rc;
    private long[] _state;
    
    static {
        SBOX = new int[] { 24, 35, 198, 232, 135, 184, 1, 79, 54, 166, 210, 245, 121, 111, 145, 82, 96, 188, 155, 142, 163, 12, 123, 53, 29, 224, 215, 194, 46, 75, 254, 87, 21, 119, 55, 229, 159, 240, 74, 218, 88, 201, 41, 10, 177, 160, 107, 133, 189, 93, 16, 244, 203, 62, 5, 103, 228, 39, 65, 139, 167, 125, 149, 216, 251, 238, 124, 102, 221, 23, 71, 158, 202, 45, 191, 7, 173, 90, 131, 51, 99, 2, 170, 113, 200, 25, 73, 217, 242, 227, 91, 136, 154, 38, 50, 176, 233, 15, 213, 128, 190, 205, 52, 72, 255, 122, 144, 95, 32, 104, 26, 174, 180, 84, 147, 34, 100, 241, 115, 18, 64, 8, 195, 236, 219, 161, 141, 61, 151, 0, 207, 43, 118, 130, 214, 27, 181, 175, 106, 80, 69, 243, 48, 239, 63, 85, 162, 234, 101, 186, 47, 192, 222, 28, 253, 77, 146, 117, 6, 138, 178, 230, 14, 31, 98, 212, 168, 150, 249, 197, 37, 89, 132, 114, 57, 76, 94, 120, 56, 140, 209, 165, 226, 97, 179, 33, 156, 30, 67, 199, 252, 4, 81, 153, 109, 13, 250, 223, 126, 36, 59, 171, 206, 17, 143, 78, 183, 235, 60, 129, 148, 247, 185, 19, 44, 211, 231, 110, 196, 3, 86, 68, 127, 169, 42, 187, 193, 83, 220, 11, 157, 108, 49, 116, 246, 70, 172, 137, 20, 225, 22, 58, 105, 9, 112, 182, 208, 237, 204, 66, 152, 164, 40, 92, 248, 134 };
        C0 = new long[256];
        C1 = new long[256];
        C2 = new long[256];
        C3 = new long[256];
        C4 = new long[256];
        C5 = new long[256];
        C6 = new long[256];
        C7 = new long[256];
        (EIGHT = new short[32])[31] = 8;
    }
    
    public WhirlpoolDigest() {
        this._rc = new long[11];
        this._buffer = new byte[64];
        this._bufferPos = 0;
        this._bitCount = new short[32];
        this._hash = new long[8];
        this._K = new long[8];
        this._L = new long[8];
        this._block = new long[8];
        this._state = new long[8];
        for (int i = 0; i < 256; ++i) {
            final int n = WhirlpoolDigest.SBOX[i];
            final int maskWithReductionPolynomial = this.maskWithReductionPolynomial(n << 1);
            final int maskWithReductionPolynomial2 = this.maskWithReductionPolynomial(maskWithReductionPolynomial << 1);
            final int n2 = maskWithReductionPolynomial2 ^ n;
            final int maskWithReductionPolynomial3 = this.maskWithReductionPolynomial(maskWithReductionPolynomial2 << 1);
            final int n3 = maskWithReductionPolynomial3 ^ n;
            WhirlpoolDigest.C0[i] = this.packIntoLong(n, n, maskWithReductionPolynomial2, n, maskWithReductionPolynomial3, n2, maskWithReductionPolynomial, n3);
            WhirlpoolDigest.C1[i] = this.packIntoLong(n3, n, n, maskWithReductionPolynomial2, n, maskWithReductionPolynomial3, n2, maskWithReductionPolynomial);
            WhirlpoolDigest.C2[i] = this.packIntoLong(maskWithReductionPolynomial, n3, n, n, maskWithReductionPolynomial2, n, maskWithReductionPolynomial3, n2);
            WhirlpoolDigest.C3[i] = this.packIntoLong(n2, maskWithReductionPolynomial, n3, n, n, maskWithReductionPolynomial2, n, maskWithReductionPolynomial3);
            WhirlpoolDigest.C4[i] = this.packIntoLong(maskWithReductionPolynomial3, n2, maskWithReductionPolynomial, n3, n, n, maskWithReductionPolynomial2, n);
            WhirlpoolDigest.C5[i] = this.packIntoLong(n, maskWithReductionPolynomial3, n2, maskWithReductionPolynomial, n3, n, n, maskWithReductionPolynomial2);
            WhirlpoolDigest.C6[i] = this.packIntoLong(maskWithReductionPolynomial2, n, maskWithReductionPolynomial3, n2, maskWithReductionPolynomial, n3, n, n);
            WhirlpoolDigest.C7[i] = this.packIntoLong(n, maskWithReductionPolynomial2, n, maskWithReductionPolynomial3, n2, maskWithReductionPolynomial, n3, n);
        }
        this._rc[0] = 0L;
        for (int j = 1; j <= 10; ++j) {
            final int n4 = (j - 1) * 8;
            this._rc[j] = ((WhirlpoolDigest.C0[n4] & 0xFF00000000000000L) ^ (WhirlpoolDigest.C1[n4 + 1] & 0xFF000000000000L) ^ (WhirlpoolDigest.C2[n4 + 2] & 0xFF0000000000L) ^ (WhirlpoolDigest.C3[n4 + 3] & 0xFF00000000L) ^ (WhirlpoolDigest.C4[n4 + 4] & 0xFF000000L) ^ (WhirlpoolDigest.C5[n4 + 5] & 0xFF0000L) ^ (WhirlpoolDigest.C6[n4 + 6] & 0xFF00L) ^ (WhirlpoolDigest.C7[n4 + 7] & 0xFFL));
        }
    }
    
    public WhirlpoolDigest(final WhirlpoolDigest whirlpoolDigest) {
        this._rc = new long[11];
        this._buffer = new byte[64];
        this._bufferPos = 0;
        this._bitCount = new short[32];
        this._hash = new long[8];
        this._K = new long[8];
        this._L = new long[8];
        this._block = new long[8];
        this._state = new long[8];
        this.reset(whirlpoolDigest);
    }
    
    private long bytesToLongFromBuffer(final byte[] array, final int n) {
        return ((long)array[n + 7] & 0xFFL) | (((long)array[n + 0] & 0xFFL) << 56 | ((long)array[n + 1] & 0xFFL) << 48 | ((long)array[n + 2] & 0xFFL) << 40 | ((long)array[n + 3] & 0xFFL) << 32 | ((long)array[n + 4] & 0xFFL) << 24 | ((long)array[n + 5] & 0xFFL) << 16 | ((long)array[n + 6] & 0xFFL) << 8);
    }
    
    private void convertLongToByteArray(final long n, final byte[] array, final int n2) {
        for (int i = 0; i < 8; ++i) {
            array[n2 + i] = (byte)(n >> 56 - i * 8 & 0xFFL);
        }
    }
    
    private byte[] copyBitLength() {
        final byte[] array = new byte[32];
        for (int i = 0; i < 32; ++i) {
            array[i] = (byte)(this._bitCount[i] & 0xFF);
        }
        return array;
    }
    
    private void finish() {
        final byte[] copyBitLength = this.copyBitLength();
        final byte[] buffer = this._buffer;
        final int bufferPos = this._bufferPos;
        final int bufferPos2 = bufferPos + 1;
        this._bufferPos = bufferPos2;
        buffer[bufferPos] |= (byte)128;
        if (bufferPos2 == buffer.length) {
            this.processFilledBuffer(buffer, 0);
        }
        if (this._bufferPos > 32) {
            while (this._bufferPos != 0) {
                this.update((byte)0);
            }
        }
        while (this._bufferPos <= 32) {
            this.update((byte)0);
        }
        System.arraycopy(copyBitLength, 0, this._buffer, 32, copyBitLength.length);
        this.processFilledBuffer(this._buffer, 0);
    }
    
    private void increment() {
        int i = this._bitCount.length - 1;
        int n = 0;
        while (i >= 0) {
            final short[] bitCount = this._bitCount;
            final int n2 = (bitCount[i] & 0xFF) + WhirlpoolDigest.EIGHT[i] + n;
            n = n2 >>> 8;
            bitCount[i] = (short)(n2 & 0xFF);
            --i;
        }
    }
    
    private int maskWithReductionPolynomial(final int n) {
        int n2 = n;
        if (n >= 256L) {
            n2 = (n ^ 0x11D);
        }
        return n2;
    }
    
    private long packIntoLong(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8) {
        return (long)n2 << 48 ^ (long)n << 56 ^ (long)n3 << 40 ^ (long)n4 << 32 ^ (long)n5 << 24 ^ (long)n6 << 16 ^ (long)n7 << 8 ^ (long)n8;
    }
    
    private void processFilledBuffer(final byte[] array, int i) {
        for (i = 0; i < this._state.length; ++i) {
            this._block[i] = this.bytesToLongFromBuffer(this._buffer, i * 8);
        }
        this.processBlock();
        this._bufferPos = 0;
        Arrays.fill(this._buffer, (byte)0);
    }
    
    @Override
    public Memoable copy() {
        return new WhirlpoolDigest(this);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        this.finish();
        for (int i = 0; i < 8; ++i) {
            this.convertLongToByteArray(this._hash[i], array, i * 8 + n);
        }
        this.reset();
        return this.getDigestSize();
    }
    
    @Override
    public String getAlgorithmName() {
        return "Whirlpool";
    }
    
    @Override
    public int getByteLength() {
        return 64;
    }
    
    @Override
    public int getDigestSize() {
        return 64;
    }
    
    protected void processBlock() {
        for (int i = 0; i < 8; ++i) {
            this._state[i] = (this._block[i] ^ (this._K[i] = this._hash[i]));
        }
        for (int j = 1; j <= 10; ++j) {
            for (int k = 0; k < 8; ++k) {
                final long[] l = this._L;
                l[k] = 0L;
                final long n = l[k];
                final long[] c0 = WhirlpoolDigest.C0;
                final long[] m = this._K;
                l[k] = (c0[(int)(m[k + 0 & 0x7] >>> 56) & 0xFF] ^ n);
                l[k] ^= WhirlpoolDigest.C1[(int)(m[k - 1 & 0x7] >>> 48) & 0xFF];
                l[k] ^= WhirlpoolDigest.C2[(int)(m[k - 2 & 0x7] >>> 40) & 0xFF];
                l[k] ^= WhirlpoolDigest.C3[(int)(m[k - 3 & 0x7] >>> 32) & 0xFF];
                l[k] ^= WhirlpoolDigest.C4[(int)(m[k - 4 & 0x7] >>> 24) & 0xFF];
                l[k] ^= WhirlpoolDigest.C5[(int)(m[k - 5 & 0x7] >>> 16) & 0xFF];
                l[k] ^= WhirlpoolDigest.C6[(int)(m[k - 6 & 0x7] >>> 8) & 0xFF];
                l[k] ^= WhirlpoolDigest.C7[(int)m[k - 7 & 0x7] & 0xFF];
            }
            final long[] l2 = this._L;
            final long[] k2 = this._K;
            System.arraycopy(l2, 0, k2, 0, k2.length);
            final long[] k3 = this._K;
            k3[0] ^= this._rc[j];
            for (int n2 = 0; n2 < 8; ++n2) {
                final long[] l3 = this._L;
                l3[n2] = this._K[n2];
                final long n3 = l3[n2];
                final long[] c2 = WhirlpoolDigest.C0;
                final long[] state = this._state;
                l3[n2] = (n3 ^ c2[(int)(state[n2 + 0 & 0x7] >>> 56) & 0xFF]);
                l3[n2] ^= WhirlpoolDigest.C1[(int)(state[n2 - 1 & 0x7] >>> 48) & 0xFF];
                l3[n2] ^= WhirlpoolDigest.C2[(int)(state[n2 - 2 & 0x7] >>> 40) & 0xFF];
                l3[n2] ^= WhirlpoolDigest.C3[(int)(state[n2 - 3 & 0x7] >>> 32) & 0xFF];
                l3[n2] ^= WhirlpoolDigest.C4[(int)(state[n2 - 4 & 0x7] >>> 24) & 0xFF];
                l3[n2] ^= WhirlpoolDigest.C5[(int)(state[n2 - 5 & 0x7] >>> 16) & 0xFF];
                l3[n2] ^= WhirlpoolDigest.C6[(int)(state[n2 - 6 & 0x7] >>> 8) & 0xFF];
                l3[n2] ^= WhirlpoolDigest.C7[(int)state[n2 - 7 & 0x7] & 0xFF];
            }
            final long[] l4 = this._L;
            final long[] state2 = this._state;
            System.arraycopy(l4, 0, state2, 0, state2.length);
        }
        for (int n4 = 0; n4 < 8; ++n4) {
            final long[] hash = this._hash;
            hash[n4] ^= (this._state[n4] ^ this._block[n4]);
        }
    }
    
    @Override
    public void reset() {
        this._bufferPos = 0;
        Arrays.fill(this._bitCount, (short)0);
        Arrays.fill(this._buffer, (byte)0);
        Arrays.fill(this._hash, 0L);
        Arrays.fill(this._K, 0L);
        Arrays.fill(this._L, 0L);
        Arrays.fill(this._block, 0L);
        Arrays.fill(this._state, 0L);
    }
    
    @Override
    public void reset(final Memoable memoable) {
        final WhirlpoolDigest whirlpoolDigest = (WhirlpoolDigest)memoable;
        final long[] rc = whirlpoolDigest._rc;
        final long[] rc2 = this._rc;
        System.arraycopy(rc, 0, rc2, 0, rc2.length);
        final byte[] buffer = whirlpoolDigest._buffer;
        final byte[] buffer2 = this._buffer;
        System.arraycopy(buffer, 0, buffer2, 0, buffer2.length);
        this._bufferPos = whirlpoolDigest._bufferPos;
        final short[] bitCount = whirlpoolDigest._bitCount;
        final short[] bitCount2 = this._bitCount;
        System.arraycopy(bitCount, 0, bitCount2, 0, bitCount2.length);
        final long[] hash = whirlpoolDigest._hash;
        final long[] hash2 = this._hash;
        System.arraycopy(hash, 0, hash2, 0, hash2.length);
        final long[] k = whirlpoolDigest._K;
        final long[] i = this._K;
        System.arraycopy(k, 0, i, 0, i.length);
        final long[] l = whirlpoolDigest._L;
        final long[] j = this._L;
        System.arraycopy(l, 0, j, 0, j.length);
        final long[] block = whirlpoolDigest._block;
        final long[] block2 = this._block;
        System.arraycopy(block, 0, block2, 0, block2.length);
        final long[] state = whirlpoolDigest._state;
        final long[] state2 = this._state;
        System.arraycopy(state, 0, state2, 0, state2.length);
    }
    
    @Override
    public void update(final byte b) {
        final byte[] buffer = this._buffer;
        final int bufferPos = this._bufferPos;
        buffer[bufferPos] = b;
        final int bufferPos2 = bufferPos + 1;
        this._bufferPos = bufferPos2;
        if (bufferPos2 == buffer.length) {
            this.processFilledBuffer(buffer, 0);
        }
        this.increment();
    }
    
    @Override
    public void update(final byte[] array, int n, int i) {
        while (i > 0) {
            this.update(array[n]);
            ++n;
            --i;
        }
    }
}
