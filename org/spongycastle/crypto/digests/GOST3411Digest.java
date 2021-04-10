package org.spongycastle.crypto.digests;

import java.lang.reflect.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.util.*;

public class GOST3411Digest implements ExtendedDigest, Memoable
{
    private static final byte[] C2;
    private static final int DIGEST_LENGTH = 32;
    private byte[][] C;
    private byte[] H;
    private byte[] K;
    private byte[] L;
    private byte[] M;
    byte[] S;
    private byte[] Sum;
    byte[] U;
    byte[] V;
    byte[] W;
    byte[] a;
    private long byteCount;
    private BlockCipher cipher;
    private byte[] sBox;
    short[] wS;
    short[] w_S;
    private byte[] xBuf;
    private int xBufOff;
    
    static {
        C2 = new byte[] { 0, -1, 0, -1, 0, -1, 0, -1, -1, 0, -1, 0, -1, 0, -1, 0, 0, -1, -1, 0, -1, 0, 0, -1, -1, 0, 0, 0, -1, -1, 0, -1 };
    }
    
    public GOST3411Digest() {
        this.H = new byte[32];
        this.L = new byte[32];
        this.M = new byte[32];
        this.Sum = new byte[32];
        this.C = (byte[][])Array.newInstance(Byte.TYPE, 4, 32);
        this.xBuf = new byte[32];
        this.cipher = new GOST28147Engine();
        this.K = new byte[32];
        this.a = new byte[8];
        this.wS = new short[16];
        this.w_S = new short[16];
        this.S = new byte[32];
        this.U = new byte[32];
        this.V = new byte[32];
        this.W = new byte[32];
        this.sBox = GOST28147Engine.getSBox("D-A");
        this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
        this.reset();
    }
    
    public GOST3411Digest(final GOST3411Digest gost3411Digest) {
        this.H = new byte[32];
        this.L = new byte[32];
        this.M = new byte[32];
        this.Sum = new byte[32];
        this.C = (byte[][])Array.newInstance(Byte.TYPE, 4, 32);
        this.xBuf = new byte[32];
        this.cipher = new GOST28147Engine();
        this.K = new byte[32];
        this.a = new byte[8];
        this.wS = new short[16];
        this.w_S = new short[16];
        this.S = new byte[32];
        this.U = new byte[32];
        this.V = new byte[32];
        this.W = new byte[32];
        this.reset(gost3411Digest);
    }
    
    public GOST3411Digest(final byte[] array) {
        this.H = new byte[32];
        this.L = new byte[32];
        this.M = new byte[32];
        this.Sum = new byte[32];
        this.C = (byte[][])Array.newInstance(Byte.TYPE, 4, 32);
        this.xBuf = new byte[32];
        this.cipher = new GOST28147Engine();
        this.K = new byte[32];
        this.a = new byte[8];
        this.wS = new short[16];
        this.w_S = new short[16];
        this.S = new byte[32];
        this.U = new byte[32];
        this.V = new byte[32];
        this.W = new byte[32];
        this.sBox = Arrays.clone(array);
        this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
        this.reset();
    }
    
    private byte[] A(final byte[] array) {
        for (int i = 0; i < 8; ++i) {
            this.a[i] = (byte)(array[i] ^ array[i + 8]);
        }
        System.arraycopy(array, 8, array, 0, 24);
        System.arraycopy(this.a, 0, array, 24, 8);
        return array;
    }
    
    private void E(final byte[] array, final byte[] array2, final int n, final byte[] array3, final int n2) {
        this.cipher.init(true, new KeyParameter(array));
        this.cipher.processBlock(array3, n2, array2, n);
    }
    
    private byte[] P(final byte[] array) {
        for (int i = 0; i < 8; ++i) {
            final byte[] k = this.K;
            final int n = i * 4;
            k[n] = array[i];
            k[n + 1] = array[i + 8];
            k[n + 2] = array[i + 16];
            k[n + 3] = array[i + 24];
        }
        return this.K;
    }
    
