package org.spongycastle.crypto.digests;

import org.spongycastle.util.*;

public class SHA1Digest extends GeneralDigest implements EncodableDigest
{
    private static final int DIGEST_LENGTH = 20;
    private static final int Y1 = 1518500249;
    private static final int Y2 = 1859775393;
    private static final int Y3 = -1894007588;
    private static final int Y4 = -899497514;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int H5;
    private int[] X;
    private int xOff;
    
    public SHA1Digest() {
        this.X = new int[80];
        this.reset();
    }
    
    public SHA1Digest(final SHA1Digest sha1Digest) {
        super(sha1Digest);
        this.X = new int[80];
        this.copyIn(sha1Digest);
    }
    
    public SHA1Digest(final byte[] array) {
        super(array);
        this.X = new int[80];
        this.H1 = Pack.bigEndianToInt(array, 16);
        this.H2 = Pack.bigEndianToInt(array, 20);
        this.H3 = Pack.bigEndianToInt(array, 24);
        this.H4 = Pack.bigEndianToInt(array, 28);
        this.H5 = Pack.bigEndianToInt(array, 32);
        this.xOff = Pack.bigEndianToInt(array, 36);
        for (int i = 0; i != this.xOff; ++i) {
            this.X[i] = Pack.bigEndianToInt(array, i * 4 + 40);
        }
    }
    
    private void copyIn(final SHA1Digest sha1Digest) {
        this.H1 = sha1Digest.H1;
        this.H2 = sha1Digest.H2;
        this.H3 = sha1Digest.H3;
        this.H4 = sha1Digest.H4;
        this.H5 = sha1Digest.H5;
        final int[] x = sha1Digest.X;
        System.arraycopy(x, 0, this.X, 0, x.length);
        this.xOff = sha1Digest.xOff;
    }
    
    private int f(final int n, final int n2, final int n3) {
        return (n & n2) | (n3 & ~n);
    }
    
    private int g(final int n, final int n2, final int n3) {
        return (n & n2) | (n & n3) | (n2 & n3);
    }
    
    private int h(final int n, final int n2, final int n3) {
        return n ^ n2 ^ n3;
    }
    
    @Override
    public Memoable copy() {
        return new SHA1Digest(this);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        this.finish();
        Pack.intToBigEndian(this.H1, array, n);
        Pack.intToBigEndian(this.H2, array, n + 4);
        Pack.intToBigEndian(this.H3, array, n + 8);
        Pack.intToBigEndian(this.H4, array, n + 12);
        Pack.intToBigEndian(this.H5, array, n + 16);
        this.reset();
        return 20;
    }
    
    @Override
    public String getAlgorithmName() {
        return "SHA-1";
    }
    
    @Override
    public int getDigestSize() {
        return 20;
    }
    
    @Override
    public byte[] getEncodedState() {
        final byte[] array = new byte[this.xOff * 4 + 40];
        super.populateState(array);
        Pack.intToBigEndian(this.H1, array, 16);
        Pack.intToBigEndian(this.H2, array, 20);
        Pack.intToBigEndian(this.H3, array, 24);
        Pack.intToBigEndian(this.H4, array, 28);
        Pack.intToBigEndian(this.H5, array, 32);
        Pack.intToBigEndian(this.xOff, array, 36);
        for (int i = 0; i != this.xOff; ++i) {
            Pack.intToBigEndian(this.X[i], array, i * 4 + 40);
        }
        return array;
    }
    
