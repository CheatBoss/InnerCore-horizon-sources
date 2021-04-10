package org.spongycastle.crypto.digests;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public abstract class LongDigest implements ExtendedDigest, EncodableDigest, Memoable
{
    private static final int BYTE_LENGTH = 128;
    static final long[] K;
    protected long H1;
    protected long H2;
    protected long H3;
    protected long H4;
    protected long H5;
    protected long H6;
    protected long H7;
    protected long H8;
    private long[] W;
    private long byteCount1;
    private long byteCount2;
    private int wOff;
    private byte[] xBuf;
    private int xBufOff;
    
    static {
        K = new long[] { 4794697086780616226L, 8158064640168781261L, -5349999486874862801L, -1606136188198331460L, 4131703408338449720L, 6480981068601479193L, -7908458776815382629L, -6116909921290321640L, -2880145864133508542L, 1334009975649890238L, 2608012711638119052L, 6128411473006802146L, 8268148722764581231L, -9160688886553864527L, -7215885187991268811L, -4495734319001033068L, -1973867731355612462L, -1171420211273849373L, 1135362057144423861L, 2597628984639134821L, 3308224258029322869L, 5365058923640841347L, 6679025012923562964L, 8573033837759648693L, -7476448914759557205L, -6327057829258317296L, -5763719355590565569L, -4658551843659510044L, -4116276920077217854L, -3051310485924567259L, 489312712824947311L, 1452737877330783856L, 2861767655752347644L, 3322285676063803686L, 5560940570517711597L, 5996557281743188959L, 7280758554555802590L, 8532644243296465576L, -9096487096722542874L, -7894198246740708037L, -6719396339535248540L, -6333637450476146687L, -4446306890439682159L, -4076793802049405392L, -3345356375505022440L, -2983346525034927856L, -860691631967231958L, 1182934255886127544L, 1847814050463011016L, 2177327727835720531L, 2830643537854262169L, 3796741975233480872L, 4115178125766777443L, 5681478168544905931L, 6601373596472566643L, 7507060721942968483L, 8399075790359081724L, 8693463985226723168L, -8878714635349349518L, -8302665154208450068L, -8016688836872298968L, -6606660893046293015L, -4685533653050689259L, -4147400797238176981L, -3880063495543823972L, -3348786107499101689L, -1523767162380948706L, -757361751448694408L, 500013540394364858L, 748580250866718886L, 1242879168328830382L, 1977374033974150939L, 2944078676154940804L, 3659926193048069267L, 4368137639120453308L, 4836135668995329356L, 5532061633213252278L, 6448918945643986474L, 6902733635092675308L, 7801388544844847127L };
    }
    
    protected LongDigest() {
        this.xBuf = new byte[8];
        this.W = new long[80];
        this.xBufOff = 0;
        this.reset();
    }
    
    protected LongDigest(final LongDigest longDigest) {
        this.xBuf = new byte[8];
        this.W = new long[80];
        this.copyIn(longDigest);
    }
    
    private long Ch(final long n, final long n2, final long n3) {
        return (~n & n3) ^ (n2 & n);
    }
    
    private long Maj(final long n, final long n2, final long n3) {
        return (n & n3) ^ (n & n2) ^ (n2 & n3);
    }
    
    private long Sigma0(final long n) {
        return n >>> 7 ^ ((n << 63 | n >>> 1) ^ (n << 56 | n >>> 8));
    }
    
    private long Sigma1(final long n) {
        return n >>> 6 ^ ((n << 45 | n >>> 19) ^ (n << 3 | n >>> 61));
    }
    
    private long Sum0(final long n) {
        return (n >>> 39 | n << 25) ^ ((n << 36 | n >>> 28) ^ (n << 30 | n >>> 34));
    }
    
    private long Sum1(final long n) {
        return (n >>> 41 | n << 23) ^ ((n << 50 | n >>> 14) ^ (n << 46 | n >>> 18));
    }
    
    private void adjustByteCounts() {
        final long byteCount1 = this.byteCount1;
        if (byteCount1 > 2305843009213693951L) {
            this.byteCount2 += byteCount1 >>> 61;
            this.byteCount1 = (byteCount1 & 0x1FFFFFFFFFFFFFFFL);
        }
    }
    
    protected void copyIn(final LongDigest longDigest) {
        final byte[] xBuf = longDigest.xBuf;
        System.arraycopy(xBuf, 0, this.xBuf, 0, xBuf.length);
        this.xBufOff = longDigest.xBufOff;
        this.byteCount1 = longDigest.byteCount1;
        this.byteCount2 = longDigest.byteCount2;
        this.H1 = longDigest.H1;
        this.H2 = longDigest.H2;
        this.H3 = longDigest.H3;
        this.H4 = longDigest.H4;
        this.H5 = longDigest.H5;
        this.H6 = longDigest.H6;
        this.H7 = longDigest.H7;
        this.H8 = longDigest.H8;
        final long[] w = longDigest.W;
        System.arraycopy(w, 0, this.W, 0, w.length);
        this.wOff = longDigest.wOff;
    }
    
    public void finish() {
        this.adjustByteCounts();
        final long byteCount1 = this.byteCount1;
        final long byteCount2 = this.byteCount2;
        byte b = -128;
        while (true) {
            this.update(b);
            if (this.xBufOff == 0) {
                break;
            }
            b = 0;
        }
        this.processLength(byteCount1 << 3, byteCount2);
        this.processBlock();
    }
    
    @Override
    public int getByteLength() {
        return 128;
    }
    
    protected int getEncodedStateSize() {
        return this.wOff * 8 + 96;
    }
    
    protected void populateState(final byte[] array) {
        final byte[] xBuf = this.xBuf;
        final int xBufOff = this.xBufOff;
        int i = 0;
        System.arraycopy(xBuf, 0, array, 0, xBufOff);
        Pack.intToBigEndian(this.xBufOff, array, 8);
        Pack.longToBigEndian(this.byteCount1, array, 12);
        Pack.longToBigEndian(this.byteCount2, array, 20);
        Pack.longToBigEndian(this.H1, array, 28);
        Pack.longToBigEndian(this.H2, array, 36);
        Pack.longToBigEndian(this.H3, array, 44);
        Pack.longToBigEndian(this.H4, array, 52);
        Pack.longToBigEndian(this.H5, array, 60);
        Pack.longToBigEndian(this.H6, array, 68);
        Pack.longToBigEndian(this.H7, array, 76);
        Pack.longToBigEndian(this.H8, array, 84);
        Pack.intToBigEndian(this.wOff, array, 92);
        while (i < this.wOff) {
            Pack.longToBigEndian(this.W[i], array, i * 8 + 96);
            ++i;
        }
    }
    
    protected void processBlock() {
        this.adjustByteCounts();
        for (int i = 16; i <= 79; ++i) {
            final long[] w = this.W;
            final long sigma1 = this.Sigma1(w[i - 2]);
            final long[] w2 = this.W;
            w[i] = sigma1 + w2[i - 7] + this.Sigma0(w2[i - 15]) + this.W[i - 16];
        }
        long h1 = this.H1;
        long h2 = this.H2;
        long h3 = this.H3;
        long h4 = this.H4;
        long h5 = this.H5;
        long h6 = this.H6;
        long h7 = this.H7;
        long h8 = this.H8;
        int j = 0;
        int n = 0;
        while (j < 10) {
            final long sum1 = this.Sum1(h5);
            final long ch = this.Ch(h5, h6, h7);
            final long n2 = LongDigest.K[n];
            final long[] w3 = this.W;
            final int n3 = n + 1;
            final long n4 = h8 + (sum1 + ch + n2 + w3[n]);
            final long n5 = h4 + n4;
            final long n6 = n4 + (this.Sum0(h1) + this.Maj(h1, h2, h3));
            final long sum2 = this.Sum1(n5);
            final long ch2 = this.Ch(n5, h5, h6);
            final long n7 = LongDigest.K[n3];
            final long[] w4 = this.W;
            final int n8 = n3 + 1;
            final long n9 = h7 + (sum2 + ch2 + n7 + w4[n3]);
            final long n10 = h3 + n9;
            final long n11 = n9 + (this.Sum0(n6) + this.Maj(n6, h1, h2));
            final long sum3 = this.Sum1(n10);
            final long ch3 = this.Ch(n10, n5, h5);
            final long n12 = LongDigest.K[n8];
            final long[] w5 = this.W;
            final int n13 = n8 + 1;
            final long n14 = h6 + (sum3 + ch3 + n12 + w5[n8]);
            final long n15 = h2 + n14;
            final long n16 = n14 + (this.Sum0(n11) + this.Maj(n11, n6, h1));
            final long sum4 = this.Sum1(n15);
            final long ch4 = this.Ch(n15, n10, n5);
            final long n17 = LongDigest.K[n13];
            final long[] w6 = this.W;
            final int n18 = n13 + 1;
            final long n19 = h5 + (sum4 + ch4 + n17 + w6[n13]);
            final long n20 = h1 + n19;
            final long n21 = n19 + (this.Sum0(n16) + this.Maj(n16, n11, n6));
            final long sum5 = this.Sum1(n20);
            final long ch5 = this.Ch(n20, n15, n10);
            final long n22 = LongDigest.K[n18];
            final long[] w7 = this.W;
            final int n23 = n18 + 1;
            final long n24 = n5 + (sum5 + ch5 + n22 + w7[n18]);
            h8 = n6 + n24;
            h4 = n24 + (this.Sum0(n21) + this.Maj(n21, n16, n11));
            final long sum6 = this.Sum1(h8);
            final long ch6 = this.Ch(h8, n20, n15);
            final long n25 = LongDigest.K[n23];
            final long[] w8 = this.W;
            final int n26 = n23 + 1;
            final long n27 = n10 + (sum6 + ch6 + n25 + w8[n23]);
            h7 = n11 + n27;
            h3 = n27 + (this.Sum0(h4) + this.Maj(h4, n21, n16));
            final long sum7 = this.Sum1(h7);
            final long ch7 = this.Ch(h7, h8, n20);
            final long n28 = LongDigest.K[n26];
            final long[] w9 = this.W;
            final int n29 = n26 + 1;
            final long n30 = n15 + (sum7 + ch7 + n28 + w9[n26]);
            h6 = n16 + n30;
            final long sum8 = this.Sum0(h3);
            final long maj = this.Maj(h3, h4, n21);
            final long sum9 = this.Sum1(h6);
            h2 = n30 + (sum8 + maj);
            final long n31 = n20 + (sum9 + this.Ch(h6, h7, h8) + LongDigest.K[n29] + this.W[n29]);
            final long sum10 = this.Sum0(h2);
            final long maj2 = this.Maj(h2, h3, h4);
            ++j;
            h5 = n21 + n31;
            n = n29 + 1;
            h1 = n31 + (sum10 + maj2);
        }
        this.H1 += h1;
        this.H2 += h2;
        this.H3 += h3;
        this.H4 += h4;
        this.H5 += h5;
        this.H6 += h6;
        this.H7 += h7;
        this.H8 += h8;
        int k = 0;
        this.wOff = 0;
        while (k < 16) {
            this.W[k] = 0L;
            ++k;
        }
    }
    
    protected void processLength(final long n, final long n2) {
        if (this.wOff > 14) {
            this.processBlock();
        }
        final long[] w = this.W;
        w[14] = n2;
        w[15] = n;
    }
    
    protected void processWord(final byte[] array, int wOff) {
        this.W[this.wOff] = Pack.bigEndianToLong(array, wOff);
        wOff = this.wOff + 1;
        this.wOff = wOff;
        if (wOff == 16) {
            this.processBlock();
        }
    }
    
    @Override
    public void reset() {
        this.byteCount1 = 0L;
        this.byteCount2 = 0L;
        final int n = 0;
        this.xBufOff = 0;
        int n2 = 0;
        while (true) {
            final byte[] xBuf = this.xBuf;
            if (n2 >= xBuf.length) {
                break;
            }
            xBuf[n2] = 0;
            ++n2;
        }
        this.wOff = 0;
        int n3 = n;
        while (true) {
            final long[] w = this.W;
            if (n3 == w.length) {
                break;
            }
            w[n3] = 0L;
            ++n3;
        }
    }
    
    protected void restoreState(final byte[] array) {
        final int bigEndianToInt = Pack.bigEndianToInt(array, 8);
        this.xBufOff = bigEndianToInt;
        final byte[] xBuf = this.xBuf;
        int i = 0;
        System.arraycopy(array, 0, xBuf, 0, bigEndianToInt);
        this.byteCount1 = Pack.bigEndianToLong(array, 12);
        this.byteCount2 = Pack.bigEndianToLong(array, 20);
        this.H1 = Pack.bigEndianToLong(array, 28);
        this.H2 = Pack.bigEndianToLong(array, 36);
        this.H3 = Pack.bigEndianToLong(array, 44);
        this.H4 = Pack.bigEndianToLong(array, 52);
        this.H5 = Pack.bigEndianToLong(array, 60);
        this.H6 = Pack.bigEndianToLong(array, 68);
        this.H7 = Pack.bigEndianToLong(array, 76);
        this.H8 = Pack.bigEndianToLong(array, 84);
        this.wOff = Pack.bigEndianToInt(array, 92);
        while (i < this.wOff) {
            this.W[i] = Pack.bigEndianToLong(array, i * 8 + 96);
            ++i;
        }
    }
    
    @Override
    public void update(final byte b) {
        final byte[] xBuf = this.xBuf;
        final int xBufOff = this.xBufOff;
        final int xBufOff2 = xBufOff + 1;
        this.xBufOff = xBufOff2;
        xBuf[xBufOff] = b;
        if (xBufOff2 == xBuf.length) {
            this.processWord(xBuf, 0);
            this.xBufOff = 0;
        }
        ++this.byteCount1;
    }
    
    @Override
    public void update(final byte[] array, int n, int n2) {
        int n3 = n2;
        int n4 = n;
        while (true) {
            n = n4;
            n2 = n3;
            if (this.xBufOff == 0) {
                break;
            }
            n = n4;
            if ((n2 = n3) <= 0) {
                break;
            }
            this.update(array[n4]);
            ++n4;
            --n3;
        }
        int n5;
        int i;
        while (true) {
            n5 = n;
            if ((i = n2) <= this.xBuf.length) {
                break;
            }
            this.processWord(array, n);
            final byte[] xBuf = this.xBuf;
            n += xBuf.length;
            n2 -= xBuf.length;
            this.byteCount1 += xBuf.length;
        }
        while (i > 0) {
            this.update(array[n5]);
            ++n5;
            --i;
        }
    }
}