    private void cpyBytesToShort(final byte[] array, final short[] array2) {
        for (int i = 0; i < array.length / 2; ++i) {
            final int n = i * 2;
            array2[i] = (short)((array[n] & 0xFF) | (array[n + 1] << 8 & 0xFF00));
        }
    }
    
    private void cpyShortToBytes(final short[] array, final byte[] array2) {
        for (int i = 0; i < array2.length / 2; ++i) {
            final int n = i * 2;
            array2[n + 1] = (byte)(array[i] >> 8);
            array2[n] = (byte)array[i];
        }
    }
    
    private void finish() {
        Pack.longToLittleEndian(this.byteCount * 8L, this.L, 0);
        while (this.xBufOff != 0) {
            this.update((byte)0);
        }
        this.processBlock(this.L, 0);
        this.processBlock(this.Sum, 0);
    }
    
    private void fw(final byte[] array) {
        this.cpyBytesToShort(array, this.wS);
        final short[] w_S = this.w_S;
        final short[] ws = this.wS;
        w_S[15] = (short)(ws[0] ^ ws[1] ^ ws[2] ^ ws[3] ^ ws[12] ^ ws[15]);
        System.arraycopy(ws, 1, w_S, 0, 15);
        this.cpyShortToBytes(this.w_S, array);
    }
    
    private void sumByteArray(final byte[] array) {
        int n = 0;
        int n2 = 0;
        while (true) {
            final byte[] sum = this.Sum;
            if (n == sum.length) {
                break;
            }
            final int n3 = (sum[n] & 0xFF) + (array[n] & 0xFF) + n2;
            sum[n] = (byte)n3;
            n2 = n3 >>> 8;
            ++n;
        }
    }
    
    @Override
    public Memoable copy() {
        return new GOST3411Digest(this);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        this.finish();
        final byte[] h = this.H;
        System.arraycopy(h, 0, array, n, h.length);
        this.reset();
        return 32;
    }
    
    @Override
    public String getAlgorithmName() {
        return "GOST3411";
    }
    
    @Override
    public int getByteLength() {
        return 32;
    }
    
    @Override
    public int getDigestSize() {
        return 32;
    }
    
    protected void processBlock(byte[] array, int i) {
        System.arraycopy(array, i, this.M, 0, 32);
        System.arraycopy(this.H, 0, this.U, 0, 32);
        System.arraycopy(this.M, 0, this.V, 0, 32);
        for (i = 0; i < 32; ++i) {
            this.W[i] = (byte)(this.U[i] ^ this.V[i]);
        }
        this.E(this.P(this.W), this.S, 0, this.H, 0);
        int j;
        int k;
        byte[] s;
        int n;
        for (i = 1; i < 4; ++i) {
            array = this.A(this.U);
            for (j = 0; j < 32; ++j) {
                this.U[j] = (byte)(array[j] ^ this.C[i][j]);
            }
            this.V = this.A(this.A(this.V));
            for (k = 0; k < 32; ++k) {
                this.W[k] = (byte)(this.U[k] ^ this.V[k]);
            }
            array = this.P(this.W);
            s = this.S;
            n = i * 8;
            this.E(array, s, n, this.H, n);
        }
        for (i = 0; i < 12; ++i) {
            this.fw(this.S);
        }
        for (i = 0; i < 32; ++i) {
            array = this.S;
            array[i] ^= this.M[i];
        }
        this.fw(this.S);
        for (i = 0; i < 32; ++i) {
            array = this.S;
            array[i] ^= this.H[i];
        }
        for (i = 0; i < 61; ++i) {
            this.fw(this.S);
        }
        array = this.S;
        final byte[] h = this.H;
        System.arraycopy(array, 0, h, 0, h.length);
    }
    
