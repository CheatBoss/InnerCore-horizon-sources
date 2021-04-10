package org.spongycastle.crypto.digests;

import org.spongycastle.util.*;

public class RIPEMD128Digest extends GeneralDigest
{
    private static final int DIGEST_LENGTH = 16;
    private int H0;
    private int H1;
    private int H2;
    private int H3;
    private int[] X;
    private int xOff;
    
    public RIPEMD128Digest() {
        this.X = new int[16];
        this.reset();
    }
    
    public RIPEMD128Digest(final RIPEMD128Digest ripemd128Digest) {
        super(ripemd128Digest);
        this.X = new int[16];
        this.copyIn(ripemd128Digest);
    }
    
    private int F1(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        return this.RL(n + this.f1(n2, n3, n4) + n5, n6);
    }
    
    private int F2(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        return this.RL(n + this.f2(n2, n3, n4) + n5 + 1518500249, n6);
    }
    
    private int F3(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        return this.RL(n + this.f3(n2, n3, n4) + n5 + 1859775393, n6);
    }
    
    private int F4(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        return this.RL(n + this.f4(n2, n3, n4) + n5 - 1894007588, n6);
    }
    
    private int FF1(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        return this.RL(n + this.f1(n2, n3, n4) + n5, n6);
    }
    
    private int FF2(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        return this.RL(n + this.f2(n2, n3, n4) + n5 + 1836072691, n6);
    }
    
    private int FF3(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        return this.RL(n + this.f3(n2, n3, n4) + n5 + 1548603684, n6);
    }
    
    private int FF4(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
        return this.RL(n + this.f4(n2, n3, n4) + n5 + 1352829926, n6);
    }
    
    private int RL(final int n, final int n2) {
        return n << n2 | n >>> 32 - n2;
    }
    
    private void copyIn(final RIPEMD128Digest ripemd128Digest) {
        super.copyIn(ripemd128Digest);
        this.H0 = ripemd128Digest.H0;
        this.H1 = ripemd128Digest.H1;
        this.H2 = ripemd128Digest.H2;
        this.H3 = ripemd128Digest.H3;
        final int[] x = ripemd128Digest.X;
        System.arraycopy(x, 0, this.X, 0, x.length);
        this.xOff = ripemd128Digest.xOff;
    }
    
    private int f1(final int n, final int n2, final int n3) {
        return n ^ n2 ^ n3;
    }
    
    private int f2(final int n, final int n2, final int n3) {
        return (n & n2) | (n3 & ~n);
    }
    
    private int f3(final int n, final int n2, final int n3) {
        return (n | ~n2) ^ n3;
    }
    
    private int f4(final int n, final int n2, final int n3) {
        return (n & n3) | (n2 & ~n3);
    }
    
    private void unpackWord(final int n, final byte[] array, final int n2) {
        array[n2] = (byte)n;
        array[n2 + 1] = (byte)(n >>> 8);
        array[n2 + 2] = (byte)(n >>> 16);
        array[n2 + 3] = (byte)(n >>> 24);
    }
    
    @Override
    public Memoable copy() {
        return new RIPEMD128Digest(this);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        this.finish();
        this.unpackWord(this.H0, array, n);
        this.unpackWord(this.H1, array, n + 4);
        this.unpackWord(this.H2, array, n + 8);
        this.unpackWord(this.H3, array, n + 12);
        this.reset();
        return 16;
    }
    
    @Override
    public String getAlgorithmName() {
        return "RIPEMD128";
    }
    
    @Override
    public int getDigestSize() {
        return 16;
    }
    
