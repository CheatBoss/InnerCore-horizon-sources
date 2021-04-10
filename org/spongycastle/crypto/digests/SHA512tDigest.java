package org.spongycastle.crypto.digests;

import org.spongycastle.util.*;

public class SHA512tDigest extends LongDigest
{
    private long H1t;
    private long H2t;
    private long H3t;
    private long H4t;
    private long H5t;
    private long H6t;
    private long H7t;
    private long H8t;
    private int digestLength;
    
    public SHA512tDigest(int digestLength) {
        if (digestLength >= 512) {
            throw new IllegalArgumentException("bitLength cannot be >= 512");
        }
        if (digestLength % 8 != 0) {
            throw new IllegalArgumentException("bitLength needs to be a multiple of 8");
        }
        if (digestLength != 384) {
            digestLength /= 8;
            this.tIvGenerate((this.digestLength = digestLength) * 8);
            this.reset();
            return;
        }
        throw new IllegalArgumentException("bitLength cannot be 384 use SHA384 instead");
    }
    
    public SHA512tDigest(final SHA512tDigest sha512tDigest) {
        super(sha512tDigest);
        this.digestLength = sha512tDigest.digestLength;
        this.reset(sha512tDigest);
    }
    
    public SHA512tDigest(final byte[] array) {
        this(readDigestLength(array));
        this.restoreState(array);
    }
    
    private static void intToBigEndian(final int n, final byte[] array, final int n2, int min) {
        min = Math.min(4, min);
        while (true) {
            --min;
            if (min < 0) {
                break;
            }
            array[n2 + min] = (byte)(n >>> (3 - min) * 8);
        }
    }
    
    private static void longToBigEndian(final long n, final byte[] array, final int n2, final int n3) {
        if (n3 > 0) {
            intToBigEndian((int)(n >>> 32), array, n2, n3);
            if (n3 > 4) {
                intToBigEndian((int)(n & 0xFFFFFFFFL), array, n2 + 4, n3 - 4);
            }
        }
    }
    
    private static int readDigestLength(final byte[] array) {
        return Pack.bigEndianToInt(array, array.length - 4);
    }
    
    private void tIvGenerate(int n) {
        this.H1 = -3482333909917012819L;
        this.H2 = 2216346199247487646L;
        this.H3 = -7364697282686394994L;
        this.H4 = 65953792586715988L;
        this.H5 = -816286391624063116L;
        this.H6 = 4512832404995164602L;
        this.H7 = -5033199132376557362L;
        this.H8 = -124578254951840548L;
        this.update((byte)83);
        this.update((byte)72);
        this.update((byte)65);
        this.update((byte)45);
        this.update((byte)53);
        this.update((byte)49);
        this.update((byte)50);
        this.update((byte)47);
        int n2 = 0;
        Label_0144: {
            if (n > 100) {
                this.update((byte)(n / 100 + 48));
                n %= 100;
            }
            else if ((n2 = n) <= 10) {
                break Label_0144;
            }
            this.update((byte)(n / 10 + 48));
            n2 = n % 10;
        }
        this.update((byte)(n2 + 48));
        this.finish();
        this.H1t = this.H1;
        this.H2t = this.H2;
        this.H3t = this.H3;
        this.H4t = this.H4;
        this.H5t = this.H5;
        this.H6t = this.H6;
        this.H7t = this.H7;
        this.H8t = this.H8;
    }
    
    @Override
    public Memoable copy() {
        return new SHA512tDigest(this);
    }
    
    @Override
    public int doFinal(final byte[] array, final int n) {
        this.finish();
        longToBigEndian(this.H1, array, n, this.digestLength);
        longToBigEndian(this.H2, array, n + 8, this.digestLength - 8);
        longToBigEndian(this.H3, array, n + 16, this.digestLength - 16);
        longToBigEndian(this.H4, array, n + 24, this.digestLength - 24);
        longToBigEndian(this.H5, array, n + 32, this.digestLength - 32);
        longToBigEndian(this.H6, array, n + 40, this.digestLength - 40);
        longToBigEndian(this.H7, array, n + 48, this.digestLength - 48);
        longToBigEndian(this.H8, array, n + 56, this.digestLength - 56);
        this.reset();
        return this.digestLength;
    }
    
    @Override
    public String getAlgorithmName() {
        final StringBuilder sb = new StringBuilder();
        sb.append("SHA-512/");
        sb.append(Integer.toString(this.digestLength * 8));
        return sb.toString();
    }
    
    @Override
    public int getDigestSize() {
        return this.digestLength;
    }
    
    @Override
    public byte[] getEncodedState() {
        final int encodedStateSize = this.getEncodedStateSize();
        final byte[] array = new byte[encodedStateSize + 4];
        this.populateState(array);
        Pack.intToBigEndian(this.digestLength * 8, array, encodedStateSize);
        return array;
    }
    
    @Override
    public void reset() {
        super.reset();
        this.H1 = this.H1t;
        this.H2 = this.H2t;
        this.H3 = this.H3t;
        this.H4 = this.H4t;
        this.H5 = this.H5t;
        this.H6 = this.H6t;
        this.H7 = this.H7t;
        this.H8 = this.H8t;
    }
    
    @Override
    public void reset(final Memoable memoable) {
        final SHA512tDigest sha512tDigest = (SHA512tDigest)memoable;
        if (this.digestLength == sha512tDigest.digestLength) {
            super.copyIn(sha512tDigest);
            this.H1t = sha512tDigest.H1t;
            this.H2t = sha512tDigest.H2t;
            this.H3t = sha512tDigest.H3t;
            this.H4t = sha512tDigest.H4t;
            this.H5t = sha512tDigest.H5t;
            this.H6t = sha512tDigest.H6t;
            this.H7t = sha512tDigest.H7t;
            this.H8t = sha512tDigest.H8t;
            return;
        }
        throw new MemoableResetException("digestLength inappropriate in other");
    }
}
