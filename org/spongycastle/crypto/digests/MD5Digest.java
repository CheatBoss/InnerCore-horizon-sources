package org.spongycastle.crypto.digests;

import org.spongycastle.util.*;

public class MD5Digest extends GeneralDigest implements EncodableDigest
{
    private static final int DIGEST_LENGTH = 16;
    private static final int S11 = 7;
    private static final int S12 = 12;
    private static final int S13 = 17;
    private static final int S14 = 22;
    private static final int S21 = 5;
    private static final int S22 = 9;
    private static final int S23 = 14;
    private static final int S24 = 20;
    private static final int S31 = 4;
    private static final int S32 = 11;
    private static final int S33 = 16;
    private static final int S34 = 23;
    private static final int S41 = 6;
    private static final int S42 = 10;
    private static final int S43 = 15;
    private static final int S44 = 21;
    private int H1;
    private int H2;
    private int H3;
    private int H4;
    private int[] X;
    private int xOff;
    
    public MD5Digest() {
        this.X = new int[16];
        this.reset();
    }
    
    public MD5Digest(final MD5Digest md5Digest) {
        super(md5Digest);
        this.X = new int[16];
        this.copyIn(md5Digest);
    }
    
    public MD5Digest(final byte[] array) {
        super(array);
        this.X = new int[16];
        this.H1 = Pack.bigEndianToInt(array, 16);
        this.H2 = Pack.bigEndianToInt(array, 20);
        this.H3 = Pack.bigEndianToInt(array, 24);
        this.H4 = Pack.bigEndianToInt(array, 28);
        this.xOff = Pack.bigEndianToInt(array, 32);
        for (int i = 0; i != this.xOff; ++i) {
            this.X[i] = Pack.bigEndianToInt(array, i * 4 + 36);
        }
    }
    
    private int F(final int n, final int n2, final int n3) {
        return (n & n2) | (n3 & ~n);
    }
    
    private int G(final int n, final int n2, final int n3) {
        return (n & n3) | (n2 & ~n3);
    }
    
    private int H(final int n, final int n2, final int n3) {
        return n ^ n2 ^ n3;
    }
    
    private int K(final int n, final int n2, final int n3) {
        return (n | ~n3) ^ n2;
    }
    
    private void copyIn(final MD5Digest md5Digest) {
        super.copyIn(md5Digest);
        this.H1 = md5Digest.H1;
        this.H2 = md5Digest.H2;
        this.H3 = md5Digest.H3;
        this.H4 = md5Digest.H4;
        final int[] x = md5Digest.X;
        System.arraycopy(x, 0, this.X, 0, x.length);
        this.xOff = md5Digest.xOff;
    }
    
    private int rotateLeft(final int n, final int n2) {
        return n << n2 | n >>> 32 - n2;
    }
    
    private void unpackWord(final int n, final byte[] array, final int n2) {
        array[n2] = (byte)n;
        array[n2 + 1] = (byte)(n >>> 8);
        array[n2 + 2] = (byte)(n >>> 16);
        array[n2 + 3] = (byte)(n >>> 24);
    }
    
    @Override
    public Memoable copy() {
        return new MD5Digest(this);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        this.finish();
        this.unpackWord(this.H1, array, n);
        this.unpackWord(this.H2, array, n + 4);
        this.unpackWord(this.H3, array, n + 8);
        this.unpackWord(this.H4, array, n + 12);
        this.reset();
        return 16;
    }
    
    @Override
    public String getAlgorithmName() {
        return "MD5";
    }
    
    @Override
    public int getDigestSize() {
        return 16;
    }
    
    @Override
    public byte[] getEncodedState() {
        final byte[] array = new byte[this.xOff * 4 + 36];
        super.populateState(array);
        Pack.intToBigEndian(this.H1, array, 16);
        Pack.intToBigEndian(this.H2, array, 20);
        Pack.intToBigEndian(this.H3, array, 24);
        Pack.intToBigEndian(this.H4, array, 28);
        Pack.intToBigEndian(this.xOff, array, 32);
        for (int i = 0; i != this.xOff; ++i) {
            Pack.intToBigEndian(this.X[i], array, i * 4 + 36);
        }
        return array;
    }
    
