package org.spongycastle.crypto.digests;

import org.spongycastle.util.*;

public class SM3Digest extends GeneralDigest
{
    private static final int BLOCK_SIZE = 16;
    private static final int DIGEST_LENGTH = 32;
    private static final int[] T;
    private int[] V;
    private int[] W;
    private int[] W1;
    private int[] inwords;
    private int xOff;
    
    static {
        T = new int[64];
        int n = 0;
        int i;
        while (true) {
            i = 16;
            if (n >= 16) {
                break;
            }
            SM3Digest.T[n] = (2043430169 >>> 32 - n | 2043430169 << n);
            ++n;
        }
        while (i < 64) {
            final int n2 = i % 32;
            SM3Digest.T[i] = (2055708042 << n2 | 2055708042 >>> 32 - n2);
            ++i;
        }
    }
    
    public SM3Digest() {
        this.V = new int[8];
        this.inwords = new int[16];
        this.W = new int[68];
        this.W1 = new int[64];
        this.reset();
    }
    
    public SM3Digest(final SM3Digest sm3Digest) {
        super(sm3Digest);
        this.V = new int[8];
        this.inwords = new int[16];
        this.W = new int[68];
        this.W1 = new int[64];
        this.copyIn(sm3Digest);
    }
    
    private int FF0(final int n, final int n2, final int n3) {
        return n ^ n2 ^ n3;
    }
    
    private int FF1(final int n, final int n2, final int n3) {
        return (n & n2) | (n & n3) | (n2 & n3);
    }
    
    private int GG0(final int n, final int n2, final int n3) {
        return n ^ n2 ^ n3;
    }
    
    private int GG1(final int n, final int n2, final int n3) {
        return (n & n2) | (n3 & ~n);
    }
    
    private int P0(final int n) {
        return (n >>> 15 | n << 17) ^ ((n << 9 | n >>> 23) ^ n);
    }
    
    private int P1(final int n) {
        return (n >>> 9 | n << 23) ^ ((n << 15 | n >>> 17) ^ n);
    }
    
    private void copyIn(final SM3Digest sm3Digest) {
        final int[] v = sm3Digest.V;
        final int[] v2 = this.V;
        System.arraycopy(v, 0, v2, 0, v2.length);
        final int[] inwords = sm3Digest.inwords;
        final int[] inwords2 = this.inwords;
        System.arraycopy(inwords, 0, inwords2, 0, inwords2.length);
        this.xOff = sm3Digest.xOff;
    }
    
    @Override
    public Memoable copy() {
        return new SM3Digest(this);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        this.finish();
        Pack.intToBigEndian(this.V[0], array, n + 0);
        Pack.intToBigEndian(this.V[1], array, n + 4);
        Pack.intToBigEndian(this.V[2], array, n + 8);
        Pack.intToBigEndian(this.V[3], array, n + 12);
        Pack.intToBigEndian(this.V[4], array, n + 16);
        Pack.intToBigEndian(this.V[5], array, n + 20);
        Pack.intToBigEndian(this.V[6], array, n + 24);
        Pack.intToBigEndian(this.V[7], array, n + 28);
        this.reset();
        return 32;
    }
    
    @Override
    public String getAlgorithmName() {
        return "SM3";
    }
    
    @Override
    public int getDigestSize() {
        return 32;
    }
    
