package org.spongycastle.crypto.digests;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class Blake2bDigest implements ExtendedDigest
{
    private static final int BLOCK_LENGTH_BYTES = 128;
    private static final long[] blake2b_IV;
    private static final byte[][] blake2b_sigma;
    private static int rOUNDS;
    private byte[] buffer;
    private int bufferPos;
    private long[] chainValue;
    private int digestLength;
    private long f0;
    private long[] internalState;
    private byte[] key;
    private int keyLength;
    private byte[] personalization;
    private byte[] salt;
    private long t0;
    private long t1;
    
    static {
        blake2b_IV = new long[] { 7640891576956012808L, -4942790177534073029L, 4354685564936845355L, -6534734903238641935L, 5840696475078001361L, -7276294671716946913L, 2270897969802886507L, 6620516959819538809L };
        blake2b_sigma = new byte[][] { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 }, { 14, 10, 4, 8, 9, 15, 13, 6, 1, 12, 0, 2, 11, 7, 5, 3 }, { 11, 8, 12, 0, 5, 2, 15, 13, 10, 14, 3, 6, 7, 1, 9, 4 }, { 7, 9, 3, 1, 13, 12, 11, 14, 2, 6, 5, 10, 4, 0, 15, 8 }, { 9, 0, 5, 7, 2, 4, 10, 15, 14, 1, 11, 12, 6, 8, 3, 13 }, { 2, 12, 6, 10, 0, 11, 8, 3, 4, 13, 7, 5, 15, 14, 1, 9 }, { 12, 5, 1, 15, 14, 13, 4, 10, 0, 7, 6, 3, 9, 2, 8, 11 }, { 13, 11, 7, 14, 12, 1, 3, 9, 5, 0, 15, 4, 8, 6, 2, 10 }, { 6, 15, 14, 9, 11, 3, 0, 8, 12, 2, 13, 7, 1, 4, 10, 5 }, { 10, 2, 8, 4, 7, 6, 1, 5, 15, 11, 9, 14, 3, 12, 13, 0 }, { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 }, { 14, 10, 4, 8, 9, 15, 13, 6, 1, 12, 0, 2, 11, 7, 5, 3 } };
        Blake2bDigest.rOUNDS = 12;
    }
    
    public Blake2bDigest() {
        this(512);
    }
    
    public Blake2bDigest(final int n) {
        this.digestLength = 64;
        this.keyLength = 0;
        this.salt = null;
        this.personalization = null;
        this.key = null;
        this.buffer = null;
        this.bufferPos = 0;
        this.internalState = new long[16];
        this.chainValue = null;
        this.t0 = 0L;
        this.t1 = 0L;
        this.f0 = 0L;
        if (n != 160 && n != 256 && n != 384 && n != 512) {
            throw new IllegalArgumentException("Blake2b digest restricted to one of [160, 256, 384, 512]");
        }
        this.buffer = new byte[128];
        this.keyLength = 0;
        this.digestLength = n / 8;
        this.init();
    }
    
    public Blake2bDigest(final Blake2bDigest blake2bDigest) {
        this.digestLength = 64;
        this.keyLength = 0;
        this.salt = null;
        this.personalization = null;
        this.key = null;
        this.buffer = null;
        this.bufferPos = 0;
        this.internalState = new long[16];
        this.chainValue = null;
        this.t0 = 0L;
        this.t1 = 0L;
        this.f0 = 0L;
        this.bufferPos = blake2bDigest.bufferPos;
        this.buffer = Arrays.clone(blake2bDigest.buffer);
        this.keyLength = blake2bDigest.keyLength;
        this.key = Arrays.clone(blake2bDigest.key);
        this.digestLength = blake2bDigest.digestLength;
        this.chainValue = Arrays.clone(blake2bDigest.chainValue);
        this.personalization = Arrays.clone(blake2bDigest.personalization);
        this.salt = Arrays.clone(blake2bDigest.salt);
        this.t0 = blake2bDigest.t0;
        this.t1 = blake2bDigest.t1;
        this.f0 = blake2bDigest.f0;
    }
    
    public Blake2bDigest(final byte[] array) {
        this.digestLength = 64;
        this.keyLength = 0;
        this.salt = null;
        this.personalization = null;
        this.key = null;
        this.buffer = null;
        this.bufferPos = 0;
        this.internalState = new long[16];
        this.chainValue = null;
        this.t0 = 0L;
        this.t1 = 0L;
        this.f0 = 0L;
        this.buffer = new byte[128];
        if (array != null) {
            System.arraycopy(array, 0, this.key = new byte[array.length], 0, array.length);
            if (array.length > 64) {
                throw new IllegalArgumentException("Keys > 64 are not supported");
            }
            this.keyLength = array.length;
            System.arraycopy(array, 0, this.buffer, 0, array.length);
            this.bufferPos = 128;
        }
        this.digestLength = 64;
        this.init();
    }
    
    public Blake2bDigest(final byte[] array, final int digestLength, byte[] array2, final byte[] array3) {
        this.digestLength = 64;
        this.keyLength = 0;
        this.salt = null;
        this.personalization = null;
        this.key = null;
        this.buffer = null;
        this.bufferPos = 0;
        this.internalState = new long[16];
        this.chainValue = null;
        this.t0 = 0L;
        this.t1 = 0L;
        this.f0 = 0L;
        this.buffer = new byte[128];
        if (digestLength >= 1 && digestLength <= 64) {
            this.digestLength = digestLength;
            if (array2 != null) {
                if (array2.length != 16) {
                    throw new IllegalArgumentException("salt length must be exactly 16 bytes");
                }
                System.arraycopy(array2, 0, this.salt = new byte[16], 0, array2.length);
            }
            if (array3 != null) {
                if (array3.length != 16) {
                    throw new IllegalArgumentException("personalization length must be exactly 16 bytes");
                }
                array2 = new byte[16];
                System.arraycopy(array3, 0, this.personalization = array2, 0, array3.length);
            }
            if (array != null) {
                array2 = new byte[array.length];
                System.arraycopy(array, 0, this.key = array2, 0, array.length);
                if (array.length > 64) {
                    throw new IllegalArgumentException("Keys > 64 are not supported");
                }
                this.keyLength = array.length;
                System.arraycopy(array, 0, this.buffer, 0, array.length);
                this.bufferPos = 128;
            }
            this.init();
            return;
        }
        throw new IllegalArgumentException("Invalid digest length (required: 1 - 64)");
    }
    
    private void G(final long n, final long n2, final int n3, final int n4, final int n5, final int n6) {
        final long[] internalState = this.internalState;
        internalState[n3] = internalState[n3] + internalState[n4] + n;
        internalState[n6] = this.rotr64(internalState[n6] ^ internalState[n3], 32);
        final long[] internalState2 = this.internalState;
        internalState2[n5] += internalState2[n6];
        internalState2[n4] = this.rotr64(internalState2[n4] ^ internalState2[n5], 24);
        final long[] internalState3 = this.internalState;
        internalState3[n3] = internalState3[n3] + internalState3[n4] + n2;
        internalState3[n6] = this.rotr64(internalState3[n6] ^ internalState3[n3], 16);
        final long[] internalState4 = this.internalState;
        internalState4[n5] += internalState4[n6];
        internalState4[n4] = this.rotr64(internalState4[n4] ^ internalState4[n5], 63);
    }
    
    private final long bytes2long(final byte[] array, final int n) {
        return ((long)array[n + 7] & 0xFFL) << 56 | (((long)array[n] & 0xFFL) | ((long)array[n + 1] & 0xFFL) << 8 | ((long)array[n + 2] & 0xFFL) << 16 | ((long)array[n + 3] & 0xFFL) << 24 | ((long)array[n + 4] & 0xFFL) << 32 | ((long)array[n + 5] & 0xFFL) << 40 | ((long)array[n + 6] & 0xFFL) << 48);
    }
    
    private void compress(final byte[] array, int n) {
        this.initializeInternalState();
        final long[] array2 = new long[16];
        final int n2 = 0;
        for (int i = 0; i < 16; ++i) {
            array2[i] = this.bytes2long(array, i * 8 + n);
        }
        n = 0;
        int n3;
        while (true) {
            n3 = n2;
            if (n >= Blake2bDigest.rOUNDS) {
                break;
            }
            final byte[][] blake2b_sigma = Blake2bDigest.blake2b_sigma;
            this.G(array2[blake2b_sigma[n][0]], array2[blake2b_sigma[n][1]], 0, 4, 8, 12);
            final byte[][] blake2b_sigma2 = Blake2bDigest.blake2b_sigma;
            this.G(array2[blake2b_sigma2[n][2]], array2[blake2b_sigma2[n][3]], 1, 5, 9, 13);
            final byte[][] blake2b_sigma3 = Blake2bDigest.blake2b_sigma;
            this.G(array2[blake2b_sigma3[n][4]], array2[blake2b_sigma3[n][5]], 2, 6, 10, 14);
            final byte[][] blake2b_sigma4 = Blake2bDigest.blake2b_sigma;
            this.G(array2[blake2b_sigma4[n][6]], array2[blake2b_sigma4[n][7]], 3, 7, 11, 15);
            final byte[][] blake2b_sigma5 = Blake2bDigest.blake2b_sigma;
            this.G(array2[blake2b_sigma5[n][8]], array2[blake2b_sigma5[n][9]], 0, 5, 10, 15);
            final byte[][] blake2b_sigma6 = Blake2bDigest.blake2b_sigma;
            this.G(array2[blake2b_sigma6[n][10]], array2[blake2b_sigma6[n][11]], 1, 6, 11, 12);
            final byte[][] blake2b_sigma7 = Blake2bDigest.blake2b_sigma;
            this.G(array2[blake2b_sigma7[n][12]], array2[blake2b_sigma7[n][13]], 2, 7, 8, 13);
            final byte[][] blake2b_sigma8 = Blake2bDigest.blake2b_sigma;
            this.G(array2[blake2b_sigma8[n][14]], array2[blake2b_sigma8[n][15]], 3, 4, 9, 14);
            ++n;
        }
        while (true) {
            final long[] chainValue = this.chainValue;
            if (n3 >= chainValue.length) {
                break;
            }
            final long n4 = chainValue[n3];
            final long[] internalState = this.internalState;
            chainValue[n3] = (n4 ^ internalState[n3] ^ internalState[n3 + 8]);
            ++n3;
        }
    }
    
    private void init() {
        if (this.chainValue == null) {
            final long[] chainValue = new long[8];
            this.chainValue = chainValue;
            final long[] blake2b_IV = Blake2bDigest.blake2b_IV;
            chainValue[0] = (blake2b_IV[0] ^ (long)(this.digestLength | this.keyLength << 8 | 0x1010000));
            chainValue[1] = blake2b_IV[1];
            chainValue[2] = blake2b_IV[2];
            chainValue[3] = blake2b_IV[3];
            chainValue[4] = blake2b_IV[4];
            chainValue[5] = blake2b_IV[5];
            final byte[] salt = this.salt;
            if (salt != null) {
                chainValue[4] ^= this.bytes2long(salt, 0);
                final long[] chainValue2 = this.chainValue;
                chainValue2[5] ^= this.bytes2long(this.salt, 8);
            }
            final long[] chainValue3 = this.chainValue;
            final long[] blake2b_IV2 = Blake2bDigest.blake2b_IV;
            chainValue3[6] = blake2b_IV2[6];
            chainValue3[7] = blake2b_IV2[7];
            final byte[] personalization = this.personalization;
            if (personalization != null) {
                chainValue3[6] ^= this.bytes2long(personalization, 0);
                final long[] chainValue4 = this.chainValue;
                chainValue4[7] ^= this.bytes2long(this.personalization, 8);
            }
        }
    }
    
    private void initializeInternalState() {
        final long[] chainValue = this.chainValue;
        System.arraycopy(chainValue, 0, this.internalState, 0, chainValue.length);
        System.arraycopy(Blake2bDigest.blake2b_IV, 0, this.internalState, this.chainValue.length, 4);
        final long[] internalState = this.internalState;
        final long t0 = this.t0;
        final long[] blake2b_IV = Blake2bDigest.blake2b_IV;
        internalState[12] = (t0 ^ blake2b_IV[4]);
        internalState[13] = (this.t1 ^ blake2b_IV[5]);
        internalState[14] = (this.f0 ^ blake2b_IV[6]);
        internalState[15] = blake2b_IV[7];
    }
    
    private final byte[] long2bytes(final long n) {
        return new byte[] { (byte)n, (byte)(n >> 8), (byte)(n >> 16), (byte)(n >> 24), (byte)(n >> 32), (byte)(n >> 40), (byte)(n >> 48), (byte)(n >> 56) };
    }
    
    private long rotr64(final long n, final int n2) {
        return n << 64 - n2 | n >>> n2;
    }
    
    public void clearKey() {
        final byte[] key = this.key;
        if (key != null) {
            Arrays.fill(key, (byte)0);
            Arrays.fill(this.buffer, (byte)0);
        }
    }
    
    public void clearSalt() {
        final byte[] salt = this.salt;
        if (salt != null) {
            Arrays.fill(salt, (byte)0);
        }
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        this.f0 = -1L;
        final long t0 = this.t0;
        final long n2 = this.bufferPos;
        final long t2 = t0 + n2;
        this.t0 = t2;
        if (t2 < 0L && n2 > -t2) {
            ++this.t1;
        }
        this.compress(this.buffer, 0);
        Arrays.fill(this.buffer, (byte)0);
        Arrays.fill(this.internalState, 0L);
        int n3 = 0;
        while (true) {
            final long[] chainValue = this.chainValue;
            if (n3 >= chainValue.length) {
                break;
            }
            final int n4 = n3 * 8;
            if (n4 >= this.digestLength) {
                break;
            }
            final byte[] long2bytes = this.long2bytes(chainValue[n3]);
            final int digestLength = this.digestLength;
            if (n4 < digestLength - 8) {
                System.arraycopy(long2bytes, 0, array, n4 + n, 8);
            }
            else {
                System.arraycopy(long2bytes, 0, array, n + n4, digestLength - n4);
            }
            ++n3;
        }
        Arrays.fill(this.chainValue, 0L);
        this.reset();
        return this.digestLength;
    }
    
    @Override
    public String getAlgorithmName() {
        return "Blake2b";
    }
    
    @Override
    public int getByteLength() {
        return 128;
    }
    
    @Override
    public int getDigestSize() {
        return this.digestLength;
    }
    
    @Override
    public void reset() {
        this.bufferPos = 0;
        this.f0 = 0L;
        this.t0 = 0L;
        this.t1 = 0L;
        this.chainValue = null;
        Arrays.fill(this.buffer, (byte)0);
        final byte[] key = this.key;
        if (key != null) {
            System.arraycopy(key, 0, this.buffer, 0, key.length);
            this.bufferPos = 128;
        }
        this.init();
    }
    
    @Override
    public void update(final byte b) {
        final int bufferPos = this.bufferPos;
        if (128 - bufferPos == 0) {
            final long t0 = this.t0 + 128L;
            this.t0 = t0;
            if (t0 == 0L) {
                ++this.t1;
            }
            this.compress(this.buffer, 0);
            Arrays.fill(this.buffer, (byte)0);
            this.buffer[0] = b;
            this.bufferPos = 1;
            return;
        }
        this.buffer[bufferPos] = b;
        this.bufferPos = bufferPos + 1;
    }
    
    @Override
    public void update(final byte[] array, int i, int n) {
        if (array != null) {
            if (n == 0) {
                return;
            }
            final int bufferPos = this.bufferPos;
            int n2;
            if (bufferPos != 0) {
                n2 = 128 - bufferPos;
                if (n2 >= n) {
                    System.arraycopy(array, i, this.buffer, bufferPos, n);
                    this.bufferPos += n;
                    return;
                }
                System.arraycopy(array, i, this.buffer, bufferPos, n2);
                final long t0 = this.t0 + 128L;
                this.t0 = t0;
                if (t0 == 0L) {
                    ++this.t1;
                }
                this.compress(this.buffer, 0);
                this.bufferPos = 0;
                Arrays.fill(this.buffer, (byte)0);
            }
            else {
                n2 = 0;
            }
            long t2;
            for (n += i, i += n2; i < n - 128; i += 128) {
                t2 = this.t0 + 128L;
                this.t0 = t2;
                if (t2 == 0L) {
                    ++this.t1;
                }
                this.compress(array, i);
            }
            final byte[] buffer = this.buffer;
            n -= i;
            System.arraycopy(array, i, buffer, 0, n);
            this.bufferPos += n;
        }
    }
}