    @Override
    protected void processBlock() {
        final int h1 = this.H1;
        final int h2 = this.H2;
        final int h3 = this.H3;
        final int h4 = this.H4;
        final int n = this.rotateLeft(h1 + this.F(h2, h3, h4) + this.X[0] - 680876936, 7) + h2;
        final int n2 = this.rotateLeft(h4 + this.F(n, h2, h3) + this.X[1] - 389564586, 12) + n;
        final int n3 = this.rotateLeft(h3 + this.F(n2, n, h2) + this.X[2] + 606105819, 17) + n2;
        final int n4 = this.rotateLeft(h2 + this.F(n3, n2, n) + this.X[3] - 1044525330, 22) + n3;
        final int n5 = this.rotateLeft(n + this.F(n4, n3, n2) + this.X[4] - 176418897, 7) + n4;
        final int n6 = this.rotateLeft(n2 + this.F(n5, n4, n3) + this.X[5] + 1200080426, 12) + n5;
        final int n7 = this.rotateLeft(n3 + this.F(n6, n5, n4) + this.X[6] - 1473231341, 17) + n6;
        final int n8 = this.rotateLeft(n4 + this.F(n7, n6, n5) + this.X[7] - 45705983, 22) + n7;
        final int n9 = this.rotateLeft(n5 + this.F(n8, n7, n6) + this.X[8] + 1770035416, 7) + n8;
        final int n10 = this.rotateLeft(n6 + this.F(n9, n8, n7) + this.X[9] - 1958414417, 12) + n9;
        final int n11 = this.rotateLeft(n7 + this.F(n10, n9, n8) + this.X[10] - 42063, 17) + n10;
        final int n12 = this.rotateLeft(n8 + this.F(n11, n10, n9) + this.X[11] - 1990404162, 22) + n11;
        final int n13 = this.rotateLeft(n9 + this.F(n12, n11, n10) + this.X[12] + 1804603682, 7) + n12;
        final int n14 = this.rotateLeft(n10 + this.F(n13, n12, n11) + this.X[13] - 40341101, 12) + n13;
        final int n15 = this.rotateLeft(n11 + this.F(n14, n13, n12) + this.X[14] - 1502002290, 17) + n14;
        final int n16 = this.rotateLeft(n12 + this.F(n15, n14, n13) + this.X[15] + 1236535329, 22) + n15;
        final int n17 = this.rotateLeft(n13 + this.G(n16, n15, n14) + this.X[1] - 165796510, 5) + n16;
        final int n18 = this.rotateLeft(n14 + this.G(n17, n16, n15) + this.X[6] - 1069501632, 9) + n17;
        final int n19 = this.rotateLeft(n15 + this.G(n18, n17, n16) + this.X[11] + 643717713, 14) + n18;
        final int n20 = this.rotateLeft(n16 + this.G(n19, n18, n17) + this.X[0] - 373897302, 20) + n19;
        final int n21 = this.rotateLeft(n17 + this.G(n20, n19, n18) + this.X[5] - 701558691, 5) + n20;
        final int n22 = this.rotateLeft(n18 + this.G(n21, n20, n19) + this.X[10] + 38016083, 9) + n21;
        final int n23 = this.rotateLeft(n19 + this.G(n22, n21, n20) + this.X[15] - 660478335, 14) + n22;
        final int n24 = this.rotateLeft(n20 + this.G(n23, n22, n21) + this.X[4] - 405537848, 20) + n23;
        final int n25 = this.rotateLeft(n21 + this.G(n24, n23, n22) + this.X[9] + 568446438, 5) + n24;
        final int n26 = this.rotateLeft(n22 + this.G(n25, n24, n23) + this.X[14] - 1019803690, 9) + n25;
        final int n27 = this.rotateLeft(n23 + this.G(n26, n25, n24) + this.X[3] - 187363961, 14) + n26;
        final int n28 = this.rotateLeft(n24 + this.G(n27, n26, n25) + this.X[8] + 1163531501, 20) + n27;
        final int n29 = this.rotateLeft(n25 + this.G(n28, n27, n26) + this.X[13] - 1444681467, 5) + n28;
        final int n30 = this.rotateLeft(n26 + this.G(n29, n28, n27) + this.X[2] - 51403784, 9) + n29;
        final int n31 = this.rotateLeft(n27 + this.G(n30, n29, n28) + this.X[7] + 1735328473, 14) + n30;
        final int n32 = this.rotateLeft(n28 + this.G(n31, n30, n29) + this.X[12] - 1926607734, 20) + n31;
        final int n33 = this.rotateLeft(n29 + this.H(n32, n31, n30) + this.X[5] - 378558, 4) + n32;
        final int n34 = this.rotateLeft(n30 + this.H(n33, n32, n31) + this.X[8] - 2022574463, 11) + n33;
        final int n35 = this.rotateLeft(n31 + this.H(n34, n33, n32) + this.X[11] + 1839030562, 16) + n34;
        final int n36 = this.rotateLeft(n32 + this.H(n35, n34, n33) + this.X[14] - 35309556, 23) + n35;
        final int n37 = this.rotateLeft(n33 + this.H(n36, n35, n34) + this.X[1] - 1530992060, 4) + n36;
        final int n38 = this.rotateLeft(n34 + this.H(n37, n36, n35) + this.X[4] + 1272893353, 11) + n37;
        final int n39 = this.rotateLeft(n35 + this.H(n38, n37, n36) + this.X[7] - 155497632, 16) + n38;
        final int n40 = this.rotateLeft(n36 + this.H(n39, n38, n37) + this.X[10] - 1094730640, 23) + n39;
        final int n41 = this.rotateLeft(n37 + this.H(n40, n39, n38) + this.X[13] + 681279174, 4) + n40;
        final int n42 = this.rotateLeft(n38 + this.H(n41, n40, n39) + this.X[0] - 358537222, 11) + n41;
        final int n43 = this.rotateLeft(n39 + this.H(n42, n41, n40) + this.X[3] - 722521979, 16) + n42;
        final int n44 = this.rotateLeft(n40 + this.H(n43, n42, n41) + this.X[6] + 76029189, 23) + n43;
        final int n45 = this.rotateLeft(n41 + this.H(n44, n43, n42) + this.X[9] - 640364487, 4) + n44;
        final int n46 = this.rotateLeft(n42 + this.H(n45, n44, n43) + this.X[12] - 421815835, 11) + n45;
        final int n47 = this.rotateLeft(n43 + this.H(n46, n45, n44) + this.X[15] + 530742520, 16) + n46;
        final int n48 = this.rotateLeft(n44 + this.H(n47, n46, n45) + this.X[2] - 995338651, 23) + n47;
        final int n49 = this.rotateLeft(n45 + this.K(n48, n47, n46) + this.X[0] - 198630844, 6) + n48;
        final int n50 = this.rotateLeft(n46 + this.K(n49, n48, n47) + this.X[7] + 1126891415, 10) + n49;
        final int n51 = this.rotateLeft(n47 + this.K(n50, n49, n48) + this.X[14] - 1416354905, 15) + n50;
        final int n52 = this.rotateLeft(n48 + this.K(n51, n50, n49) + this.X[5] - 57434055, 21) + n51;
        final int n53 = this.rotateLeft(n49 + this.K(n52, n51, n50) + this.X[12] + 1700485571, 6) + n52;
        final int n54 = this.rotateLeft(n50 + this.K(n53, n52, n51) + this.X[3] - 1894986606, 10) + n53;
        final int n55 = this.rotateLeft(n51 + this.K(n54, n53, n52) + this.X[10] - 1051523, 15) + n54;
        final int n56 = this.rotateLeft(n52 + this.K(n55, n54, n53) + this.X[1] - 2054922799, 21) + n55;
        final int n57 = this.rotateLeft(n53 + this.K(n56, n55, n54) + this.X[8] + 1873313359, 6) + n56;
        final int n58 = this.rotateLeft(n54 + this.K(n57, n56, n55) + this.X[15] - 30611744, 10) + n57;
        final int n59 = this.rotateLeft(n55 + this.K(n58, n57, n56) + this.X[6] - 1560198380, 15) + n58;
        final int n60 = this.rotateLeft(n56 + this.K(n59, n58, n57) + this.X[13] + 1309151649, 21) + n59;
        final int n61 = this.rotateLeft(n57 + this.K(n60, n59, n58) + this.X[4] - 145523070, 6) + n60;
        final int n62 = this.rotateLeft(n58 + this.K(n61, n60, n59) + this.X[11] - 1120210379, 10) + n61;
        final int n63 = this.rotateLeft(n59 + this.K(n62, n61, n60) + this.X[2] + 718787259, 15) + n62;
        final int rotateLeft = this.rotateLeft(n60 + this.K(n63, n62, n61) + this.X[9] - 343485551, 21);
        this.H1 += n61;
        this.H2 += rotateLeft + n63;
        this.H3 += n63;
        this.H4 += n62;
        this.xOff = 0;
        int n64 = 0;
        while (true) {
            final int[] x = this.X;
            if (n64 == x.length) {
                break;
            }
            x[n64] = 0;
            ++n64;
        }
    }
    
    @Override
    protected void processLength(final long n) {
        if (this.xOff > 14) {
            this.processBlock();
        }
        final int[] x = this.X;
        x[14] = (int)(n & -1L);
        x[15] = (int)(n >>> 32);
    }
    
    @Override
    protected void processWord(final byte[] array, final int n) {
        final int[] x = this.X;
        final int xOff = this.xOff;
        final int xOff2 = xOff + 1;
        this.xOff = xOff2;
        x[xOff] = ((array[n + 3] & 0xFF) << 24 | ((array[n] & 0xFF) | (array[n + 1] & 0xFF) << 8 | (array[n + 2] & 0xFF) << 16));
        if (xOff2 == 16) {
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
        this.copyIn((MD5Digest)memoable);
    }
}