    @Override
    protected void processBlock() {
        final int h0 = this.H0;
        final int h2 = this.H1;
        final int h3 = this.H2;
        final int h4 = this.H3;
        final int f1 = this.F1(h0, h2, h3, h4, this.X[0], 11);
        final int f2 = this.F1(h4, f1, h2, h3, this.X[1], 14);
        final int f3 = this.F1(h3, f2, f1, h2, this.X[2], 15);
        final int f4 = this.F1(h2, f3, f2, f1, this.X[3], 12);
        final int f5 = this.F1(f1, f4, f3, f2, this.X[4], 5);
        final int f6 = this.F1(f2, f5, f4, f3, this.X[5], 8);
        final int f7 = this.F1(f3, f6, f5, f4, this.X[6], 7);
        final int f8 = this.F1(f4, f7, f6, f5, this.X[7], 9);
        final int f9 = this.F1(f5, f8, f7, f6, this.X[8], 11);
        final int f10 = this.F1(f6, f9, f8, f7, this.X[9], 13);
        final int f11 = this.F1(f7, f10, f9, f8, this.X[10], 14);
        final int f12 = this.F1(f8, f11, f10, f9, this.X[11], 15);
        final int f13 = this.F1(f9, f12, f11, f10, this.X[12], 6);
        final int f14 = this.F1(f10, f13, f12, f11, this.X[13], 7);
        final int f15 = this.F1(f11, f14, f13, f12, this.X[14], 9);
        final int f16 = this.F1(f12, f15, f14, f13, this.X[15], 8);
        final int f17 = this.F2(f13, f16, f15, f14, this.X[7], 7);
        final int f18 = this.F2(f14, f17, f16, f15, this.X[4], 6);
        final int f19 = this.F2(f15, f18, f17, f16, this.X[13], 8);
        final int f20 = this.F2(f16, f19, f18, f17, this.X[1], 13);
        final int f21 = this.F2(f17, f20, f19, f18, this.X[10], 11);
        final int f22 = this.F2(f18, f21, f20, f19, this.X[6], 9);
        final int f23 = this.F2(f19, f22, f21, f20, this.X[15], 7);
        final int f24 = this.F2(f20, f23, f22, f21, this.X[3], 15);
        final int f25 = this.F2(f21, f24, f23, f22, this.X[12], 7);
        final int f26 = this.F2(f22, f25, f24, f23, this.X[0], 12);
        final int f27 = this.F2(f23, f26, f25, f24, this.X[9], 15);
        final int f28 = this.F2(f24, f27, f26, f25, this.X[5], 9);
        final int f29 = this.F2(f25, f28, f27, f26, this.X[2], 11);
        final int f30 = this.F2(f26, f29, f28, f27, this.X[14], 7);
        final int f31 = this.F2(f27, f30, f29, f28, this.X[11], 13);
        final int f32 = this.F2(f28, f31, f30, f29, this.X[8], 12);
        final int f33 = this.F3(f29, f32, f31, f30, this.X[3], 11);
        final int f34 = this.F3(f30, f33, f32, f31, this.X[10], 13);
        final int f35 = this.F3(f31, f34, f33, f32, this.X[14], 6);
        final int f36 = this.F3(f32, f35, f34, f33, this.X[4], 7);
        final int f37 = this.F3(f33, f36, f35, f34, this.X[9], 14);
        final int f38 = this.F3(f34, f37, f36, f35, this.X[15], 9);
        final int f39 = this.F3(f35, f38, f37, f36, this.X[8], 13);
        final int f40 = this.F3(f36, f39, f38, f37, this.X[1], 15);
        final int f41 = this.F3(f37, f40, f39, f38, this.X[2], 14);
        final int f42 = this.F3(f38, f41, f40, f39, this.X[7], 8);
        final int f43 = this.F3(f39, f42, f41, f40, this.X[0], 13);
        final int f44 = this.F3(f40, f43, f42, f41, this.X[6], 6);
        final int f45 = this.F3(f41, f44, f43, f42, this.X[13], 5);
        final int f46 = this.F3(f42, f45, f44, f43, this.X[11], 12);
        final int f47 = this.F3(f43, f46, f45, f44, this.X[5], 7);
        final int f48 = this.F3(f44, f47, f46, f45, this.X[12], 5);
        final int f49 = this.F4(f45, f48, f47, f46, this.X[1], 11);
        final int f50 = this.F4(f46, f49, f48, f47, this.X[9], 12);
        final int f51 = this.F4(f47, f50, f49, f48, this.X[11], 14);
        final int f52 = this.F4(f48, f51, f50, f49, this.X[10], 15);
        final int f53 = this.F4(f49, f52, f51, f50, this.X[0], 14);
        final int f54 = this.F4(f50, f53, f52, f51, this.X[8], 15);
        final int f55 = this.F4(f51, f54, f53, f52, this.X[12], 9);
        final int f56 = this.F4(f52, f55, f54, f53, this.X[4], 8);
        final int f57 = this.F4(f53, f56, f55, f54, this.X[13], 9);
        final int f58 = this.F4(f54, f57, f56, f55, this.X[3], 14);
        final int f59 = this.F4(f55, f58, f57, f56, this.X[7], 5);
        final int f60 = this.F4(f56, f59, f58, f57, this.X[15], 6);
        final int f61 = this.F4(f57, f60, f59, f58, this.X[14], 8);
        final int f62 = this.F4(f58, f61, f60, f59, this.X[5], 6);
        final int f63 = this.F4(f59, f62, f61, f60, this.X[6], 5);
        final int f64 = this.F4(f60, f63, f62, f61, this.X[2], 12);
        final int ff4 = this.FF4(h0, h2, h3, h4, this.X[5], 8);
        final int ff5 = this.FF4(h4, ff4, h2, h3, this.X[14], 9);
        final int ff6 = this.FF4(h3, ff5, ff4, h2, this.X[7], 9);
        final int ff7 = this.FF4(h2, ff6, ff5, ff4, this.X[0], 11);
        final int ff8 = this.FF4(ff4, ff7, ff6, ff5, this.X[9], 13);
        final int ff9 = this.FF4(ff5, ff8, ff7, ff6, this.X[2], 15);
        final int ff10 = this.FF4(ff6, ff9, ff8, ff7, this.X[11], 15);
        final int ff11 = this.FF4(ff7, ff10, ff9, ff8, this.X[4], 5);
        final int ff12 = this.FF4(ff8, ff11, ff10, ff9, this.X[13], 7);
        final int ff13 = this.FF4(ff9, ff12, ff11, ff10, this.X[6], 7);
        final int ff14 = this.FF4(ff10, ff13, ff12, ff11, this.X[15], 8);
        final int ff15 = this.FF4(ff11, ff14, ff13, ff12, this.X[8], 11);
        final int ff16 = this.FF4(ff12, ff15, ff14, ff13, this.X[1], 14);
        final int ff17 = this.FF4(ff13, ff16, ff15, ff14, this.X[10], 14);
        final int ff18 = this.FF4(ff14, ff17, ff16, ff15, this.X[3], 12);
        final int ff19 = this.FF4(ff15, ff18, ff17, ff16, this.X[12], 6);
        final int ff20 = this.FF3(ff16, ff19, ff18, ff17, this.X[6], 9);
        final int ff21 = this.FF3(ff17, ff20, ff19, ff18, this.X[11], 13);
        final int ff22 = this.FF3(ff18, ff21, ff20, ff19, this.X[3], 15);
        final int ff23 = this.FF3(ff19, ff22, ff21, ff20, this.X[7], 7);
        final int ff24 = this.FF3(ff20, ff23, ff22, ff21, this.X[0], 12);
        final int ff25 = this.FF3(ff21, ff24, ff23, ff22, this.X[13], 8);
        final int ff26 = this.FF3(ff22, ff25, ff24, ff23, this.X[5], 9);
        final int ff27 = this.FF3(ff23, ff26, ff25, ff24, this.X[10], 11);
        final int ff28 = this.FF3(ff24, ff27, ff26, ff25, this.X[14], 7);
        final int ff29 = this.FF3(ff25, ff28, ff27, ff26, this.X[15], 7);
        final int ff30 = this.FF3(ff26, ff29, ff28, ff27, this.X[8], 12);
        final int ff31 = this.FF3(ff27, ff30, ff29, ff28, this.X[12], 7);
        final int ff32 = this.FF3(ff28, ff31, ff30, ff29, this.X[4], 6);
        final int ff33 = this.FF3(ff29, ff32, ff31, ff30, this.X[9], 15);
        final int ff34 = this.FF3(ff30, ff33, ff32, ff31, this.X[1], 13);
        final int ff35 = this.FF3(ff31, ff34, ff33, ff32, this.X[2], 11);
        final int ff36 = this.FF2(ff32, ff35, ff34, ff33, this.X[15], 9);
        final int ff37 = this.FF2(ff33, ff36, ff35, ff34, this.X[5], 7);
        final int ff38 = this.FF2(ff34, ff37, ff36, ff35, this.X[1], 15);
        final int ff39 = this.FF2(ff35, ff38, ff37, ff36, this.X[3], 11);
        final int ff40 = this.FF2(ff36, ff39, ff38, ff37, this.X[7], 8);
        final int ff41 = this.FF2(ff37, ff40, ff39, ff38, this.X[14], 6);
        final int ff42 = this.FF2(ff38, ff41, ff40, ff39, this.X[6], 6);
        final int ff43 = this.FF2(ff39, ff42, ff41, ff40, this.X[9], 14);
        final int ff44 = this.FF2(ff40, ff43, ff42, ff41, this.X[11], 12);
        final int ff45 = this.FF2(ff41, ff44, ff43, ff42, this.X[8], 13);
        final int ff46 = this.FF2(ff42, ff45, ff44, ff43, this.X[12], 5);
        final int ff47 = this.FF2(ff43, ff46, ff45, ff44, this.X[2], 14);
        final int ff48 = this.FF2(ff44, ff47, ff46, ff45, this.X[10], 13);
        final int ff49 = this.FF2(ff45, ff48, ff47, ff46, this.X[0], 13);
        final int ff50 = this.FF2(ff46, ff49, ff48, ff47, this.X[4], 7);
        final int ff51 = this.FF2(ff47, ff50, ff49, ff48, this.X[13], 5);
        final int ff52 = this.FF1(ff48, ff51, ff50, ff49, this.X[8], 15);
        final int ff53 = this.FF1(ff49, ff52, ff51, ff50, this.X[6], 5);
        final int ff54 = this.FF1(ff50, ff53, ff52, ff51, this.X[4], 8);
        final int ff55 = this.FF1(ff51, ff54, ff53, ff52, this.X[1], 11);
        final int ff56 = this.FF1(ff52, ff55, ff54, ff53, this.X[3], 14);
        final int ff57 = this.FF1(ff53, ff56, ff55, ff54, this.X[11], 14);
        final int ff58 = this.FF1(ff54, ff57, ff56, ff55, this.X[15], 6);
        final int ff59 = this.FF1(ff55, ff58, ff57, ff56, this.X[0], 14);
        final int ff60 = this.FF1(ff56, ff59, ff58, ff57, this.X[5], 6);
        final int ff61 = this.FF1(ff57, ff60, ff59, ff58, this.X[12], 9);
        final int ff62 = this.FF1(ff58, ff61, ff60, ff59, this.X[2], 12);
        final int ff63 = this.FF1(ff59, ff62, ff61, ff60, this.X[13], 9);
        final int ff64 = this.FF1(ff60, ff63, ff62, ff61, this.X[9], 12);
        final int ff65 = this.FF1(ff61, ff64, ff63, ff62, this.X[7], 5);
        final int ff66 = this.FF1(ff62, ff65, ff64, ff63, this.X[10], 15);
        final int ff67 = this.FF1(ff63, ff66, ff65, ff64, this.X[14], 8);
        final int h5 = this.H1;
        this.H1 = this.H2 + f62 + ff64;
        this.H2 = this.H3 + f61 + ff67;
        this.H3 = this.H0 + f64 + ff66;
        this.H0 = ff65 + (f63 + h5);
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
        this.H0 = 1732584193;
        this.H1 = -271733879;
        this.H2 = -1732584194;
        this.H3 = 271733878;
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
        this.copyIn((RIPEMD128Digest)memoable);
    }
}
