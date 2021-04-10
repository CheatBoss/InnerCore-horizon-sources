package org.spongycastle.crypto.digests;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;

public class MD2Digest implements ExtendedDigest, Memoable
{
    private static final int DIGEST_LENGTH = 16;
    private static final byte[] S;
    private byte[] C;
    private int COff;
    private byte[] M;
    private byte[] X;
    private int mOff;
    private int xOff;
    
    static {
        S = new byte[] { 41, 46, 67, -55, -94, -40, 124, 1, 61, 54, 84, -95, -20, -16, 6, 19, 98, -89, 5, -13, -64, -57, 115, -116, -104, -109, 43, -39, -68, 76, -126, -54, 30, -101, 87, 60, -3, -44, -32, 22, 103, 66, 111, 24, -118, 23, -27, 18, -66, 78, -60, -42, -38, -98, -34, 73, -96, -5, -11, -114, -69, 47, -18, 122, -87, 104, 121, -111, 21, -78, 7, 63, -108, -62, 16, -119, 11, 34, 95, 33, -128, 127, 93, -102, 90, -112, 50, 39, 53, 62, -52, -25, -65, -9, -105, 3, -1, 25, 48, -77, 72, -91, -75, -47, -41, 94, -110, 42, -84, 86, -86, -58, 79, -72, 56, -46, -106, -92, 125, -74, 118, -4, 107, -30, -100, 116, 4, -15, 69, -99, 112, 89, 100, 113, -121, 32, -122, 91, -49, 101, -26, 45, -88, 2, 27, 96, 37, -83, -82, -80, -71, -10, 28, 70, 97, 105, 52, 64, 126, 15, 85, 71, -93, 35, -35, 81, -81, 58, -61, 92, -7, -50, -70, -59, -22, 38, 44, 83, 13, 110, -123, 40, -124, 9, -45, -33, -51, -12, 65, -127, 77, 82, 106, -36, 55, -56, 108, -63, -85, -6, 36, -31, 123, 8, 12, -67, -79, 74, 120, -120, -107, -117, -29, 99, -24, 109, -23, -53, -43, -2, 59, 0, 29, 57, -14, -17, -73, 14, 102, 88, -48, -28, -90, 119, 114, -8, -21, 117, 75, 10, 49, 68, 80, -76, -113, -19, 31, 26, -37, -103, -115, 51, -97, 17, -125, 20 };
    }
    
    public MD2Digest() {
        this.X = new byte[48];
        this.M = new byte[16];
        this.C = new byte[16];
        this.reset();
    }
    
    public MD2Digest(final MD2Digest md2Digest) {
        this.X = new byte[48];
        this.M = new byte[16];
        this.C = new byte[16];
        this.copyIn(md2Digest);
    }
    
    private void copyIn(final MD2Digest md2Digest) {
        final byte[] x = md2Digest.X;
        System.arraycopy(x, 0, this.X, 0, x.length);
        this.xOff = md2Digest.xOff;
        final byte[] m = md2Digest.M;
        System.arraycopy(m, 0, this.M, 0, m.length);
        this.mOff = md2Digest.mOff;
        final byte[] c = md2Digest.C;
        System.arraycopy(c, 0, this.C, 0, c.length);
        this.COff = md2Digest.COff;
    }
    
    @Override
    public Memoable copy() {
        return new MD2Digest(this);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        final int length = this.M.length;
        int mOff = this.mOff;
        final byte b = (byte)(length - mOff);
        byte[] m;
        while (true) {
            m = this.M;
            if (mOff >= m.length) {
                break;
            }
            m[mOff] = b;
            ++mOff;
        }
        this.processCheckSum(m);
        this.processBlock(this.M);
        this.processBlock(this.C);
        System.arraycopy(this.X, this.xOff, array, n, 16);
        this.reset();
        return 16;
    }
    
    @Override
    public String getAlgorithmName() {
        return "MD2";
    }
    
    @Override
    public int getByteLength() {
        return 16;
    }
    
    @Override
    public int getDigestSize() {
        return 16;
    }
    
    protected void processBlock(byte[] x) {
        for (int i = 0; i < 16; ++i) {
            final byte[] x2 = this.X;
            x2[i + 16] = x[i];
            x2[i + 32] = (byte)(x[i] ^ x2[i]);
        }
        int j = 0;
        int n = 0;
        while (j < 18) {
            final int n2 = 0;
            int n3 = n;
            for (int k = n2; k < 48; ++k) {
                x = this.X;
                final byte b = (byte)(MD2Digest.S[n3] ^ x[k]);
                x[k] = b;
                n3 = (b & 0xFF);
            }
            n = (n3 + j) % 256;
            ++j;
        }
    }
    
    protected void processCheckSum(final byte[] array) {
        byte b = this.C[15];
        for (int i = 0; i < 16; ++i) {
            final byte[] c = this.C;
            c[i] ^= MD2Digest.S[(b ^ array[i]) & 0xFF];
            b = c[i];
        }
    }
    
    @Override
    public void reset() {
        this.xOff = 0;
        int n = 0;
        while (true) {
            final byte[] x = this.X;
            if (n == x.length) {
                break;
            }
            x[n] = 0;
            ++n;
        }
        this.mOff = 0;
        int n2 = 0;
        while (true) {
            final byte[] m = this.M;
            if (n2 == m.length) {
                break;
            }
            m[n2] = 0;
            ++n2;
        }
        this.COff = 0;
        int n3 = 0;
        while (true) {
            final byte[] c = this.C;
            if (n3 == c.length) {
                break;
            }
            c[n3] = 0;
            ++n3;
        }
    }
    
    @Override
    public void reset(final Memoable memoable) {
        this.copyIn((MD2Digest)memoable);
    }
    
    @Override
    public void update(final byte b) {
        final byte[] m = this.M;
        final int mOff = this.mOff;
        final int mOff2 = mOff + 1;
        this.mOff = mOff2;
        m[mOff] = b;
        if (mOff2 == 16) {
            this.processCheckSum(m);
            this.processBlock(this.M);
            this.mOff = 0;
        }
    }
    
    @Override
    public void update(final byte[] array, int n, int n2) {
        int n3 = n;
        int n4;
        while (true) {
            n = n3;
            n4 = n2;
            if (this.mOff == 0) {
                break;
            }
            n = n3;
            if ((n4 = n2) <= 0) {
                break;
            }
            this.update(array[n3]);
            ++n3;
            --n2;
        }
        int i;
        while (true) {
            n2 = n;
            if ((i = n4) <= 16) {
                break;
            }
            System.arraycopy(array, n, this.M, 0, 16);
            this.processCheckSum(this.M);
            this.processBlock(this.M);
            n4 -= 16;
            n += 16;
        }
        while (i > 0) {
            this.update(array[n2]);
            ++n2;
            --i;
        }
    }
}