    @Override
    protected void processBlock() {
        for (int i = 16; i < 80; ++i) {
            final int[] x = this.X;
            final int n = x[i - 3] ^ x[i - 8] ^ x[i - 14] ^ x[i - 16];
            x[i] = (n << 1 | n >>> 31);
        }
        int h1 = this.H1;
        int h2 = this.H2;
        int h3 = this.H3;
        int h4 = this.H4;
        int h5 = this.H5;
        int j = 0;
        int n2 = 0;
        while (j < 4) {
            final int f = this.f(h2, h3, h4);
            final int[] x2 = this.X;
            final int n3 = n2 + 1;
            final int n4 = h5 + ((h1 << 5 | h1 >>> 27) + f + x2[n2] + 1518500249);
            final int n5 = h2 << 30 | h2 >>> 2;
            final int f2 = this.f(h1, n5, h3);
            final int[] x3 = this.X;
            final int n6 = n3 + 1;
            final int n7 = h4 + ((n4 << 5 | n4 >>> 27) + f2 + x3[n3] + 1518500249);
            final int n8 = h1 << 30 | h1 >>> 2;
            final int f3 = this.f(n4, n8, n5);
            final int[] x4 = this.X;
            final int n9 = n6 + 1;
            final int n10 = h3 + ((n7 << 5 | n7 >>> 27) + f3 + x4[n6] + 1518500249);
            h5 = (n4 << 30 | n4 >>> 2);
            final int f4 = this.f(n7, h5, n8);
            final int[] x5 = this.X;
            final int n11 = n9 + 1;
            h2 = n5 + ((n10 << 5 | n10 >>> 27) + f4 + x5[n9] + 1518500249);
            h4 = (n7 << 30 | n7 >>> 2);
            h1 = n8 + ((h2 << 5 | h2 >>> 27) + this.f(n10, h4, h5) + this.X[n11] + 1518500249);
            h3 = (n10 << 30 | n10 >>> 2);
            ++j;
            n2 = n11 + 1;
        }
        int n20;
        for (int k = 0; k < 4; ++k, n2 = n20 + 1) {
            final int h6 = this.h(h2, h3, h4);
            final int[] x6 = this.X;
            final int n12 = n2 + 1;
            final int n13 = h5 + ((h1 << 5 | h1 >>> 27) + h6 + x6[n2] + 1859775393);
            final int n14 = h2 << 30 | h2 >>> 2;
            final int h7 = this.h(h1, n14, h3);
            final int[] x7 = this.X;
            final int n15 = n12 + 1;
            final int n16 = h4 + ((n13 << 5 | n13 >>> 27) + h7 + x7[n12] + 1859775393);
            final int n17 = h1 << 30 | h1 >>> 2;
            final int h8 = this.h(n13, n17, n14);
            final int[] x8 = this.X;
            final int n18 = n15 + 1;
            final int n19 = h3 + ((n16 << 5 | n16 >>> 27) + h8 + x8[n15] + 1859775393);
            h5 = (n13 << 30 | n13 >>> 2);
            final int h9 = this.h(n16, h5, n17);
            final int[] x9 = this.X;
            n20 = n18 + 1;
            h2 = n14 + ((n19 << 5 | n19 >>> 27) + h9 + x9[n18] + 1859775393);
            h4 = (n16 << 30 | n16 >>> 2);
            h1 = n17 + ((h2 << 5 | h2 >>> 27) + this.h(n19, h4, h5) + this.X[n20] + 1859775393);
            h3 = (n19 << 30 | n19 >>> 2);
        }
        int n29;
        for (int l = 0; l < 4; ++l, n2 = n29 + 1) {
            final int g = this.g(h2, h3, h4);
            final int[] x10 = this.X;
            final int n21 = n2 + 1;
            final int n22 = h5 + ((h1 << 5 | h1 >>> 27) + g + x10[n2] - 1894007588);
            final int n23 = h2 << 30 | h2 >>> 2;
            final int g2 = this.g(h1, n23, h3);
            final int[] x11 = this.X;
            final int n24 = n21 + 1;
            final int n25 = h4 + ((n22 << 5 | n22 >>> 27) + g2 + x11[n21] - 1894007588);
            final int n26 = h1 << 30 | h1 >>> 2;
            final int g3 = this.g(n22, n26, n23);
            final int[] x12 = this.X;
            final int n27 = n24 + 1;
            final int n28 = h3 + ((n25 << 5 | n25 >>> 27) + g3 + x12[n24] - 1894007588);
            h5 = (n22 << 30 | n22 >>> 2);
            final int g4 = this.g(n25, h5, n26);
            final int[] x13 = this.X;
            n29 = n27 + 1;
            h2 = n23 + ((n28 << 5 | n28 >>> 27) + g4 + x13[n27] - 1894007588);
            h4 = (n25 << 30 | n25 >>> 2);
            h1 = n26 + ((h2 << 5 | h2 >>> 27) + this.g(n28, h4, h5) + this.X[n29] - 1894007588);
            h3 = (n28 << 30 | n28 >>> 2);
        }
        final int n30 = 0;
        int n31 = h5;
        int n32 = n2;
        int n33 = h4;
        int n34 = h3;
        int n44;
        for (int n35 = n30; n35 <= 3; ++n35, n32 = n44 + 1) {
            final int h10 = this.h(h2, n34, n33);
            final int[] x14 = this.X;
            final int n36 = n32 + 1;
            final int n37 = n31 + ((h1 << 5 | h1 >>> 27) + h10 + x14[n32] - 899497514);
            final int n38 = h2 << 30 | h2 >>> 2;
            final int h11 = this.h(h1, n38, n34);
            final int[] x15 = this.X;
            final int n39 = n36 + 1;
            final int n40 = n33 + ((n37 << 5 | n37 >>> 27) + h11 + x15[n36] - 899497514);
            final int n41 = h1 << 30 | h1 >>> 2;
            final int h12 = this.h(n37, n41, n38);
            final int[] x16 = this.X;
            final int n42 = n39 + 1;
            final int n43 = n34 + ((n40 << 5 | n40 >>> 27) + h12 + x16[n39] - 899497514);
            n31 = (n37 << 30 | n37 >>> 2);
            final int h13 = this.h(n40, n31, n41);
            final int[] x17 = this.X;
            n44 = n42 + 1;
            h2 = n38 + ((n43 << 5 | n43 >>> 27) + h13 + x17[n42] - 899497514);
            n33 = (n40 << 30 | n40 >>> 2);
            h1 = n41 + ((h2 << 5 | h2 >>> 27) + this.h(n43, n33, n31) + this.X[n44] - 899497514);
            n34 = (n43 << 30 | n43 >>> 2);
        }
        this.H1 += h1;
        this.H2 += h2;
        this.H3 += n34;
        this.H4 += n33;
        this.H5 += n31;
        this.xOff = 0;
        for (int n45 = 0; n45 < 16; ++n45) {
            this.X[n45] = 0;
        }
    }
    