    @Override
    public void reset() {
        this.byteCount = 0L;
        this.xBufOff = 0;
        int n = 0;
        while (true) {
            final byte[] h = this.H;
            if (n >= h.length) {
                break;
            }
            h[n] = 0;
            ++n;
        }
        int n2 = 0;
        while (true) {
            final byte[] l = this.L;
            if (n2 >= l.length) {
                break;
            }
            l[n2] = 0;
            ++n2;
        }
        int n3 = 0;
        while (true) {
            final byte[] m = this.M;
            if (n3 >= m.length) {
                break;
            }
            m[n3] = 0;
            ++n3;
        }
        int n4 = 0;
        while (true) {
            final byte[][] c = this.C;
            if (n4 >= c[1].length) {
                break;
            }
            c[1][n4] = 0;
            ++n4;
        }
        int n5 = 0;
        while (true) {
            final byte[][] c2 = this.C;
            if (n5 >= c2[3].length) {
                break;
            }
            c2[3][n5] = 0;
            ++n5;
        }
        int n6 = 0;
        while (true) {
            final byte[] sum = this.Sum;
            if (n6 >= sum.length) {
                break;
            }
            sum[n6] = 0;
            ++n6;
        }
        int n7 = 0;
        while (true) {
            final byte[] xBuf = this.xBuf;
            if (n7 >= xBuf.length) {
                break;
            }
            xBuf[n7] = 0;
            ++n7;
        }
        final byte[] c3 = GOST3411Digest.C2;
        System.arraycopy(c3, 0, this.C[2], 0, c3.length);
    }
    
    @Override
    public void reset(final Memoable memoable) {
        final GOST3411Digest gost3411Digest = (GOST3411Digest)memoable;
        this.sBox = gost3411Digest.sBox;
        this.cipher.init(true, new ParametersWithSBox(null, this.sBox));
        this.reset();
        final byte[] h = gost3411Digest.H;
        System.arraycopy(h, 0, this.H, 0, h.length);
        final byte[] l = gost3411Digest.L;
        System.arraycopy(l, 0, this.L, 0, l.length);
        final byte[] m = gost3411Digest.M;
        System.arraycopy(m, 0, this.M, 0, m.length);
        final byte[] sum = gost3411Digest.Sum;
        System.arraycopy(sum, 0, this.Sum, 0, sum.length);
        final byte[][] c = gost3411Digest.C;
        System.arraycopy(c[1], 0, this.C[1], 0, c[1].length);
        final byte[][] c2 = gost3411Digest.C;
        System.arraycopy(c2[2], 0, this.C[2], 0, c2[2].length);
        final byte[][] c3 = gost3411Digest.C;
        System.arraycopy(c3[3], 0, this.C[3], 0, c3[3].length);
        final byte[] xBuf = gost3411Digest.xBuf;
        System.arraycopy(xBuf, 0, this.xBuf, 0, xBuf.length);
        this.xBufOff = gost3411Digest.xBufOff;
        this.byteCount = gost3411Digest.byteCount;
    }
    
    @Override
    public void update(final byte b) {
        final byte[] xBuf = this.xBuf;
        final int xBufOff = this.xBufOff;
        final int xBufOff2 = xBufOff + 1;
        this.xBufOff = xBufOff2;
        xBuf[xBufOff] = b;
        if (xBufOff2 == xBuf.length) {
            this.sumByteArray(xBuf);
            this.processBlock(this.xBuf, 0);
            this.xBufOff = 0;
        }
        ++this.byteCount;
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
            final byte[] xBuf = this.xBuf;
            n5 = n;
            if ((i = n2) <= xBuf.length) {
                break;
            }
            System.arraycopy(array, n, xBuf, 0, xBuf.length);
            this.sumByteArray(this.xBuf);
            this.processBlock(this.xBuf, 0);
            final byte[] xBuf2 = this.xBuf;
            n += xBuf2.length;
            n2 -= xBuf2.length;
            this.byteCount += xBuf2.length;
        }
        while (i > 0) {
            this.update(array[n5]);
            ++n5;
            --i;
        }
    }
}
