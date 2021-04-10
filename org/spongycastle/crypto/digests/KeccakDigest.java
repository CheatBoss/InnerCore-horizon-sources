package org.spongycastle.crypto.digests;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class KeccakDigest implements ExtendedDigest
{
    private static int[] KeccakRhoOffsets;
    private static long[] KeccakRoundConstants;
    protected int bitsInQueue;
    protected byte[] dataQueue;
    protected int fixedOutputLength;
    protected int rate;
    protected boolean squeezing;
    protected long[] state;
    
    static {
        KeccakDigest.KeccakRoundConstants = keccakInitializeRoundConstants();
        KeccakDigest.KeccakRhoOffsets = keccakInitializeRhoOffsets();
    }
    
    public KeccakDigest() {
        this(288);
    }
    
    public KeccakDigest(final int n) {
        this.state = new long[25];
        this.dataQueue = new byte[192];
        this.init(n);
    }
    
    public KeccakDigest(final KeccakDigest keccakDigest) {
        final long[] state = new long[25];
        this.state = state;
        this.dataQueue = new byte[192];
        final long[] state2 = keccakDigest.state;
        System.arraycopy(state2, 0, state, 0, state2.length);
        final byte[] dataQueue = keccakDigest.dataQueue;
        System.arraycopy(dataQueue, 0, this.dataQueue, 0, dataQueue.length);
        this.rate = keccakDigest.rate;
        this.bitsInQueue = keccakDigest.bitsInQueue;
        this.fixedOutputLength = keccakDigest.fixedOutputLength;
        this.squeezing = keccakDigest.squeezing;
    }
    
    private void KeccakAbsorb(final byte[] array, int i) {
        final int rate = this.rate;
        final int n = 0;
        int n2 = i;
        long[] state;
        for (i = n; i < rate >> 6; ++i) {
            state = this.state;
            state[i] ^= Pack.littleEndianToLong(array, n2);
            n2 += 8;
        }
        this.KeccakPermutation();
    }
    
    private void KeccakExtract() {
        Pack.longToLittleEndian(this.state, 0, this.rate >> 6, this.dataQueue, 0);
    }
    
    private void KeccakPermutation() {
        for (int i = 0; i < 24; ++i) {
            theta(this.state);
            rho(this.state);
            pi(this.state);
            chi(this.state);
            iota(this.state, i);
        }
    }
    
    private static boolean LFSR86540(final byte[] array) {
        final boolean b = (array[0] & 0x1) != 0x0;
        if ((array[0] & 0x80) != 0x0) {
            array[0] = (byte)(array[0] << 1 ^ 0x71);
            return b;
        }
        array[0] <<= 1;
        return b;
    }
    
    private static void chi(final long[] array) {
        for (int i = 0; i < 25; i += 5) {
            final int n = i + 0;
            final long n2 = array[n];
            final int n3 = i + 1;
            final long n4 = array[n3];
            final int n5 = i + 2;
            final long n6 = array[n5];
            final long n7 = array[n3];
            final long n8 = array[n5];
            final int n9 = i + 3;
            final long n10 = array[n9];
            final long n11 = array[n5];
            final long n12 = array[n9];
            final int n13 = i + 4;
            final long n14 = array[n13];
            final long n15 = array[n9];
            final long n16 = array[n13];
            final long n17 = array[n];
            final long n18 = array[n13];
            final long n19 = array[n];
            final long n20 = array[n3];
            array[n] = (n2 ^ (~n4 & n6));
            array[n3] = (n7 ^ (~n8 & n10));
            array[n5] = (n11 ^ (~n12 & n14));
            array[n9] = (n15 ^ (~n16 & n17));
            array[n13] = (n18 ^ (~n19 & n20));
        }
    }
    
    private void init(final int n) {
        if (n != 128 && n != 224 && n != 256 && n != 288 && n != 384 && n != 512) {
            throw new IllegalArgumentException("bitLength must be one of 128, 224, 256, 288, 384, or 512.");
        }
        this.initSponge(1600 - (n << 1));
    }
    
    private void initSponge(final int rate) {
        if (rate > 0 && rate < 1600 && rate % 64 == 0) {
            this.rate = rate;
            int n = 0;
            while (true) {
                final long[] state = this.state;
                if (n >= state.length) {
                    break;
                }
                state[n] = 0L;
                ++n;
            }
            Arrays.fill(this.dataQueue, (byte)0);
            this.bitsInQueue = 0;
            this.squeezing = false;
            this.fixedOutputLength = (1600 - rate) / 2;
            return;
        }
        throw new IllegalStateException("invalid rate value");
    }
    
    private static void iota(final long[] array, final int n) {
        array[0] ^= KeccakDigest.KeccakRoundConstants[n];
    }
    
    private static int[] keccakInitializeRhoOffsets() {
        final int[] array = new int[25];
        array[0] = 0;
        int n = 0;
        int n2 = 0;
        int n3 = 1;
        while (true) {
            final int n4 = n3;
            if (n2 >= 24) {
                break;
            }
            final int n5 = n2 + 1;
            array[n4 % 5 + n % 5 * 5] = (n2 + 2) * n5 / 2 % 64;
            n3 = (n4 * 0 + n * 1) % 5;
            n = (n4 * 2 + n * 3) % 5;
            n2 = n5;
        }
        return array;
    }
    
    private static long[] keccakInitializeRoundConstants() {
        final long[] array = new long[24];
        for (int i = 0; i < 24; ++i) {
            array[i] = 0L;
            for (int j = 0; j < 7; ++j) {
                if (LFSR86540(new byte[] { 1 })) {
                    array[i] ^= 1L << (1 << j) - 1;
                }
            }
        }
        return array;
    }
    
    private static long leftRotate(final long n, final int n2) {
        return n >>> -n2 | n << n2;
    }
    
    private void padAndSwitchToSqueezingPhase() {
        final byte[] dataQueue = this.dataQueue;
        final int bitsInQueue = this.bitsInQueue;
        final int n = bitsInQueue >> 3;
        dataQueue[n] |= (byte)(1L << (bitsInQueue & 0x7));
        final int bitsInQueue2 = bitsInQueue + 1;
        this.bitsInQueue = bitsInQueue2;
        final int rate = this.rate;
        int i = 0;
        if (bitsInQueue2 == rate) {
            this.KeccakAbsorb(dataQueue, 0);
            this.bitsInQueue = 0;
        }
        final int bitsInQueue3 = this.bitsInQueue;
        final int n2 = bitsInQueue3 >> 6;
        final int n3 = bitsInQueue3 & 0x3F;
        int n4 = 0;
        while (i < n2) {
            final long[] state = this.state;
            state[i] ^= Pack.littleEndianToLong(this.dataQueue, n4);
            n4 += 8;
            ++i;
        }
        if (n3 > 0) {
            final long[] state2 = this.state;
            state2[n2] ^= ((1L << n3) - 1L & Pack.littleEndianToLong(this.dataQueue, n4));
        }
        final long[] state3 = this.state;
        final int n5 = this.rate - 1 >> 6;
        state3[n5] ^= Long.MIN_VALUE;
        this.KeccakPermutation();
        this.KeccakExtract();
        this.bitsInQueue = this.rate;
        this.squeezing = true;
    }
    
    private static void pi(final long[] array) {
        final long n = array[1];
        array[1] = array[6];
        array[6] = array[9];
        array[9] = array[22];
        array[22] = array[14];
        array[14] = array[20];
        array[20] = array[2];
        array[2] = array[12];
        array[12] = array[13];
        array[13] = array[19];
        array[19] = array[23];
        array[23] = array[15];
        array[15] = array[4];
        array[4] = array[24];
        array[24] = array[21];
        array[21] = array[8];
        array[8] = array[16];
        array[16] = array[5];
        array[5] = array[3];
        array[3] = array[18];
        array[18] = array[17];
        array[17] = array[11];
        array[11] = array[7];
        array[7] = array[10];
        array[10] = n;
    }
    
    private static void rho(final long[] array) {
        for (int i = 1; i < 25; ++i) {
            array[i] = leftRotate(array[i], KeccakDigest.KeccakRhoOffsets[i]);
        }
    }
    
    private static void theta(final long[] array) {
        final long n = array[0] ^ array[5] ^ array[10] ^ array[15] ^ array[20];
        final long n2 = array[1] ^ array[6] ^ array[11] ^ array[16] ^ array[21];
        final long n3 = array[2] ^ array[7] ^ array[12] ^ array[17] ^ array[22];
        final long n4 = array[3] ^ array[8] ^ array[13] ^ array[18] ^ array[23];
        final long n5 = array[4] ^ array[9] ^ array[14] ^ array[19] ^ array[24];
        final long n6 = leftRotate(n2, 1) ^ n5;
        array[0] ^= n6;
        array[5] ^= n6;
        array[10] ^= n6;
        array[15] ^= n6;
        array[20] ^= n6;
        final long n7 = leftRotate(n3, 1) ^ n;
        array[1] ^= n7;
        array[6] ^= n7;
        array[11] ^= n7;
        array[16] ^= n7;
        array[21] ^= n7;
        final long n8 = leftRotate(n4, 1) ^ n2;
        array[2] ^= n8;
        array[7] ^= n8;
        array[12] ^= n8;
        array[17] ^= n8;
        array[22] ^= n8;
        final long n9 = leftRotate(n5, 1) ^ n3;
        array[3] ^= n9;
        array[8] ^= n9;
        array[13] ^= n9;
        array[18] ^= n9;
        array[23] ^= n9;
        final long n10 = leftRotate(n, 1) ^ n4;
        array[4] ^= n10;
        array[9] ^= n10;
        array[14] ^= n10;
        array[19] ^= n10;
        array[24] ^= n10;
    }
    
    protected void absorb(final byte[] array, final int n, final int n2) {
        final int bitsInQueue = this.bitsInQueue;
        if (bitsInQueue % 8 != 0) {
            throw new IllegalStateException("attempt to absorb with odd length queue");
        }
        if (!this.squeezing) {
            final int n3 = this.rate >> 3;
            int n4 = bitsInQueue >> 3;
            int i = 0;
            while (i < n2) {
                if (n4 == 0) {
                    final int n5 = n2 - n3;
                    if (i <= n5) {
                        int n6;
                        do {
                            this.KeccakAbsorb(array, n + i);
                            n6 = i + n3;
                        } while ((i = n6) <= n5);
                        i = n6;
                        continue;
                    }
                }
                final int min = Math.min(n3 - n4, n2 - i);
                System.arraycopy(array, n + i, this.dataQueue, n4, min);
                final int n7 = n4 + min;
                final int n8 = i += min;
                if ((n4 = n7) == n3) {
                    this.KeccakAbsorb(this.dataQueue, 0);
                    n4 = 0;
                    i = n8;
                }
            }
            this.bitsInQueue = n4 << 3;
            return;
        }
        throw new IllegalStateException("attempt to absorb while squeezing");
    }
    
    protected void absorbBits(final int n, final int n2) {
        if (n2 < 1 || n2 > 7) {
            throw new IllegalArgumentException("'bits' must be in the range 1 to 7");
        }
        final int bitsInQueue = this.bitsInQueue;
        if (bitsInQueue % 8 != 0) {
            throw new IllegalStateException("attempt to absorb with odd length queue");
        }
        if (!this.squeezing) {
            this.dataQueue[bitsInQueue >> 3] = (byte)(n & (1 << n2) - 1);
            this.bitsInQueue = bitsInQueue + n2;
            return;
        }
        throw new IllegalStateException("attempt to absorb while squeezing");
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        this.squeeze(array, n, this.fixedOutputLength);
        this.reset();
        return this.getDigestSize();
    }
    
    protected int doFinal(final byte[] array, final int n, final byte b, final int n2) {
        if (n2 > 0) {
            this.absorbBits(b, n2);
        }
        this.squeeze(array, n, this.fixedOutputLength);
        this.reset();
        return this.getDigestSize();
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Keccak-");
        sb.append(this.fixedOutputLength);
        return sb.toString();
    }
    
    @Override
    public int getByteLength() {
        return this.rate / 8;
    }
    
    @Override
    public int getDigestSize() {
        return this.fixedOutputLength / 8;
    }
    
    @Override
    public void reset() {
        this.init(this.fixedOutputLength);
    }
    
    protected void squeeze(final byte[] array, final int n, final long n2) {
        if (!this.squeezing) {
            this.padAndSwitchToSqueezingPhase();
        }
        long n3 = 0L;
        if (n2 % 8L == 0L) {
            while (n3 < n2) {
                if (this.bitsInQueue == 0) {
                    this.KeccakPermutation();
                    this.KeccakExtract();
                    this.bitsInQueue = this.rate;
                }
                final int n4 = (int)Math.min(this.bitsInQueue, n2 - n3);
                System.arraycopy(this.dataQueue, (this.rate - this.bitsInQueue) / 8, array, (int)(n3 / 8L) + n, n4 / 8);
                this.bitsInQueue -= n4;
                n3 += n4;
            }
            return;
        }
        throw new IllegalStateException("outputLength not a multiple of 8");
    }
    
    @Override
    public void update(final byte b) {
        this.absorb(new byte[] { b }, 0, 1);
    }
    
    @Override
    public void update(final byte[] array, final int n, final int n2) {
        this.absorb(array, n, n2);
    }
}