    @Override
    protected void processLength(final long n) {
        if (this.xOff > 14) {
            this.processBlock();
        }
        final int[] x = this.X;
        x[14] = (int)(n >>> 32);
        x[15] = (int)(n & -1L);
    }
    
    @Override
    protected void processWord(final byte[] array, int xOff) {
        final byte b = array[xOff];
        final int n = xOff + 1;
        xOff = array[n];
        final int n2 = n + 1;
        final byte b2 = array[n2];
        final byte b3 = array[n2 + 1];
        final int[] x = this.X;
        final int xOff2 = this.xOff;
        x[xOff2] = ((b3 & 0xFF) | (b << 24 | (xOff & 0xFF) << 16 | (b2 & 0xFF) << 8));
        xOff = xOff2 + 1;
        this.xOff = xOff;
        if (xOff == 16) {
            this.processBlock();
        }
    }
    
    @Override
    public void reset() {
        super.reset();
        this.H1 = 1732584193;
        this.H2 = -271733879;
        this.H3 = -1732584194;
        this.H4 = 271733878;
        this.H5 = -1009589776;
        this.xOff = 0;
        int n = 0;
        while (true) {
            final int[] x = this.X;
            if (n == x.length) {
                break;
            }
            x[n] = 0;
            ++n;
        }
    }
    
    @Override
    public void reset(final Memoable memoable) {
        final SHA1Digest sha1Digest = (SHA1Digest)memoable;
        super.copyIn(sha1Digest);
        this.copyIn(sha1Digest);
    }
}