    @Override
    protected void processBlock() {
        for (int i = 0; i < 16; ++i) {
            this.W[i] = this.inwords[i];
        }
        for (int j = 16; j < 68; ++j) {
            final int[] w = this.W;
            final int n = w[j - 3];
            final int n2 = w[j - 13];
            w[j] = ((n2 << 7 | n2 >>> 25) ^ this.P1((n << 15 | n >>> 17) ^ (w[j - 16] ^ w[j - 9])) ^ this.W[j - 6]);
        }
        for (int k = 0; k < 64; ++k) {
            final int[] w2 = this.W1;
            final int[] w3 = this.W;
            w2[k] = (w3[k + 4] ^ w3[k]);
        }
        final int[] v = this.V;
        int n3 = v[0];
        int n4 = v[1];
        int n5 = v[2];
        int n6 = v[3];
        int n7 = v[4];
        int n8 = v[5];
        int n9 = v[6];
        int n10 = v[7];
        int n11;
        int n13;
        int ff0;
        int n14;
        int p0;
        int n15;
        int n16;
        for (int l = 0; l < 16; ++l, n10 = n9, n9 = (n8 << 19 | n8 >>> 13), n15 = n5, n8 = n7, n7 = p0, n5 = (n4 << 9 | n4 >>> 23), n16 = ff0 + n6 + (n13 ^ n11) + n14, n4 = n3, n3 = n16, n6 = n15) {
            n11 = (n3 << 12 | n3 >>> 20);
            final int n12 = n11 + n7 + SM3Digest.T[l];
            n13 = (n12 << 7 | n12 >>> 25);
            ff0 = this.FF0(n3, n4, n5);
            n14 = this.W1[l];
            p0 = this.P0(this.GG0(n7, n8, n9) + n10 + n13 + this.W[l]);
        }
        final int n17 = 16;
        int n18 = n10;
        int n19 = n17;
        int n20;
        while (true) {
            n20 = n6;
            if (n19 >= 64) {
                break;
            }
            final int n21 = n3 << 12 | n3 >>> 20;
            final int n22 = n21 + n7 + SM3Digest.T[n19];
            final int n23 = n22 << 7 | n22 >>> 25;
            final int ff2 = this.FF1(n3, n4, n5);
            final int n24 = this.W1[n19];
            final int p2 = this.P0(this.GG1(n7, n8, n9) + n18 + n23 + this.W[n19]);
            ++n19;
            n18 = n9;
            n9 = (n8 << 19 | n8 >>> 13);
            n6 = n5;
            final int n25 = ff2 + n20 + (n21 ^ n23) + n24;
            n5 = (n4 >>> 23 | n4 << 9);
            final int n26 = p2;
            n4 = n3;
            n3 = n25;
            n8 = n7;
            n7 = n26;
        }
        final int[] v2 = this.V;
        v2[0] ^= n3;
        v2[1] ^= n4;
        v2[2] ^= n5;
        v2[3] ^= n20;
        v2[4] ^= n7;
        v2[5] ^= n8;
        v2[6] ^= n9;
        v2[7] ^= n18;
        this.xOff = 0;
    }
    
    @Override
    protected void processLength(final long n) {
        final int xOff = this.xOff;
        if (xOff > 14) {
            this.inwords[xOff] = 0;
            this.xOff = xOff + 1;
            this.processBlock();
        }
        int xOff2;
        while (true) {
            xOff2 = this.xOff;
            if (xOff2 >= 14) {
                break;
            }
            this.inwords[xOff2] = 0;
            this.xOff = xOff2 + 1;
        }
        final int[] inwords = this.inwords;
        final int xOff3 = xOff2 + 1;
        this.xOff = xOff3;
        inwords[xOff2] = (int)(n >>> 32);
        this.xOff = xOff3 + 1;
        inwords[xOff3] = (int)n;
    }
    
    @Override
    protected void processWord(final byte[] array, int xOff) {
        final byte b = array[xOff];
        final int n = xOff + 1;
        xOff = array[n];
        final int n2 = n + 1;
        final byte b2 = array[n2];
        final byte b3 = array[n2 + 1];
        final int[] inwords = this.inwords;
        final int xOff2 = this.xOff;
        inwords[xOff2] = ((b3 & 0xFF) | ((b & 0xFF) << 24 | (xOff & 0xFF) << 16 | (b2 & 0xFF) << 8));
        xOff = xOff2 + 1;
        this.xOff = xOff;
        if (xOff >= 16) {
            this.processBlock();
        }
    }
    
    @Override
    public void reset() {
        super.reset();
        final int[] v = this.V;
        v[0] = 1937774191;
        v[1] = 1226093241;
        v[2] = 388252375;
        v[3] = -628488704;
        v[4] = -1452330820;
        v[5] = 372324522;
        v[6] = -477237683;
        v[7] = -1325724082;
        this.xOff = 0;
    }
    
    @Override
    public void reset(final Memoable memoable) {
        final SM3Digest sm3Digest = (SM3Digest)memoable;
        super.copyIn(sm3Digest);
        this.copyIn(sm3Digest);
    }
}
